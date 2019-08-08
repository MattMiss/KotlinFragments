package com.mattmiss.kotlinfragments.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MealRepository(private val mealDao: MealDao){
    val allMeals: LiveData<List<Meal>> = mealDao.getAllMeals()


    //You must call this on a non-UI thread or your app will crash. Room ensures that you
    // don't do any long-running operations on the main thread, blocking the UI. Add the
    // @WorkerThread annotation, to mark that this method needs to be called from a non-UI thread.
    // Add the suspend modifier to tell the compiler that this needs to be called from a
    // coroutine or another suspending function.

    @WorkerThread
    suspend fun insert(meal: Meal){
        mealDao.insert(meal)
    }

    @WorkerThread
    suspend fun delete(meal: Meal){
        mealDao.delete(meal)
    }
}