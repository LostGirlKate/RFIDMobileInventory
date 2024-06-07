package lost.girl.rfidmobileinventory.data.repository

import android.content.Context
import lost.girl.rfidmobileinventory.data.readers.barcode2D.BarcodeReader
import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

class BarcodeReaderRepositoryImpl(private val reader: BarcodeReader) : BarcodeReaderRepository {
    override fun start(): Boolean {
        return reader.start()
    }

    override fun stop() {
        reader.stop()
    }

    override fun close() {
        reader.close()
    }

    override suspend fun open(context: Context, onSuccess: (String) -> Unit): Boolean {
        return reader.open(context, onSuccess)
    }
}