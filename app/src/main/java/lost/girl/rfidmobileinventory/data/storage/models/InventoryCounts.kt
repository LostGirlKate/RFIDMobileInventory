package lost.girl.rfidmobileinventory.data.storage.models

data class InventoryCounts(
    val countAll: Int,
    val countFound: Int,
    val countNotFound: Int,
    val countFoundInWrongPlace: Int
)