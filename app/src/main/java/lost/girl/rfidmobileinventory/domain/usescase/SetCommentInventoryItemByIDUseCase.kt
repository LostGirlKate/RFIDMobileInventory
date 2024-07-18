package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

// use case Установить примечание
class SetCommentInventoryItemByIDUseCase(private val repository: InventoryRepository) {
    fun execute(id: Int, comment: String) {
        return repository.setCommentInventoryItemByID(id, comment)
    }
}