package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class GetAllInventoryItemUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(): List<InventoryItemFullModel> {
        return inventoryRepository.getAllInventoryFullList()
    }
}