package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class GetInventoryItemDetailUseCase(
    private val repository: InventoryRepository,
    private val context: Context
) {
    fun execute(id: Int, statusColor: Int): List<InventoryItemDetailModel> {
        val itemDetail = repository.getInventoryItemDetail(id)
        return itemDetail.toListOfDetail(context, statusColor)
    }
}
