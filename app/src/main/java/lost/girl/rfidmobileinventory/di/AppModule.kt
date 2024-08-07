package lost.girl.rfidmobileinventory.di

import lost.girl.rfidmobileinventory.ui.detail.InventoryItemDetailViewModel
import lost.girl.rfidmobileinventory.ui.list.InventoryListViewModel
import lost.girl.rfidmobileinventory.ui.main.InventoryMainViewModel
import lost.girl.rfidmobileinventory.ui.rfidscan.RfidScannerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<InventoryMainViewModel> {
        InventoryMainViewModel(
            application = androidApplication(),
            getInventoryInfo = get(),
            loadDataToDataBaseUseCase = get(),
            getDataForExcelUseCase = get(),
            clearDataBaseUseCase = get(),
            getDataFromExcelUseCase = get(),
            exportDataToExcelFileUseCase = get(),
            isRFIDReaderInitializedUseCase = get()
        )
    }

    viewModel<InventoryListViewModel> {
        InventoryListViewModel(
            application = androidApplication(),
            getAllLocationsUseCase = get(),
            getInventoryInfo = get(),
            getInventoryItemByLocationIDUseCase = get(),
            resetLocationInventoryItemByID = get(),
            setFoundInventoryItemByIDUseCase = get(),
            setCommentInventoryItemByIDUseCase = get()
        )
    }

    viewModel<RfidScannerViewModel> {
        RfidScannerViewModel(
            application = androidApplication(),
            getInventoryItemByLocationIDUseCase = get(),
            getInventoryInfoUseCase = get(),
            updateInventoryItemUseCase = get(),
            getAllInventoryItemListForRfidScanningUseCase = get(),
            startRFiDInventoryUseCase = get(),
            stopRFiDInventoryUseCase = get(),
            getRFIDReaderPowerUseCase = get(),
            isRFIDReaderInitializedUseCase = get(),
            openBarcodeReaderUseCase = get(),
            closeBarcodeReaderUseCase = get(),
            startBarcodeReaderUseCase = get(),
            stopBarcodeReaderUseCase = get(),
            resetLocationInventoryItemByID = get(),
            setFoundInventoryItemByIDUseCase = get(),
            setCommentInventoryItemByIDUseCase = get()
        )
    }

    viewModel<InventoryItemDetailViewModel> {
        InventoryItemDetailViewModel(
            application = androidApplication(),
            getInventoryItemDetailUseCase = get()
        )
    }
}
