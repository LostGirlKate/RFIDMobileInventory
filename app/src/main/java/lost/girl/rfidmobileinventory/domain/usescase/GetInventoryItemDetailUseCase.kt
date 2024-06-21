package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository
import lost.girl.rfidmobileinventory.utils.ResourcesProvider

// use case Получение списка параметров ТМЦ по id для отображения детализации
class GetInventoryItemDetailUseCase(
    private val repository: InventoryRepository,
    private val resourcesProvider: ResourcesProvider,
) {
    fun execute(id: Int, statusColor: Int): List<InventoryItemDetailModel> {
        val itemDetail = repository.getInventoryItemDetail(id)
        return itemDetail.toListOfDetail(resourcesProvider, statusColor)
    }
}
