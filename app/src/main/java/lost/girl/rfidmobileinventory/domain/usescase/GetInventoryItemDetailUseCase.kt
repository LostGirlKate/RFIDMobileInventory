package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class GetInventoryItemDetailUseCase(
    private val repository: InventoryRepository,
    private val context: Context
) {
    fun execute(id: Int, statusColor: Int): List<InventoryItemDetailModel> {
        val itemDetail = repository.getInventoryItemDetail(id)
        return itemDetail.let {
            listOf(
                InventoryItemDetailModel(
                    context.getString(R.string.status),
                    it.status,
                    true,
                    statusColor
                ),
                InventoryItemDetailModel(context.getString(R.string.type), it.type),
                InventoryItemDetailModel(context.getString(R.string.model), it.model),
                InventoryItemDetailModel(context.getString(R.string.location), it.location),
                InventoryItemDetailModel(
                    context.getString(R.string.location_fact),
                    it.actualLocation.orEmpty()
                ),
                InventoryItemDetailModel(context.getString(R.string.inventory_num), it.inventoryNum),
                InventoryItemDetailModel(context.getString(R.string.serial_num), it.serialNum),
                InventoryItemDetailModel(context.getString(R.string.shipment_num), it.shipmentNum),
                InventoryItemDetailModel(context.getString(R.string.rfid_dec), it.rfidCardNum),
                InventoryItemDetailModel(context.getString(R.string.manager_name), it.managerName)
            )
        }
    }
}