package lost.girl.rfidmobileinventory.ui.rfidscan

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.data.readers.ReaderType
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.usescase.CloseBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetAllInventoryItemListForScanningUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemByLocationIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetRFIDReaderPowerUseCase
import lost.girl.rfidmobileinventory.domain.usescase.IsRFIDReaderInitializedUseCase
import lost.girl.rfidmobileinventory.domain.usescase.OpenBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.ResetLocationInventoryItemByIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StartBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StartRFiDInventoryUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StopBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StopRFIDInventoryUseCase
import lost.girl.rfidmobileinventory.domain.usescase.UpdateInventoryItemUseCase
import lost.girl.rfidmobileinventory.mvi.MviViewModel

class RfidScannerViewModel(
    private val updateInventoryItemUseCase: UpdateInventoryItemUseCase,
    private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
    private val getInventoryInfoUseCase: GetInventoryInfoUseCase,
    private val startRFiDInventoryUseCase: StartRFiDInventoryUseCase,
    private val stopRFiDInventoryUseCase: StopRFIDInventoryUseCase,
    private val getRFIDReaderPowerUseCase: GetRFIDReaderPowerUseCase,
    private val isRFIDReaderInitializedUseCase: IsRFIDReaderInitializedUseCase,
    private val openBarcodeReaderUseCase: OpenBarcodeReaderUseCase,
    private val closeBarcodeReaderUseCase: CloseBarcodeReaderUseCase,
    private val startBarcodeReaderUseCase: StartBarcodeReaderUseCase,
    private val stopBarcodeReaderUseCase: StopBarcodeReaderUseCase,
    private val resetLocationInventoryItemByID: ResetLocationInventoryItemByIDUseCase,
    private val getAllInventoryItemListForRfidScanningUseCase: GetAllInventoryItemListForScanningUseCase,
    application: Application,

    ) :
    MviViewModel<RfidScannerViewState, RfidScannerViewEffect, RfidScannerViewEvent>(application),
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
            is RfidScannerViewEvent.SetCurrentLocation -> {
                setCurrentLocation(
                    viewEvent.iLocation,
                    viewEvent.locationName
                )
            }

            is RfidScannerViewEvent.SetScanningState -> {
                setScanningState(viewEvent.state)
            }

            is RfidScannerViewEvent.SetScanningPower -> {
                setScannerPowerValue(viewEvent.power)
            }

            is RfidScannerViewEvent.SetScannerType -> {
                setScannerType(viewEvent.type)
            }

            is RfidScannerViewEvent.ShowAlertDialog -> {
                showAlertDialog(
                    viewEvent.message,
                    viewEvent.onOkClickListener
                )
            }

            is RfidScannerViewEvent.ResetInventoryItemState -> {
                resetLocationInventory(viewEvent.item)
            }
        }
    }

    // При сворачивании окна останавливаем считывание если оно было запущено
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (viewState.isScanningStart) {
            setScanningState(false)
            stopInventory()
        }
    }


    private fun showAlertDialog(message: Int, onOkClickListener: () -> Unit) {
        if (!viewState.isScanningStart) {
            viewEffect = RfidScannerViewEffect.ShowAlertDialog(message, onOkClickListener)
        }
    }

    // Установка типа считывателя
    private fun setScannerType(type: ReaderType) {
        initReader(type)
    }

    // Старт инвентаризации (для RFID начинаем считывание, для 2D начинаем сканирование)
    private fun startInventory() {
        when (viewState.scannerType) {
            ReaderType.RFID -> {
                // получаем мощность из настроек
                val power = viewState.scannerPowerValue
                // стартуем считывание, при успешном - запускаем проверку метки на наличие в списке ТМЦ
                startRFiDInventoryUseCase.execute(power, onError = this::onError) {
                    for (tag in it) {
                        checkItemNewLocationAndUpdate(tag)
                    }
                }
            }

            ReaderType.BARCODE_2D -> {
                startBarcodeReaderUseCase.execute()
            }

            else -> {
                onError()
            }
        }
    }

    // Ошибки инициализации считывателя
    private fun onError(message: String = "") {
        viewEffect = RfidScannerViewEffect.ShowToast(
            R.string.init_rfid_message, R.string.init_error_rfid_message,
            true
        )
    }

    // Останавливаем инвентаризацию (для RFID останавливаем считывание, для 2D останавливаем сканирование)
    private fun stopInventory() {
        when (viewState.scannerType) {
            ReaderType.RFID -> {
                stopRFiDInventoryUseCase.execute()
            }

            ReaderType.BARCODE_2D -> {
                stopBarcodeReaderUseCase.execute()
            }

            else -> {
                onError()
            }
        }
    }

    // инициализация считывателя
    private fun initReader(type: ReaderType) {
        viewModelScope.launch {
            val result = when (type) {
                ReaderType.RFID -> {
                    // проверяем инициализирован ли счытыватель
                    isRFIDReaderInitializedUseCase.execute()
                }

                ReaderType.BARCODE_2D -> {
                    withContext(Dispatchers.IO) {
                        // инициализируем 2D сканер с callback для обработки результата сканировния
                        openBarcodeReaderUseCase.execute { checkItemNewLocationAndUpdate(it) }
                    }
                }
            }
            // если RFID счытыватель инициализирован - получем его мощность
            var power = if (result && type == ReaderType.RFID) {
                getRFIDReaderPowerUseCase.execute()
            } else 0
            if (power < 0 && type == ReaderType.RFID) {
                power = -1
                onError()
            }
            // обновляем состояние окна по результату инициализации
            viewState = viewState.copy(
                scannerType = type,
                isReaderInit = result,
                panelSettingsVisible = (result && type == ReaderType.RFID),
                startScanningButtonVisible = (result && type == ReaderType.RFID),
                scannerPowerValue = power
            )
        }
    }

    // Установка текущего местоположения
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

    // Установка состояние сканирования
    private fun setScanningState(state: Boolean) {
        viewState = viewState.copy(
            canBackPress = !state,
            isScanningStart = state,
            startScanningButtonVisible = (!state && viewState.scannerType == ReaderType.RFID),
            panelSettingsVisible = (!state && viewState.scannerType == ReaderType.RFID),
            stopScanningButtonVisible = (state && viewState.scannerType == ReaderType.RFID)
        )
        if (state) {
            startInventory()
        } else {
            stopInventory()
        }
    }

    // Обновления мощности при ручной настройке
    private fun setScannerPowerValue(value: Int) {
        viewState = viewState.copy(
            scannerPowerValue = value
        )
    }

    // Обновление актуального местоположения ТМЦ
    private fun updateInventoryItem(locationID: Int, location: String, id: Int) {
        updateInventoryItemUseCase.execute(locationID, location, id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfoUseCase.execute(viewState.currentLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocation
            ),
            inventoryItemsFullRFIDList = getAllInventoryItemListForRfidScanningUseCase.execute(),
        )
    }


    // Сброс статуса
    private fun resetLocationInventory(item: InventoryItemForListModel) {
        viewModelScope.launch(Dispatchers.IO) {
            item.id?.let { resetLocationInventoryById(it) }
        }
    }

    // Сброс статуса
    private fun resetLocationInventoryById(id: Int) {
        resetLocationInventoryItemByID.execute(id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfoUseCase.execute(viewState.currentLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocation
            ),
            inventoryItemsFullRFIDList = getAllInventoryItemListForRfidScanningUseCase.execute(),
        )
    }


    // Проверка наличия метки в списке ТМЦ
    private fun checkItemNewLocationAndUpdate(scanCode: String) {
        val allItem = viewState.inventoryItemsFullRFIDList
        //ищем id ТМЦ (RFID по rfidCardNum, 2D по shipmentNum)
        val itemId =
            when (viewState.scannerType) {
                ReaderType.RFID -> {
                    allItem.firstOrNull { it.rfidCardNum == scanCode && it.actualLocationID != viewState.currentLocation }?.id
                        ?: 0
                }

                ReaderType.BARCODE_2D -> {
                    allItem.firstOrNull { it.shipmentNum == scanCode && it.actualLocationID != viewState.currentLocation }?.id
                        ?: 0
                }

                else -> {
                    0
                }
            }
        // если метка найдена обновляем актальное местоположение у ТМЦ и проигрываем звук
        if (itemId > 0) {
            viewEffect = RfidScannerViewEffect.PlaySound(1)
            updateInventoryItem(
                viewState.currentLocation,
                viewState.currentLocationName,
                itemId
            )
            // после обновления проверяем текущее состояние инвентаризации
            // (если 100% найдено, тогда сообщаем - Все метки найдены)
            if (viewState.inventoryState.percentFound == 100) {
                viewEffect = RfidScannerViewEffect.InventoryReady(R.string.all_tag_found)
            }
        }
        // если активен 2D сканер - останавливаем его после каждого успешного сканирования
        if (viewState.scannerType == ReaderType.BARCODE_2D) {
            setScanningState(false)
        }
    }
}
