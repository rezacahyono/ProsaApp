package com.rchyn.prosa.ui.fragments.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceFragmentCompat
import com.rchyn.prosa.R


class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            ?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(pref: SharedPreferences, key: String) {
        when (key) {
            getString(R.string.theme_key) -> {
                pref.edit().putBoolean(
                    getString(R.string.refresh_key), true).apply()
                when (pref.getString(
                    getString(R.string.theme_key),
                    getString(R.string.system_theme)
                )) {
                    getString(R.string.dark_theme) -> {
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                        )
                    }
                    getString(R.string.light_theme) -> {
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
            }
            getString(R.string.accent_key) -> hotReloadActivity(pref)
        }
    }

    private fun hotReloadActivity(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putBoolean(
            getString(R.string.refresh_key), true).apply()
        ActivityCompat.recreate(requireActivity())
    }
}