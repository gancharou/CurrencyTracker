package tech.hancharou.currencytracker

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import tech.hancharou.currencytracker.domain.Repository

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var repository: Repository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            try {
                repository.refreshCurrencies()
            } catch (e: Exception) {
                Log.e("App", "Failed to refresh currencies: ${e.message}", e)
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }
}