package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

// use case Установить статус найдено вручную
class SetFoundInventoryItemByIDUseCase(private val repository: InventoryRepository) {
    fun execute(id: Int) {
        return repository.setFoundInventoryItemByID(id)
    }
}
