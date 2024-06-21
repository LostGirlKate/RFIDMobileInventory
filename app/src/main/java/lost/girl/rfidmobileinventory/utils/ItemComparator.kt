package lost.girl.rfidmobileinventory.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailModel

class ItemComparator : DiffUtil.ItemCallback<InventoryItemDetailModel>() {
    override fun areItemsTheSame(
        oldItem: InventoryItemDetailModel,
        newItem: InventoryItemDetailModel,
    ): Boolean {
        return oldItem.paraName == newItem.paraName
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: InventoryItemDetailModel,
        newItem: InventoryItemDetailModel,
    ): Boolean {
        return oldItem == newItem
    }
}
