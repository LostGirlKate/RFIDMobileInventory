package lost.girl.rfidmobileinventory.activities

import android.app.Application
import lost.girl.rfidmobileinventory.data.storage.roomdb.MainDataBase
import lost.girl.rfidmobileinventory.di.appModule
import lost.girl.rfidmobileinventory.di.dataModule
import lost.girl.rfidmobileinventory.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApp : Application() {
    val database by lazy { MainDataBase.getDataBase(this) }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApp)
            modules(listOf(appModule, domainModule, dataModule))

        }
    }
}