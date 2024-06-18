package lost.girl.rfidmobileinventory.data.readers.barcode2D

import android.content.Context

interface BarcodeReader {
    var isOpen: Boolean
    fun start(): Boolean
    fun stop()
    fun close()
    suspend fun open(context: Context, onSuccess: (String) -> Unit): Boolean
}
