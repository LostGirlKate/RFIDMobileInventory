package lost.girl.rfidmobileinventory.mvi

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class MviViewModel<STATE, EFFECT, EVENT>(application: Application) :
    AndroidViewModel(application), ViewModelContract<EVENT> {

    private val _viewStates: MutableLiveData<STATE> = MutableLiveData()
    internal fun viewStates(): LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("You must initialize viewState")
        set(value) {
            _viewState = value
            _viewStates.postValue(value)
        }

    private val _viewEffects: MutableLiveData<EFFECT> = MutableLiveData()
    internal fun viewEffects(): LiveData<EFFECT> = _viewEffects

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect
            ?: throw UninitializedPropertyAccessException("You must initialize viewEffect")
        set(value) {
            _viewEffect = value
            _viewEffects.postValue(value)
        }

    @CallSuper
    override fun process(viewEvent: EVENT) {
    }

}