package com.hazrat.usecase

import org.junit.Test


/**
 * @author hazratummar
 * Created on 16/05/26
 */

class GetUpcomingIslamicEventUseCaseTest {

    @Test
    fun getRamadanData() {
        val data = GetUpcomingIslamicEventUseCase().invoke()

        println("This is my test ${data.hijriMonth}")
        assert(data.hijriMonth == "Ramadhan")

    }

}