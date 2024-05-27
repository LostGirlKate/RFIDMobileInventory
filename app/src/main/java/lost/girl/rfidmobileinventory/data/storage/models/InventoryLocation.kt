package lost.girl.rfidmobileinventory.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel

@Entity(tableName = "inventory_location")
class InventoryLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String
) {
    fun toInventoryLocationFullModel() = InventoryLocationFullModel(
        id = this.id,
        name = this.name
    )

}