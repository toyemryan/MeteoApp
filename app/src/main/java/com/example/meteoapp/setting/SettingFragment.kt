package com.example.meteoapp.setting

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.meteoapp.R


class SettingFragment : PreferenceFragmentCompat() {

    override fun onResume() {
        super.onResume()
        val toolbarTitle = (activity as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString (R.string.setting)
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}