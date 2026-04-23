package com.hazrat.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class LocationNameDto(
    val address: AddressDto,
)

@Serializable
data class AddressDto(
    val village: String? = null,
    val city: String? = null,
    val town: String? = null,
    val suburb: String? = null
)
