package com.irepka3.mygarden.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.irepka3.mygarden.util.Const.APP_TAG
import java.sql.SQLException

/**
 * База данных ROOM
 *
 * Created by i.repkina on 31.10.2021.
 */
@Database(
    entities = [
        FlowerbedEntity::class,
        PlantEntity::class,
        FlowerbedPhotoEntity::class,
        PlantPhotoEntity::class,
        RepeatWorkEntity::class,
        ScheduleEntity::class,
        WorkEntity::class], version = 2
)
abstract class AppRoomDataBase: RoomDatabase() {
    abstract val flowerbedDao: FlowerbedEntityDao
    abstract val plantDao: PlantEntityDao
    abstract val flowerbedPhotoDao: FlowerbedPhotoEntityDao
    abstract val plantPhotoDao: PlantPhotoEntityDao
    abstract val repeatWorkDao: RepeatWorkEntityDao
    abstract val scheduleDao: ScheduleEntityDao
    abstract val workDao: WorkEntityDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDataBase? = null
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // todo вынести миграции в отдельный Object класс и каждую миграцию в нем оформить отдельным методом
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
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d(TAG, "Database.onCreate() called")
                            super.onCreate(db)
                            createDemoData(db)
                        }
                    })
                    .build()
                    .also { roomDatabase ->
                        INSTANCE = roomDatabase
                        Log.d(TAG, "Room.databaseBuilder.build(), INSTANCE called")
                    }
            }
        }

        // Создание клумб с растениями для демо-версии
        private fun createDemoData(db: SupportSQLiteDatabase) {
            Log.d(TAG, "createDemoData() called")
            try {
                db.beginTransaction()
                for (flowerbedIndex in 1..COUNT_DEMO_FLOWERBED) {

                    db.execSQL(
                        "INSERT INTO ${FlowerbedEntity.TABLE_NAME} " +
                                "(${FlowerbedEntity.COLUMN_NAME}, " +
                                "${FlowerbedEntity.COLUMN_DESCRIPTION}, " +
                                "${FlowerbedEntity.COLUMN_ORDER}" +
                                ") VALUES (" +
                                "\"Flowerbed $flowerbedIndex\", " +
                                "\"Demo flowerbed description$flowerbedIndex\", " +
                                "\"$flowerbedIndex\"" +
                                ")"
                    )
                }
                for (plantIndex in 1..COUNT_DEMO_PLANT) {
                    db.execSQL(
                        "INSERT INTO ${PlantEntity.TABLE_NAME}" +
                                "(${PlantEntity.COLUMN_FLOWERBED_ID}," +
                                "${PlantEntity.COLUMN_NAME}," +
                                "${PlantEntity.COLUMN_DESCRIPTION}," +
                                "${PlantEntity.COLUMN_ORDER}, " +
                                "${PlantEntity.COLUMN_COUNT}" +
                                ") SELECT " +
                                "${FlowerbedEntity.COLUMN_FLOWERBED_ID}, " +
                                "\"Plant $plantIndex\", " +
                                "\"Plant $plantIndex description\", " +
                                "$plantIndex, " +
                                " 1 " +
                                "FROM ${FlowerbedEntity.TABLE_NAME} fb "
                    )
                }
                db.setTransactionSuccessful()
            } catch (e: SQLException) {
                Log.e(TAG, "createDemoData() failed with exception", e)
            } finally {
                db.endTransaction()
            }
        }

    }
}

const val COUNT_DEMO_FLOWERBED = 3
const val COUNT_DEMO_PLANT = 4

private const val TAG = "$APP_TAG.AppRoomDataBase"