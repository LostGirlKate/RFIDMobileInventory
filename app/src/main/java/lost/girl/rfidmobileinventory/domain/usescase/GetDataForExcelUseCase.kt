package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.models.InventoryItemForExportModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class GetDataForExcelUseCase(private val inventoryRepository: InventoryRepository) {
    fun execute(): List<List<String>> {
        return inventoryRepository.getAllInventoryItemForExport().map { mapInventoryItemForImportModelToListOfString(it) }
    }

    private fun mapInventoryItemForImportModelToListOfString(item: InventoryItemForExportModel) : List<String> {
        return item.toListOfString()
    }

}