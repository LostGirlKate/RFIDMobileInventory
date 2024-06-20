package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

// use case Закрыть 2D сканнер
class CloseBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute() {
        repository.close()
    }
}
