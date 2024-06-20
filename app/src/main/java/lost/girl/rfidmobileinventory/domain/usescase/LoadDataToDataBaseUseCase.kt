package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository
import lost.girl.rfidmobileinventory.utils.LoadDataToDataBaseUtil

// use case Загрузка массива данных в локальную BD
class LoadDataToDataBaseUseCase(
    private val inventoryRepository: InventoryRepository,
) {
    suspend fun execute(data: Array<Array<String>>): Boolean {
        return LoadDataToDataBaseUtil.load(inventoryRepository, data)
    }
}
