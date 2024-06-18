package lost.girl.rfidmobileinventory.domain.repository

import android.content.Context

interface RFIDReaderRepository {
    suspend fun initReader(context: Context): Boolean
    fun stopReader()
    fun startInventory(
        power: Int,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<String>) -> Unit
    )

    fun stopInventory()
    fun getPower(): Int

    fun isReaderInitialized(): Boolean
}
