package lost.girl.rfidmobileinventory.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.activities.MainApp
import lost.girl.rfidmobileinventory.data.repository.InventoryRepositoryImpl
import lost.girl.rfidmobileinventory.databinding.FragmentInventoryMainBinding
import lost.girl.rfidmobileinventory.domain.usescase.ClearDataBaseUseCase
import lost.girl.rfidmobileinventory.domain.usescase.ExportDataToExcelFileUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetDataForExcelUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetDataFromExcelUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.LoadDataToDataBaseUseCase
import lost.girl.rfidmobileinventory.mvi.MviFragment
import lost.girl.rfidmobileinventory.utils.ExcelUtil
import lost.girl.rfidmobileinventory.utils.UIHelper.Companion.alertDialog
import lost.girl.rfidmobileinventory.utils.UIHelper.Companion.alertProcessDialog
import lost.girl.rfidmobileinventory.utils.UIHelper.Companion.showToastMessage

class InventoryMainFragment :
    MviFragment<InventoryMainViewState, InventoryMainViewEffect, InventoryMainViewEvent, InventoryMainViewModel>() {

    private lateinit var binding: FragmentInventoryMainBinding
    private val storage by lazy {
        InventoryRepositoryImpl(
            (context?.applicationContext as MainApp).database.getDao()
        )
    }
    override val viewModel: InventoryMainViewModel by activityViewModels {
        InventoryMainViewModel.InventoryMainViewModelFactory(
            application = requireActivity().application,
            GetInventoryInfoUseCase(storage),
            LoadDataToDataBaseUseCase(storage),
            GetDataForExcelUseCase(storage),
            ClearDataBaseUseCase(storage, requireContext()),
            GetDataFromExcelUseCase(),
            ExportDataToExcelFileUseCase(requireContext())
        )
    }

    private var activeProcessDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInventoryMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        viewModel.process(InventoryMainViewEvent.RefreshData)
    }


    private fun initButtons() = with(binding) {
        openInventoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_inventoryMainFragment_to_inventoryListFragment)
        }

        loadFromFileButton.setOnClickListener {
            viewModel.process(InventoryMainViewEvent.OpenFileManager)
        }

        exportButton.setOnClickListener {
            viewModel.process(
                InventoryMainViewEvent.ShowProcessDialog(
                    R.string.file_save_action_message,
                    InventoryMainViewEvent.SaveDataToFile()
                )
            )
        }

        closeInventoryButton.setOnClickListener {
            viewModel.process(InventoryMainViewEvent.ShowAlertDialog(R.string.clear_all_message) {
                viewModel.process(
                    InventoryMainViewEvent.ShowProcessDialog(
                        R.string.file_save_action_message,
                        InventoryMainViewEvent.CloseInventory()
                    )
                )
            }
            )
        }
    }


    override fun renderViewEffect(viewEffect: InventoryMainViewEffect) {
        when (viewEffect) {
            is InventoryMainViewEffect.OpenExcelFileLauncher -> openFileManager()

            is InventoryMainViewEffect.ShowAlertDialog -> showAlertDialog(
                viewEffect.message,
                viewEffect.onOkClickListener
            )

            is InventoryMainViewEffect.ShowAlertProcessDialog ->
                showProcessDialog(viewEffect.message, viewEffect.processEvent)

            is InventoryMainViewEffect.ShowToast -> showToast(
                viewEffect.message,
                viewEffect.errorMessage,
                viewEffect.isError
            )

            is InventoryMainViewEffect.HideAlertProcessDialog -> {
                viewEffect.processDialog?.dismiss()
            }

            InventoryMainViewEffect.OpenFileManager -> openFileManager()
        }

    }

    override fun renderViewState(viewState: InventoryMainViewState) = with(binding) {
        progressPercentText.text = viewState.inventoryState.percentFoundString
        progressBar.progress = viewState.inventoryState.percentFound
        countAllText.text = viewState.inventoryState.countAllString
        countFoundText.text = viewState.inventoryState.countFoundString
        countNotFoundText.text = viewState.inventoryState.countNotFoundString
        countFoundInWrongPlaceText.text = viewState.inventoryState.countFoundInWrongPlaceString

        mainInfoBlock.visibility = if (viewState.mainInfoBlockVisible) View.VISIBLE else View.GONE
        openInventoryButton.visibility =
            if (viewState.openInventoryButtonVisible) View.VISIBLE else View.GONE
        closeInventoryButton.visibility =
            if (viewState.closeInventoryButtonVisible) View.VISIBLE else View.GONE
        exportButton.visibility = if (viewState.exportButtonVisible) View.VISIBLE else View.GONE
        loadFromFileButton.visibility =
            if (viewState.loadFromFileButtonVisible) View.VISIBLE else View.GONE
        loadingWarningText.visibility =
            if (viewState.loadingWarningTextVisible) View.VISIBLE else View.GONE
        progressPercentText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                viewState.progressPercentTextColor
            )
        )
    }


    private fun showToast(message: Int, errorMessage: Int, isError: Boolean = false) {
        showToastMessage(
            requireContext(),
            if (isError) getString(errorMessage) else getString(message),
            if (isError) R.color.light_red else R.color.light_green
        )
    }


    private fun showProcessDialog(message: Int, processEvent: InventoryMainViewEvent) {
        val dialog = alertProcessDialog(
            requireContext(), getString(message)
        )
        activeProcessDialog = dialog
        dialog.show()
        when (processEvent) {
            is InventoryMainViewEvent.LoadDataFromFile -> viewModel.process(
                processEvent.copy(processDialog = activeProcessDialog)
            )

            is InventoryMainViewEvent.SaveDataToFile -> viewModel.process(
                processEvent.copy(processDialog = activeProcessDialog)
            )

            is InventoryMainViewEvent.CloseInventory -> viewModel.process(
                processEvent.copy(processDialog = activeProcessDialog)
            )

            else -> {}
        }
    }

    private fun showAlertDialog(msg: Int, onOkClickListener: () -> Unit) {
        alertDialog(requireContext(), getString(msg)) {
            onOkClickListener()
        }
    }

    private val openExcelFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val excelFileUri: Uri? = data?.data
                excelFileUri?.let {
                    activity?.let { it1 ->
                        viewModel.process(
                            InventoryMainViewEvent.ShowProcessDialog(
                                R.string.file_load_action_message,
                                InventoryMainViewEvent.LoadDataFromFile(
                                    it,
                                    it1.contentResolver
                                )
                            )
                        )
                    }
                }
            }
        }

    private fun openFileManager() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
        val mimetypes = arrayOf(
            ExcelUtil.MEM_TYPE_XLS,  // .xls
            ExcelUtil.MEM_TYPE_XLSX // .xlsx
        )
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        openExcelFileLauncher.launch(intent)
    }
}