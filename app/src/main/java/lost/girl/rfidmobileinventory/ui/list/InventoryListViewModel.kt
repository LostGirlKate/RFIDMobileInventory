package lost.girl.rfidmobileinventory.ui.list

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lost.girl.rfidmobileinventory.domain.usescase.GetAllLocationsUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemByLocationIDUseCase
import lost.girl.rfidmobileinventory.mvi.MviViewModel

class InventoryListViewModel(
    private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
    private val getInventoryInfo: GetInventoryInfoUseCase,
    application: Application,
    getAllLocationsUseCase: GetAllLocationsUseCase,
) : MviViewModel<InventoryListViewState, InventoryListViewEffect, InventoryListViewEvent>(
    application
),
    DefaultLifecycleObserver {

    init {
        viewState = InventoryListViewState(
            locations = getAllLocationsUseCase.execute()
        )
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewState = viewState.copy(
            inventoryState = getInventoryInfo.execute(viewState.currentLocationID),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocationID
            )
        )
    }

    override fun process(viewEvent: InventoryListViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is InventoryListViewEvent.EditCurrentLocation -> {
                editCurrentLocation(viewEvent.locationID)
            }
        }
    }

    // Обновление данных(общего списка ТМЦ и информации об инвентаризации) с учетом выбранного фильтра по местоположению
    private fun editCurrentLocation(idLocation: Int) = viewModelScope.launch {
        viewState = viewState.copy(
            currentLocationID = idLocation,
            inventoryState = getInventoryInfo.execute(idLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                idLocation
            )
        )
    }
}
