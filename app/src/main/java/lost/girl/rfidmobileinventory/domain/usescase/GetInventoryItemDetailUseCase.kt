package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailItem
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class GetInventoryItemDetailUseCase(
    private val repository: InventoryRepository,
    private val context: Context
) {
    fun execute(id: Int, statusColor: Int): List<InventoryItemDetailItem> {
        val itemDetail = repository.getInventoryItemDetail(id)
        return itemDetail.let {
            listOf(
                InventoryItemDetailItem(
                    context.getString(R.string.status),
                    it.status,
                    true,
                    statusColor
                ),
                InventoryItemDetailItem(context.getString(R.string.type), it.type),
                InventoryItemDetailItem(context.getString(R.string.model), it.model),
                InventoryItemDetailItem(context.getString(R.string.location), it.location),
                InventoryItemDetailItem(
                    context.getString(R.string.location_fact),
                    it.actualLocation.orEmpty()
                ),
                InventoryItemDetailItem(context.getString(R.string.inventory_num), it.inventoryNum),
                InventoryItemDetailItem(context.getString(R.string.serial_num), it.serialNum),
                InventoryItemDetailItem(context.getString(R.string.shipment_num), it.shipmentNum),
                InventoryItemDetailItem(context.getString(R.string.rfid_dec), it.rfidCardNum),
                InventoryItemDetailItem(context.getString(R.string.manager_name), it.managerName)
            )
        }
    }
}