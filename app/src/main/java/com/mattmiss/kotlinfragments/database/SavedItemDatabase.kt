package com.mattmiss.kotlinfragments.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(SavedItem::class, SavedRecipe::class,
    Category::class, Meal::class), version = 1)
public abstract class SavedItemDatabase : RoomDatabase() {

    abstract fun savedItemDao(): SavedItemDao
    abstract fun savedRecipeDao(): SavedRecipeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun mealDao(): MealDao

    companion object{
        @Volatile
        private var INSTANCE: SavedItemDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SavedItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SavedItemDatabase::class.java,
                    "SavedItem_Database")
                    .addCallback(
                        SavedItemDatabaseCallback(
                            scope
                        )
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class SavedItemDatabaseCallback(
            private val scope: CoroutineScope) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.savedItemDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(savedItemDao: SavedItemDao) {
            //savedItemDao.deleteAll()

            //var savedItem = SavedItem("{\"food_id\":\"2635\",\"food_name\":\"Chicken or Turkey and Vegetables (Including Carrots, Broccoli, and\\/or Dark-Green Leafy, No Potatoes, Mixture)\",\"food_type\":\"Generic\",\"food_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)\",\"servings\":{\"serving\":[{\"calcium\":\"3\",\"calories\":\"146\",\"carbohydrate\":\"11.08\",\"cholesterol\":\"45\",\"fat\":\"3.90\",\"fiber\":\"2.4\",\"iron\":\"9\",\"measurement_description\":\"cup\",\"metric_serving_amount\":\"162.000\",\"metric_serving_unit\":\"g\",\"monounsaturated_fat\":\"1.343\",\"number_of_units\":\"1.000\",\"polyunsaturated_fat\":\"0.966\",\"potassium\":\"332\",\"protein\":\"16.93\",\"saturated_fat\":\"1.042\",\"serving_description\":\"1 cup\",\"serving_id\":\"9714\",\"serving_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)?portionid=9714&portionamount=1.000\",\"sodium\":\"411\",\"sugar\":\"2.90\",\"vitamin_a\":\"41\",\"vitamin_c\":\"13\"},{\"calcium\":\"3\",\"calories\":\"110\",\"carbohydrate\":\"8.34\",\"cholesterol\":\"34\",\"fat\":\"2.94\",\"fiber\":\"1.8\",\"iron\":\"7\",\"measurement_description\":\"serving (122g)\",\"metric_serving_amount\":\"122.000\",\"metric_serving_unit\":\"g\",\"monounsaturated_fat\":\"1.011\",\"number_of_units\":\"1.000\",\"polyunsaturated_fat\":\"0.727\",\"potassium\":\"250\",\"protein\":\"12.75\",\"saturated_fat\":\"0.784\",\"serving_description\":\"1 serving (122 g)\",\"serving_id\":\"10326\",\"serving_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)?portionid=10326&portionamount=1.000\",\"sodium\":\"310\",\"sugar\":\"2.18\",\"vitamin_a\":\"31\",\"vitamin_c\":\"10\"},{\"calcium\":\"2\",\"calories\":\"90\",\"carbohydrate\":\"6.84\",\"cholesterol\":\"28\",\"fat\":\"2.41\",\"fiber\":\"1.5\",\"iron\":\"5\",\"measurement_description\":\"g\",\"metric_serving_amount\":\"100.000\",\"metric_serving_unit\":\"g\",\"monounsaturated_fat\":\"0.829\",\"number_of_units\":\"100.000\",\"polyunsaturated_fat\":\"0.596\",\"potassium\":\"205\",\"protein\":\"10.45\",\"saturated_fat\":\"0.643\",\"serving_description\":\"100 g\",\"serving_id\":\"51315\",\"serving_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)?portionid=51315&portionamount=100.000\",\"sodium\":\"254\",\"sugar\":\"1.79\",\"vitamin_a\":\"26\",\"vitamin_c\":\"8\"},{\"calcium\":\"1\",\"calories\":\"26\",\"carbohydrate\":\"1.94\",\"cholesterol\":\"8\",\"fat\":\"0.68\",\"fiber\":\"0.4\",\"iron\":\"2\",\"measurement_description\":\"oz\",\"metric_serving_amount\":\"28.350\",\"metric_serving_unit\":\"g\",\"monounsaturated_fat\":\"0.235\",\"number_of_units\":\"1.000\",\"polyunsaturated_fat\":\"0.169\",\"potassium\":\"58\",\"protein\":\"2.96\",\"saturated_fat\":\"0.182\",\"serving_description\":\"1 oz\",\"serving_id\":\"181027\",\"serving_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)?portionid=181027&portionamount=1.000\",\"sodium\":\"72\",\"sugar\":\"0.51\",\"vitamin_a\":\"7\",\"vitamin_c\":\"2\"}]}}")
            //savedItemDao.insert(savedItem)
            //savedItem = SavedItem("{\"food_id\":\"2635\",\"food_name\":\"Chicken or Turkey and Vegetables (Including Carrots, Broccoli, and\\/or Dark-Green Leafy, No Potatoes, Mixture)\",\"food_type\":\"Generic\",\"food_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)\",\"servings\":{\"serving\":[{\"calcium\":\"3\",\"calories\":\"146\",\"carbohydrate\":\"11.08\",\"cholesterol\":\"45\",\"fat\":\"3.90\",\"fiber\":\"2.4\",\"iron\":\"9\",\"measurement_description\":\"cup\",\"metric_serving_amount\":\"162.000\",\"metric_serving_unit\":\"g\",\"monounsaturated_fat\":\"1.343\",\"number_of_units\":\"1.000\",\"polyunsaturated_fat\":\"0.966\",\"potassium\":\"332\",\"protein\":\"16.93\",\"saturated_fat\":\"1.042\",\"serving_description\":\"1 cup\",\"serving_id\":\"9714\",\"serving_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)?portionid=9714&portionamount=1.000\",\"sodium\":\"411\",\"sugar\":\"2.90\",\"vitamin_a\":\"41\",\"vitamin_c\":\"13\"},{\"calcium\":\"3\",\"calories\":\"110\",\"carbohydrate\":\"8.34\",\"cholesterol\":\"34\",\"fat\":\"2.94\",\"fiber\":\"1.8\",\"iron\":\"7\",\"measurement_description\":\"serving (122g)\",\"metric_serving_amount\":\"122.000\",\"metric_serving_unit\":\"g\",\"monounsaturated_fat\":\"1.011\",\"number_of_units\":\"1.000\",\"polyunsaturated_fat\":\"0.727\",\"potassium\":\"250\",\"protein\":\"12.75\",\"saturated_fat\":\"0.784\",\"serving_description\":\"1 serving (122 g)\",\"serving_id\":\"10326\",\"serving_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)?portionid=10326&portionamount=1.000\",\"sodium\":\"310\",\"sugar\":\"2.18\",\"vitamin_a\":\"31\",\"vitamin_c\":\"10\"},{\"calcium\":\"2\",\"calories\":\"90\",\"carbohydrate\":\"6.84\",\"cholesterol\":\"28\",\"fat\":\"2.41\",\"fiber\":\"1.5\",\"iron\":\"5\",\"measurement_description\":\"g\",\"metric_serving_amount\":\"100.000\",\"metric_serving_unit\":\"g\",\"monounsaturated_fat\":\"0.829\",\"number_of_units\":\"100.000\",\"polyunsaturated_fat\":\"0.596\",\"potassium\":\"205\",\"protein\":\"10.45\",\"saturated_fat\":\"0.643\",\"serving_description\":\"100 g\",\"serving_id\":\"51315\",\"serving_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)?portionid=51315&portionamount=100.000\",\"sodium\":\"254\",\"sugar\":\"1.79\",\"vitamin_a\":\"26\",\"vitamin_c\":\"8\"},{\"calcium\":\"1\",\"calories\":\"26\",\"carbohydrate\":\"1.94\",\"cholesterol\":\"8\",\"fat\":\"0.68\",\"fiber\":\"0.4\",\"iron\":\"2\",\"measurement_description\":\"oz\",\"metric_serving_amount\":\"28.350\",\"metric_serving_unit\":\"g\",\"monounsaturated_fat\":\"0.235\",\"number_of_units\":\"1.000\",\"polyunsaturated_fat\":\"0.169\",\"potassium\":\"58\",\"protein\":\"2.96\",\"saturated_fat\":\"0.182\",\"serving_description\":\"1 oz\",\"serving_id\":\"181027\",\"serving_url\":\"https:\\/\\/www.fatsecret.com\\/calories-nutrition\\/generic\\/chicken-or-turkey-and-vegetables-(including-carrots-broccoli-and-or-dark-green-leafy-(no-potatoes))-no-sauce-(mixture)?portionid=181027&portionamount=1.000\",\"sodium\":\"72\",\"sugar\":\"0.51\",\"vitamin_a\":\"7\",\"vitamin_c\":\"2\"}]}}")
            //savedItemDao.insert(savedItem)
        }
    }






}