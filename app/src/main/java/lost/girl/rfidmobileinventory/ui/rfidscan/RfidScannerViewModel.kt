package lost.girl.rfidmobileinventory.ui.rfidscan

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.domain.usescase.GetAllInventoryItemListForRfidScanningUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemByLocationIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.UpdateInventoryItemUseCase
import lost.girl.rfidmobileinventory.mvi.MviViewModel
import ru.lostgirl.testforlongwork.RfidReader

class RfidScannerViewModel(
    application: Application,
    private val updateInventoryItemUseCase: UpdateInventoryItemUseCase,
    private val getAllInventoryItemListForRfidScanningUseCase: GetAllInventoryItemListForRfidScanningUseCase,
    private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
    private val getInventoryInfo: GetInventoryInfoUseCase

) : MviViewModel<RfidScannerViewState, RfidScannerViewEffect, RfidScannerViewEvent>(application),
    DefaultLifecycleObserver {

    private val reader: RfidReader = RfidReader()
    private var readerJob: Job? = null

    init {
        viewState =
            RfidScannerViewState(
                inventoryItemsFullRFIDList = getAllInventoryItemListForRfidScanningUseCase.execute(),
                scannerPowerValue = reader.getPower()
            )
    }

    override fun process(viewEvent: RfidScannerViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is RfidScannerViewEvent.SetCurrentLocation -> setCurrentLocation(
                viewEvent.iLocation,
                viewEvent.locationName
            )

            is RfidScannerViewEvent.SetScannerPowerValue -> setScannerPowerValue(viewEvent.powerValue)
            is RfidScannerViewEvent.SetScanningState -> setScanningState(viewEvent.state)
            RfidScannerViewEvent.ClearEffect -> viewEffect = RfidScannerViewEffect.EmptyEffect
        }

    }

    private fun startInventory() {
        reader.startInventoryTag()
        scanningProcessRfid()
    }


    private fun stopInventory() {
        reader.stopInventory()
        readerJob?.cancel()
    }

    private fun initReader() {
        reader.init()
    }

    private fun scanningProcessRfid() {
        readerJob = viewModelScope.launch(Dispatchers.IO) {
            while (viewState.isScanningStart) {
                val rfidTag = reader.readTagFromBuffer()
                checkItemNewLocationAndUpdate(rfidTag, viewState.currentLocation)
            }
        }

    }

    private fun setCurrentLocation(locationId: Int, locationName: String) {
        viewState = viewState.copy(
            currentLocation = locationId,
            currentLocationName = locationName,
            inventoryState = getInventoryInfo.execute(locationId),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                locationId
            )
        )
    }

    private fun setScanningState(state: Boolean) {
        viewState = viewState.copy(
            canBackPress = !state,
            isScanningStart = state,
            startRfidScanningButtonVisible = !state,
            panelSettingsVisible = !state,
            stopScanningButtonVisible = state
        )
        if (state) startInventory() else stopInventory()
    }

    private fun setScannerPowerValue(value: Int) {
        viewState = viewState.copy(
            scannerPowerValue = value
        )
        reader.setPower(value)
    }


    private suspend fun updateInventoryItem(locationID: Int, location: String, id: Int) {
        updateInventoryItemUseCase.execute(locationID, location, id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfo.execute(viewState.currentLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocation
            )
        )
    }

    private suspend fun checkItemNewLocationAndUpdate(rfidCardNum: String, locationId: Int) {
        val allItem = viewState.inventoryItemsFullRFIDList
        if (!allItem.none { it.second == rfidCardNum }) {
            val itemId = allItem.first { it.second == rfidCardNum }.first
            updateInventoryItem(viewState.currentLocation, viewState.currentLocationName, itemId)
            if (viewState.inventoryState.percentFound == 100)
                viewEffect = RfidScannerViewEffect.InventoryReady(R.string.all_tag_found)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (viewState.isScanningStart) {
            setScanningState(false)
            stopInventory()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        initReader()
    }

    class RfidScannerViewModelFactory(
        private val application: Application,
        private val updateInventoryItemUseCase: UpdateInventoryItemUseCase,
        private val getAllInventoryItemListForRfidScanningUseCase: GetAllInventoryItemListForRfidScanningUseCase,
        private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
        private val getInventoryInfo: GetInventoryInfoUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RfidScannerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RfidScannerViewModel(
                    application,
                    updateInventoryItemUseCase,
                    getAllInventoryItemListForRfidScanningUseCase,
                    getInventoryItemByLocationIDUseCase,
                    getInventoryInfo
                ) as T
            }
            throw java.lang.IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}