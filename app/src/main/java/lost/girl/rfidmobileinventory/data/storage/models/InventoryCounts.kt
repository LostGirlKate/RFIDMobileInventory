package lost.girl.rfidmobileinventory.data.storage.models

import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryState
import java.text.DecimalFormat

data class InventoryCounts(
    val countAll: Int,
    val countFound: Int,
    val countNotFound: Int,
    val countFoundInWrongPlace: Int
) {
    fun toInventoryInfoModel(): InventoryInfoModel {
        // расчет статуса инвентаризации на основании общего и найденного кол-ва меток
        val state = when (this.countAll) {
            0 -> InventoryState.STATE_NOT_START
            (this.countFound + this.countFoundInWrongPlace) -> InventoryState.STATE_READY
            else -> InventoryState.STATE_WORK
        }
        val percentFound = if (this.countAll > 0) {
            ((this.countFound + this.countFoundInWrongPlace) / (this.countAll.toDouble()) * 100).toInt()
        } else {
            0
        }

        val percentFoundString = "$percentFound%"

        return InventoryInfoModel(
            countAllString = this.countAll.formatterToString(),
            countFoundString = this.countFound.formatterToString(),
            countNotFoundString = this.countNotFound.formatterToString(),
            countFoundInWrongPlaceString = this.countFoundInWrongPlace.formatterToString(),
            percentFound = percentFound,
            percentFoundString = percentFoundString,
            inventoryState = state
        )
    }

    private fun Int.formatterToString() =
        DecimalFormat("#,###")
            .format(this)
            .replace(",", " ")
}
