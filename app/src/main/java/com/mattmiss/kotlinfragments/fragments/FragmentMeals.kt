package com.mattmiss.kotlinfragments.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mattmiss.kotlinfragments.*
import com.mattmiss.kotlinfragments.adapters.SavedItemListAdapter
import com.mattmiss.kotlinfragments.adapters.SavedMealListAdapter
import com.mattmiss.kotlinfragments.adapters.SimpleAdapter
import com.mattmiss.kotlinfragments.database.Meal
import com.mattmiss.kotlinfragments.models.SavedItemViewModel
import com.mattmiss.kotlinfragments.utils.SwipeToDeleteCallback
import com.mattmiss.kotlinfragments.utils.Utils
import kotlinx.android.synthetic.main.fragment_meals.*
import org.json.JSONObject
import java.util.*


class FragmentMeals : androidx.fragment.app.Fragment(), AddMealDialogFragment.AddMealToDayListener {

    private lateinit var savedItemViewModel: SavedItemViewModel
    private lateinit var adapter : SavedMealListAdapter

    private var savedMeals = arrayListOf<JSONObject>()
    private var savedDates = arrayListOf<String>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meals, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SavedMealListAdapter(
            context!!,
            { savedMeal: JSONObject -> mealClicked(savedMeal) })


        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(recyclerview.context)



        val swipeHandler = object : SwipeToDeleteCallback(recyclerview.context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                // delete the row right here

                //adapter.re(viewHolder.adapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerview)

        // Add a line in between each item
        recyclerview.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerview.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )


        // viewLifecycleOwner is used instead of (this) so it doesnt reattach an observer every time the fragment attaches again
        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)

        savedItemViewModel.allMeals.observe(viewLifecycleOwner, Observer { meals ->
            meals.let { updateMealList(it, adapter) }
        })

        btnAddMeal.setOnClickListener{

            val ft = activity!!.supportFragmentManager.beginTransaction()

            val newFragment = AddMealDialogFragment.newInstance()
            newFragment.setListener(this)
            newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("fragment_add_meal_dialog")
            newFragment.show(ft, "fragment_add_meal_dialog")

        }

    }

    companion object {
        fun newInstance(): FragmentMeals {
            val fragment = FragmentMeals()
            return fragment
        }
    }


    private fun mealClicked(savedItem : JSONObject) {
        val ft = activity!!.supportFragmentManager.beginTransaction()

        val newFragment = EditMealDialogFragment.newInstance(savedItem)
        newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

        ft.addToBackStack("edit_meal_fragment_dialog")
        newFragment.show(ft, "edit_meal_fragment_dialog")

    }

    fun updateMealList(meals: List<Meal>, adapter : SavedMealListAdapter){

        for (item in meals){
            val tempJSON = JSONObject(item.meal)
            savedMeals.add(tempJSON)
            savedDates.add(tempJSON.get("date").toString())
        }

        adapter.setSavedItems(savedMeals)
    }

    override fun onDone(dayMeals: JSONObject) {
        val mealToAdd = Meal(dayMeals.toString())
        savedItemViewModel.insertMeal(mealToAdd)
    }


    fun askToDeleteMeals(meals: JSONObject){
        val builder = AlertDialog.Builder(activity)

        // Display a message on alert dialog
        builder.setMessage("Delete all meals for ${meals.getString("date")}?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Delete"){dialog, which ->
            val allMeals = Meal(meals.toString())
            savedItemViewModel.deleteMeal(allMeals)
            savedMeals.remove(meals)

            adapter.notifyDataSetChanged()
        }

        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->

        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}