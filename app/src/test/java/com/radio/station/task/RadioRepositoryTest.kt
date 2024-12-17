package com.radio.station.task

import com.radio.station.task.repository.LazyRadioRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class RadioRepositoryTest {
    @Test
    fun `fetchStations returns valid data`() = runBlocking {
        val repository = LazyRadioRepository()
        val stations = repository.getNextStations()
        assertTrue(stations.isNotEmpty())

    }
}
