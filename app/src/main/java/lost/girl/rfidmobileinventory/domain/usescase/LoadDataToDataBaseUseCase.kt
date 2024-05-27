package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository
import lost.girl.rfidmobileinventory.utils.LoadDataToDataBaseUtil

class LoadDataToDataBaseUseCase(
    private val inventoryRepository: InventoryRepository
) {
    suspend fun execute(data: Array<Array<String>>): Boolean {
       return LoadDataToDataBaseUtil.load(inventoryRepository, data)
    }
}