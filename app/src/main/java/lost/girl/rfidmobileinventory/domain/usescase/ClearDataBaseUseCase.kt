package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

// use case Очистка DB
class ClearDataBaseUseCase(
    private val inventoryRepository: InventoryRepository,
    private val context: Context,
) {
    suspend fun execute(): Boolean {
        return inventoryRepository.clearAll(context)
    }
}
