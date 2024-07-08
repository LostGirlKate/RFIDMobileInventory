package lost.girl.rfidmobileinventory.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.databinding.InventoryItemBinding
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemState
import lost.girl.rfidmobileinventory.domain.models.toColor
import lost.girl.rfidmobileinventory.utils.FilterableListAdapter
import lost.girl.rfidmobileinventory.utils.ItemComparatorAllItems
import lost.girl.rfidmobileinventory.utils.OnItemClickListener

const val FILTER_INDEX_NOT_FOUND = 1
const val FILTER_INDEX_FOUND = 2
const val FILTER_INDEX_FOUND_IN_WRONG_PLACE = 3

class InventoryItemsFilterableAdapter(
    private val itemClickListener: OnItemClickListener<InventoryItemForListModel>,
    delimiter: String,
) :
    FilterableListAdapter<InventoryItemForListModel, InventoryItemsFilterableAdapter.ItemHolder>(
        ItemComparatorAllItems(),
        delimiter
    ) {

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = InventoryItemBinding.bind(view)

        fun setData(
            item: InventoryItemForListModel,
            itemClickListener: OnItemClickListener<InventoryItemForListModel>,
        ) = with(binding) {
            itemName.text = item.model
            itemLocation.text = item.location
            itemNum.text = item.inventoryNum
            managerName.text = item.managerName
            rfidNum.text = item.rfidCardNum
            backgroudConstrain.background = ContextCompat.getDrawable(
                binding.root.context,
                item.state.toColor()
            )
            cardView.setOnClickListener {
                itemClickListener.onItemClick(item)
            }
            cardView.setOnLongClickListener {
                itemClickListener.onLongClick(item)
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
        constraint: String,
        delimiter: String,
    ): List<InventoryItemForListModel> {
        val filterList = constraint.split(delimiter)
        val firstFilter = filterList.first().lowercase()
        val showNotFound = filterList[FILTER_INDEX_NOT_FOUND].toBoolean()
        val showFound = filterList[FILTER_INDEX_FOUND].toBoolean()
        val showFoundInWrongPlace = filterList[FILTER_INDEX_FOUND_IN_WRONG_PLACE].toBoolean()
        return list.filter {
            (
                it.model.lowercase().contains(firstFilter) ||
                    it.inventoryNum.lowercase().contains(firstFilter)
                ) &&
                (showNotFound || it.state != InventoryItemState.STATE_NOT_FOUND) &&
                (showFound || it.state != InventoryItemState.STATE_FOUND) &&
                (showFoundInWrongPlace || it.state != InventoryItemState.STATE_FOUND_IN_WRONG_PLACE)
        }
    }
}
