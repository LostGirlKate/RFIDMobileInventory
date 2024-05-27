package lost.girl.rfidmobileinventory.di

import lost.girl.rfidmobileinventory.activities.MainApp
import lost.girl.rfidmobileinventory.data.repository.InventoryRepositoryImpl
import lost.girl.rfidmobileinventory.data.storage.InventoryStorage
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val dataModule = module {
    single<InventoryStorage> {
        (androidContext() as MainApp).database.getDao()
    }

    single<InventoryRepository> {
        InventoryRepositoryImpl(inventoryStorage = get())
    }
}