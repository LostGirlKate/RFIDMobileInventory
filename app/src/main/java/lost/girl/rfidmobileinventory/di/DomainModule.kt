package lost.girl.rfidmobileinventory.di

import lost.girl.rfidmobileinventory.domain.usescase.ClearDataBaseUseCase
import lost.girl.rfidmobileinventory.domain.usescase.ExportDataToExcelFileUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetAllInventoryItemListForRfidScanningUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetAllInventoryItemUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetAllLocationsUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetDataForExcelUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetDataFromExcelUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemByLocationIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemDetailUseCase
import lost.girl.rfidmobileinventory.domain.usescase.LoadDataToDataBaseUseCase
import lost.girl.rfidmobileinventory.domain.usescase.UpdateInventoryItemUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<ClearDataBaseUseCase> {
        ClearDataBaseUseCase(inventoryRepository = get(), context = get())
    }

    factory<ExportDataToExcelFileUseCase> {
        ExportDataToExcelFileUseCase(context = get())
    }

    factory<GetAllInventoryItemListForRfidScanningUseCase> {
        GetAllInventoryItemListForRfidScanningUseCase(inventoryRepository = get())
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
        GetInventoryItemDetailUseCase(repository = get(), context = get())
    }

    factory<LoadDataToDataBaseUseCase> {
        LoadDataToDataBaseUseCase(inventoryRepository = get())
    }

    factory<UpdateInventoryItemUseCase> {
        UpdateInventoryItemUseCase(inventoryRepository = get())
    }

}