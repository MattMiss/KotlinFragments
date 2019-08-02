package com.mattmiss.kotlinfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.food_saved_dialog.*
import org.json.JSONException
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat


class SavedResultFragment : androidx.fragment.app.Fragment() {

    val FOOD_NAME_STRING = "food_name"
    val SERVINGS_STRING = "servings"
    val SINGLE_SERVING_STRING = "serving"
    val CALCIUM_STRING = "calcium"
    val CALORIES_STRING= "calories"
    val CARBOHYDRATE_STRING = "carbohydrate"
    val CHOLESTEROL_STRING = "cholesterol"
    val FAT_STRING = "fat"
    val FIBER_STRING = "fiber"
    val IRON_STRING = "iron"
    val MEASUREMENT_DESCRIPTION_STRING = "measurement_description"
    val METRIC_SERVING_AMOUNT_STRING = "metric_serving_amount"
    val METRIC_SERVING_UNIT_STRING = "metric_serving_unit"
    val MONOUNSATURATED_FAT_STRING = "monounsaturated_fat"
    val NUMBER_OF_UNITS_STRING = "number_of_units"
    val POLYUNSATURATED_FAT_STRING = "polyunsaturated_fat"
    val POTASSIUM_STRING = "potassium"
    val PROTEIN_STRING = "protein"
    val SATURATED_FAT_STRING = "saturated_fat"
    val SERVING_DESCRIPTION_STRING = "serving_description"
    val SERVING_ID_STRING = "serving_id"
    val SODIUM_STRING = "sodium"
    val TRANS_FAT = "trans_fat"
    val VITAMIN_A_STRING = "vitamin_a"
    val VITAMIN_C_STRING = "vitamin_c"

    val NO_VALUE_STRING = "-"

    private lateinit var savedItemViewModel: SavedItemViewModel


    companion object {
        fun newInstance(foodJSONString: String, position: Int): SavedResultFragment {
            println("Creating Instance of SearchResultFragment")
            val sf = SavedResultFragment()

            val bundle = Bundle(1)

            bundle.putString("foodJSONString", foodJSONString)
            bundle.putInt("position", position)

            sf.setArguments(bundle)

            return sf

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.food_saved_dialog, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodJSON = JSONObject(arguments?.getString("foodJSONString"))
        val position = arguments?.getInt("position")

        val savedItem = SavedItem(foodJSON.toString())

        setLabels(foodJSON, position!!.toInt())

        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)

    }




    fun setLabels(foodJSON : JSONObject, position : Int){
        //textViewFoodNameLabel.text = foodJSON.getString(FOOD_NAME_STRING)

        var servingCheckString = ""

        try{
            // First try to grab the array of servings if they are available
            servingCheckString = foodJSON.getJSONObject(SERVINGS_STRING)
                .getJSONArray(SINGLE_SERVING_STRING).get(position).toString()
        }catch (je: JSONException) {
            // If the array fails, then grab the serving from the JSONObject
            servingCheckString = foodJSON.getJSONObject(SERVINGS_STRING)
                .getJSONObject(SINGLE_SERVING_STRING).toString()
        }

        val foodStringCheck = foodJSON.getJSONObject(SERVINGS_STRING).toString()

        val containsCalcium = servingCheckString.contains(CALCIUM_STRING, ignoreCase = true)
        val containsCalories = servingCheckString.contains(CALORIES_STRING, ignoreCase = true)
        val containsCarbohydrate = servingCheckString.contains(CARBOHYDRATE_STRING, ignoreCase = true)
        val containsCholesterol = servingCheckString.contains(CHOLESTEROL_STRING, ignoreCase = true)
        val containsFat = servingCheckString.contains(FAT_STRING, ignoreCase = true)
        val containsFiber = servingCheckString.contains(FIBER_STRING, ignoreCase = true)
        val containsIron = servingCheckString.contains(IRON_STRING, ignoreCase = true)
        val containsMeasurementDescription = servingCheckString.contains(MEASUREMENT_DESCRIPTION_STRING, ignoreCase = true)
        val containsMetricServingAmount = servingCheckString.contains(METRIC_SERVING_AMOUNT_STRING, ignoreCase = true)
        val containsMetricServingUnit = servingCheckString.contains(METRIC_SERVING_UNIT_STRING, ignoreCase = true)
        val containsMonoUnsaturatedFat = servingCheckString.contains(MONOUNSATURATED_FAT_STRING, ignoreCase = true)
        val containsNumberOfUnits = servingCheckString.contains(NUMBER_OF_UNITS_STRING, ignoreCase = true)
        val containsPolyUnsaturatedFat = servingCheckString.contains(POLYUNSATURATED_FAT_STRING, ignoreCase = true)
        val containsPotassium = servingCheckString.contains(POTASSIUM_STRING, ignoreCase = true)
        val containsProtein = servingCheckString.contains(PROTEIN_STRING, ignoreCase = true)
        val containsSaturatedFat = servingCheckString.contains(SATURATED_FAT_STRING, ignoreCase = true)
        val containsServingDescription = servingCheckString.contains(SERVING_DESCRIPTION_STRING, ignoreCase = true)
        val containsServingID = servingCheckString.contains(SERVING_ID_STRING, ignoreCase = true)
        val containsSodium = servingCheckString.contains(SODIUM_STRING, ignoreCase = true)
        val containsTransFat = servingCheckString.contains(TRANS_FAT, ignoreCase = true)
        val containsVitaminA = servingCheckString.contains(VITAMIN_A_STRING, ignoreCase = true)
        val containsVitaminC = servingCheckString.contains(VITAMIN_C_STRING, ignoreCase = true)


        val dFormat = DecimalFormat("#.##")
        dFormat.roundingMode = RoundingMode.CEILING


        println(foodJSON.toString())


        val servingCheckJSON = JSONObject(servingCheckString)

        if (containsCalcium){
            textViewCalciumAmount.text = servingCheckJSON.getString(CALCIUM_STRING)
        }else{
            textViewCalciumAmount.text = NO_VALUE_STRING
            textViewCalciumSize.text = " "
        }
        if (containsCalories){
            textViewCaloriesAmount.text = servingCheckJSON.getString(CALORIES_STRING)
        }else{
            textViewCaloriesAmount.text = NO_VALUE_STRING
        }
        if (containsCarbohydrate){
            textViewTotalCarbsAmount.text = servingCheckJSON.getString(CARBOHYDRATE_STRING)
        }else{
            textViewTotalCarbsAmount.text = NO_VALUE_STRING
            textViewTotalCarbsSize.text = " "
        }
        if (containsCholesterol){
            textViewCholesterolAmount.text = servingCheckJSON.getString(CHOLESTEROL_STRING)
        }else{
            textViewCholesterolAmount.text = NO_VALUE_STRING
            textViewCalciumSize.text = " "
        }
        if (containsFat){
            textViewTotalFatAmount.text = servingCheckJSON.getString(FAT_STRING)
        }else{
            textViewTotalFatAmount.text = NO_VALUE_STRING
            textViewTotalFatSize.text = " "
        }
        if (containsFiber){
            textViewFiberAmount.text = servingCheckJSON.getString(FIBER_STRING)
        }else{
            textViewFiberAmount.text = NO_VALUE_STRING
            textViewFiberSize.text = " "
        }
        if (containsIron){
            textViewIronAmount.text = servingCheckJSON.getString(IRON_STRING)
        }else{
            textViewIronAmount.text = NO_VALUE_STRING
            textViewIronSize.text = " "
        }
        if (containsServingDescription && containsMetricServingAmount && containsMetricServingUnit){
            val servingDescriptionText = "${servingCheckJSON.getString(SERVING_DESCRIPTION_STRING)} " +
                    "(${servingCheckJSON.getString(METRIC_SERVING_AMOUNT_STRING)} " +
                    "${servingCheckJSON.getString(METRIC_SERVING_UNIT_STRING)})"
            textViewServingDescription.text = servingDescriptionText
            //textViewCalciumAmount.text = servingCheckJSON.getString(CALCIUM_STRING)
        }else{
            //textViewCalciumAmount.text = NO_VALUE_STRING
        }

        if (containsMeasurementDescription){
            //textViewCalciumAmount.text = servingCheckJSON.getString(CALCIUM_STRING)
        }else{
            //textViewCalciumAmount.text = NO_VALUE_STRING
        }

        if (containsMonoUnsaturatedFat){
            textViewMonounsaturatedAmount.text = servingCheckJSON.getString(MONOUNSATURATED_FAT_STRING)
        }else{
            textViewMonounsaturatedAmount.text = NO_VALUE_STRING
            textViewMonounsaturatedSize.text = " "
        }
        if (containsNumberOfUnits){
            //textViewCalciumAmount.text = servingCheckJSON.getString(CALCIUM_STRING)
        }else{
            //textViewCalciumAmount.text = NO_VALUE_STRING
        }
        if (containsPolyUnsaturatedFat){
            textViewPolyunsaturatedAmount.text = servingCheckJSON.getString(POLYUNSATURATED_FAT_STRING)
        }else{
            textViewPolyunsaturatedAmount.text = NO_VALUE_STRING
            textViewPolyunsaturatedSize.text = " "
        }
        if (containsPotassium){
            textViewPotassiumAmount.text = servingCheckJSON.getString(POTASSIUM_STRING)
        }else{
            textViewPotassiumAmount.text = NO_VALUE_STRING
            textViewPotassiumSize.text = " "
        }
        if (containsProtein){
            textViewProteinAmount.text = servingCheckJSON.getString(PROTEIN_STRING)
        }else{
            textViewProteinAmount.text = NO_VALUE_STRING
            textViewProteinSize.text = " "
        }
        if (containsSaturatedFat){
            textViewSaturatedFatAmount.text = servingCheckJSON.getString(SATURATED_FAT_STRING)
        }else{
            textViewSaturatedFatAmount.text = NO_VALUE_STRING
            textViewSaturatedFatSize.text = " "
        }

        if (containsServingID){
            //textViewCalciumAmount.text = servingCheckJSON.getString(CALCIUM_STRING)
        }else{
            //textViewCalciumAmount.text = NO_VALUE_STRING
        }
        if (containsSodium){
            textViewSodiumAmount.text = servingCheckJSON.getString(SODIUM_STRING)
        }else{
            textViewSodiumAmount.text = NO_VALUE_STRING
            textViewSodiumSize.text = " "
        }
        if (containsTransFat){
            textViewTransFatAmount.text = servingCheckJSON.getString(TRANS_FAT)
        }else{
            textViewTransFatAmount.text = NO_VALUE_STRING
            textViewTransFatSize.text = " "
        }
        if (containsVitaminA){
            textViewVitaminAAmount.text = servingCheckJSON.getString(VITAMIN_A_STRING)
        }else{
            textViewVitaminAAmount.text = NO_VALUE_STRING
            textViewVitaminASize.text = " "
        }
        if (containsVitaminC){
            textViewVitaminCAmount.text = servingCheckJSON.getString(VITAMIN_C_STRING)
        }else{
            textViewVitaminCAmount.text = NO_VALUE_STRING
            textViewVitaminCSize.text = " "
        }
    }


}