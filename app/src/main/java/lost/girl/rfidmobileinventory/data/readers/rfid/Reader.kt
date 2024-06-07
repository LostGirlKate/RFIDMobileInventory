package lost.girl.rfidmobileinventory.data.readers.rfid

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.rscja.deviceapi.RFIDWithUHFUART
import com.rscja.deviceapi.interfaces.IUHF
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import kotlin.math.floor


class Reader(
    // время ожидания когда ждем новые метки - физика работы ридера такова, что мы не можем  100%
    // быть уверены что при записи в поле одна метка. Этот таймаут задает время пока мы ищем новые метки,
    // после этого считаем что в поле только одна
    private val singleTimeout: Long = 500,
    // нужно регулировать минимальную и максимальную мощность, чтобы в интерфейсе задавать только
    // в процентах от этого диапазона, иначе сильно усложняется визуалка + не все модели железа
    // заранее говорят свой предел - его нужно задавать в коде или конфиге
    private val maxPower: Int = 30,
    private val minPower: Int = 8,
    // чтобы рисовать расстояние до метки в попугаях (близко-далеко) в интерфейсе, нужно калибровать расстояния,
    // т.к. есть большая зависимость RSSI от типа метки, окружения и ридера.
    private val distanceNear: Int = 90,
    private val distanceFar: Int = 70,
) : IRfidReader {

    private var range = (distanceNear - distanceFar).toDouble()
    private var lastPower = -1
    private var bgJob: Job? = null


    private var mReader: RFIDWithUHFUART? = null

    override var isRunned: Boolean = false
        get() {
            return synchronized(this) {
                field
            }
        }
        set(value) {
            synchronized(this) {
                field = value
            }
        }

    override var isReaderInitialized = false

    override fun poweron(context: Context): Boolean {
        isReaderInitialized = try {
            mReader = RFIDWithUHFUART.getInstance()
            val connected = mReader!!.init()
            Log.d("chainwayReader", "connected = $connected")
            val freq = connected && mReader!!.setFrequencyMode(4)
            val filter = freq && mReader!!.setFilter(RFIDWithUHFUART.Bank_EPC, 32, 0, "")
            filter


        } catch (ex: Exception) {
            Log.d("chainwayReader Error", ex.message ?: "")
            false
        }
        return isReaderInitialized
    }

    override fun poweroff() {

        if (!isReaderInitialized)
            return
        stop()
        ignoreErr { mReader!!.stopInventory() }
        mReader = null
        isReaderInitialized = false
        Log.d("chainwayReader", "isReaderInitialized = $isReaderInitialized")
    }

    override fun start(
        power: Int,
        onError: (message: String) -> Unit,
        onTags: (tagsRaw: List<String>) -> Unit
    ) {
        if (!isReaderInitialized) {

            Log.d("chainwayReader", "Считыватель не инициализирован")
            onError("Считыватель не инициализирован")
            return
        }
        val (isOk, _) = setPwr(power)
        if (!isOk) {
            Log.d("chainwayReader", "Ошибка установки мощности")
            onError("Ошибка при установке мощности")
            return //onError(_!!)
        }

        bgJob?.cancel()
        bgJob = runBgRead(onTags)

        isRunned = mReader!!.startInventoryTag()
        if (!isRunned) {
            Log.d("chainwayReader", "Не удалось запустить")
            onError("Не удалось запустить")
            bgJob?.cancel() //onError("Не удалось запустить")
            return
        }
    }

    override fun stop() {
        if (!isRunned)
            return
        isRunned = false
        bgJob?.cancel()
        ignoreErr {
            mReader!!.stopInventory()
            mReader!!.setFilter(RFIDWithUHFUART.Bank_EPC, 32, 0, "")
        }
    }

    override fun getPower(): Int {
        return mReader!!.power
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun runBgRead(onTags: (tagsRaw: List<String>) -> Unit): Job {
        return GlobalScope.launch {
            var start = System.currentTimeMillis()
            val readedTags = hashSetOf<String>()
            while (isActive) {
                val res = mReader!!.readTagFromBuffer()
                if (res == null) {
                    if (readedTags.isNotEmpty()) {
                        onTags(readedTags.toList())
                        readedTags.clear()
                        start = System.currentTimeMillis()
                    }
                    delay(100)
                    continue
                }
                readedTags.add(res.epc)
                if (readedTags.size > 10 || start - System.currentTimeMillis() > 200) {
                    onTags(readedTags.toList())
                    readedTags.clear()
                    delay(100)
                    start = System.currentTimeMillis()
                }
            }
        }
    }

    private fun setPwr(power: Int): Pair<Boolean, String?> {
        if (lastPower != power) {
            lastPower = power.coerceAtMost(maxPower).coerceAtLeast(minPower)
            val ret = mReader!!.setPower(lastPower)
            if (!ret) {
                lastPower = -1
                return Pair(false, "Не удалось задать мощность")
            }
        }
        return Pair(true, null)
    }


    override fun startSearch(
        power: Int,
        mask: Pair<Int, String>,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<Pair<String, Int>>) -> Unit
    ) {
        if (!isReaderInitialized) {
            onError("Считыватель не инициализирован")
            return
        }
        val (isOk, err) = setPwr(power)
        if (!isOk) {
            onError(err!!)
            return
        }
        bgJob = runBgSearch(onTags)
        val tmp = mReader!!.setFilter(
            RFIDWithUHFUART.Bank_EPC,
            32 + mask.first * 4,
            mask.second.length * 4,
            mask.second
        )
        if (!tmp) {
            onError("Не удалось задать фильтр")
            return
        }
        isRunned = mReader!!.startInventoryTag()
        if (!isRunned) {
            onError("Не удалось запустить")
            return
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun runBgSearch(onTags: (tagsRaw: List<Pair<String, Int>>) -> Unit): Job {
        return GlobalScope.launch {
            var start = System.currentTimeMillis()
            val searchTags = hashMapOf<String, Int>()
            while (isActive) {
                val res = mReader!!.readTagFromBuffer()
                if (res == null) {
                    if (searchTags.isNotEmpty()) {
                        onTags(searchTags.toList())
                        searchTags.clear()
                        start = System.currentTimeMillis()
                    }
                    delay(100)
                    continue
                }
                val curr = searchTags[res.epc]
                val rssi = rescaleRssi(res.rssi.split(",")[0].toDouble().toInt())
                if (curr == null || curr < rssi)
                    searchTags[res.epc] = rssi

                if (searchTags.size > 10 || start - System.currentTimeMillis() > 200) {
                    onTags(searchTags.toList())
                    searchTags.clear()
                    delay(100)
                    start = System.currentTimeMillis()
                }
            }
        }
    }

    // код при поиске выдает в процентах диапазон расстояний от 0 до 19
    // опираясь на данные калибровки
    private fun rescaleRssi(rawRssi: Int): Int {
        var rssi = rawRssi
        if (rssi > distanceNear)
            rssi = distanceNear
        if (rssi < distanceFar)
            rssi = distanceFar
        val scaled = floor(((rssi - distanceFar) / range) * 20).toInt()
        Log.d("chainwayReader", "Tag rssi $rawRssi = $scaled")
        return scaled
    }


    // ищем именно одну метку - до записи. Для пользователя все метки физически одинаковые.
    // Нам нужно убедиться что он не перепутает и не наклеит не то. Для этого следим что ридер
    // читает только одну метку. Обычно и мощность нужно ставить пониженную мощность
    override fun readSingleEpc(power: Int): Triple<String?, Boolean, String?> {
        if (!isReaderInitialized)
            return Triple(null, false, "Считыватель не инициализирован")
        if (isRunned)
            return Triple(null, false, "Считыватель занят")
        val (isOk, err) = setPwr(power)
        if (!isOk)
            return Triple(null, false, err!!)
        val tags = HashSet<String>()
        bgJob?.cancel()
        bgJob = runBgRead { res -> res.map { tags.add(it) } }
        isRunned = mReader!!.startInventoryTag()
        if (!isRunned) {
            return Triple(null, false, "Ошибка чтения")
        }
        sleep(singleTimeout)
        stop()
        if (tags.isEmpty())
            return Triple(null, false, "Нет меток")
        if (tags.size > 1)
            return Triple(null, false, "Много меток (${tags.size}шт.)")
        Log.d("chainwayReader", "Tag readed single ${tags.first()}")
        return Triple(tags.first(), true, null)
    }

    override fun readEpcs(power: Int): Triple<List<String>, Boolean, String?> {
        if (!isReaderInitialized)
            return Triple(listOf(), false, "Считыватель не инициализирован")
        if (isRunned)
            return Triple(listOf(), false, "Считыватель занят")
        val (isOk, err) = setPwr(power)
        if (!isOk)
            return Triple(listOf(), false, err!!)
        val tags = HashSet<String>()
        bgJob?.cancel()
        bgJob = runBgRead { res -> res.map { tags.add(it) } }
        isRunned = mReader!!.startInventoryTag()
        if (!isRunned) {
            return Triple(listOf(), false, "Ошибка чтения")
        }
        sleep(singleTimeout)
        stop()
        return Triple(tags.toList(), true, null)
    }

    override fun writeSingleEpc(
        power: Int,
        targetEpc: String,
        newEpc: String
    ): Pair<Boolean, String?> {
        Log.d("chainwayReader", "Tag try write $newEpc to $targetEpc")
        if (!isReaderInitialized)
            return Pair(false, "Считыватель не инициализирован")
        if (isRunned)
            return Pair(false, "Считыватель занят")
        val (isOk, err) = setPwr(power)
        if (!isOk)
            return Pair(false, err!!)
        sleep(200)

        val ret = mReader!!.writeData(
            "00000000",
            IUHF.Bank_EPC, 32, 96, targetEpc.uppercase(),
            IUHF.Bank_EPC, 2, 6, newEpc.uppercase()
        )

        if (!ret) {
            return Pair(false, "Ошибка записи")
        }
        Log.d("chainwayReader", "Tag write $newEpc")
        return Pair(true, null)
    }

    override fun readSingleReserved(
        power: Int,
        targetEpc: String
    ): Pair<String?, String?> {
        Log.d("chainwayReader", "Tag try readSingleReserved from $targetEpc")
        if (!isReaderInitialized)
            return Pair(null, "Считыватель не инициализирован")
        if (isRunned)
            return Pair(null, "Считыватель занят")
        val (isOk, err) = setPwr(power)
        if (!isOk)
            return Pair(null, err!!)
        sleep(200)
        val ret = try {
            mReader!!.readData(
                "00000000",
                IUHF.Bank_EPC, 32, 96, targetEpc.uppercase(),
                IUHF.Bank_RESERVED, 0, 4
            )
        } catch (ex: Exception) {
            Log.e("chainwayReader", ex.message!!)
            return Pair(null, "Ошибка чтения: ${ex.message}")
        }
        if (ret == null || ret.isEmpty()) {
            return Pair(null, "Не удалось считать данные - пустой ответ")
        }
        Log.d("chainwayReader", "Tag read $targetEpc Reserved:$ret")
        return Pair(ret, null)
    }

    override fun writeSingleReserved(
        power: Int,
        targetEpc: String,
        newReserved: ByteArray
    ): Pair<Boolean, String?> {
        Log.d("chainwayReader", "Tag try writeSingleReserved to $targetEpc with $newReserved")
        if (newReserved.size != 8) {
            return Pair(false, "Неправильный формат. Ожидалось Reserved 8 байт")
        }
        if (!isReaderInitialized)
            return Pair(false, "Считыватель не инициализирован")
        if (isRunned)
            return Pair(false, "Считыватель занят")

        val (isOk, err) = setPwr(power)
        if (!isOk)
            return Pair(false, err!!)
        sleep(200)
        val ret = try {
            mReader!!.writeData(
                "00000000",
                IUHF.Bank_EPC, 32, 96, targetEpc.uppercase(),
                IUHF.Bank_RESERVED, 0, 4, newReserved.toHex()
            )
        } catch (ex: Exception) {
            Log.e("chainwayReader", ex.message!!)
            return Pair(false, "Ошибка записи: ${ex.message}")
        }
        if (!ret) {
            return Pair(false, "Не удалось записать данные")
        }
        Log.d("chainwayReader", "Tag writeSingleReserved $targetEpc Reserved:$newReserved OK")
        return Pair(true, null)
    }

    companion object {
        fun ByteArray.toHex() =
            this.joinToString(separator = "") { it.toInt().and(0xff).toString(16).padStart(2, '0') }
                .uppercase()

        @SuppressLint("LongLogTag")
        private fun ignoreErr(oper: () -> Unit) {
            try {
                oper()
            } catch (ex: Exception) {
                Log.d("ignoredErr(chainwayReader)", ex.message ?: "")
            }
        }
    }
}