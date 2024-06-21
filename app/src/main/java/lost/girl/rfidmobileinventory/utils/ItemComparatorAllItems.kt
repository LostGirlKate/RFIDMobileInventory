package lost.girl.rfidmobileinventory.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel

class ItemComparatorAllItems : DiffUtil.ItemCallback<InventoryItemForListModel>() {
    override fun areItemsTheSame(
        oldItem: InventoryItemForListModel,
        newItem: InventoryItemForListModel,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: InventoryItemForListModel,
        newItem: InventoryItemForListModel,
    ): Boolean {
        return oldItem == newItem
    }
}
