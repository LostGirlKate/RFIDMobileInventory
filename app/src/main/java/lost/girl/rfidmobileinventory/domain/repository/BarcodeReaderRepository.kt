package lost.girl.rfidmobileinventory.domain.repository

import android.content.Context

interface BarcodeReaderRepository {

    // старт сканирования
    fun start(): Boolean

    // остановка сканирования
    fun stop()

    // закрытие 2D сканера
    fun close()

    // инициализация 2D сканера с установкой callback для обработки результата сканировния
    suspend fun open(context: Context, onSuccess: (String) -> Unit): Boolean
}
