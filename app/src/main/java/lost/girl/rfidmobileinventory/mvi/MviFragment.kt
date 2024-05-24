package lost.girl.rfidmobileinventory.mvi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class MviFragment<STATE, EFFECT, EVENT, ViewModel : MviViewModel<STATE, EFFECT, EVENT>> :
    Fragment() {

    abstract val viewModel: ViewModel

    private val viewStateObserver = Observer<STATE> {
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
        renderViewEffect(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates().observe(viewLifecycleOwner, viewStateObserver)
        viewModel.viewEffects().observe(viewLifecycleOwner, viewEffectObserver)
    }

    abstract fun renderViewState(viewState: STATE)

    abstract fun renderViewEffect(viewEffect: EFFECT)
}