package com.hazrat.islam24.core.domain.model.ui_text_model

import androidx.annotation.StringRes
import com.hazrat.islam24.R

data class BenefitsOfRecitingData(
    val number: Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
)
val benefitsOfRecitingDataList = listOf(
    BenefitsOfRecitingData(
        number = 1,
        title = R.string.the_prevalence_of_scientific_miracles,
        description = R.string.the_prevalence_of_scientific_miracles_description
    ),
    BenefitsOfRecitingData(
        number = 2,
        title = R.string.InspirationforScientificResearch,
        description = R.string.InspirationforScientificResearchDescription
    ),
    BenefitsOfRecitingData(
        number = 3,
        title = R.string.integration_of_faith_and_science,
        description = R.string.integration_of_faith_and_science_description
    ),
    BenefitsOfRecitingData(
        number = 4,
        title = R.string.stress_reduction,
        description = R.string.stress_reduction_description
    ),
    BenefitsOfRecitingData(
        number = 5,
        title =  R.string.cognitive_enhancement,
        description = R.string.cognitive_enhancement_description
    ),
    BenefitsOfRecitingData(
        number = 6,
        title = R.string.emotional_well_being,
        description = R.string.emotional_well_being_description
    ),
    BenefitsOfRecitingData(
        number = 7,
        title =  R.string.enhanced_recitation,
        description = R.string.enhanced_recitation_description
    ),
    BenefitsOfRecitingData(
        number = 8,
        title = R.string.clearer_understanding,
        description = R.string.clearer_understanding_description
    ),
)
