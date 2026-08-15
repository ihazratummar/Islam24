package com.hazrat.domain.repository


/**
 * @author hazratummar
 * Created on 14/08/26
 */

interface SyncRepository {

    suspend fun performFullSync(): Boolean

}