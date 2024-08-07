package lost.girl.rfidmobileinventory.di

import lost.girl.rfidmobileinventory.domain.usescase.ClearDataBaseUseCase
import lost.girl.rfidmobileinventory.domain.usescase.CloseBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.ExportDataToExcelFileUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetAllInventoryItemListForScanningUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetAllInventoryItemUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetAllLocationsUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetDataForExcelUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetDataFromExcelUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemByLocationIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemDetailUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetRFIDReaderPowerUseCase
import lost.girl.rfidmobileinventory.domain.usescase.InitRFIDReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.IsRFIDReaderInitializedUseCase
import lost.girl.rfidmobileinventory.domain.usescase.LoadDataToDataBaseUseCase
import lost.girl.rfidmobileinventory.domain.usescase.OpenBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.ResetLocationInventoryItemByIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.SetCommentInventoryItemByIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.SetFoundInventoryItemByIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StartBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StartRFiDInventoryUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StopBarcodeReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StopRFIDInventoryUseCase
import lost.girl.rfidmobileinventory.domain.usescase.StopRFIDReaderUseCase
import lost.girl.rfidmobileinventory.domain.usescase.UpdateInventoryItemUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<ClearDataBaseUseCase> {
        ClearDataBaseUseCase(inventoryRepository = get(), dataBase = get())
    }

    factory<ExportDataToExcelFileUseCase> {
        ExportDataToExcelFileUseCase(resourcesProvider = get())
    }

    factory<GetAllInventoryItemListForScanningUseCase> {
        GetAllInventoryItemListForScanningUseCase(inventoryRepository = get())
    }

    factory<GetAllInventoryItemUseCase> {
        GetAllInventoryItemUseCase(inventoryRepository = get())
    }

    factory<GetAllLocationsUseCase> {
        GetAllLocationsUseCase(inventoryRepository = get())
    }

    factory<GetDataForExcelUseCase> {
        GetDataForExcelUseCase(inventoryRepository = get())
    }

    factory<GetDataFromExcelUseCase> {
        GetDataFromExcelUseCase()
    }

    factory<GetInventoryInfoUseCase> {
        GetInventoryInfoUseCase(inventoryRepository = get())
    }

    factory<GetInventoryItemByLocationIDUseCase> {
        GetInventoryItemByLocationIDUseCase(inventoryRepository = get())
    }

    factory<GetInventoryItemDetailUseCase> {
        GetInventoryItemDetailUseCase(repository = get(), resourcesProvider = get())
    }

    factory<LoadDataToDataBaseUseCase> {
        LoadDataToDataBaseUseCase(inventoryRepository = get())
    }

    factory<UpdateInventoryItemUseCase> {
        UpdateInventoryItemUseCase(inventoryRepository = get())
    }

    factory<InitRFIDReaderUseCase> {
        InitRFIDReaderUseCase(
            reader = get()
        )
    }

    factory<StartRFiDInventoryUseCase> {
        StartRFiDInventoryUseCase(repository = get())
    }

    factory<StopRFIDInventoryUseCase> {
        StopRFIDInventoryUseCase(repository = get())
    }

    factory<GetRFIDReaderPowerUseCase> {
        GetRFIDReaderPowerUseCase(repository = get())
    }

    factory<StopRFIDReaderUseCase> {
        StopRFIDReaderUseCase(repository = get())
    }

    factory<IsRFIDReaderInitializedUseCase> {
        IsRFIDReaderInitializedUseCase(repository = get())
    }

    factory<OpenBarcodeReaderUseCase> {
        OpenBarcodeReaderUseCase(reader = get())
    }

    factory<StartBarcodeReaderUseCase> {
        StartBarcodeReaderUseCase(repository = get())
    }

    factory<StopBarcodeReaderUseCase> {
        StopBarcodeReaderUseCase(repository = get())
    }

    factory<CloseBarcodeReaderUseCase> {
        CloseBarcodeReaderUseCase(repository = get())
    }

    factory<ResetLocationInventoryItemByIDUseCase> {
        ResetLocationInventoryItemByIDUseCase(repository = get())
    }

    factory<SetFoundInventoryItemByIDUseCase> {
        SetFoundInventoryItemByIDUseCase(repository = get())
    }

    factory<SetCommentInventoryItemByIDUseCase> {
        SetCommentInventoryItemByIDUseCase(repository = get())
    }
}
