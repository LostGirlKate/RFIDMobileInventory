package lost.girl.rfidmobileinventory.ui.rfidscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.Slider
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.activities.MainApp
import lost.girl.rfidmobileinventory.data.repository.InventoryStorageImpl
import lost.girl.rfidmobileinventory.databinding.FragmentRfidScannerBinding
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.usescase.GetAllInventoryItemListForRfidScanningUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryItemByLocationIDUseCase
import lost.girl.rfidmobileinventory.domain.usescase.UpdateInventoryItemUseCase
import lost.girl.rfidmobileinventory.mvi.MviFragment
import lost.girl.rfidmobileinventory.ui.list.InventoryItemsFilterableAdapter
import lost.girl.rfidmobileinventory.utils.OnItemClickListener
import lost.girl.rfidmobileinventory.utils.UIHelper
import lost.girl.rfidmobileinventory.utils.UIHelper.Companion.refreshToggleButton
import lost.girl.rfidmobileinventory.utils.UIHelper.Companion.setOnCheckedChangeListenerToFilterButton


class RfidScannerFragment :
    MviFragment<RfidScannerViewState, RfidScannerViewEffect, RfidScannerViewEvent, RfidScannerViewModel>() {
    private lateinit var binding: FragmentRfidScannerBinding
    private lateinit var adapter: InventoryItemsFilterableAdapter
    private var canBackPress: Boolean = true


    private var activeReadyDialog: androidx.appcompat.app.AlertDialog? = null


    private val storage by lazy {
        InventoryStorageImpl(
            (context?.applicationContext as MainApp).database.getDao()
        )
    }
    override val viewModel: RfidScannerViewModel by activityViewModels {
        RfidScannerViewModel.RfidScannerViewModelFactory(
            application = requireActivity().application,
            UpdateInventoryItemUseCase(storage),
            GetAllInventoryItemListForRfidScanningUseCase(storage),
            GetInventoryItemByLocationIDUseCase(storage),
            GetInventoryInfoUseCase(storage)
        )
    }

    private val args: RfidScannerFragmentArgs by navArgs()

    private val onBacPressedCallBack =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (canBackPress) {
                    findNavController().popBackStack(R.id.inventoryListFragment, false)
                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, onBacPressedCallBack)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRfidScannerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationID = args.locationId
        val locationName = args.title
        initSetting()
        initRcView()
        initFilterButtons()
        lifecycle.addObserver(viewModel)
        viewModel.process(RfidScannerViewEvent.SetCurrentLocation(locationID, locationName))
    }

    override fun renderViewEffect(viewEffect: RfidScannerViewEffect) {
        when (viewEffect) {
            is RfidScannerViewEffect.InventoryReady -> inventoryReady(viewEffect.message)
            RfidScannerViewEffect.EmptyEffect -> {}
        }

        if (viewEffect !is RfidScannerViewEffect.EmptyEffect) {
            viewModel.process(RfidScannerViewEvent.ClearEffect)
        }
    }

    private fun inventoryReady(message: Int) {
        if (activeReadyDialog == null) {
            activeReadyDialog = UIHelper.detailDialog(
                requireContext(),
                getString(message),
                R.drawable.green_background,
                true,
                R.color.green_percent_text,
                true
            )
        }
    }

    override fun renderViewState(viewState: RfidScannerViewState) = with(binding) {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(viewState.canBackPress)
            actionBar.setDisplayHomeAsUpEnabled(viewState.canBackPress)
            actionBar.setDisplayShowHomeEnabled(viewState.canBackPress)
        }
        startRfidButton.visibility =
            if (viewState.startRfidScanningButtonVisible) View.VISIBLE else View.GONE
        stopRfidButton.visibility =
            if (viewState.stopScanningButtonVisible) View.VISIBLE else View.GONE
        settingPanel.visibility = if (viewState.panelSettingsVisible) View.VISIBLE else View.GONE
        scannerPowerValueCaption.text =
            getString(R.string.current_power, viewState.scannerPowerValue)
        scannerPowerSlider.value = viewState.scannerPowerValue.toFloat()
        canBackPress = viewState.canBackPress


        refreshToggleButton(
            filterButtonNotFound,
            viewState.inventoryState.countNotFoundString
        )
        refreshToggleButton(filterButtonFound, viewState.inventoryState.countFoundString)
        refreshToggleButton(
            filterButtonFoundInWrongPlace,
            viewState.inventoryState.countFoundInWrongPlaceString
        )


        val filter = getFilterString()
        adapter.submitListWithFilter(viewState.inventoryItems, filter)
    }


    private fun initSetting() {
        binding.startRfidButton.setOnClickListener {
            viewModel.process(RfidScannerViewEvent.SetScanningState(true))
        }
        binding.stopRfidButton.setOnClickListener {
            viewModel.process(RfidScannerViewEvent.SetScanningState(false))
        }

        binding.scannerPowerSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.process(RfidScannerViewEvent.SetScannerPowerValue(slider.value.toInt()))
            }
        })
        binding.scannerPowerSlider.setLabelFormatter { value ->
            value.toInt().toString()
        }
    }

    private fun initRcView() = with(binding) {
        rcItems.layoutManager = LinearLayoutManager(activity)
        adapter = InventoryItemsFilterableAdapter(
            object : OnItemClickListener<InventoryItemForListModel> {
                override fun onItemClick(item: InventoryItemForListModel) {
                    // openItemDetail(item)
                }
            }
        )
        rcItems.adapter = adapter
    }


    private fun initFilterButtons() {
        setOnCheckedChangeListenerToFilterButton(
            requireContext(),
            binding.filterButtonNotFound,
            R.color.light_red,
            getString(R.string.tmc_not_found),
            R.drawable.red_background
        ) { filterData() }
        setOnCheckedChangeListenerToFilterButton(
            requireContext(),
            binding.filterButtonFound,
            R.color.light_green,
            getString(R.string.tmc_found),
            R.drawable.green_background
        ) { filterData() }
        setOnCheckedChangeListenerToFilterButton(
            requireContext(),
            binding.filterButtonFoundInWrongPlace,
            R.color.light_orange,
            getString(R.string.tmc_found_in_wrong_place),
            R.drawable.orange_background
        ) { filterData() }
    }


    private fun getFilterString(): String =
        "~" + binding.filterButtonNotFound.isChecked.toString() +
                "~" + binding.filterButtonFound.isChecked.toString() +
                "~" + binding.filterButtonFoundInWrongPlace.isChecked.toString()


    private fun filterData() {
        val filter = getFilterString()
        adapter.filter.filter(filter)
    }

}