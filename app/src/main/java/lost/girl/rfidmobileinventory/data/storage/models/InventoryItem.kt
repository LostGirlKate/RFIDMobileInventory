package lost.girl.rfidmobileinventory.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForDetailFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForExportModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemState

@Entity(tableName = "inventory_item")
class InventoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "inventory_num")
    val inventoryNum: String,

    @ColumnInfo(name = "manager_name")
    var managerName: String,

    @ColumnInfo(name = "location_id")
    var locationID: Int?,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "type")
    var type: String,

    @ColumnInfo(name = "model")
    var model: String,

    @ColumnInfo(name = "serial_num")
    var serialNum: String,

    @ColumnInfo(name = "shipment_num")
    var shipmentNum: String,

    @ColumnInfo(name = "rfid_card_num")
    val rfidCardNum: String,

    @ColumnInfo(name = "actual_location_id")
    var actualLocationID: Int?,

    @ColumnInfo(name = "actual_location")
    var actualLocation: String?,

    ) {
    fun toInventoryItemForExportModel(num: Int) =
        InventoryItemForExportModel(
            rowNum = num.toString(),
            inventoryNum = this.inventoryNum,
            managerName = this.managerName,
            location = this.location,
            type = this.type,
            model = this.model,
            serialNum = this.serialNum,
            shipmentNum = this.shipmentNum,
            rfidCardNum = this.rfidCardNum,
            actualLocation = this.actualLocation.toString()
        )

    fun toInventoryItemFullModelForDetail(): InventoryItemForDetailFullModel {
        val state = when (this.actualLocationID) {
            null -> "Не найдено"
            this.locationID -> "Найдено"
            else -> "Найдено не на своем месте"
        }
        return InventoryItemForDetailFullModel(
            id = this.id,
            inventoryNum = this.inventoryNum,
            managerName = this.managerName,
            location = this.location,
            type = this.type,
            model = this.model,
            serialNum = this.serialNum,
            shipmentNum = this.shipmentNum,
            rfidCardNum = this.rfidCardNum,
            actualLocation = this.actualLocation,
            status = state
        )
    }

    fun toInventoryItemModelForList(): InventoryItemForListModel {
        val state = when (this.actualLocationID) {
            null -> InventoryItemState.STATE_NOT_FOUND
            this.locationID -> InventoryItemState.STATE_FOUND
            else -> InventoryItemState.STATE_FOUND_IN_WRONG_PLACE
        }
        return InventoryItemForListModel(
            id = this.id,
            model = this.model,
            state = state,
            inventoryNum = this.inventoryNum,
            location = this.actualLocation ?: this.location
        )
    }

    fun toInventoryItemFullModel() =
        InventoryItemFullModel(
            id = this.id,
            inventoryNum = this.inventoryNum,
            managerName = this.managerName,
            locationID = this.locationID,
            location = this.location,
            type = this.type,
            model = this.model,
            serialNum = this.serialNum,
            shipmentNum = this.shipmentNum,
            rfidCardNum = this.rfidCardNum,
            actualLocationID = this.actualLocationID,
            actualLocation = this.actualLocation
        )

}