package lost.girl.rfidmobileinventory.ui.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lost.girl.rfidmobileinventory.domain.usescase.GetAllLocationsUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemByLocationIDUseCase
import lost.girl.rfidmobileinventory.mvi.MviViewModel

class InventoryListViewModel(
    application: Application,
    getAllLocationsUseCase: GetAllLocationsUseCase,
    private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
    private val getInventoryInfo: GetInventoryInfoUseCase
): MviViewModel<InventoryListViewState, InventoryListViewEffect, InventoryListViewEvent>(application){

       init {
        viewState = InventoryListViewState(
            locations = getAllLocationsUseCase.execute(),
            inventoryState = getInventoryInfo.execute(0)
        )
    }


    override fun process(viewEvent: InventoryListViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is InventoryListViewEvent.EditCurrentLocation -> editCurrentLocation(viewEvent.locationID)
        }

    }

    private fun editCurrentLocation(idLocation: Int) = viewModelScope.launch {
        viewState = viewState.copy(
            currentLocationID = idLocation,
            inventoryState = getInventoryInfo.execute(idLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                idLocation
            )
        )
    }


    class InventoryListViewModelFactory(
        private val application: Application,
        private val getAllLocationsUseCase: GetAllLocationsUseCase,
        private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
        private val getInventoryInfo: GetInventoryInfoUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InventoryListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InventoryListViewModel(
                    application,
                    getAllLocationsUseCase,
                    getInventoryItemByLocationIDUseCase,
                    getInventoryInfo
                ) as T
            }
            throw java.lang.IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}