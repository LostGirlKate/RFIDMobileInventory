package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

class CloseBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute() {
        repository.close()
    }
}