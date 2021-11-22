package hu.triszt4n.kittygram.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.triszt4n.kittygram.data.dao.CollectionDao
import hu.triszt4n.kittygram.data.dao.KittyDao
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty

@Database(entities = [Kitty::class, Collection::class], version = 1, exportSchema = false)
abstract class KittyDatabase : RoomDatabase() {

    abstract fun kittyDao(): KittyDao
    abstract fun collectionDao(): CollectionDao

    companion object {
        @Volatile
        private var INSTANCE: KittyDatabase? = null

        fun getDatabase(context: Context): KittyDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                KittyDatabase::class.java,
                "kitty_database"
            ).build()
            INSTANCE = instance
            return instance
        }
    }

}