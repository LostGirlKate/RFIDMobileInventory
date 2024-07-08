package lost.girl.rfidmobileinventory.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.data.readers.ReaderType
import lost.girl.rfidmobileinventory.databinding.FragmentInventoryListBinding
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemState
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel
import lost.girl.rfidmobileinventory.mvi.MviFragment
import lost.girl.rfidmobileinventory.utils.OnItemClickListener
import lost.girl.rfidmobileinventory.utils.UIHelper
import lost.girl.rfidmobileinventory.utils.UIHelper.refreshToggleButton
import lost.girl.rfidmobileinventory.utils.UIHelper.setOnCheckedChangeListenerToFilterButton
import lost.girl.rfidmobileinventory.utils.searchablespinner.OnItemSelectListener
import lost.girl.rfidmobileinventory.utils.searchablespinner.SearchableSpinner
import org.koin.androidx.viewmodel.ext.android.viewModel

class InventoryListFragment :
    MviFragment<InventoryListViewState, InventoryListViewEffect, InventoryListViewEvent, InventoryListViewModel>() {

    override val viewModel by viewModel<InventoryListViewModel>()
    private lateinit var binding: FragmentInventoryListBinding
    private lateinit var adapter: InventoryItemsFilterableAdapter
    private val locationListName = ArrayList<String>()
    private val locationList = ArrayList<InventoryLocationFullModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInventoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initSearch()
        initButtons()
        initFilterButtons()
        initRcView()
        lifecycle.addObserver(viewModel)
    }

    override fun renderViewEffect(viewEffect: InventoryListViewEffect) {
        when (viewEffect) {
            is InventoryListViewEffect.ShowAlertDialog -> showAlertDialog(
                viewEffect.message,
                viewEffect.onOkClickListener
            )
        }
    }

    override fun renderViewState(viewState: InventoryListViewState) = with(binding) {
        val filter = getFilterString()
        adapter.submitListWithFilter(viewState.inventoryItems, filter)
        for (location in viewState.locations) {
            if (locationList.none { it.id == location.id!! }) {
                locationList.add(location)
                locationListName.add(location.name)
            }
        }
        binding.locationSpinner.text =
            locationList.firstOrNull { it.id == viewState.currentLocationID }?.name ?: ""
        refreshToggleButton(filterButtonNotFound, viewState.inventoryState.countNotFoundString)
        refreshToggleButton(filterButtonFound, viewState.inventoryState.countFoundString)
        refreshToggleButton(
            filterButtonFoundInWrongPlace,
            viewState.inventoryState.countFoundInWrongPlaceString
        )
        filterData()
    }

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    private fun showAlertDialog(msg: Int, onOkClickListener: () -> Unit) {
        UIHelper.alertDialog(requireContext(), getString(msg)) {
            onOkClickListener()
        }
    }

    // Инициализация RecyclerView
    private fun initRcView() = with(binding) {
        rcInventoryList.layoutManager = LinearLayoutManager(activity)
        adapter = InventoryItemsFilterableAdapter(
            object : OnItemClickListener<InventoryItemForListModel> {
                override fun onItemClick(item: InventoryItemForListModel) {
                    openItemDetail(item)
                }

                override fun onLongClick(item: InventoryItemForListModel): Boolean {
                    if (item.state == InventoryItemState.STATE_FOUND_IN_WRONG_PLACE) {
                        viewModel.process(
                            InventoryListViewEvent.ShowAlertDialog(R.string.reset_inventory_item_state) {
                                viewModel.process(
                                    InventoryListViewEvent.ResetInventoryItemState(
                                        item
                                    )
                                )
                            }
                        )
                    }

                    return true
                }
            },
            getString(R.string.filter_delimetr)
        )
        rcInventoryList.adapter = adapter
    }

    // Открытие окна с детализацией по ТМЦ
    private fun openItemDetail(item: InventoryItemForListModel) {
        val action =
            InventoryListFragmentDirections.actionInventoryListFragmentToInventoryItemDetailFragment(
                item.model,
                item.id!!,
                item.state
            )
        findNavController().navigate(action)
    }

    // Открытие окна для сканирования, доступно только если выбран фильтр по местоположению
    private fun openScannerFragment(type: ReaderType) {
        val title = binding.locationSpinner.text
        val locationID = locationList.firstOrNull { it.name == title }?.id ?: 0
        if (locationID < 1) {
            UIHelper.detailDialog(
                requireContext(),
                getString(R.string.you_need_choose_location_file),
                0,
                false,
                R.color.textColor
            )
        } else {
            val action =
                InventoryListFragmentDirections.actionInventoryListFragmentToRfidScannerFragment(
                    title.toString(),
                    locationID,
                    type
                )
            findNavController().navigate(action)
        }
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

    // Инициализация кнопок для перехода в режим сканирования
    private fun initButtons() {
        binding.openRfidScannerButton.setOnClickListener { openScannerFragment(ReaderType.RFID) }
        binding.open2dScannerButton.setOnClickListener { openScannerFragment(ReaderType.BARCODE_2D) }
    }

    // Получение актуального фильтра для адаптера
    private fun getFilterString(): String =
        listOf(
            binding.searchView.query.toString(),
            binding.filterButtonNotFound.isChecked.toString(),
            binding.filterButtonFound.isChecked.toString(),
            binding.filterButtonFoundInWrongPlace.isChecked.toString()
        ).joinToString(getString(R.string.filter_delimetr))

    // Фильтрация данных с списке ТМЦ
    private fun filterData() {
        val filter = getFilterString()
        adapter.filter.filter(filter)
    }

    // Инициализация SearchView
    private fun initSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterData()
                return false
            }
        })
        binding.searchView.clearFocus()
    }

    // Инициализация SearchableSpinner выбор местоположения
    private fun initSpinner() {
        val searchableSpinner = SearchableSpinner(requireContext())
        searchableSpinner.windowTitle = getString(R.string.location_search)
        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(position: Int, selectedString: String) {
                val newLocation = locationList.firstOrNull { it.name == selectedString }?.id ?: 0
                refreshInventoryData(newLocation)
            }
        }
        searchableSpinner.setSpinnerListItems(locationListName)
        binding.locationSpinner.setOnClickListener {
            searchableSpinner.show()
        }
    }

    // Обновление информации об инвентаризации по местоположению
    fun refreshInventoryData(locationID: Int) {
        viewModel.process(InventoryListViewEvent.EditCurrentLocation(locationID))
    }
}
