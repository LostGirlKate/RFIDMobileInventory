package lost.girl.rfidmobileinventory.data.readers.barcode2D

import android.content.Context
import com.rscja.barcode.BarcodeDecoder
import com.rscja.barcode.BarcodeFactory
import com.rscja.barcode.BarcodeUtility

class Barcode2DReader : BarcodeReader {
    override var isOpen = false

    private var barcodeDecoder =
        BarcodeFactory.getInstance().barcodeDecoder

    override fun start(): Boolean {
        return barcodeDecoder.startScan()
    }

    override fun stop() {
        barcodeDecoder.stopScan()

    }

    override fun close() {
        isOpen = false
        barcodeDecoder.close()
    }

    override suspend fun open(context: Context, onSuccess: (String) -> Unit): Boolean {
        isOpen = barcodeDecoder.open(context)
        BarcodeUtility.getInstance().enableVibrate(context, true)
        barcodeDecoder.setDecodeCallback { barcodeEntity ->
            if (barcodeEntity.resultCode == BarcodeDecoder.DECODE_SUCCESS) {
                onSuccess(barcodeEntity.barcodeData)
            }
        }
        return isOpen
    }
}