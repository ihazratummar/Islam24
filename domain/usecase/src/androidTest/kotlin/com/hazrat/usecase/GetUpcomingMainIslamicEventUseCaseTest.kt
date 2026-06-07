package com.hazrat.usecase

import org.junit.Test


/**
 * @author hazratummar
 * Created on 16/05/26
 */

class GetUpcomingMainIslamicEventUseCaseTest {

    @Test
    fun getRamadanData() {
        val data = GetUpcomingMainIslamicEventUseCase().invoke()

        println("This is my test ${data.hijriMonth}")
        assert(data.hijriMonth == "Ramadhan")

    }

}