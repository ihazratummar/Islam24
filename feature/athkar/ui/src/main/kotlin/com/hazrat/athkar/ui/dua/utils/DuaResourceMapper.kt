package com.hazrat.athkar.ui.dua.utils

import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.ui.R

/**
 * Maps HisnulMuslimCategory to drawables in core/ui.
 * @author hazratummar
 */
object DuaResourceMapper {
    fun getCategoryIcon(category: HisnulMuslimCategory): Int {
        return when (category) {
            HisnulMuslimCategory.MORNING_EVENING -> R.drawable.ic_cat_morning_evening
            HisnulMuslimCategory.PRAYER -> R.drawable.ic_cat_prayer
            HisnulMuslimCategory.PRAISING_ALLAH -> R.drawable.ic_cat_praising_allah
            HisnulMuslimCategory.HAJJ_UMRAH -> R.drawable.ic_cat_hajj_umrah
            HisnulMuslimCategory.TRAVEL -> R.drawable.ic_cat_travel
            HisnulMuslimCategory.JOY_DISTRESS -> R.drawable.ic_cat_joy_distress
            HisnulMuslimCategory.NATURE -> R.drawable.ic_cat_nature
            HisnulMuslimCategory.GOOD_ETIQUETTE -> R.drawable.ic_cat_good_etiquette
            HisnulMuslimCategory.HOME_FAMILY -> R.drawable.ic_cat_home_family
            HisnulMuslimCategory.FOOD_DRINK -> R.drawable.ic_cat_food_drink
            HisnulMuslimCategory.SICKNESS_DEATH -> R.drawable.ic_cat_sickness_death
        }
    }

    fun getCategoryBanner(category: HisnulMuslimCategory): Int {
        return when (category) {
            HisnulMuslimCategory.MORNING_EVENING -> R.drawable.banner_morning_evening
            HisnulMuslimCategory.PRAYER -> R.drawable.banner_prayer
            HisnulMuslimCategory.PRAISING_ALLAH -> R.drawable.banner_praising_allah
            HisnulMuslimCategory.HAJJ_UMRAH -> R.drawable.banner_hajj_umrah
            HisnulMuslimCategory.TRAVEL -> R.drawable.banner_travel
            HisnulMuslimCategory.JOY_DISTRESS -> R.drawable.banner_joy_distress
            HisnulMuslimCategory.NATURE -> R.drawable.banner_nature
            HisnulMuslimCategory.GOOD_ETIQUETTE -> R.drawable.banner_good_etiquette
            HisnulMuslimCategory.HOME_FAMILY -> R.drawable.banner_home_family
            HisnulMuslimCategory.FOOD_DRINK -> R.drawable.banner_food_drink
            HisnulMuslimCategory.SICKNESS_DEATH -> R.drawable.banner_sickness_death
        }
    }
}
