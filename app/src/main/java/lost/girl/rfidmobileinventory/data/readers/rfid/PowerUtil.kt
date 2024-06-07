package lost.girl.rfidmobileinventory.data.readers.rfid

// мощность power 8=min 30=max
fun Int.toPower(): Int {
    return (22 / 100.0 * this + 8).toInt()
}

fun Int.toPowerPercent(): Int {
    return ((this - 8) / 22.0 * 100).toInt()
}