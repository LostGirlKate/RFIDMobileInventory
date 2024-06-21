package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.data.storage.roomdb.MainDataBase
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

// use case Очистка DB
class ClearDataBaseUseCase(
    private val inventoryRepository: InventoryRepository,
    private val dataBase: MainDataBase,
) {
    suspend fun execute(): Boolean {
        return inventoryRepository.clearAll(dataBase)
    }
}
