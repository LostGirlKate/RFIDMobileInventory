package lost.girl.rfidmobileinventory.domain.models

// Модель данных ТМЦ для экспорта в Excel
data class InventoryItemForExportModel(
    val rowNum: String,
    val inventoryNum: String,
    val managerName: String,
    val location: String,
    val type: String,
    val model: String,
    val serialNum: String,
    val shipmentNum: String,
    val rfidCardNum: String,
    val actualLocation: String,
) {
    // Преобразование объекта в List<String> для выгрузки
    fun toListOfString(): List<String> {
        return """$rowNum,
            $inventoryNum,
            $managerName,
            $location,
            $type,
            $model,
            $serialNum,
            $shipmentNum,
            $rfidCardNum,
            ${if (actualLocation == "null") "" else actualLocation}
        """.trimMargin().split(
            ','
        )
    }
}
