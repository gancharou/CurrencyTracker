package tech.hancharou.currencytracker.extension

import android.util.Log
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import tech.hancharou.currencytracker.R

fun Fragment.turnOnBottomBar() {
    if (!isAdded || activity == null) return

    try {
        activity?.findViewById<ComposeView>(R.id.nav_view)?.visibility = View.VISIBLE
    } catch (e: Exception) {
        Log.w("BottomBar", "Failed to show bottom bar", e)
    }
}

fun Fragment.turnOffBottomBar() {
    if (!isAdded || activity == null) return

    try {
        activity?.findViewById<ComposeView>(R.id.nav_view)?.visibility = View.GONE
    } catch (e: Exception) {
        Log.w("BottomBar", "Failed to hide bottom bar", e)
    }
}