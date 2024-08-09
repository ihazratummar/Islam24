package com.hazrat.islam24.auth.repository

import android.content.Context
import android.content.Intent
import com.hazrat.islam24.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class ProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ProfileRepository {
    override fun inviteFriend() {
        val text = context.getString(R.string.invite_friend)
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val shareIntent = Intent.createChooser(intent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(shareIntent)
    }

    override fun rateUs() {
        TODO("Not yet implemented")
    }
}