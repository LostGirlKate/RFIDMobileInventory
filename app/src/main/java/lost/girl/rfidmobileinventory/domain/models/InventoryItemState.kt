package lost.girl.rfidmobileinventory.domain.models

import androidx.annotation.Keep

@Keep
enum class InventoryItemState {
    STATE_FOUND, STATE_NOT_FOUND, STATE_FOUND_IN_WRONG_PLACE
}