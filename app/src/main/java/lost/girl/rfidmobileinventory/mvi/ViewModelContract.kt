package lost.girl.rfidmobileinventory.mvi

interface ViewModelContract<EVENT> {
    fun process(viewEvent: EVENT)
}