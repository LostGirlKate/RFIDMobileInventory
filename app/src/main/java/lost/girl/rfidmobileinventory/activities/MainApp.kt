package lost.girl.rfidmobileinventory.activities

import android.app.Application
import lost.girl.rfidmobileinventory.data.storage.roomdb.MainDataBase

class MainApp : Application() {
    val database by lazy { MainDataBase.getDataBase(this)}
}