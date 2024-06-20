package lost.girl.rfidmobileinventory.domain.models

import android.content.Context
import lost.girl.rfidmobileinventory.R

// Модель данных ТМЦ с полным списком параметров (+статус для отображения) для окна детализации
data class InventoryItemForDetailFullModel(
    val id: Int?,
    val inventoryNum: String,
    val managerName: String,
    val location: String,
    val type: String,
    val model: String,
    val serialNum: String,
    val shipmentNum: String,
    val rfidCardNum: String,
    val actualLocation: String?,
    val status: String,
) {
    // Преобразование объекта в список параметров
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
