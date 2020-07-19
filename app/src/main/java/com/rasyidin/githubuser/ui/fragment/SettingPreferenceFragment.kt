package com.rasyidin.githubuser.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.services.AlarmReceiver

class SettingPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var languageSetting: String
    private lateinit var reminder: String

    private lateinit var reminderPreference: SwitchPreference
    private lateinit var languagePreference: Preference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preference)

        alarmReceiver = AlarmReceiver()
        init()
        onClickChangeLanguage()
        setSummaries()
    }

    private fun init() {
        reminder = resources.getString(R.string.key_reminder)
        languageSetting = resources.getString(R.string.key_language)

        reminderPreference = findPreference<SwitchPreference>(reminder) as SwitchPreference
        languagePreference = findPreference<Preference>(languageSetting) as Preference
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        reminderPreference.isChecked = sh.getBoolean(reminder, false)
        languagePreference.summary =
            sh.getString(languageSetting, resources.getString(R.string.set_language))
    }

    private fun onClickChangeLanguage() {
        languagePreference.setOnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            return@setOnPreferenceClickListener true
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == reminder) {
            reminderPreference.isChecked = sharedPreferences.getBoolean(reminder, false)
        }
        val state =
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(reminder, false)
        setReminder(state)

        if (key == languageSetting) {
            languagePreference.summary = sharedPreferences.getString(
                languageSetting,
                resources.getString(R.string.set_language)
            )

        }
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun setReminder(state: Boolean) {
        if (state) {
            context?.let { alarmReceiver.setRepeatingAlarm(it) }
        } else {
            context?.let { alarmReceiver.cancelAlarm(it) }
        }
    }

}