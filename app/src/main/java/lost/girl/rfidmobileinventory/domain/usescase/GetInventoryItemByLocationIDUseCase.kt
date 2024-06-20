package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

// use case Получение списка ТМЦ по местоположению
class GetInventoryItemByLocationIDUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(locationID: Int): List<InventoryItemForListModel> {
        return inventoryRepository.getInventoryItemByLocationID(locationID)
            .sortedBy { it.inventoryNum }
    }
}
