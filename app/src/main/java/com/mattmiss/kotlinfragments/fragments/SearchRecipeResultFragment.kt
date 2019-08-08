package com.mattmiss.kotlinfragments.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import com.mattmiss.kotlinfragments.*
import com.mattmiss.kotlinfragments.adapters.CustomListAdapter
import com.mattmiss.kotlinfragments.database.SavedItem
import com.mattmiss.kotlinfragments.models.SavedItemViewModel
import com.mattmiss.kotlinfragments.utils.Utils
import kotlinx.android.synthetic.main.recipe_search_dialog.*
import org.json.JSONException
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat


class SearchRecipeResultFragment : androidx.fragment.app.Fragment() {

    val SERVING_SIZES_STRING = "serving_sizes"
    val SINGLE_SERVING_STRING = "serving"
    val RECIPE_DESCRIPTION_STRING = "recipe_description"
    val CALCIUM_STRING = "calcium"
    val CALORIES_STRING= "calories"
    val CARBOHYDRATE_STRING = "carbohydrate"
    val CHOLESTEROL_STRING = "cholesterol"
    val FAT_STRING = "fat"
    val FIBER_STRING = "fiber"
    val IRON_STRING = "iron"
    val MONOUNSATURATED_FAT_STRING = "monounsaturated_fat"
    val POLYUNSATURATED_FAT_STRING = "polyunsaturated_fat"
    val POTASSIUM_STRING = "potassium"
    val PROTEIN_STRING = "protein"
    val SATURATED_FAT_STRING = "saturated_fat"
    val SODIUM_STRING = "sodium"
    val SUGAR_STRING = "sugar"
    val TRANS_FAT = "trans_fat"
    val VITAMIN_A_STRING = "vitamin_a"
    val VITAMIN_C_STRING = "vitamin_c"

    val NO_VALUE_STRING = "-"

    val INGREDIENTS_STRING = "ingredients"
    val DIRECTIONS_STRING = "directions"
    val NUMBER_OF_SERVINGS_STRING = "number_of_servings"

    private var ingredientCheck = false
    private var directionCheck = false
    private val ingredientsArray = arrayListOf<String>()
    private val directionsArray = arrayListOf<String>()
    private var isIngredientsExpanded = false
    private var isDirectionsExpanded = false
    private var arrowDownDrawable : Drawable? = null
    private var arrowUpDrawable : Drawable? = null
    private var numberOfServingsCheck = false

    private val utils = Utils


    private lateinit var savedItemViewModel: SavedItemViewModel


    companion object {
        fun newInstance(recipeJSONString: String, position: Int): SearchRecipeResultFragment {

            val sf = SearchRecipeResultFragment()

            val bundle = Bundle(1)

            bundle.putString("recipeJSONString", recipeJSONString)
            bundle.putInt("position", position)

            sf.setArguments(bundle)

            return sf

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.recipe_search_dialog, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeJSON = JSONObject(arguments?.getString("recipeJSONString"))
        val position = arguments?.getInt("position")

        val savedItem = SavedItem(recipeJSON.toString())

        ingredientCheck = recipeJSON.toString().contains(INGREDIENTS_STRING, ignoreCase = true)
        directionCheck = recipeJSON.toString().contains(DIRECTIONS_STRING, ignoreCase = true)
        numberOfServingsCheck = recipeJSON.toString().contains(NUMBER_OF_SERVINGS_STRING, ignoreCase = true)


        arrowDownDrawable = ResourcesCompat.getDrawable(activity!!.resources,
            R.drawable.ic_arrow_drop_down_black_24dp, null)

        arrowUpDrawable = ResourcesCompat.getDrawable(activity!!.resources,
            R.drawable.ic_arrow_drop_up_black_24dp, null)

        setLists(recipeJSON)
        setLabels(recipeJSON, position!!.toInt(), 1f)

        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)


        // Set adapter and buttons for Ingredients Layout
        var ingredientsAdapter = CustomListAdapter(
            ingredientList.context,
            R.layout.directions_list_view,
            ingredientsArray,
            "ingredient"
        )
        ingredientList.adapter = ingredientsAdapter

        // This sets the height of the noteList, otherwise it is only 1 note high
        Utils.setListViewHeightBasedOnChildren(ingredientList)
        expandableLayout_ingredients.initLayout()

        ingredientsBtnLayout.setOnClickListener{

            //// Sets the length of the List depending on how many items there are
            Utils.setListViewHeightBasedOnChildren(ingredientList)

            expandableLayout_ingredients.toggle()

            if (isIngredientsExpanded){
                arrowBtn1.setImageDrawable(arrowUpDrawable)
                isIngredientsExpanded = false
            }else{
                arrowBtn1.setImageDrawable(arrowDownDrawable)
                isIngredientsExpanded = true
            }

            expandableLayout_ingredients.initLayout()
        }


        // Set adapter and buttons for Ingredients Layout
        var directionsAdapter = CustomListAdapter(
            directionList.context,
            R.layout.directions_list_view,
            directionsArray,
            "direction"
        )
        directionList.adapter = directionsAdapter

        // This sets the height of the noteList, otherwise it is only 1 note high
        Utils.setListViewHeightBasedOnChildren(directionList)
        expandableLayout_directions.initLayout()

        directionsBtnLayout.setOnClickListener{

            //// Sets the length of the List depending on how many items there are
            Utils.setListViewHeightBasedOnChildren(directionList)

            expandableLayout_directions.toggle()

            if (isDirectionsExpanded){
                arrowBtn2.setImageDrawable(arrowUpDrawable)
                isDirectionsExpanded = false
            }else{
                arrowBtn2.setImageDrawable(arrowDownDrawable)
                isDirectionsExpanded = true
            }

            expandableLayout_directions.initLayout()
        }

        radioBtnPerServing.isChecked = true

        radioBtnTotalServings.setOnClickListener{
            radioBtnPerServing.isChecked = false

            val numberOfServings = recipeJSON.getString(NUMBER_OF_SERVINGS_STRING).toFloat()
            setLabels(recipeJSON, position, numberOfServings)
        }

        radioBtnPerServing.setOnClickListener{
            radioBtnTotalServings.isChecked = false

            if (numberOfServingsCheck){

                setLabels(recipeJSON, position, 1f)
            }
        }

    }

    fun setLists(recipeJSON: JSONObject){
        // Ingredients Check and Set
        if (ingredientCheck){
            val ingredientsJSONArray = recipeJSON.getJSONObject("ingredients").getJSONArray("ingredient")

            var x = 0
            while (x < ingredientsJSONArray.length()){
                ingredientsArray.add(ingredientsJSONArray.get(x).toString())
                x++
            }
            Utils.setListViewHeightBasedOnChildren(ingredientList)
        }

        // Directions Check and Set
        if (directionCheck){
            val directionsJSONArray = recipeJSON.getJSONObject("directions").getJSONArray("direction")

            var x = 0
            while (x < directionsJSONArray.length()){
                directionsArray.add(directionsJSONArray.get(x).toString())
                x++
            }
            Utils.setListViewHeightBasedOnChildren(directionList)
        }
    }


    fun setLabels(recipeJSON : JSONObject, position : Int, servingAmount : Float){
        //textViewFoodNameLabel.text = recipeJSON.getString(FOOD_NAME_STRING)

        val containsDescription = recipeJSON.toString().contains(RECIPE_DESCRIPTION_STRING, ignoreCase = true)



        var servingCheckString = ""

        try{
            // First try to grab the array of servings if they are available
            servingCheckString = recipeJSON.getJSONObject(SERVING_SIZES_STRING)
                .getJSONObject(SINGLE_SERVING_STRING).toString()
        }catch (je: JSONException) {

        }

        val containsCalcium = servingCheckString.contains(CALCIUM_STRING, ignoreCase = true)
        val containsCalories = servingCheckString.contains(CALORIES_STRING, ignoreCase = true)
        val containsCarbohydrate = servingCheckString.contains(CARBOHYDRATE_STRING, ignoreCase = true)
        val containsCholesterol = servingCheckString.contains(CHOLESTEROL_STRING, ignoreCase = true)
        val containsFat = servingCheckString.contains(FAT_STRING, ignoreCase = true)
        val containsFiber = servingCheckString.contains(FIBER_STRING, ignoreCase = true)
        val containsIron = servingCheckString.contains(IRON_STRING, ignoreCase = true)
        val containsMonoUnsaturatedFat = servingCheckString.contains(MONOUNSATURATED_FAT_STRING, ignoreCase = true)
        val containsPolyUnsaturatedFat = servingCheckString.contains(POLYUNSATURATED_FAT_STRING, ignoreCase = true)
        val containsPotassium = servingCheckString.contains(POTASSIUM_STRING, ignoreCase = true)
        val containsProtein = servingCheckString.contains(PROTEIN_STRING, ignoreCase = true)
        val containsSaturatedFat = servingCheckString.contains(SATURATED_FAT_STRING, ignoreCase = true)
        val containsSodium = servingCheckString.contains(SODIUM_STRING, ignoreCase = true)
        val containsSugar = servingCheckString.contains(SUGAR_STRING, ignoreCase = true)
        val containsTransFat = servingCheckString.contains(TRANS_FAT, ignoreCase = true)
        val containsVitaminA = servingCheckString.contains(VITAMIN_A_STRING, ignoreCase = true)
        val containsVitaminC = servingCheckString.contains(VITAMIN_C_STRING, ignoreCase = true)


        val dFormat = DecimalFormat("#.##")
        dFormat.roundingMode = RoundingMode.CEILING



        val servingCheckJSON = JSONObject(servingCheckString)

        if (containsDescription){
            textViewServingDescription.text = recipeJSON.getString(RECIPE_DESCRIPTION_STRING)
        }

        if (containsCalcium){
            textViewCalciumAmount.text = dFormat.format((servingCheckJSON.getString(CALCIUM_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewCalciumAmount.text = NO_VALUE_STRING
            textViewCalciumSize.text = " "
        }
        if (containsCalories){
            textViewCaloriesAmount.text = dFormat.format((servingCheckJSON.getString(CALORIES_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewCaloriesAmount.text = NO_VALUE_STRING
        }
        if (containsCarbohydrate){
            textViewTotalCarbsAmount.text = dFormat.format((servingCheckJSON.getString(CARBOHYDRATE_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewTotalCarbsAmount.text = NO_VALUE_STRING
            textViewTotalCarbsSize.text = " "
        }
        if (containsCholesterol){
            textViewCholesterolAmount.text = dFormat.format((servingCheckJSON.getString(CHOLESTEROL_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewCholesterolAmount.text = NO_VALUE_STRING
            textViewCalciumSize.text = " "
        }
        if (containsFat){
            textViewTotalFatAmount.text = dFormat.format((servingCheckJSON.getString(FAT_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewTotalFatAmount.text = NO_VALUE_STRING
            textViewTotalFatSize.text = " "
        }
        if (containsFiber){
            textViewFiberAmount.text = dFormat.format((servingCheckJSON.getString(FIBER_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewFiberAmount.text = NO_VALUE_STRING
            textViewFiberSize.text = " "
        }
        if (containsIron){
            textViewIronAmount.text = dFormat.format((servingCheckJSON.getString(IRON_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewIronAmount.text = NO_VALUE_STRING
            textViewIronSize.text = " "
        }

        if (containsMonoUnsaturatedFat){
            textViewMonounsaturatedAmount.text = dFormat.format((servingCheckJSON.getString(MONOUNSATURATED_FAT_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewMonounsaturatedAmount.text = NO_VALUE_STRING
            textViewMonounsaturatedSize.text = " "
        }

        if (containsPolyUnsaturatedFat){
            textViewPolyunsaturatedAmount.text = dFormat.format((servingCheckJSON.getString(POLYUNSATURATED_FAT_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewPolyunsaturatedAmount.text = NO_VALUE_STRING
            textViewPolyunsaturatedSize.text = " "
        }
        if (containsPotassium){
            textViewPotassiumAmount.text = dFormat.format((servingCheckJSON.getString(POTASSIUM_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewPotassiumAmount.text = NO_VALUE_STRING
            textViewPotassiumSize.text = " "
        }
        if (containsProtein){
            textViewProteinAmount.text = dFormat.format((servingCheckJSON.getString(PROTEIN_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewProteinAmount.text = NO_VALUE_STRING
            textViewProteinSize.text = " "
        }
        if (containsSaturatedFat){
            textViewSaturatedFatAmount.text = dFormat.format((servingCheckJSON.getString(SATURATED_FAT_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewSaturatedFatAmount.text = NO_VALUE_STRING
            textViewSaturatedFatSize.text = " "
        }

        if (containsSodium){
            textViewSodiumAmount.text = dFormat.format((servingCheckJSON.getString(SODIUM_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewSodiumAmount.text = NO_VALUE_STRING
            textViewSodiumSize.text = " "
        }
        if (containsSugar){
            textViewTotalSugarsAmount.text = dFormat.format((servingCheckJSON.getString(SUGAR_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewTotalSugarsAmount.text = NO_VALUE_STRING
            textViewTotalSugarsSize.text = " "
        }
        if (containsTransFat){
            textViewTransFatAmount.text = dFormat.format((servingCheckJSON.getString(TRANS_FAT).toFloat()) * (servingAmount)).toString()
        }else{
            textViewTransFatAmount.text = NO_VALUE_STRING
            textViewTransFatSize.text = " "
        }
        if (containsVitaminA){
            textViewVitaminAAmount.text = dFormat.format((servingCheckJSON.getString(VITAMIN_A_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewVitaminAAmount.text = NO_VALUE_STRING
            textViewVitaminASize.text = " "
        }
        if (containsVitaminC){
            textViewVitaminCAmount.text = dFormat.format((servingCheckJSON.getString(VITAMIN_C_STRING).toFloat()) * (servingAmount)).toString()
        }else{
            textViewVitaminCAmount.text = NO_VALUE_STRING
            textViewVitaminCSize.text = " "
        }
    }


}