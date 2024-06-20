package lost.girl.rfidmobileinventory.data.repository

import android.content.Context
import lost.girl.rfidmobileinventory.data.readers.barcode2D.BarcodeReader
import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

class BarcodeReaderRepositoryImpl(private val reader: BarcodeReader) : BarcodeReaderRepository {

    // старт сканирования
    override fun start(): Boolean {
        return reader.start()
    }

    // остановка сканирования
    override fun stop() {
        reader.stop()
    }

    // закрытие 2D сканера
    override fun close() {
        reader.close()
    }

    // инициализация 2D сканера с установкой callback для обработки результата сканировния
    override suspend fun open(context: Context, onSuccess: (String) -> Unit): Boolean {
        return reader.open(context, onSuccess)
    }
}
