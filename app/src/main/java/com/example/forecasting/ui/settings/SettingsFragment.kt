package com.example.forecasting.ui.settings

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import com.example.forecasting.R
import com.example.forecasting.data.provider.enums.LanguageUnitSystem
import com.example.forecasting.ui.favourite.MapsActivity
import java.util.*


@Suppress("UNREACHABLE_CODE")
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            requireContext().resources.getString(
                requireContext().resources.getIdentifier(
                    "settings_label", "string",
                    requireContext().packageName
                )
            )
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null

        val mListPreference =
            preferenceManager.findPreference<Preference>("CUSTOM_LOCATION") as Preference
        mListPreference.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                Log.i("settings", "custom: ")
                val intent = Intent(activity, MapsActivity::class.java)
                intent.putExtra("case", "custom")
                startActivity(intent)
                true
            }


        val languageListPreference =
            preferenceManager.findPreference<Preference>("LANGUAGE_UNIT_SYSTEM") as ListPreference
        languageListPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { old, new ->
                Log.i("language", "layout-${new.toString()}")
                var lang = "en"
                if (new.toString() == "ARABIC") {
                    Log.i("language", "arabic")
                    lang = "ar"
                } else {
                    Log.i("language", "english")
                    lang = "en"
                }
                Log.i("language", "origin ${lang}")
                val locale = Locale(lang)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                config.setLocale(locale)
                activity?.baseContext?.resources?.updateConfiguration(
                    config,
                    activity?.baseContext?.resources?.displayMetrics
                )
                activity?.recreate()
                // Log.i("seetings", "onActivityCreated: activity recreated")
                true
            }

        val mapTextPreference =
            preferenceManager.findPreference<Preference>("USE_DEVICE_LOCATION") as SwitchPreference
        mapTextPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { old, new ->
                Log.i("settings", "temp: " + old)
                // Log.i("settings", "temp2: "+new)
                if (new == false) {

                }
                true
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }


    /*  override fun onSharedPreferenceChanged(
          sharedPreferences: SharedPreferences?,
          key: String?
      ) {
          when (key) {
              "LANGUAGE_UNIT_SYSTEM" -> {
                  LocalHelper.setLocale(
                      context,
                  "English"
                  )
                  requireActivity().recreate() // necessary here because this Activity is currently running and thus a recreate() in onResume() would be too late
              }
          }
      }

      override fun onResume() {
          super.onResume()
          // documentation requires that a reference to the listener is kept as long as it may be called, which is the case as it can only be called from this Fragment
          preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
      }

      override fun onPause() {
          super.onPause()
          preferenceScreen.sharedPreferences
              .unregisterOnSharedPreferenceChangeListener(this)
      }*/

}