package lost.girl.rfidmobileinventory.domain.models

import android.content.Context
import lost.girl.rfidmobileinventory.R

data class InventoryItemForDetailFullModel(
    val id: Int?,
    val inventoryNum: String,
    var managerName: String,
    var location: String,
    var type: String,
    var model: String,
    var serialNum: String,
    var shipmentNum: String,
    val rfidCardNum: String,
    var actualLocation: String?,
    var status: String
) {
    fun toListOfDetail(context: Context, statusColor: Int): List<InventoryItemDetailModel> {
        return this.let {
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
