package com.mattmiss.kotlinfragments.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.fragment_search_food.*
import org.json.JSONObject

import com.fatsecret.platform.services.RequestBuilder
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.Volley

import com.mattmiss.kotlinfragments.R
import com.mattmiss.kotlinfragments.adapters.SearchResultsAdapter
import com.mattmiss.kotlinfragments.database.Category
import com.mattmiss.kotlinfragments.models.SavedItemViewModel
import kotlin.collections.ArrayList
import com.mattmiss.kotlinfragments.utils.Utils


class FragmentSearchFood : androidx.fragment.app.Fragment() {

    private val key = "34272aa80f904f7099c06d3ee92a37e6"
    private val secret = "f5f7cf2d050344178a6155bce32d24c8"
    private val query = "pasta"
    private val MAX_RESULTS = 20

    private lateinit var adapter: SearchResultsAdapter
    private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
    private lateinit var queue: RequestQueue
    private lateinit var builder: RequestBuilder
    private lateinit var savedItemViewModel: SavedItemViewModel
    private lateinit var categoryList: ArrayList<Category>

    var searchResultsArrayList = ArrayList<JSONObject>()
    var isFoodChecked = true



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        queue = Volley.newRequestQueue(context)

        builder = RequestBuilder(key, secret)
        linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity!!)
        warningRecycler.layoutManager = linearLayoutManager

        // Add the adapter which is using the JSONList returned from api call
        // and the Clicklistener which calls the foodItemClicked method
        adapter = SearchResultsAdapter(
            searchResultsArrayList,
            { foodSave: JSONObject, resultCode: Int -> foodItemClicked(foodSave, resultCode) })
        warningRecycler.adapter = adapter

        // Add a line in between each item
        warningRecycler.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                warningRecycler.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        // viewLifecycleOwner is used instead of (this) so it doesnt reattach an observer every time the fragment attaches again
        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)

        //savedItemViewModel.allSavedFood.observe(viewLifecycleOwner, Observer { savedItems ->
        //    savedItems.let { adapter.setSavedItems(it) }
        //})

        savedItemViewModel.allCategories.observe(viewLifecycleOwner, Observer { category ->
            category.let { updateCategoryList(it) }
        })

        searchButton.setOnClickListener{
            searchClick()
        }
        searchButton.setOnKeyListener{ v, actionId, event ->
            if(event.action == KeyEvent.KEYCODE_ENTER){
                searchClick()
                true
            } else {
                false
            }
        }


        // hides the keyboard when clicked away from the searchFood textbox (or fragment change)
        searchFood.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (!hasFocus) {
                    Utils.hideSoftKeyBoard(v.context, v)
                }
            }
        })

        toggleButtonFood.isChecked = true

        toggleButtonFood.setOnClickListener{
            toggleButtonRecipe.toggle()
            searchFood.hint = "Enter Food or Product name"
            isFoodChecked = true
        }
        toggleButtonRecipe.setOnClickListener{
            toggleButtonFood.toggle()
            searchFood.hint = "Enter Recipe name"
            isFoodChecked = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_food, container, false)
    }

    companion object {
        fun newInstance(): FragmentSearchFood {
            val fragment = FragmentSearchFood()
            return fragment
        }
    }


    fun searchClick(){
        //Toast.makeText(getActivity(),service.searchFood(4384).toString(),Toast.LENGTH_SHORT).show()

        val search = searchFood.text


        // if the search is not empty, call the searchFood method
        if (search.isNotEmpty()){
            searchResultsArrayList.clear()
            adapter.notifyDataSetChanged()

            if (isFoodChecked){
                // Use the search text
                //searchFood(search.toString())
                searchFoodOrRecipe(search.toString(), 0) // Method 0 is for Food Search
            }else{
                searchFoodOrRecipe(search.toString(), 1) // Method 1 is for Recipe Search
            }
        }

        // Minimizes the keyboard after hitting Search
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

    }


    private fun searchFoodOrRecipe(query: String, method : Int){
        var url = ""

        when (method){
            0 -> url = builder.buildFoodsSearchUrl(query, 0)
            1 -> url = builder.buildRecipesSearchUrl(query, 0)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, "",
            Response.Listener<JSONObject> { response ->
                when (method){
                    0 -> getFoodFromID(response.toString())
                    1 -> getRecipeFromID(response.toString())
                }
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                print(error)
            }
        )
        queue.add(jsonObjectRequest)
    }

    private fun getFoodFromID(response: String){

        // Get the entire JSON object in one long line
        val responseJSON = JSONObject(response)

        //Utils.longInfo(responseJSON.toString())

        // get the "foods" object ("foods" is the only object anyways
        val foodsJSON = responseJSON.getJSONObject("foods")

        // Get the array inside of foods. usually around 20-50 long
        val foodArray = foodsJSON.getJSONArray("food")

        val arrayLength = foodArray.length()
        var x = 1

        // Use arrayLength for full amount of fooditems
        // Or use a custom number
        while (x < MAX_RESULTS){
            val food = foodArray.getJSONObject(x)
            val foodID = food.getLong("food_id")

            val url = builder.buildFoodGetUrl(foodID)

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, "",
                Response.Listener<JSONObject> { response ->
                    searchResultsArrayList.add(response.getJSONObject("food"))
                    //adapter.notifyItemInserted(x-1)
                    if (x == MAX_RESULTS){
                        adapter.notifyDataSetChanged()
                    }

                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                    print(error)
                }
            )

            queue.add(jsonObjectRequest)
            x++
        }
    }


    private fun getRecipeFromID(response: String){

        // Get the entire JSON object in one long line
        val responseJSON = JSONObject(response)

        // Prints out the super long recipe response
        Utils.longInfo(responseJSON.toString())

        // get the "foods" object ("foods" is the only object anyways
        val recipeJSON = responseJSON.getJSONObject("recipes")

        // Get the array inside of foods. usually around 20-50 long
        val recipeArray = recipeJSON.getJSONArray("recipe")

        val arrayLength = recipeArray.length()
        var x = 1

        // Use arrayLength for full amount of fooditems
        // Or use a custom number
        while (x < MAX_RESULTS){
            val recipe = recipeArray.getJSONObject(x)
            val recipeID = recipe.getLong("recipe_id")

            val url = builder.buildRecipeGetUrl(recipeID)

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, "",
                Response.Listener<JSONObject> { response ->
                    searchResultsArrayList.add(response.getJSONObject("recipe"))
                    //adapter.notifyItemInserted(x-1)
                    if (x == MAX_RESULTS){
                        adapter.notifyDataSetChanged()
                    }
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                    print(error)
                }
            )

            queue.add(jsonObjectRequest)
            x++
        }
    }



    private fun foodItemClicked(foodOrRecipeSave : JSONObject, resultCode : Int) {
        val ft = activity!!.supportFragmentManager.beginTransaction()


        // If this works, take out the if/else
        if (resultCode == 1){
            val newFragment = SearchResultViewPagerParent.newInstance(
                foodOrRecipeSave,
                resultCode
            )
            newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("search_result_fragment_dialog")
            newFragment.show(ft, "search_result_fragment_dialog")
        }else if(resultCode == 2){
            val newFragment = SearchResultViewPagerParent.newInstance(
                foodOrRecipeSave,
                resultCode
            )
            newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

            ft.addToBackStack("search_result_fragment_dialog")
            newFragment.show(ft, "search_result_fragment_dialog")
        }else{
            Toast.makeText(activity!!, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }


    }


    fun updateCategoryList(categories : List<Category>){
        for (category in categories){
            categoryList.add(category)
        }
    }



}