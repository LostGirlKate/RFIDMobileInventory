package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.data.readers.barcode2D.BarcodeReader

// use case Открытие(инициализация) 2D сканера
class OpenBarcodeReaderUseCase(
    private val reader: BarcodeReader,
) {
    suspend fun execute(onSuccess: (String) -> Unit): Boolean {
        return reader.setOnSuccess(onSuccess)
    }
}
