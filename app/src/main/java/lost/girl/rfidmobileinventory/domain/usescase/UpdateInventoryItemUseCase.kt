package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class UpdateInventoryItemUseCase(private val inventoryRepository: InventoryRepository) {

    suspend fun execute(locationID: Int, location: String, id: Int) {
        return inventoryRepository.updateLocationInventoryItemByID(locationID, location, id)
    }
}