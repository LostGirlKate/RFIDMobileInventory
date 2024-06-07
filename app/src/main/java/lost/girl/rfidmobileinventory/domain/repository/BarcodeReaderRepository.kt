package lost.girl.rfidmobileinventory.domain.repository

import android.content.Context

interface BarcodeReaderRepository {
    fun start(): Boolean
    fun stop()
    fun close()
    suspend fun open(context: Context, onSuccess: (String) -> Unit): Boolean
}