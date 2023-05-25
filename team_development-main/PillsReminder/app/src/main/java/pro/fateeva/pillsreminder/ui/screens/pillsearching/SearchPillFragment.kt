package pro.fateeva.pillsreminder.ui.screens.pillsearching

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.databinding.FragmentSearchPillBinding
import pro.fateeva.pillsreminder.extensions.hideKeyboard
import pro.fateeva.pillsreminder.ui.screens.BaseFragment

private const val DEFAULT_DEBOUNCE = 400L
private const val DEFAULT_STATEFLOW_VALUE = "-1"

class SearchPillFragment :
    BaseFragment<FragmentSearchPillBinding>(FragmentSearchPillBinding::inflate) {

    private val pillSearchingViewModel by viewModel<SearchPillViewModel>()
    private val queryFlow = MutableStateFlow(DEFAULT_STATEFLOW_VALUE)
    private val textListener = TextTypeListener()
    private val searchPillAdapter = SearchPillAdapter(object : SearchItemClickListener {
        override fun onSearchItemClick(drugDomain: DrugDomain) {
            onItemClick(drugDomain)
        }
    })

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pillSearchingRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchPillAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        binding.pillSearchingRecyclerView.hideKeyboard()
                    }
                }
            })
        }

        pillSearchingViewModel.getData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        textListener.onTypeTextListener(binding.searchEditText, queryFlow)

        viewLifecycleOwner.lifecycleScope.launch {
            queryFlow
                .filter { it != DEFAULT_STATEFLOW_VALUE }
                .debounce(DEFAULT_DEBOUNCE)
                .collect {
                    pillSearchingViewModel.searchPills(it)
                }
        }
    }

    private fun onItemClick(drugDomain: DrugDomain) {
        binding.root.hideKeyboard()
        navigator.navigateToEventFrequencyScreen(drugDomain)
    }

    private fun renderData(state: SearchPillState) {
        when (state) {
            SearchPillState.Loading -> {
                binding.pillsSearchingProgressBar.isVisible = true
            }
            is SearchPillState.Success -> {
                searchPillAdapter.setData(state.dataList)
                binding.pillsSearchingProgressBar.isVisible = false
            }
            is SearchPillState.Error -> {
                Snackbar.make(binding.root, state.error, Snackbar.LENGTH_SHORT).show()
                binding.pillsSearchingProgressBar.isVisible = false
            }
        }
    }
}