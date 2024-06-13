package lost.girl.rfidmobileinventory.ui.rfidscan

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.data.readers.ReaderType
import lost.girl.rfidmobileinventory.domain.usescase.CloseBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetAllInventoryItemListForScanningUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemByLocationIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetRFIDReaderPowerUseCase
import lost.girl.rfidmobileinventory.domain.usescase.IsRFIDReaderInitializedUseCase
import lost.girl.rfidmobileinventory.domain.usescase.OpenBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StartBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StartRFiDInventoryUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StopBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StopRFIDInventoryUseCase
import lost.girl.rfidmobileinventory.domain.usescase.UpdateInventoryItemUseCase
import lost.girl.rfidmobileinventory.mvi.MviViewModel

class RfidScannerViewModel(
    application: Application,
    private val updateInventoryItemUseCase: UpdateInventoryItemUseCase,
    private val getAllInventoryItemListForRfidScanningUseCase: GetAllInventoryItemListForScanningUseCase,
    private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
    private val getInventoryInfoUseCase: GetInventoryInfoUseCase,
    private val startRFiDInventoryUseCase: StartRFiDInventoryUseCase,
    private val stopRFiDInventoryUseCase: StopRFIDInventoryUseCase,
    private val getRFIDReaderPowerUseCase: GetRFIDReaderPowerUseCase,
    private val isRFIDReaderInitializedUseCase: IsRFIDReaderInitializedUseCase,
    private val openBarcodeReaderUseCase: OpenBarcodeReaderUseCase,
    private val closeBarcodeReaderUseCase: CloseBarcodeReaderUseCase,
    private val startBarcodeReaderUseCase: StartBarcodeReaderUseCase,
    private val stopBarcodeReaderUseCase: StopBarcodeReaderUseCase

) : MviViewModel<RfidScannerViewState, RfidScannerViewEffect, RfidScannerViewEvent>(application),
    DefaultLifecycleObserver {
    init {
        viewState =
            RfidScannerViewState(
                inventoryItemsFullRFIDList = getAllInventoryItemListForRfidScanningUseCase.execute(),
                scannerPowerValue = 0
            )
    }

    override fun process(viewEvent: RfidScannerViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is RfidScannerViewEvent.SetCurrentLocation -> setCurrentLocation(
                viewEvent.iLocation,
                viewEvent.locationName
            )

            is RfidScannerViewEvent.SetScanningState -> setScanningState(viewEvent.state)
            is RfidScannerViewEvent.SetScanningPower -> setScannerPowerValue(viewEvent.power)
            is RfidScannerViewEvent.SetScannerType -> setScannerType(viewEvent.type)
        }

    }

    private fun setScannerType(type: ReaderType) {
        initReader(type)
    }


    private fun startInventory() {
        when (viewState.scannerType) {
            ReaderType.RFID -> {
                val power = viewState.scannerPowerValue
                startRFiDInventoryUseCase.execute(power, onError = this::onError) {
                    for (tag in it) {
                        checkItemNewLocationAndUpdate(tag)
                    }
                }
            }

            ReaderType.BARCODE_2D -> {
                val start = startBarcodeReaderUseCase.execute()
                Log.d("start", start.toString())
            }

            null -> {}
        }

    }

    private fun onError(message: String) {
        viewEffect = RfidScannerViewEffect.ShowToast(
            R.string.init_rfid_message, R.string.init_error_rfid_message,
            true
        )
    }


    private fun stopInventory() {
        when (viewState.scannerType) {
            ReaderType.RFID -> stopRFiDInventoryUseCase.execute()
            ReaderType.BARCODE_2D -> stopBarcodeReaderUseCase.execute()
            null -> {}
        }
    }

    private fun initReader(type: ReaderType) {
        viewModelScope.launch {
            var result = false
            var power = 0
            when (type) {
                ReaderType.RFID -> {
                    result = isRFIDReaderInitializedUseCase.execute()
                    if (result) {
                        power = getRFIDReaderPowerUseCase.execute()// setScannerPowerValue()
                    }
                }

                ReaderType.BARCODE_2D -> {
                    result = withContext(Dispatchers.IO) {
                        openBarcodeReaderUseCase.execute { checkItemNewLocationAndUpdate(it) }
                    }


                }
            }


            viewState = viewState.copy(
                scannerType = type,
                isReaderInit = result,
                panelSettingsVisible = (result && type == ReaderType.RFID),
                startScanningButtonVisible = result,
                scannerPowerValue = power
            )
        }
    }

    private fun setCurrentLocation(locationId: Int, locationName: String) {
        viewState = viewState.copy(
            currentLocation = locationId,
            currentLocationName = locationName,
            inventoryState = getInventoryInfoUseCase.execute(locationId),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                locationId
            )
        )
    }

    private fun setScanningState(state: Boolean) {
        viewState = viewState.copy(
            canBackPress = !state,
            isScanningStart = state,
            startScanningButtonVisible = !state,
            panelSettingsVisible = (!state && viewState.scannerType == ReaderType.RFID),
            stopScanningButtonVisible = state
        )
        if (state) startInventory() else stopInventory()
    }

    private fun setScannerPowerValue(value: Int) {
        viewState = viewState.copy(
            scannerPowerValue = value
        )
    }


    private fun updateInventoryItem(locationID: Int, location: String, id: Int) {
        updateInventoryItemUseCase.execute(locationID, location, id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfoUseCase.execute(viewState.currentLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocation
            )
        )
    }

    private fun checkItemNewLocationAndUpdate(scanCode: String) {
        val allItem = viewState.inventoryItemsFullRFIDList
        var itemId = 0
        when (viewState.scannerType) {
            ReaderType.RFID -> if (!allItem.none { it.rfidCardNum == scanCode }) {
                itemId = allItem.first { it.rfidCardNum == scanCode }.id ?: 0
            }

            ReaderType.BARCODE_2D -> if (!allItem.none { it.shipmentNum == scanCode }) {
                itemId = allItem.first { it.shipmentNum == scanCode }.id ?: 0
            }

            null -> {}
        }
        if (itemId > 0) {
            updateInventoryItem(
                viewState.currentLocation,
                viewState.currentLocationName,
                itemId
            )
            if (viewState.inventoryState.percentFound == 100)
                viewEffect = RfidScannerViewEffect.InventoryReady(R.string.all_tag_found)
        }

        if (viewState.scannerType == ReaderType.BARCODE_2D) {
            setScanningState(false)
        }

    }


    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (viewState.isScanningStart) {
            setScanningState(false)
            stopInventory()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (viewState.scannerType == ReaderType.BARCODE_2D) closeBarcodeReaderUseCase.execute()
    }

}