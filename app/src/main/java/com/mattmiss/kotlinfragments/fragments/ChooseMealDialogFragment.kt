package com.mattmiss.kotlinfragments.fragments

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattmiss.kotlinfragments.*
import com.mattmiss.kotlinfragments.adapters.SavedItemListAdapter
import com.mattmiss.kotlinfragments.database.SavedItem
import com.mattmiss.kotlinfragments.database.SavedRecipe
import com.mattmiss.kotlinfragments.models.SavedItemViewModel
import kotlinx.android.synthetic.main.choose_meal_fragment.*
import kotlinx.android.synthetic.main.choose_meal_fragment.recyclerview
import org.json.JSONObject


class ChooseMealDialogFragment : androidx.fragment.app.DialogFragment() {

    interface MealChoiceDialogListener {
        fun onChoice(meal: JSONObject)
    }

    private lateinit var savedItemViewModel: SavedItemViewModel

    private var listener: MealChoiceDialogListener? = null

    private var savedFoodAndRecipes = arrayListOf<JSONObject>()


    fun setListener(listener: MealChoiceDialogListener) {
        this.listener = listener
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.choose_meal_fragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Database stuff
        //val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerview)

        val adapter = SavedItemListAdapter(
            context!!,
            { savedItem: JSONObject -> foodItemChosen(savedItem) })

        recyclerview.adapter = adapter

        recyclerview.layoutManager = LinearLayoutManager(recyclerview.context)

        // Add a line in between each item
        recyclerview.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerview.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        // viewLifecycleOwner is used instead of (this) so it doesnt reattach an observer every time the fragment attaches again
        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)

        savedItemViewModel.allSavedFood.observe(viewLifecycleOwner, Observer { savedItems ->
            savedItems.let { updateAdapterFood(it, adapter) }
        })

        savedItemViewModel.allSavedRecipes.observe(viewLifecycleOwner, Observer { savedItems ->
            savedItems.let { updateAdapterRecipe(it, adapter) }
        })

        btnChoose.setOnClickListener{
            dismiss()
        }



    }


    companion object {
        fun newInstance(): ChooseMealDialogFragment {
            val fragment = ChooseMealDialogFragment()
            return fragment
        }
    }


    // Use this method in the recyclerview click if I want to open a new fragment with the food item info
    private fun foodSaveClicked(savedItem : JSONObject) {
        val ft = activity!!.supportFragmentManager.beginTransaction()

        if (savedItem.toString().contains("recipe")){
            val newFragment = SavedRecipeDialogFragment.newInstance(savedItem)
            newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("recipe_fragment_dialog")
            newFragment.show(ft, "recipe_fragment_dialog")
        }else{
            val newFragment = SavedFoodViewPagerParent.newInstance(savedItem)
            newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("fragment_dialog")
            newFragment.show(ft, "fragment_dialog")
        }



    }

    fun updateAdapterFood(savedItems: List<SavedItem>, adapter : SavedItemListAdapter){

        for (item in savedItems){
            savedFoodAndRecipes.add(JSONObject(item.savedItem))
        }

        adapter.setSavedItems(savedFoodAndRecipes)
    }

    fun updateAdapterRecipe(savedRecipes: List<SavedRecipe>, adapter : SavedItemListAdapter){

        for (item in savedRecipes){
            savedFoodAndRecipes.add(JSONObject(item.savedRecipe))
        }

        adapter.setSavedItems(savedFoodAndRecipes)
    }

    // Use this if I just want to choose the food item on click
    fun foodItemChosen(savedItem: JSONObject){
        println(savedItem.toString())
        listener?.onChoice(savedItem)
        dismiss()
    }



}
