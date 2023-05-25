package pro.fateeva.pillsreminder.ui.screens.pillslist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.databinding.FragmentPillsListBinding
import pro.fateeva.pillsreminder.databinding.ItemPillBinding
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.toCalendar
import pro.fateeva.pillsreminder.ui.RecyclerAdapter
import pro.fateeva.pillsreminder.ui.screens.BaseFragment

class PillsListFragment :
    BaseFragment<FragmentPillsListBinding>(FragmentPillsListBinding::inflate) {

    private val viewModel: PillsListViewModel by viewModel()

    val adapter = RecyclerAdapter<MedicationReminder>(
        emptyList(),
        R.layout.item_pill
    ) { medicationreminder, _ ->
        ItemPillBinding.bind(this).apply {
            medicationTitleTextView.text = medicationreminder.medicationName
            scheduleTextView.text =
                medicationreminder.medicationIntakes.map { it.time.toCalendar().formatTime() }
                    .joinToString(", ")
            pillCardView.setOnClickListener {
                val size = medicationreminder.medicationIntakes.size
                if(size == 1){
                    navigator.navigateToOncePerDayScreen(medicationreminder.id)
                } else if (size == 2){
                    navigator.navigateToTwicePerDayScreen(medicationreminder.id)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addMedicationEventFab.setOnClickListener {
            navigator.navigateToPillSearchingScreen()
        }

        binding.recyclerView.adapter = adapter

        viewModel.getLiveData.observe(viewLifecycleOwner) {
            renderMedicationReminders(it)
        }

        viewModel.getMedicationReminders()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMedicationReminders()
    }

    private fun renderMedicationReminders(medicationReminders: List<MedicationReminder>) {
        binding.emptyPillsListLottie.isVisible = medicationReminders.isEmpty()
        adapter.itemList = medicationReminders
    }
}