package tech.hancharou.currencytracker.presentation.main

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tech.hancharou.currencytracker.R
import tech.hancharou.currencytracker.databinding.ActivityMainBinding
import tech.hancharou.currencytracker.presentation.main.widget.BottomMenu
import tech.hancharou.currencytracker.presentation.theme.CurrencyTrackerTheme

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorStatusBar()
        setupNavigation()
        observeViewModel()
        setupBottomMenu()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navController.graph = navGraph
    }

    private fun setupBottomMenu() {
        binding.navView.setContent {
            CurrencyTrackerTheme {
                BottomMenu(
                    viewModel = mainViewModel,
                    navController = navController
                )
            }
        }
    }

    private fun setColorStatusBar() {
        val window = this.window

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
                window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)
            }

            else -> {
                window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setColorStatusBar()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.actions.collect { action ->
                    when (action) {
                        is MainActions.OnCurrenciesClick -> {
                            navController.navigate(R.id.action_global_currenciesFragment)
                        }

                        is MainActions.OnFavoritesClick -> {
                            navController.navigate(R.id.action_global_favoritesFragment)
                        }
                    }
                }
            }
        }
    }
}