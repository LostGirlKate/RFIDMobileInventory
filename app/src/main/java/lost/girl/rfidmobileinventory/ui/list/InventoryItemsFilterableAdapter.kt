package lost.girl.rfidmobileinventory.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.databinding.InventoryItemBinding
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemState
import lost.girl.rfidmobileinventory.utils.FilterableListAdapter
import lost.girl.rfidmobileinventory.utils.OnItemClickListener

class InventoryItemsFilterableAdapter(private val itemClickListener: OnItemClickListener<InventoryItemForListModel>) :
    FilterableListAdapter<InventoryItemForListModel, InventoryItemsFilterableAdapter.ItemHolder>(
        ItemComparatorAllItems()
    ) {

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = InventoryItemBinding.bind(view)

        fun setData(
            item: InventoryItemForListModel,
            itemClickListener: OnItemClickListener<InventoryItemForListModel>
        ) = with(binding) {
            itemName.text = item.model
            itemLocation.text = item.location
            itemNum.text = item.inventoryNum
            when (item.state) {
                InventoryItemState.STATE_NOT_FOUND -> backgroudConstrain.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.red_item_background)

                InventoryItemState.STATE_FOUND -> backgroudConstrain.background =
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.green_item_background
                    )

                InventoryItemState.STATE_FOUND_IN_WRONG_PLACE -> backgroudConstrain.background =
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.orange_item_background
                    )
            }
            cardView.setOnClickListener {
                itemClickListener.onItemClick(item)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.inventory_item, parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), itemClickListener)
    }

    override fun onFilter(
        list: List<InventoryItemForListModel>,
        constraint: String
    ): List<InventoryItemForListModel> {
        val filterList = constraint.split("~")
        val firstFilter = filterList.first().lowercase()
        val showNotFound = filterList[1].toBoolean()
        val showFound = filterList[2].toBoolean()
        val showFoundInWrongPlace = filterList[3].toBoolean()
        return list.filter {
            (it.model.lowercase().contains(firstFilter) ||
              it.inventoryNum.lowercase().contains(firstFilter)
            )
                    && (showNotFound || it.state != InventoryItemState.STATE_NOT_FOUND)
                    && (showFound || it.state != InventoryItemState.STATE_FOUND)
                    && (showFoundInWrongPlace || it.state != InventoryItemState.STATE_FOUND_IN_WRONG_PLACE)
        }
    }
}

class ItemComparatorAllItems : DiffUtil.ItemCallback<InventoryItemForListModel>() {
    override fun areItemsTheSame(
        oldItem: InventoryItemForListModel,
        newItem: InventoryItemForListModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: InventoryItemForListModel,
        newItem: InventoryItemForListModel
    ): Boolean {
        return oldItem == newItem
    }
}