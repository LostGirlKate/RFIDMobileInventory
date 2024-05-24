package lost.girl.rfidmobileinventory.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

    )