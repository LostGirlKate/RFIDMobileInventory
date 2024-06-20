package lost.girl.rfidmobileinventory.data.storage.roomdb

import android.content.Context

// Очистка DB + сброс sequence (автогенераторы id)
object ClearDataBase {
    fun execute(context: Context) {
        MainDataBase.getDataBase(context = context).clearAllTables()
        MainDataBase.getDataBase(context = context).openHelper.writableDatabase.execSQL("DELETE FROM sqlite_sequence")
    }
}
