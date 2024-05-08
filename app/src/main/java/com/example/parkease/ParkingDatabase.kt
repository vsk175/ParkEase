package com.example.parkease

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context


@Database(entities = [ParkingPlace::class], version = 1, exportSchema = false)
abstract class ParkingDatabase : RoomDatabase() {
    // Abstract method that returns your DAO
    abstract fun parkingDao(): ParkingDAO

    // Companion object to provide a singleton instance
    companion object {
        @Volatile
        private var INSTANCE: ParkingDatabase? = null

        fun getDatabase(context: Context): ParkingDatabase {
            // Return the existing instance or create a new one
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParkingDatabase::class.java,
                    "parking_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
