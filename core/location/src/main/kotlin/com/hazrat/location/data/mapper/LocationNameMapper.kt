package com.hazrat.location.data.mapper

import com.hazrat.database.entity.LocationDetailsEntity
import com.hazrat.remote.dto.LocationNameDto


/**
 * @author hazratummar
 * Created on 28/01/26
 */

fun LocationNameDto.toLocationNameFinder() : String {
    return address.village ?: address.city ?: address.town ?: address.suburb ?:""
}



fun LocationDetailsEntity.toLocationNameFinder() : String {
    return locationName
}