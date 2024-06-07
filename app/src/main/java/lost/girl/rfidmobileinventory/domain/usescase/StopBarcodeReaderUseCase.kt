package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

class StopBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute() {
        repository.stop()
    }
}