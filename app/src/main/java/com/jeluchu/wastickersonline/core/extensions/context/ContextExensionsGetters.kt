package com.jeluchu.wastickersonline.core.extensions.context

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun ViewGroup.inflate(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

class ContextHandler
    (private val context: Context) {
    val appContext: Context get() = context.applicationContext
}

inline val buildIsMAndLower: Boolean
    get() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.M

@SuppressLint("NewApi")
fun Context.checkNetworkState(): Boolean {
    val connectivityManager =
            this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (!buildIsMAndLower) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val nw = connectivityManager.activeNetworkInfo ?: return false
        return when (nw.type) {
            (NetworkCapabilities.TRANSPORT_WIFI) -> true
            (NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}