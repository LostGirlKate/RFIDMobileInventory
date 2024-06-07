package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

class StartBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute(): Boolean {
        return repository.start()
    }
}