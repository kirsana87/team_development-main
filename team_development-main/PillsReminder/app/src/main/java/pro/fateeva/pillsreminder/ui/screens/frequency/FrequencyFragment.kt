package pro.fateeva.pillsreminder.ui.screens.frequency

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.databinding.FragmentFrequencyBinding
import pro.fateeva.pillsreminder.ui.screens.BaseFragment

private const val DRUG_ARG_KEY = "DRUG"

class FrequencyFragment :
    BaseFragment<FragmentFrequencyBinding>(FragmentFrequencyBinding::inflate) {

    private val daysCounter = DaysCounter()

    companion object {
        fun newInstance(drugDomain: DrugDomain): FrequencyFragment {
            return FrequencyFragment().apply {
                arguments = bundleOf(DRUG_ARG_KEY to drugDomain)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.whenNeededRadioButton.isEnabled = false // временно отключены все варианты, кроме "Один раз в день"

        val selectedDrug = arguments?.getParcelable(DRUG_ARG_KEY) ?: DrugDomain()

        binding.frequencyQuestionTextView.text =
            getString(R.string.medication_frequency_header, selectedDrug.drugName)

        binding.medicationDaysDecrementButton.setOnClickListener {
            daysCounter.decrement(binding.frequencyDaysCountTextView)
        }

        binding.medicationDaysIncrementButton.setOnClickListener {
            daysCounter.increment(binding.frequencyDaysCountTextView)
        }

        binding.frequencyNextScreenButton.setOnClickListener {
            when (binding.frequencyRadioGroup.checkedRadioButtonId) {
                binding.oncePerDayRadioButton.id -> {
                    navigator.navigateToOncePerDayScreen(
                        selectedDrug,
                        binding.frequencyDaysCountTextView.text.toString().toInt())
                }
                binding.twicePerDayRadioButton.id ->{
                    navigator.navigateToTwicePerDayScreen(
                        selectedDrug,
                        binding.frequencyDaysCountTextView.text.toString().toInt()
                    )
                }
            }
        }
    }
}