package com.mattmiss.kotlinfragments.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mattmiss.kotlinfragments.R
import kotlinx.android.synthetic.main.choose_category_dialog_fragment.*

class ChooseCategoryFragment : androidx.fragment.app.DialogFragment(), View.OnClickListener{


    interface CategoryChoiceListener {
        fun onCategoryChoice(category: String)
    }

    private var listener : CategoryChoiceListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.choose_category_dialog_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    fun setListener(listener: CategoryChoiceListener) {
        this.listener = listener
    }

    fun categoryChosen(category: String){
        listener?.onCategoryChoice(category)
    }

    override fun onClick(view: View?) {
        when (view?.id){
            alcoholLayout.id -> categoryChosen(alcoholName.text.toString())
            drinksLayout.id -> categoryChosen(drinksName.text.toString())
            beefLayout.id -> categoryChosen(beefName.text.toString())
            categoryFishSeafoodLayout.id -> categoryChosen(fishSeafoodName.text.toString())
            categoryPoultryLayout.id -> categoryChosen(poultryName.text.toString())
            fruitLayout.id -> categoryChosen(fruitName.text.toString())
            vegetablesLayout.id -> categoryChosen(vegetablesName.text.toString())
            breakfastLayout.id -> categoryChosen(breakfastName.text.toString())
            categoryDinnerLayout.id -> categoryChosen(dinnerName.text.toString())
            categoryFastFoodLayout.id -> categoryChosen(fastFoodName.text.toString())
            categoryRestaurantLayout.id -> categoryChosen(restaurantName.text.toString())
            saladsLayout.id -> categoryChosen(saladsName.text.toString())
            categorySaucesSpreadsLayout.id -> categoryChosen(saucesSpreadsName.text.toString())
            categorySoupsLayout.id -> categoryChosen(soupsName.text.toString())
            snacksLayout.id -> categoryChosen(snacksName.text.toString())
            candySweetsLayout.id -> categoryChosen(candySweetsName.text.toString())
            dairyLayout.id -> categoryChosen(dairyName.text.toString())
            categoryNutsSeedsLayout.id -> categoryChosen(nutsSeedsName.text.toString())
            categoryPastaRiceNoodlesLayout.id -> categoryChosen(pastaRiceNoodlesName.text.toString())
        }
        dismiss()
    }

    fun setOnClickListeners(){
        alcoholLayout.setOnClickListener(this)
        drinksLayout.setOnClickListener(this)
        beefLayout.setOnClickListener(this)
        categoryFishSeafoodLayout.setOnClickListener(this)
        categoryPoultryLayout.setOnClickListener(this)
        fruitLayout.setOnClickListener(this)
        vegetablesLayout.setOnClickListener(this)
        breakfastLayout.setOnClickListener(this)
        categoryDinnerLayout.setOnClickListener(this)
        categoryFastFoodLayout.setOnClickListener(this)
        categoryRestaurantLayout.setOnClickListener(this)
        saladsLayout.setOnClickListener(this)
        categorySaucesSpreadsLayout.setOnClickListener(this)
        categorySoupsLayout.setOnClickListener(this)
        snacksLayout.setOnClickListener(this)
        candySweetsLayout.setOnClickListener(this)
        dairyLayout.setOnClickListener(this)
        categoryNutsSeedsLayout.setOnClickListener(this)
        categoryPastaRiceNoodlesLayout.setOnClickListener(this)
    }


}