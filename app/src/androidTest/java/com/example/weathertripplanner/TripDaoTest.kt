package com.example.weathertripplanner

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weathertripplanner.data.database.AppDatabase
import com.example.weathertripplanner.data.database.TripDao
import com.example.weathertripplanner.data.model.TripEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TripDaoTest {

    private lateinit var tripDao: TripDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        tripDao = db.tripDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeTripAndReadInList() = runBlocking {
        val trip = TripEntity(title = "Summer Trip", city = "Minsk", date = "20.06.2025")
        tripDao.insertTrip(trip)
        val allTrips = tripDao.getAllTrips().first()
        assertEquals(allTrips[0].title, trip.title)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTripAndCheckEmpty() = runBlocking {
        val trip = TripEntity(id = 1, title = "To Delete", city = "Brest", date = "01.01.2026")
        tripDao.insertTrip(trip)
        tripDao.deleteTrip(trip)
        val allTrips = tripDao.getAllTrips().first()
        assertTrue(allTrips.isEmpty())
    }
}
