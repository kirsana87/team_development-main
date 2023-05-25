package pro.fateeva.pillsreminder.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import pro.fateeva.pillsreminder.ui.mainactivity.AppNavigation

abstract class BaseFragment<VB : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater,
        root: ViewGroup?,
        attachToRoot: Boolean,
    ) -> VB,
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    protected lateinit var navigator: AppNavigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = inflateBinding.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = requireActivity() as AppNavigation
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}