package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

// use case Получение общего списка местоположений
class GetAllLocationsUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(): List<InventoryLocationFullModel> {
        return inventoryRepository.getAllLocationList().sortedBy { it.name }
    }
}
