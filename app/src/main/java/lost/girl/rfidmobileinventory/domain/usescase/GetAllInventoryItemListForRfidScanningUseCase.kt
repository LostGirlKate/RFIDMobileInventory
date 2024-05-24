package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class GetAllInventoryItemListForRfidScanningUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(): List<Pair<Int, String>> {
        return inventoryRepository.getAllInventoryItemListForRfidScanning()
    }
}