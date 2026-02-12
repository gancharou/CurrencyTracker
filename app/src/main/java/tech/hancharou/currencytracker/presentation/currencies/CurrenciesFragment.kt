package tech.hancharou.currencytracker.presentation.currencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tech.hancharou.currencytracker.presentation.currencies.widget.CurrenciesScreen
import tech.hancharou.currencytracker.presentation.theme.CurrencyTrackerTheme

@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private val viewModel: CurrenciesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeActions()

        return ComposeView(requireContext()).apply {
            setContent {
                CurrencyTrackerTheme {
                    CurrenciesScreen(viewModel = viewModel)
                }
            }
        }
    }

    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        is CurrenciesActions.NavigateToFilters -> {
                            // TODO: Navigate to Filters screen
                        }

                        is CurrenciesActions.ShowError -> {
                            showError(action.message)
                        }
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}