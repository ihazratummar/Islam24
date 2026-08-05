package com.hazrat.auth.data.mapper

import com.hazrat.database.entity.profile.UserEntity
import com.hazrat.database.entity.profile.UserSupportStatusEntity
import com.hazrat.model.profile.SupporterStatusModel
import com.hazrat.model.profile.SupporterTickerModel
import com.hazrat.model.profile.UserModel
import com.hazrat.remote.dto.LiveCommunityTickerPayload
import com.hazrat.remote.dto.UserSupportStatusDto
import com.hazrat.remote.dto.auth.UserDto


/**
 * @author hazratummar
 * Created on 07/08/26
 */


fun UserEntity.toModel(): UserModel {
    return UserModel(
        id = id,
        googleId = googleId,
        name = name,
        email = email,
        picture = picture,
        createdAt = createdAt
    )
}

fun UserModel.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        googleId = googleId,
        name = name,
        email = email,
        picture = picture,
        createdAt = createdAt
    )
}

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        googleId = googleId,
        name = name,
        email = email,
        picture = picture,
        createdAt = createdAt
    )
}

fun UserDto.toModel(): UserModel {
    return UserModel(
        id = id,
        googleId = googleId,
        name = name,
        email = email,
        picture = picture,
        createdAt = createdAt
    )
}


fun LiveCommunityTickerPayload.toModel(): SupporterTickerModel {
    return SupporterTickerModel(
        eventId = eventId,
        donorName = donorName,
        type = type,
        amount = amount,
        currency = currency
    )
}

fun UserSupportStatusEntity.toModel(): SupporterStatusModel {
    return SupporterStatusModel(
        isSupporter = isSupporter,
        totalContributionUsd = totalContributionUsd,
        totalContributionLocal = totalContributionLocal,
        localCurrency = localCurrency,
        totalSupporter = totalSupporter,
    )
}

fun UserSupportStatusDto.toEntity() : UserSupportStatusEntity {
    return UserSupportStatusEntity(
        id = 1,
        isSupporter = isSupporter,
        totalContributionUsd = totalContributionUsd,
        totalContributionLocal = totalContributionLocal,
        localCurrency = localCurrency,
        totalSupporter = totalSupporter,
    )
}