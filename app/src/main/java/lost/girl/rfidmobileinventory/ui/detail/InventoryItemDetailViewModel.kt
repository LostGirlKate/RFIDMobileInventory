package lost.girl.rfidmobileinventory.ui.detail

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.domain.models.InventoryItemState
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemDetailUseCase
import lost.girl.rfidmobileinventory.mvi.MviViewModel

class InventoryItemDetailViewModel(
    private val getInventoryItemDetailUseCase: GetInventoryItemDetailUseCase,
    application: Application,
) : MviViewModel<InventoryItemDetailState, InventoryItemDetailEffect, InventoryItemDetailEvent>(
    application
),
    DefaultLifecycleObserver {

    init {
        viewState = InventoryItemDetailState()
    }

    override fun process(viewEvent: InventoryItemDetailEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is InventoryItemDetailEvent.OpenDetails -> {
                openDetails(
                    viewEvent.itemId,
                    viewEvent.state
                )
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        viewState = InventoryItemDetailState()
    }

    // Получение списка параметров ТМЦ со значениями из BD
    private fun openDetails(id: Int, state: InventoryItemState) {
        val statusColor = when (state) {
            InventoryItemState.STATE_NOT_FOUND -> R.drawable.red_item_background
            InventoryItemState.STATE_FOUND -> R.drawable.green_item_background
            InventoryItemState.STATE_FOUND_IN_WRONG_PLACE -> R.drawable.orange_item_background
        }
        viewState = viewState.copy(
            details = getInventoryItemDetailUseCase.execute(
                id,
                statusColor
            )
        )
    }
}
