package com.example.forecasting.domain.use_case.provider.unit

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.forecasting.domain.use_case.enums.TempUnitSystem
import com.example.forecasting.domain.use_case.enums.WindUnitSystem

//key in preference xml file
const val UNIT_SYSTEM = "UNIT_SYSTEM"
const val TEMP_UNIT_SYSTEM= "TEMP_UNIT_SYSTEM"
const val Wind_UNIT_SYSTEM= "Wind_UNIT_SYSTEM"
class UnitProviderImpl(context: Context) :
    UnitProvider {
    private val appContext = context.applicationContext
    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)




   /* override fun getUnitSystem():UnitSystem{
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        Toast.makeText(appContext,""+UnitSystem.METRIC,Toast.LENGTH_LONG).show()
        Toast.makeText(appContext,""+selectedName,Toast.LENGTH_LONG).show()
        Toast.makeText(appContext,""+UnitSystem.valueOf(selectedName!!),Toast.LENGTH_LONG).show()
       // Toast.makeText(appContext,""+selectedName+ Locale.getDefault().language,Toast.LENGTH_LONG).show()
        return UnitSystem.valueOf(selectedName!!)
    }*/

    override fun getTempUnitSystem(): TempUnitSystem {
        val selectedTempUnit= preferences.getString(TEMP_UNIT_SYSTEM, TempUnitSystem.KELVIN.name)
        return TempUnitSystem.valueOf(selectedTempUnit!!)
    }

    override fun getWindUnitSystem(): WindUnitSystem {
        val selectedWindUnit= preferences.getString(Wind_UNIT_SYSTEM,WindUnitSystem.METRE.name)
        return WindUnitSystem.valueOf(selectedWindUnit!!)
    }
}