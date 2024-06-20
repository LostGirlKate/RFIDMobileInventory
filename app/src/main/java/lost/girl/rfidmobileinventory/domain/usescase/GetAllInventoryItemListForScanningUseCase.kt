package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.models.InventoryItemForScanningModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

// use case Получение общего списка меток для сканирования
class GetAllInventoryItemListForScanningUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(): List<InventoryItemForScanningModel> {
        return inventoryRepository.getAllInventoryItemListForRfidScanning()
    }
}
