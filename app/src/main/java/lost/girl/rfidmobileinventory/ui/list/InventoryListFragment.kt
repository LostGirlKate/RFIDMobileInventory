package lost.girl.rfidmobileinventory.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.data.readers.ReaderType
import lost.girl.rfidmobileinventory.databinding.FragmentInventoryListBinding
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.mvi.MviFragment
import lost.girl.rfidmobileinventory.utils.OnItemClickListener
import lost.girl.rfidmobileinventory.utils.UIHelper
import lost.girl.rfidmobileinventory.utils.UIHelper.Companion.refreshToggleButton
import lost.girl.rfidmobileinventory.utils.UIHelper.Companion.setOnCheckedChangeListenerToFilterButton
import org.koin.androidx.viewmodel.ext.android.viewModel


class InventoryListFragment :
    MviFragment<InventoryListViewState, InventoryListViewEffect, InventoryListViewEvent, InventoryListViewModel>() {
    private lateinit var binding: FragmentInventoryListBinding
    private lateinit var adapter: InventoryItemsFilterableAdapter


    override val viewModel by viewModel<InventoryListViewModel>()

    private val locationList = ArrayList<String>()
    private val locationListID = ArrayList<Int>()
    private lateinit var adapterSpinner: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInventoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView
        initRcView()
        initSpinner()
        initSearch()
        initSetting()
        initFilterButtons()
    }

    override fun renderViewEffect(viewEffect: InventoryListViewEffect) {

    }

    override fun renderViewState(viewState: InventoryListViewState) = with(binding) {
        val filter = getFilterString()
        adapter.submitListWithFilter(viewState.inventoryItems, filter)
        binding.tvEmptyList.visibility =
            if (viewState.inventoryItems.isEmpty()) View.VISIBLE else View.GONE



        for (location in viewState.locations) {
            if (locationListID.none { it == location.id!! }) {
                locationList.add(location.name)
                locationListID.add(location.id!!)
            }
        }
        adapterSpinner.notifyDataSetChanged()
        val currentLocation =
            if (viewState.currentLocationID == -1) 0 else viewState.currentLocationID
        binding.locationSpinner.setSelection(locationListID.indexOf(currentLocation))

        refreshToggleButton(filterButtonNotFound, viewState.inventoryState.countNotFoundString)
        refreshToggleButton(filterButtonFound, viewState.inventoryState.countFoundString)
        refreshToggleButton(
            filterButtonFoundInWrongPlace,
            viewState.inventoryState.countFoundInWrongPlaceString
        )
    }


    private fun initRcView() = with(binding) {
        rcInventoryList.layoutManager = LinearLayoutManager(activity)
        adapter = InventoryItemsFilterableAdapter(
            object : OnItemClickListener<InventoryItemForListModel> {
                override fun onItemClick(item: InventoryItemForListModel) {
                    openItemDetail(item)
                }
            }
        )
        rcInventoryList.adapter = adapter
    }

    private fun openItemDetail(item: InventoryItemForListModel) {
        val action =
            InventoryListFragmentDirections.actionInventoryListFragmentToInventoryItemDetailFragment(
                item.model,
                item.id!!,
                item.state
            )
        findNavController().navigate(action)
    }

    private fun openScannerFragment(type: ReaderType) {
        val title = locationList[binding.locationSpinner.selectedItemPosition]
        val locationID = locationListID[binding.locationSpinner.selectedItemPosition]
        if (locationID < 1) {
            UIHelper.detailDialog(
                requireContext(),
                getString(R.string.you_need_choose_location_file),
                0,
                false,
                R.color.light_red_text
            )
        } else {
            val action =
                InventoryListFragmentDirections.actionInventoryListFragmentToRfidScannerFragment(
                    title, locationID, type
                )
            findNavController().navigate(action)
        }
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


    private fun initSetting() {
        binding.openRfidScannerButton.setOnClickListener { openScannerFragment(ReaderType.RFID) }
        binding.open2dScannerButton.setOnClickListener { openScannerFragment(ReaderType.BARCODE_2D) }
    }

    private fun getFilterString(): String =
        binding.searchView.query.toString() + "~" + binding.filterButtonNotFound.isChecked.toString() +
                "~" + binding.filterButtonFound.isChecked.toString() +
                "~" + binding.filterButtonFoundInWrongPlace.isChecked.toString()


    private fun filterData() {
        val filter = getFilterString()
        adapter.filter.filter(filter)
    }

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


    private fun initSpinner() {
        adapterSpinner =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                locationList
            )

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.locationSpinner.adapter = adapterSpinner
        binding.locationSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View?, selectedItemPosition: Int, selectedId: Long
                ) {
                    val newLocation = locationListID[selectedItemPosition]
                    viewModel.process(InventoryListViewEvent.EditCurrentLocation(newLocation))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }
}


