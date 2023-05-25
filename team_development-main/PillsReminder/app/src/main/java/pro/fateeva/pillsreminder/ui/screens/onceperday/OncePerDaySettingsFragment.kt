package pro.fateeva.pillsreminder.ui.screens.onceperday

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.databinding.FragmentOncePerDaySettingsBinding
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.initTimePicker
import pro.fateeva.pillsreminder.ui.OperationState
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import java.util.*

private const val TIME_PICKER_TAG = "TIME_PICKER"
private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1
private const val MEDICATION_REMINDER_ID_ARG_KEY = "MEDICATION_REMINDER_ID_ARG_KEY"

class OncePerDaySettingsFragment :
    BaseFragment<FragmentOncePerDaySettingsBinding>(FragmentOncePerDaySettingsBinding::inflate) {

    private val medicationReminderId by lazy {
        requireArguments().getInt(MEDICATION_REMINDER_ID_ARG_KEY)
    }

    private val viewModel: OncePerDaySettingsViewModel by stateViewModel()

    companion object {
        fun newInstance(drugDomain: DrugDomain, daysCount: Int): OncePerDaySettingsFragment {
            return OncePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    DRUG_ARG_KEY to drugDomain,
                    DAYS_COUNT_ARG_KEY to daysCount
                )
            }
        }

        fun newInstance(id: Int): OncePerDaySettingsFragment {
            return OncePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    MEDICATION_REMINDER_ID_ARG_KEY to id
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                viewModel.deleteReminder(medicationReminderId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedDrug = arguments?.getParcelable(DRUG_ARG_KEY) ?: DrugDomain()
        val medicationDaysCount = arguments?.getInt(DAYS_COUNT_ARG_KEY)
            ?: DEFAULT_DAYS_COUNT_VALUE

        if (medicationReminderId == 0) {
            viewModel.onViewCreated(selectedDrug.drugName)

            binding.planButton.setOnClickListener {
                viewModel.onCreateMedicationReminder(medicationDaysCount, selectedDrug)
            }
        } else {
            viewModel.onViewCreated(medicationReminderId)

            binding.planButton.setOnClickListener {
                viewModel.onEditMedicationReminder(medicationReminderId)
            }
        }

        binding.oncePerDayTimePickerTextView.initTimePicker(
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis,
            parentFragmentManager,
            TIME_PICKER_TAG
        ) {
            viewModel.setMedicationReminderTime(it.timeInMillis)
            binding.oncePerDayTimePickerTextView.text = it.formatTime()
        }

        binding.dosePickerEditText.doAfterTextChanged {
            viewModel.setDose(it.toString())
        }

        viewModel.hasMedicationDoseError.observe(viewLifecycleOwner) {
            binding.doseErrorTextView.isVisible = it
        }

        viewModel.saveState.observe(viewLifecycleOwner) {
            if (it == OperationState.SUCCESS) {
                navigator.navigateToPillListScreen()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.medicationReminderTime
            binding.medicationTitleTextView.text = it.medicationName
            binding.oncePerDayTimePickerTextView.text = calendar.formatTime()
            binding.dosePickerEditText.setText(it.medicationDose.toString())

            binding.oncePerDayTimePickerTextView.initTimePicker(
                it.medicationReminderTime,
                parentFragmentManager,
                TIME_PICKER_TAG
            ) { calendar ->
                viewModel.setMedicationReminderTime(calendar.timeInMillis)
                binding.oncePerDayTimePickerTextView.text = calendar.formatTime()
            }
        }

        viewModel.canDelete.observe(viewLifecycleOwner) {
            setHasOptionsMenu(it)
        }

        viewModel.deleteState.observe(viewLifecycleOwner) {
            if (it == OperationState.SUCCESS) {
                navigator.navigateToPillListScreen()
            }
        }
    }
}
