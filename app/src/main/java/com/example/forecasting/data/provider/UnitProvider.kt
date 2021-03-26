package com.example.forecasting.data.provider

import com.example.forecasting.data.provider.enums.TempUnitSystem
import com.example.forecasting.data.provider.enums.WindUnitSystem


//import com.example.forecasting.utilities.UnitSystem

interface UnitProvider {
   // fun getUnitSystem():UnitSystem
    fun getTempUnitSystem(): TempUnitSystem
    fun getWindUnitSystem(): WindUnitSystem
}