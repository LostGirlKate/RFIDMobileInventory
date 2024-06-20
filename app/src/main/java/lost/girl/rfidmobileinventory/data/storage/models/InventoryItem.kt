package lost.girl.rfidmobileinventory.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForDetailFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForExportModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForScanningModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemState
import lost.girl.rfidmobileinventory.domain.models.toStr

// Единица инвентаризации (ТМЦ)
// id - идентификатор
// inventoryNum - инвентарный номер бухгалтерии
// managerName - сотрудник
// locationID - id местоположения
// location - местоположение (начальное из файла)
// type - тип
// model - модель
// serialNum - серийный номер
// shipmentNum - номер партии (шк EAN-13)
// rfidCardNum - RFID Dec
// actualLocationID - id актуального местоположения
// actualLocation - актуальное местоположение (назначается при проведении инвентаризации)
@Entity(tableName = "inventory_item")
class InventoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "inventory_num")
    val inventoryNum: String,

    @ColumnInfo(name = "manager_name")
    val managerName: String,

    @ColumnInfo(name = "location_id")
    val locationID: Int?,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "model")
    val model: String,

    @ColumnInfo(name = "serial_num")
    val serialNum: String,

    @ColumnInfo(name = "shipment_num")
    val shipmentNum: String,

    @ColumnInfo(name = "rfid_card_num")
    val rfidCardNum: String,

    @ColumnInfo(name = "actual_location_id")
    val actualLocationID: Int?,

    @ColumnInfo(name = "actual_location")
    val actualLocation: String?,
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

    fun toInventoryItemFullModelForDetail() =
        InventoryItemForDetailFullModel(
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
            status = getState().toStr()
        )

    fun toInventoryItemModelForList() =
        InventoryItemForListModel(
            id = this.id,
            model = this.model,
            state = getState(),
            inventoryNum = this.inventoryNum,
            location = this.actualLocation ?: this.location
        )

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

    fun toInventoryItemForScanningModel() =
        InventoryItemForScanningModel(
            id = this.id,
            shipmentNum = this.shipmentNum,
            rfidCardNum = this.rfidCardNum
        )

    // вычисление статуса ТМЦ
    private fun getState() =
        when (this.actualLocationID) {
            null -> InventoryItemState.STATE_NOT_FOUND
            this.locationID -> InventoryItemState.STATE_FOUND
            else -> InventoryItemState.STATE_FOUND_IN_WRONG_PLACE
        }
}
