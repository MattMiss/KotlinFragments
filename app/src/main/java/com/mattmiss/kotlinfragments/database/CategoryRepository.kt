package com.mattmiss.kotlinfragments.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CategoryRepository(private val categoryDao: CategoryDao){
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()


    //You must call this on a non-UI thread or your app will crash. Room ensures that you
    // don't do any long-running operations on the main thread, blocking the UI. Add the
    // @WorkerThread annotation, to mark that this method needs to be called from a non-UI thread.
    // Add the suspend modifier to tell the compiler that this needs to be called from a
    // coroutine or another suspending function.

    @WorkerThread
    suspend fun insert(category: Category){
        categoryDao.insert(category)
    }

    @WorkerThread
    suspend fun delete(category: Category){
        categoryDao.delete(category)
    }
}