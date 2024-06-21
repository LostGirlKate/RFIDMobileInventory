package lost.girl.rfidmobileinventory.ui.rfidscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.Slider
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.databinding.FragmentRfidScannerBinding
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.mvi.MviFragment
import lost.girl.rfidmobileinventory.ui.list.InventoryItemsFilterableAdapter
import lost.girl.rfidmobileinventory.utils.OnItemClickListener
import lost.girl.rfidmobileinventory.utils.UIHelper
import lost.girl.rfidmobileinventory.utils.UIHelper.refreshToggleButton
import lost.girl.rfidmobileinventory.utils.UIHelper.setOnCheckedChangeListenerToFilterButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class RfidScannerFragment :
    MviFragment<RfidScannerViewState, RfidScannerViewEffect, RfidScannerViewEvent, RfidScannerViewModel>() {
    override val viewModel by viewModel<RfidScannerViewModel>()
    private lateinit var binding: FragmentRfidScannerBinding
    private lateinit var adapter: InventoryItemsFilterableAdapter
    private var canBackPress: Boolean = true
    private var activeReadyDialog: androidx.appcompat.app.AlertDialog? = null
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRfidScannerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationID = args.locationId
        val locationName = args.title
        val scannerType = args.scannerType
        initSetting()
        initRcView()
        initFilterButtons()
        lifecycle.addObserver(viewModel)
        viewModel.process(RfidScannerViewEvent.SetScannerType(scannerType))
        viewModel.process(RfidScannerViewEvent.SetCurrentLocation(locationID, locationName))
    }

    override fun renderViewEffect(viewEffect: RfidScannerViewEffect) {
        when (viewEffect) {
            is RfidScannerViewEffect.InventoryReady -> {
                inventoryReady(viewEffect.message)
            }

            is RfidScannerViewEffect.ShowToast -> {
                showToast(
                    viewEffect.message,
                    viewEffect.errorMessage,
                    viewEffect.isError
                )
            }
        }
    }

    override fun renderViewState(viewState: RfidScannerViewState) = with(binding) {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(viewState.canBackPress)
            actionBar.setDisplayHomeAsUpEnabled(viewState.canBackPress)
            actionBar.setDisplayShowHomeEnabled(viewState.canBackPress)
            actionBar.title = "${viewState.scannerType ?: ""}  ${viewState.currentLocationName}"
        }

        errorText.visibility =
            if (!viewState.isReaderInit && viewState.scannerType != null) View.VISIBLE else View.GONE
        startRfidButton.visibility =
            if (viewState.startScanningButtonVisible) View.VISIBLE else View.GONE
        stopRfidButton.visibility =
            if (viewState.stopScanningButtonVisible) View.VISIBLE else View.GONE
        settingPanel.visibility = if (viewState.panelSettingsVisible) {
            View.VISIBLE
        } else {
            if (!viewState.isReaderInit) View.INVISIBLE else View.GONE
        }
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
        adapter.submitListWithFilter(
            viewState.inventoryItems,
            filter
        )
    }

    // Оповещение о выполнении инвентаризации (все метки найдены)
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

    // Инициализация кнопок и слайдера настройки мощности
    private fun initSetting() {
        binding.startRfidButton.setOnClickListener {
            viewModel.process(RfidScannerViewEvent.SetScanningState(true))
        }
        binding.stopRfidButton.setOnClickListener {
            viewModel.process(RfidScannerViewEvent.SetScanningState(false))
        }
        binding.scannerPowerSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.process(RfidScannerViewEvent.SetScanningPower(slider.value.toInt()))
            }
        })
        binding.scannerPowerSlider.setLabelFormatter { value ->
            value.toInt().toString()
        }
    }

    // Инициализация RecyclerView
    private fun initRcView() = with(binding) {
        rcItems.layoutManager = LinearLayoutManager(activity)
        adapter = InventoryItemsFilterableAdapter(
            object : OnItemClickListener<InventoryItemForListModel> {
                override fun onItemClick(item: InventoryItemForListModel) {
                }
            },
            getString(R.string.filter_delimetr)
        )
        rcItems.adapter = adapter
    }

    // Инициализация фильтров по статусам
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

    // Получение актуального фильтра для адаптера
    private fun getFilterString(): String =
        listOf(
            "",
            binding.filterButtonNotFound.isChecked.toString(),
            binding.filterButtonFound.isChecked.toString(),
            binding.filterButtonFoundInWrongPlace.isChecked.toString()
        ).joinToString(getString(R.string.filter_delimetr))

    // Фильтрация данных с списке ТМЦ
    private fun filterData() {
        val filter = getFilterString()
        adapter.filter.filter(filter)
    }

    // Показать Toast
    private fun showToast(message: Int, errorMessage: Int, isError: Boolean = false) {
        UIHelper.showToastMessage(
            requireContext(),
            if (isError) getString(errorMessage) else getString(message),
            if (isError) R.color.toast_red_text else R.color.toast_green_text
        )
    }
}
