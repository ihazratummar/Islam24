package com.hazrat.utils.result.error


/**
 * @author hazratummar
 * Created on 30/05/26
 */

interface DatabaseError: RootError {

    data object NotFound: DatabaseError
    data object UnknownError: DatabaseError

}