package pro.fateeva.pillsreminder.ui.screens.twiceperday

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
import pro.fateeva.pillsreminder.databinding.FragmentTwicePerDaySettingsBinding
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.initTimePicker
import pro.fateeva.pillsreminder.ui.OperationState
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import java.util.*

private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1
private const val FIRST_MEDICATION_INTAKE_INDEX = 0
private const val SECOND_MEDICATION_INTAKE_INDEX = 1
private const val MEDICATION_REMINDER_ID_ARG_KEY = "MEDICATION_REMINDER_ID_ARG_KEY"
private const val TIME_PICKER_TAG = "TIME_PICKER"

class TwicePerDaySettingsFragment : BaseFragment<FragmentTwicePerDaySettingsBinding>(
    FragmentTwicePerDaySettingsBinding::inflate
) {
    private val medicationReminderId by lazy {
        requireArguments().getInt(MEDICATION_REMINDER_ID_ARG_KEY)
    }

    private val viewModel: TwicePerDaySettingsViewModel by stateViewModel()

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

        viewModel.hasFirstMedicationDoseError.observe(viewLifecycleOwner) {
            binding.firstDoseErrorTextView.isVisible = it
        }

        viewModel.hasSecondMedicationDoseError.observe(viewLifecycleOwner) {
            binding.secondDoseErrorTextView.isVisible = it
        }

        viewModel.successErrorSaveState.observe(viewLifecycleOwner){
            if (it == OperationState.SUCCESS) {
                navigator.navigateToPillListScreen()
            }
        }

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

        binding.firstTimePickerTextView.initTimePicker(
            Calendar.getInstance().apply{
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND,0)
                set(Calendar.MILLISECOND,0)
            }.timeInMillis,
            parentFragmentManager
        ) {
            viewModel.setMedicationReminderTime(it.timeInMillis, FIRST_MEDICATION_INTAKE_INDEX)
            binding.firstTimePickerTextView.setText(it.formatTime())
        }

        binding.firstDosePickerEditText.doAfterTextChanged {
            viewModel.setDose(it.toString(), FIRST_MEDICATION_INTAKE_INDEX)
        }

        binding.secondTimePickerTextView.initTimePicker(
            Calendar.getInstance().apply{
                set(Calendar.HOUR_OF_DAY, 20)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND,0)
                set(Calendar.MILLISECOND,0)
            }.timeInMillis,
            parentFragmentManager
        ) {
            viewModel.setMedicationReminderTime(it.timeInMillis, SECOND_MEDICATION_INTAKE_INDEX)
            binding.secondTimePickerTextView.setText(it.formatTime())
        }

        binding.secondDosePickerEditText.doAfterTextChanged {
            viewModel.setDose(it.toString(), SECOND_MEDICATION_INTAKE_INDEX)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.medicationTitleTextView.text = it.medicationName

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.firstMedicationReminderTime
            binding.firstTimePickerTextView.text = calendar.formatTime()
            binding.firstDosePickerEditText.setText(it.firstMedicationDose.toString())

            calendar.timeInMillis = it.secondMedicationReminderTime
            binding.secondTimePickerTextView.text = calendar.formatTime()
            binding.secondDosePickerEditText.setText(it.secondMedicationDose.toString())

            binding.firstTimePickerTextView.initTimePicker(
                it.firstMedicationReminderTime,
                parentFragmentManager,
                TIME_PICKER_TAG
            ) {
                viewModel.setMedicationReminderTime(it.timeInMillis, 0)
                binding.firstTimePickerTextView.text = it.formatTime()
            }

            binding.secondTimePickerTextView.initTimePicker(
                it.secondMedicationReminderTime,
                parentFragmentManager,
                TIME_PICKER_TAG
            ) {
                viewModel.setMedicationReminderTime(it.timeInMillis, 1)
                binding.secondTimePickerTextView.text = it.formatTime()
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

    companion object {
        fun newInstance(drugDomain: DrugDomain, daysCount: Int): TwicePerDaySettingsFragment {
            return TwicePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    DRUG_ARG_KEY to drugDomain,
                    DAYS_COUNT_ARG_KEY to daysCount
                )
            }
        }

        fun newInstance(id: Int): TwicePerDaySettingsFragment {
            return TwicePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    MEDICATION_REMINDER_ID_ARG_KEY to id
                )
            }
        }
    }
}
