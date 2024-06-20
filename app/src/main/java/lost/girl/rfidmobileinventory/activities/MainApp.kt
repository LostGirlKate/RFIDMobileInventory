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
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

class MainApp : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        plant(DebugTree())
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApp)
            modules(listOf(appModule, domainModule, dataModule))
        }
    }

    companion object {
        private var instance: MainApp? = null
        val database by lazy { MainDataBase.getDataBase(instance!!.applicationContext) }
    }
}
