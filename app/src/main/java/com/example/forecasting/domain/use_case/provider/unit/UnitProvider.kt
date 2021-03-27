package com.example.forecasting.domain.use_case.provider.unit

import com.example.forecasting.domain.use_case.enums.TempUnitSystem
import com.example.forecasting.domain.use_case.enums.WindUnitSystem


//import com.example.forecasting.utilities.UnitSystem

interface UnitProvider {
   // fun getUnitSystem():UnitSystem
    fun getTempUnitSystem(): TempUnitSystem
    fun getWindUnitSystem(): WindUnitSystem
}