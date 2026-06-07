package com.hazrat.auth.domain.usecase

import android.net.Uri
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.UserDataError

class UpdateProfilePictureUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(uri: Uri): Result<Unit, UserDataError> {
        return repository.updateProfilePicture(uri)
    }
}