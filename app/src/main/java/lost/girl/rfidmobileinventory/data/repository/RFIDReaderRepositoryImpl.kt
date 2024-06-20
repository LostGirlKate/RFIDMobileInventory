package lost.girl.rfidmobileinventory.data.repository

import android.content.Context
import lost.girl.rfidmobileinventory.data.readers.rfid.IRfidReader
import lost.girl.rfidmobileinventory.data.readers.rfid.toPower
import lost.girl.rfidmobileinventory.data.readers.rfid.toPowerPercent
import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

class RFIDReaderRepositoryImpl(private val reader: IRfidReader) : RFIDReaderRepository {

    // инициализация ридера
    override suspend fun initReader(context: Context): Boolean {
        return reader.poweron(context)
    }

    // остановка ридера
    override fun stopReader() {
        return reader.poweroff()
    }

    // старт инвентаризации (сканирования)
    override fun startInventory(
        power: Int,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<String>) -> Unit,
    ) {
        reader.start(power.toPower(), onError, onTags)
    }

    // остановка инвентаризации (сканирования)
    override fun stopInventory() {
        reader.stop()
    }

    // получение можности ридера в процентах
    override fun getPower(): Int {
        return reader.getPower().toPowerPercent()
    }

    // получение статуса инициализации ридера
    override fun isReaderInitialized(): Boolean {
        return reader.isReaderInitialized
    }
}
