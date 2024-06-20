package lost.girl.rfidmobileinventory.domain.models

import lost.girl.rfidmobileinventory.R

const val INVENTORY_ITEM_STATE_FOUND = "Найдено"
const val INVENTORY_ITEM_STATE_NOT_FOUND = "Не найдено"
const val INVENTORY_ITEM_STATE_FOUND_IN_WRONG_PLACE = "Найдено не на своем месте"

// Статусы ТМЦ
enum class InventoryItemState {
    STATE_FOUND, STATE_NOT_FOUND, STATE_FOUND_IN_WRONG_PLACE
}

// преобразование статуса ТМЦ в строку
fun InventoryItemState.toStr() = when (this) {
    InventoryItemState.STATE_FOUND -> INVENTORY_ITEM_STATE_FOUND
    InventoryItemState.STATE_NOT_FOUND -> INVENTORY_ITEM_STATE_NOT_FOUND
    InventoryItemState.STATE_FOUND_IN_WRONG_PLACE -> INVENTORY_ITEM_STATE_FOUND_IN_WRONG_PLACE
}

// преобразовать в цвет
fun InventoryItemState.toColor() = when (this) {
    InventoryItemState.STATE_FOUND -> R.drawable.green_item_background
    InventoryItemState.STATE_NOT_FOUND -> R.drawable.red_item_background
    InventoryItemState.STATE_FOUND_IN_WRONG_PLACE -> R.drawable.orange_item_background
}
