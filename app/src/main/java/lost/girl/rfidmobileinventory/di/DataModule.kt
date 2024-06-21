package lost.girl.rfidmobileinventory.di

import lost.girl.rfidmobileinventory.activities.MainApp
import lost.girl.rfidmobileinventory.data.readers.barcode2D.Barcode2DReader
import lost.girl.rfidmobileinventory.data.readers.barcode2D.BarcodeReader
import lost.girl.rfidmobileinventory.data.readers.rfid.IRfidReader
import lost.girl.rfidmobileinventory.data.readers.rfid.Reader
import lost.girl.rfidmobileinventory.data.repository.BarcodeReaderRepositoryImpl
import lost.girl.rfidmobileinventory.data.repository.InventoryRepositoryImpl
import lost.girl.rfidmobileinventory.data.repository.RFIDReaderRepositoryImpl
import lost.girl.rfidmobileinventory.data.storage.InventoryStorage
import lost.girl.rfidmobileinventory.data.storage.roomdb.MainDataBase
import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository
import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository
import lost.girl.rfidmobileinventory.utils.ResourcesProvider
import org.koin.dsl.module

val dataModule = module {

    single<MainDataBase> {
        MainApp.database
    }

    single<InventoryStorage> {
        MainApp.database.getDao()
    }

    single<InventoryRepository> {
        InventoryRepositoryImpl(inventoryStorage = get())
    }

    single<IRfidReader> {
        val reader = Reader()
        reader.poweron(context = get())
        reader
    }

    single<RFIDReaderRepository> {
        RFIDReaderRepositoryImpl(reader = get())
    }

    single<BarcodeReader> {
        val reader = Barcode2DReader()
        reader.open(context = get())
        reader
    }

    single<BarcodeReaderRepository> {
        BarcodeReaderRepositoryImpl(reader = get())
    }

    single<ResourcesProvider> {
        ResourcesProvider(context = get())
    }
}
