package com.mattmiss.kotlinfragments.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.pager_tabs2.*
import org.json.JSONObject
import java.lang.ClassCastException
import androidx.core.content.res.ResourcesCompat
import com.mattmiss.kotlinfragments.*
import com.mattmiss.kotlinfragments.adapters.CustomListAdapter
import com.mattmiss.kotlinfragments.adapters.FoodPagerAdapter
import com.mattmiss.kotlinfragments.database.SavedItem
import com.mattmiss.kotlinfragments.models.SavedItemViewModel
import com.mattmiss.kotlinfragments.utils.Utils
import org.json.JSONArray


class SavedFoodViewPagerParent : androidx.fragment.app.DialogFragment(){

    private lateinit var savedItemViewModel: SavedItemViewModel

    private var categoryCheck = false
    private var ratingCheck = false
    private var noteCheck = false
    private var tagCheck = false
    private var directionCheck = false

    private val notesArray = arrayListOf<String>()
    private val tagsArray = arrayListOf<String>()
    private val directionsArray = arrayListOf<String>()
    private val utils = Utils

    private var isNotesExpanded = false
    private var isTagsExpanded = false
    private var isDirectionsExpanded = false

    private var arrowDownDrawable : Drawable? = null
    private var arrowUpDrawable : Drawable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.pager_tabs2, container)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //Its actually using FoodPagerAdapter here but the name didn't change for some reason
        val adapter = FoodPagerAdapter(childFragmentManager)
        val foodJSON = JSONObject(arguments?.getString("foodJSON"))

        categoryCheck = foodJSON.toString().contains("food_category", ignoreCase = true)
        ratingCheck = foodJSON.toString().contains("rating", ignoreCase = true)
        noteCheck = foodJSON.toString().contains("notes", ignoreCase = true)
        tagCheck = foodJSON.toString().contains("tags", ignoreCase = true)
        directionCheck = foodJSON.toString().contains("directions", ignoreCase = true)


        arrowDownDrawable = ResourcesCompat.getDrawable(activity!!.resources,
            R.drawable.ic_arrow_drop_down_black_24dp, null)

        arrowUpDrawable = ResourcesCompat.getDrawable(activity!!.resources,
            R.drawable.ic_arrow_drop_up_black_24dp, null)



        setLabels(foodJSON)


        adapter.setJSONObject(foodJSON)

        pager.adapter = adapter

        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)

        btnCancelFood.setOnClickListener{

            val builder = AlertDialog.Builder(activity)

            // Display a message on alert dialog
            builder.setMessage("Delete?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("Delete"){dialog, which ->

                // Do something when user press the positive button
                val tempSavedItem = SavedItem(arguments?.getString("foodJSON")!!)
                savedItemViewModel.deleteFood(tempSavedItem)
                dismiss()
            }

            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()

        }

        btnSaveFood.setOnClickListener{
            // ADD function to delete original SavedItem and add the new one
            saveEditedFoodItem(foodJSON)
        }


        btnRatingCancel.setOnClickListener{
            if (ratingsLine.visibility == View.VISIBLE){
                ratingsLine.visibility = View.INVISIBLE
            }else{
                ratingBar.rating = 0f
                ratingsLine.visibility = View.VISIBLE
            }
        }

        ratingBar.setOnTouchListener { v, event ->
            ratingsLine.visibility = View.INVISIBLE
            // Must be false or else it overrides the ratingBar touch
            false
        }

        // Set adapter and buttons for Notes
        var noteAdapter = CustomListAdapter(
            noteList.context,
            R.layout.note_list_view,
            notesArray,
            "note"
        )
        noteList.adapter = noteAdapter

        // This sets the height of the noteList, otherwise it is only 1 note high
        Utils.setListViewHeightBasedOnChildren(noteList)
        expandableLayout_notes.initLayout()

        noteList.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            val o = noteList.getItemAtPosition(position)

            // Initialize a new instance of
            val builder = AlertDialog.Builder(activity)

            // Set the alert dialog title
            //builder.setTitle("Shift")

            // Display a message on alert dialog
            builder.setMessage("Delete note?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("Delete"){dialog, which ->

                // Do something when user press the positive button
                notesArray.removeAt(position)
                noteAdapter.notifyDataSetChanged()
                Utils.setListViewHeightBasedOnChildren(noteList)
                expandableLayout_notes.initLayout()
            }

            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        })


        notesBtnLayout.setOnClickListener{

            //// Sets the length of the List depending on how many items there are
            Utils.setListViewHeightBasedOnChildren(noteList)

            expandableLayout_notes.toggle()

            if (isNotesExpanded){
                arrowBtn1.setImageDrawable(arrowUpDrawable)
                isNotesExpanded = false
            }else{
                arrowBtn1.setImageDrawable(arrowDownDrawable)
                isNotesExpanded = true
            }

            expandableLayout_notes.initLayout()
        }

        btnAddNote.setOnClickListener{
            if (add_note_entry.text.isNotEmpty()){
                notesArray.add(add_note_entry.text.toString())

                add_note_entry.text.clear()
            }
            //// Sets the length of the List depending on how many items there are
            Utils.setListViewHeightBasedOnChildren(noteList)
            expandableLayout_notes.initLayout()
        }



        // Set adapter and buttons for Tags
        var tagAdapter = CustomListAdapter(
            tagList.context,
            R.layout.tag_list_view,
            tagsArray,
            "tag"
        )
        tagList.adapter = tagAdapter


        // This sets the height of the tagList, otherwise it is only 1 note high
        Utils.setListViewHeightBasedOnChildren(tagList)
        expandableLayout_tags.initLayout()

        tagList.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            val o = tagList.getItemAtPosition(position)

            // Initialize a new instance of
            val builder = AlertDialog.Builder(activity)

            // Set the alert dialog title
            //builder.setTitle("Shift")

            // Display a message on alert dialog
            builder.setMessage("Delete tag?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("Delete"){dialog, which ->

                // Do something when user press the positive button
                tagsArray.removeAt(position)
                tagAdapter.notifyDataSetChanged()
                Utils.setListViewHeightBasedOnChildren(tagList)
                expandableLayout_tags.initLayout()
            }

            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        })

        tagsBtnLayout.setOnClickListener{
            expandableLayout_tags.toggle()
            expandableLayout_tags.initLayout()


            if (isTagsExpanded){
                arrowBtn2.setImageDrawable(arrowUpDrawable)
                isTagsExpanded = false
            }else{
                arrowBtn2.setImageDrawable(arrowDownDrawable)
                isTagsExpanded = true
            }

        }

        btnAddTag.setOnClickListener{

            val entry = add_tag_entry.text

            if (entry.isNotEmpty()){
                val trimmed = entry.trim()
                val entryArray = (trimmed).split(" ")

                if (entryArray.size == 1){
                    tagsArray.add(trimmed.toString())

                    add_tag_entry.text.clear()
                }else {
                    println("NOPE")
                }
            }

            // Sets the length of the List depending on how many items there are
            Utils.setListViewHeightBasedOnChildren(tagList)
            expandableLayout_tags.initLayout()
        }


        // Set adapter and buttons for Notes
        var directionAdapter = CustomListAdapter(
            directionList.context,
            R.layout.directions_list_view, directionsArray, "direction"
        )
        directionList.adapter = directionAdapter


        // This sets the height of the tagList, otherwise it is only 1 note high
        Utils.setListViewHeightBasedOnChildren(directionList)
        expandableLayout_directions.initLayout()

        directionList.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            val o = directionList.getItemAtPosition(position)

            // Initialize a new instance of
            val builder = AlertDialog.Builder(activity)

            // Set the alert dialog title
            //builder.setTitle("Shift")

            // Display a message on alert dialog
            builder.setMessage("Delete direction?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("Delete"){dialog, which ->

                // Do something when user press the positive button
                directionsArray.removeAt(position)
                directionAdapter.notifyDataSetChanged()
                Utils.setListViewHeightBasedOnChildren(directionList)
                expandableLayout_directions.initLayout()
            }

            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        })

        directionsBtnLayout.setOnClickListener{
            expandableLayout_directions.toggle()
            expandableLayout_directions.initLayout()


            if (isDirectionsExpanded){
                arrowBtn3.setImageDrawable(arrowUpDrawable)
                isDirectionsExpanded = false
            }else{
                arrowBtn3.setImageDrawable(arrowDownDrawable)
                isDirectionsExpanded = true
            }

        }

        btnAddDirection.setOnClickListener{
            if (add_direction_entry.text.isNotEmpty()){
                directionsArray.add(add_direction_entry.text.toString())

                add_direction_entry.text.clear()
            }

            //// Sets the length of the List depending on how many items there are
            Utils.setListViewHeightBasedOnChildren(directionList)
            expandableLayout_directions.initLayout()
        }
    }



    private fun saveEditedFoodItem(foodSave : JSONObject) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(activity)

        // Set the alert dialog title
        builder.setTitle("Save Item")

        // Display a message on alert dialog
        builder.setMessage("Save ${foodSave.get("food_name")}")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Save"){dialog, which ->

            saveEditedFood(foodSave)
        }

        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->

        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }


    fun setLabels(foodJSON: JSONObject){
        //val categoryCheck = foodJSON.toString().contains("food_category", ignoreCase = true)
        val categoryArray = resources.getStringArray(R.array.category_arrays)

        lblAddNoteName.text = foodJSON.get("food_name").toString()

        // Category Check and Set
        if (categoryCheck){
            var x = 0
            while (x < categoryArray.size){
                if (categoryArray[x] == foodJSON.get("food_category").toString()){
                    categorySpinner.setSelection(x)
                    break
                }
                x++
            }
        }

        // Rating Check and Set
        if (ratingCheck){
            try{
                val ratingInt = foodJSON.get("rating") as Double
                ratingBar.rating = ratingInt.toFloat()
            }catch(e : ClassCastException){
                val ratingInt = foodJSON.get("rating") as Int
                ratingBar.rating = ratingInt.toFloat()
            }
            ratingsLine.visibility = View.INVISIBLE
        }

        // Notes Check and Set
        if (noteCheck){
            val notesJSONArray = foodJSON.getJSONObject("notes").getJSONArray("note")

            var x = 0
            while (x < notesJSONArray.length()){
                notesArray.add(notesJSONArray.get(x).toString())
                x++
            }
        }


        // Tags Check and Set
        if (tagCheck){
            val tagsJSONArray = foodJSON.getJSONObject("tags").getJSONArray("tag")

            var x = 0
            while (x < tagsJSONArray.length()){
                tagsArray.add(tagsJSONArray.get(x).toString())
                x++
            }
        }

        // Directions Check and Set
        if (directionCheck){
            val directionsJSONArray = foodJSON.getJSONObject("directions").getJSONArray("direction")

            var x = 0
            while (x < directionsJSONArray.length()){
                directionsArray.add(directionsJSONArray.get(x).toString())
                x++
            }
        }
    }

    fun saveEditedFood(foodJSON: JSONObject){
        var tempFoodJSON = foodJSON

        //val categoryCheck = foodJSON.toString().contains("food_category", ignoreCase = true)
        //val ratingCheck = foodJSON.toString().contains("rating", ignoreCase = true)

        // Category Check and Save
        if (categoryCheck){
            if (categorySpinner.selectedItem != tempFoodJSON.get("food_category")){
                tempFoodJSON.remove("food_category")
                tempFoodJSON.put("food_category", categorySpinner.selectedItem)
            }
        }else{
            tempFoodJSON.put("food_category", categorySpinner.selectedItem)
        }


        // Rating Check and Save
        //First check if ratingLine is visible or not
        if (ratingsLine.visibility == View.INVISIBLE){
            // If ratingLine is gone and "rating" exists in JSON
            if (ratingCheck){
                if (ratingBar.rating != tempFoodJSON.get("rating")){
                    tempFoodJSON.remove("rating")
                    tempFoodJSON.put("rating", ratingBar.rating)
                }

            } // If ratingLine is gone and "rating" doesn't exist (add one)
            else{
                tempFoodJSON.put("rating", ratingBar.rating)
            }
        }else{
            // if ratingLine is VISIBLE, delete rating if it already exists
            if (ratingCheck){
                tempFoodJSON.remove("rating")
            }
        }

        // Notes Check and Save
        if (noteCheck){
            tempFoodJSON.remove("notes")

            if (notesArray.size > 0){
                val noteArrayJSON = JSONArray(notesArray)
                val notesJSON = JSONObject()

                notesJSON.put("note",noteArrayJSON)
                tempFoodJSON.put("notes", notesJSON)
            }
        }else{
            val noteArrayJSON = JSONArray(notesArray)
            val notesJSON = JSONObject()

            notesJSON.put("note",noteArrayJSON)
            tempFoodJSON.put("notes", notesJSON)
        }

        // Tags Check and Save
        if (tagCheck){
            tempFoodJSON.remove("tags")

            if (tagsArray.size > 0){
                val tagArrayJSON = JSONArray(tagsArray)
                val tagsJSON = JSONObject()

                tagsJSON.put("tag",tagArrayJSON)
                tempFoodJSON.put("tags", tagsJSON)
            }
        }else{
            val tagArrayJSON = JSONArray(tagsArray)
            val tagsJSON = JSONObject()

            tagsJSON.put("tag",tagArrayJSON)
            tempFoodJSON.put("tags", tagsJSON)
        }


        // Directions Check and Save
        if (directionCheck){
            tempFoodJSON.remove("directions")

            if (directionsArray.size > 0){
                val directionArrayJSON = JSONArray(directionsArray)
                val directionsJSON = JSONObject()

                directionsJSON.put("direction",directionArrayJSON)
                tempFoodJSON.put("directions", directionsJSON)
            }
        }else{
            val directionArrayJSON = JSONArray(directionsArray)
            val directionsJSON = JSONObject()

            directionsJSON.put("direction",directionArrayJSON)
            tempFoodJSON.put("directions", directionsJSON)
        }


        val originalObject = SavedItem(arguments!!.getString("foodJSON"))
        val newObject = SavedItem(tempFoodJSON.toString())

        savedItemViewModel.deleteFood(originalObject)
        savedItemViewModel.insertFood(newObject)

        dismiss()
    }


    companion object {

        /**
         * Create a new instance of CustomDialogFragment, providing "num" as an
         * argument.
         */
        fun newInstance(foodJSON : JSONObject): SavedFoodViewPagerParent {
            val fragmentDialog = SavedFoodViewPagerParent()

             //Supply num input as an argument.
            val args = Bundle()
            args.putString("foodJSON", foodJSON.toString())
            fragmentDialog.arguments = args

            return fragmentDialog
        }
    }
}