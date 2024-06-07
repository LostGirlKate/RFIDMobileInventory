package lost.girl.rfidmobileinventory.data.repository

import android.content.Context
import lost.girl.rfidmobileinventory.data.readers.rfid.IRfidReader
import lost.girl.rfidmobileinventory.data.readers.rfid.toPower
import lost.girl.rfidmobileinventory.data.readers.rfid.toPowerPercent
import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

class RFIDReaderRepositoryImpl(private val reader: IRfidReader) : RFIDReaderRepository {
    override suspend fun initReader(context: Context): Boolean {
        return reader.poweron(context)
    }

    override fun stopReader() {
        return reader.poweroff()
    }

    override fun startInventory(
        power: Int,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<String>) -> Unit
    ) {
        reader.start(power.toPower(), onError, onTags)
    }

    override fun stopInventory() {
        reader.stop()
    }

    override fun gerPower(): Int {
        return reader.getPower().toPowerPercent()
    }

    override fun isReaderInitialized(): Boolean {
        return reader.isReaderInitialized
    }
}