package com.example.forecasting.utilities

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.preference.PreferenceManager
import java.util.*


class LocalHelper {

    companion object{
        fun onAttach(context: Context?): Context? {
            val locale = getPersistedLocale(context)
            return setLocale(context, locale)
        }

        fun getPersistedLocale(context: Context?): String? {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString("LANGUAGE_UNIT_SYSTEM", "")
        }


        fun setLocale(
            context: Context?,
            localeSpec: String?
        ): Context? {
            var locale: Locale
            if (localeSpec == "system") {
                locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Resources.getSystem().getConfiguration().getLocales().get(0)
                } else {
                    Resources.getSystem().getConfiguration().locale
                }
            } else {
                locale = Locale(localeSpec)
            }
            Locale.setDefault(locale)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context!!, locale)
            } else {
                updateResourcesLegacy(context!!, locale)
            }
        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResources(
            context: Context,
            locale: Locale
        ): Context? {
            val configuration: Configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }

        private fun updateResourcesLegacy(
            context: Context,
            locale: Locale
        ): Context? {
            val resources = context.resources
            val configuration: Configuration = resources.configuration
            configuration.locale = locale
            configuration.setLayoutDirection(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }
    }

}