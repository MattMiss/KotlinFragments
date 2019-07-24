package com.mattmiss.kotlinfragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.fragment_a.*
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.os.AsyncTask

import com.android.volley.toolbox.StringRequest
import com.fatsecret.platform.services.RequestBuilder
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.android.volley.RequestQueue
import kotlin.collections.ArrayList


class FragmentSearchFood : androidx.fragment.app.Fragment() {

    private val key = "34272aa80f904f7099c06d3ee92a37e6"
    private val secret = "f5f7cf2d050344178a6155bce32d24c8"
    private val query = "pasta"
    private val MAX_RESULTS = 20

    private lateinit var adapter: SearchResultsAdapter
    private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
    private lateinit var queue: RequestQueue
    private lateinit var builder: RequestBuilder


    var foodArrayList = ArrayList<JSONObject>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        queue = Volley.newRequestQueue(context)

        builder = RequestBuilder(key, secret)
        linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager

        // Add the adapter which is using the JSONList returned from api call
        // and the Clicklistener which calls the foodItemClicked method
        adapter = SearchResultsAdapter(foodArrayList, { foodSave: JSONObject -> foodItemClicked(foodSave)})
        recyclerView.adapter = adapter

        // Add a line in between each item
        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerView.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )


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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    companion object {
        fun newInstance(): FragmentSearchFood {
            val fragment = FragmentSearchFood()
            return fragment
        }
    }


    fun searchClick(){
        //Toast.makeText(getActivity(),service.getFood(4384).toString(),Toast.LENGTH_SHORT).show()

        val search = searchFood.text

        // for testing
        //getFood("Beef")
        // if the search is not empty, call the getFood method
        if (search.isNotEmpty()){
            foodArrayList.clear()
            // Use the search text
            getFood(search.toString())
        }

        // Minimizes the keyboard after hitting Search
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

    }

    private fun getFood(query: String) {
        object : AsyncTask<String, String, String>() {
            override fun doInBackground(vararg arg0: String): String {

                val url = builder.buildFoodsSearchUrl(query, 1)

                // Request a string response from the provided URL.
                val stringRequest = StringRequest(
                    com.android.volley.Request.Method.GET, url,
                    com.android.volley.Response.Listener<String> { response ->

                        // Get the entire JSON object in one long line
                        val responseJSON = JSONObject(response.toString())

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
                            val foodID = food.get("food_id")

                            getFoodFromID(foodID.toString())
                            x++
                        }
                    },
                    com.android.volley.Response.ErrorListener { error ->
                        // Handle error
                        Log.e("Error", "ERROR: %s".format(error.toString()))
                    })
                queue.add(stringRequest)

                return ""
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                if (result == "Error")
                    Toast.makeText(activity, "No Items Containing Your Search", Toast.LENGTH_SHORT).show()

            }
        }.execute()
    }




    private fun getFoodFromID(foodID: String) {

        object : AsyncTask<String, String, String>() {

            override fun doInBackground(vararg arg0: String): String {

                val url = builder.buildFoodGetUrl(foodID.toLong())


                println(builder.buildFoodGetUrl(foodID.toLong()).toString())

                Log.i("Getting Response", "Now")
                // Request a string response from the provided URL.
                val stringRequest = StringRequest(
                    com.android.volley.Request.Method.GET, url,
                    com.android.volley.Response.Listener<String> { response ->

                        // Get the entire JSON object in one long line
                        val responseJSON = JSONObject(response.toString())

                        // get the "foods" object ("foods" is the only object anyways
                        foodArrayList.add(responseJSON.getJSONObject("food"))
                        adapter.notifyDataSetChanged()

                    },
                    com.android.volley.Response.ErrorListener { error ->
                        // Handle error
                        Log.e("Error", "ERROR: %s".format(error.toString()))
                    })
                queue.add(stringRequest)

                return ""
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                if (result == "Error")
                    Toast.makeText(activity, "No Items Containing Your Search", Toast.LENGTH_SHORT).show()


            }

        }.execute()

    }


    private fun foodItemClicked(foodSave : JSONObject) {
        val ft = activity!!.supportFragmentManager.beginTransaction()

        val newFragment = SearchResultViewPagerParent.newInstance(1, foodSave)
        ft.addToBackStack("fragment_dialog")
        newFragment.show(ft, "fragment_dialog")


    }




}