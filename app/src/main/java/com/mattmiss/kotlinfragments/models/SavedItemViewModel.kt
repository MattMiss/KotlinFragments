package com.mattmiss.kotlinfragments.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mattmiss.kotlinfragments.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedItemViewModel(application: Application) : AndroidViewModel(application){

    private val foodRepository: SavedItemRepository
    private val recipeRepository: SavedRecipeRepository
    private val categoryRepository: CategoryRepository
    private val mealRepository: MealRepository

    val allSavedFood: LiveData<List<SavedItem>>
    val allSavedRecipes: LiveData<List<SavedRecipe>>
    val allCategories: LiveData<List<Category>>
    val allMeals: LiveData<List<Meal>>

    init {
        val savedItemDao = SavedItemDatabase.getDatabase(
            application,
            viewModelScope
        ).savedItemDao()
        val savedRecipeDao = SavedItemDatabase.getDatabase(
            application,
            viewModelScope
        ).savedRecipeDao()
        val categoryDao = SavedItemDatabase.getDatabase(
            application,
            viewModelScope
        ).categoryDao()
        val mealDao = SavedItemDatabase.getDatabase(
            application,
            viewModelScope
        ).mealDao()

        foodRepository = SavedItemRepository(savedItemDao)
        recipeRepository = SavedRecipeRepository(savedRecipeDao)
        categoryRepository = CategoryRepository(categoryDao)
        mealRepository = MealRepository(mealDao)

        allSavedFood = foodRepository.allSavedItems
        allSavedRecipes = recipeRepository.allSavedRecipes
        allCategories = categoryRepository.allCategories
        allMeals = mealRepository.allMeals
    }

    // Insert and Delete functions for SavedItems
    fun insertFood(savedItem: SavedItem) = viewModelScope.launch(Dispatchers.IO){
        foodRepository.insert(savedItem)
    }

    fun deleteFood(savedItem: SavedItem) = viewModelScope.launch(Dispatchers.IO){
        foodRepository.delete(savedItem)
    }

    // Insert and Delete functions for Recipes
    fun insertRecipe(savedRecipe: SavedRecipe) = viewModelScope.launch(Dispatchers.IO){
        recipeRepository.insert(savedRecipe)
    }

    fun deleteRecipe(savedRecipe: SavedRecipe) = viewModelScope.launch(Dispatchers.IO){
        recipeRepository.delete(savedRecipe)
    }

    // Insert and Delete functions for Categories
    fun insertCategory(category: Category) = viewModelScope.launch(Dispatchers.IO){
        categoryRepository.insert(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch(Dispatchers.IO){
        categoryRepository.delete(category)
    }

    // Insert and Delete functions for Meals
    fun insertMeal(meal: Meal) = viewModelScope.launch(Dispatchers.IO){
        mealRepository.insert(meal)
    }

    fun deleteMeal(meal: Meal) = viewModelScope.launch(Dispatchers.IO){
        mealRepository.delete(meal)
    }


}

