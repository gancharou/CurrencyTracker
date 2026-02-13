package tech.hancharou.currencytracker.presentation.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tech.hancharou.currencytracker.extension.turnOffBottomBar
import tech.hancharou.currencytracker.presentation.sort.widget.SortScreen
import tech.hancharou.currencytracker.presentation.theme.CurrencyTrackerTheme

@AndroidEntryPoint
class SortFragment : Fragment() {

    private val viewModel: SortViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeActions()

        return ComposeView(requireContext()).apply {
            setContent {
                CurrencyTrackerTheme {
                    SortScreen(viewModel = viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        turnOffBottomBar()
    }

    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        is SortActions.NavigateBack -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }
}