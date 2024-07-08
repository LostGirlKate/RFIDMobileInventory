package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

// use case reset актуального местоположения ТМЦ по id
class ResetLocationInventoryItemByIDUseCase(private val repository: InventoryRepository) {
    fun execute(id: Int) {
        return repository.resetLocationInventoryItemByID(id)
    }
}
