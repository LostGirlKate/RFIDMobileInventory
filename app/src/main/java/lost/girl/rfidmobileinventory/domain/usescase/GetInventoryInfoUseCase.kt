package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class GetInventoryInfoUseCase(private val inventoryRepository: InventoryRepository) {
    fun execute(locationID: Int = -1): InventoryInfoModel {
        return inventoryRepository.getInventoryItemsCounts(locationID = locationID)
    }
}
