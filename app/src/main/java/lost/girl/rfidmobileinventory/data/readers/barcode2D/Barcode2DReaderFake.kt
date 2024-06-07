package lost.girl.rfidmobileinventory.data.readers.barcode2D

import android.content.Context
import kotlinx.coroutines.delay

class Barcode2DReaderFake {
    private var index = -1
    private val tagList = mutableListOf(
        "PR00FG001000001",
        "PR00FG001000002",
        "PR00FG001000003",
        "PR00FG001000004",
        "PR00FG001000005",
        "PR00FG001000006",
        "PR00FG001000007",
        "PR00FG001000008",
        "PR00FG001000009",
        "PR00FG001000010",
        "PR00FG001000011",
        "PR00FG001000012",
        "PR00FG001000013",
        "PR00FG001000014",
        "PR00FG001000015",
        "PR00FG001000016",
        "PR00FG001000017",
        "PR00FG001000018",
        "PR00FG001000019",
        "PR00FG001000020",
        "PR00FG001000021",
        "PR00FG001000022",
        "PR00FG001000023",
        "PR00FG001000024",
        "PR00FG001000025",
        "PR00FG001000026",
        "PR00FG001000027",
        "PR00FG001000028",
        "PR00FG001000029",
        "PR00FG001000030",
        "PR00FG001000031",
        "PR00FG001000032",
        "PR00FG001000033",
        "PR00FG001000034",
        "PR00FG001000035",
        "PR00FG001000036",
        "PR00FG001000037",
        "PR00FG001000038",
        "PR00FG001000039",
        "PR00FG001000040",
        "PR00FG001000041",
        "PR00FG001000042",
        "PR00FG001000043",
        "PR00FG001000044",
        "PR00FG001000045",
        "PR00FG001000046",
        "PR00FG001000047",
        "PR00FG001000048",
        "PR00FG001000049",
        "PR00FG001000050",
        "PR00FG001000051",
        "PR00FG001000052",
        "PR00FG001000053",
        "PR00FG001000054",
        "PR00FG001000055",
        "PR00FG001000056",
        "PR00FG001000057",
        "PR00FG001000058",
        "PR00FG001000059",
        "PR00FG001000060",
        "PR00FG001000061",
        "PR00FG001000062",
        "PR00FG001000063",
        "PR00FG001000064",
        "PR00FG001000065",
        "PR00FG001000066",
        "PR00FG001000067",
        "PR00FG001000068",
        "PR00FG001000069",
        "PR00FG001000070",
        "PR00FG001000071",
        "PR00FG001000072",
        "PR00FG001000073",
        "PR00FG001000074",
        "PR00FG001000075",
        "PR00FG001000076",
        "PR00FG001000077",
        "PR00FG001000078",
        "PR00FG001000079",
        "PR00FG001000080",
        "PR00FG001000081",
        "PR00FG001000082",
        "PR00FG001000083",
        "PR00FG001000084",
        "PR00FG001000085",
        "PR00FG001000086",
        "PR00FG001000087",
        "PR00FG001000088",
        "PR00FG001000089",
        "PR00FG001000090",
        "PR00FG001000091",
        "PR00FG001000092",
        "PR00FG001000093",
        "PR00FG001000094",
        "PR00FG001000095",
        "PR00FG001000096",
        "PR00FG001000097",
        "PR00FG001000098",
        "PR00FG001000099",
        "PR00FG001000100"
    )


    fun startScan(): Boolean = true

    fun stopScan() {}

    fun open(context: Context): Boolean = true

    fun close() {}

    suspend fun getBarcodeData(): String {
        delay(1500)
        index++
        return tagList[index]
    }


}