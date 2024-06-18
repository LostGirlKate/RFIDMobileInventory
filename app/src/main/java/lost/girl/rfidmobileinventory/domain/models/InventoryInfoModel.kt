package lost.girl.rfidmobileinventory.domain.models

data class InventoryInfoModel(
    val countAllString: String = "0",
    val countFoundString: String = "0",
    val countNotFoundString: String = "0",
    val countFoundInWrongPlaceString: String = "0",
    val percentFound: Int = 0,
    val percentFoundString: String = "0",
    val inventoryState: InventoryState = InventoryState.STATE_NOT_START
)
