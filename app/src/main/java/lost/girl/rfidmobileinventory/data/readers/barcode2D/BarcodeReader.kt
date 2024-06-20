package lost.girl.rfidmobileinventory.data.readers.barcode2D

import android.content.Context

interface BarcodeReader {

    // статус сканера (инициализирован или нет)
    var isOpen: Boolean

    // старт сканирования
    fun start(): Boolean

    // остановка сканирования
    fun stop()

    // закрытие 2D сканера
    fun close()

    // инициализация 2D сканера с установкой callback для обработки результата сканировния
    suspend fun open(context: Context, onSuccess: (String) -> Unit): Boolean
}
