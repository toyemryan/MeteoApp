package com.example.meteoapp.setting

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.meteoapp.NotificationHelper
import com.example.meteoapp.R
import com.example.meteoapp.SharedPreferences


class SettingFragment : PreferenceFragmentCompat() {

    override fun onResume() {
        super.onResume()
        val toolbarTitle = (activity as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString (R.string.setting)
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
    /*
        val notificationPref: Preference? = findPreference("Notification")
        notificationPref?.setOnPreferenceChangeListener { preference, newValue ->
            // newValue is the new value of the preference (true/false)
            val enabled = newValue as Boolean
            val sharedPreferences = SharedPreferences(requireContext())
            if (enabled) {
                sharedPreferences.saveNotificationState(true)
                enableNotification()
            } else {
                sharedPreferences.saveNotificationState(false)
                disableNotification()
            }
            true
        }
        */
    }

    /*
    private fun disableNotification() {
        val notificationManager =xt?.let { NotificationManagerCompat.from(it) }
        if (notificationManager != null) { notificationManager.cancelAll()
        }
    }

    private fun enableNotification() {
        val weatherUpdate = "Notification activée"
        context?.let { displayWeatherNotification(it,weatherUpdate) }
    }


    fun displayWeatherNotification(context: Context, weatherUpdate: String) {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showWeatherNotification(weatherUpdate)
    }

     */

}
