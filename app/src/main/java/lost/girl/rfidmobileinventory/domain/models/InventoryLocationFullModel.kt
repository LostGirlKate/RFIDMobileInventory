package lost.girl.rfidmobileinventory.domain.models

import lost.girl.rfidmobileinventory.data.storage.models.InventoryLocation

data class InventoryLocationFullModel(
    val id: Int?,
    val name: String
) {
    fun toInventoryLocation() = InventoryLocation(
        id = this.id,
        name = this.name
    )
}
