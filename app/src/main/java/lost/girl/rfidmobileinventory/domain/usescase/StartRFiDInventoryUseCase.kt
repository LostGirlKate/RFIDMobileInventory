package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

class StartRFiDInventoryUseCase(private val repository: RFIDReaderRepository) {
    fun execute(
        power: Int,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<String>) -> Unit
    ) {
        repository.startInventory(power, onError, onTags)
    }
}
