package com.irepka3.mygarden.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

/**
 * База данных ROOM
 *
 * Created by i.repkina on 31.10.2021.
 */
@Database(entities = [FlowerbedEntity::class, PlantEntity::class], version = 2)
abstract class AppRoomDataBase: RoomDatabase() {
    abstract val flowerbedDao: FlowerbedEntityDao
    abstract val plantDao: PlantEntityDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDataBase? = null
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // todo вынести миграции в отдельный Object класс и каждую миграцию в нем оформить отдельным методом
                /*database.execSQL("DROP TABLE flowerbed")
                database.execSQL("CREATE TABLE 'FLOWERBEDENTITY'(`id` INTEGER, `name` TEXT, 'description' TEXT, 'comment' TEXT " +
                        "PRIMARY KEY(`id`))")*/
            }
        }

        fun getInstance(context: Context): AppRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    AppRoomDataBase::class.java, "AppRoomDataBase"
                )
                    // todo список миграций получать из отдельного класса миграций
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object: RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            createDemoData(getInstance(context))
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }

        // Создание клумб с растениями для демо-версии
        private fun createDemoData(database: AppRoomDataBase){
            Thread {
                for (index in 1..COUNT_DEMO_FLOWERBED) {
                    database.flowerbedDao.insert(createDemoFlowerBed(index))
                }

                val flowerBedList = database.flowerbedDao.getAll()
                if (flowerBedList != null) {
                    for (flowerbed in flowerBedList) {
                        for (index in 1..COUNT_DEMO_PLANT) {
                            database.plantDao.insert(createDemoPlants(flowerbed, index))
                        }
                    }
                }
            }.start()
        }

        private fun createDemoFlowerBed(index: Int): FlowerbedEntity{
            return FlowerbedEntity().apply {
                name = "Flowerbed $index"
                description = "Demo flowerbed description$index"
            }
        }

        private fun createDemoPlants(flowerbed: FlowerbedEntity, index: Int): PlantEntity {
            return PlantEntity().apply {
                flowerbedId = flowerbed.flowerbedId
                name = "Plant $index"
                description = "${flowerbed.name} $name description"
            }

        }
    }
}

const val COUNT_DEMO_FLOWERBED = 3
const val COUNT_DEMO_PLANT = 4
