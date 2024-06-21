package lost.girl.rfidmobileinventory.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.databinding.InventoryDetailItemBinding
import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailModel
import lost.girl.rfidmobileinventory.utils.ItemComparator

class InventoryItemDetailAdapter :
    ListAdapter<InventoryItemDetailModel, InventoryItemDetailAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = InventoryDetailItemBinding.bind(view)

        fun setData(detail: InventoryItemDetailModel) = with(binding) {
            paramName.text = detail.paraName
            paramValue.text = detail.value
            // отображаем box с цветом статуса только на параметре - Статус
            statusBox.visibility = if (detail.isStatus) View.VISIBLE else View.GONE
            if (detail.isStatus && detail.statusColor > 0) {
                statusBox.background =
                    ContextCompat.getDrawable(binding.root.context, detail.statusColor)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.inventory_detail_item, parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position))
    }
}

