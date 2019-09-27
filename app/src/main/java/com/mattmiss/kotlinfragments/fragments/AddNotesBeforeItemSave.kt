package com.mattmiss.kotlinfragments.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.mattmiss.kotlinfragments.*
import com.mattmiss.kotlinfragments.adapters.CustomListAdapter
import com.mattmiss.kotlinfragments.database.SavedItem
import com.mattmiss.kotlinfragments.database.SavedRecipe
import com.mattmiss.kotlinfragments.models.SavedItemViewModel
import com.mattmiss.kotlinfragments.utils.Utils
import kotlinx.android.synthetic.main.add_notes_save.*
import org.json.JSONArray
import org.json.JSONObject


class AddNotesBeforeItemSave : androidx.fragment.app.DialogFragment(), ChooseCategoryFragment.CategoryChoiceListener{


    var isFood = true
    val notesArray = arrayListOf<String>()
    val tagsArray = arrayListOf<String>()
    val directionsArray = arrayListOf<String>()
    val utils = Utils
    val category : String? = null
    var categoryChosen = false

    private lateinit var savedItemViewModel: SavedItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.add_notes_save, container)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemSave = JSONObject(arguments!!.getString(ITEM_JSON_STRING))

        val nameString = when (isFood){
            true -> FOOD_NAME_STRING
            false -> RECIPE_NAME_STRING
        }

        // nameString will be either "food_name" or "recipe_name"
        lblAddNoteName.text = itemSave.getString(nameString)

        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)


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

        chooseCategory.setOnClickListener {

            val ft = activity!!.supportFragmentManager.beginTransaction()

            val newFragment = ChooseCategoryFragment()
            newFragment.setListener(this)
            newFragment.setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
            ft.addToBackStack("fragment_choose_category_dialog")
            newFragment.show(ft, "fragment_choose_category_dialog")
        }



        // Set adapter and buttons for Notes
        var noteAdapter = CustomListAdapter(
            noteList.context,
            R.layout.note_list_view,
            notesArray,
            "note"
        )
        noteList.adapter = noteAdapter

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
            }

            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        })

        expandableButton_notes.setOnClickListener{
            expandableLayout_notes.toggle()
        }

        btnAddNote.setOnClickListener{
            if (add_note_entry.text.isNotEmpty()){
                notesArray.add(add_note_entry.text.toString())


                //// Sets the length of the List depending on how many items there are
                Utils.setListViewHeightBasedOnChildren(noteList)

                add_note_entry.text.clear()
            }
        }

        btnFinalSaveFood.setOnClickListener{
            if (!categoryChosen){
                Toast.makeText(activity, "Choose a category!", Toast.LENGTH_SHORT).show()

            }else{
                // If the itemSave is a food item, insert it into the viewmodel as Food
                if (isFood){
                    itemSave.put("food_category", category)

                    if (ratingsLine.visibility == View.INVISIBLE){
                        itemSave.put("rating", ratingBar.rating.toDouble())
                    }

                    if (notesArray.size > 0){
                        val noteArrayJSON = JSONArray(notesArray)
                        val notesJSON = JSONObject()

                        notesJSON.put("note",noteArrayJSON)
                        itemSave.put("notes", notesJSON)
                    }
                    if (tagsArray.size > 0){
                        val tagsArrayJSON = JSONArray(tagsArray)
                        val tagsJSON = JSONObject()

                        tagsJSON.put("tag",tagsArrayJSON)
                        itemSave.put("tags", tagsJSON)
                    }
                    if (directionsArray.size > 0){
                        val directionsArrayJSON = JSONArray(directionsArray)
                        val directionsJSON = JSONObject()

                        directionsJSON.put("direction",directionsArrayJSON)
                        itemSave.put("directions", directionsJSON)
                    }

                    val savedItem = SavedItem(itemSave.toString())

                    // INSERT THE JSON OBJECT AFTER I APPEND THE NEW NOTES N SHIT
                    savedItemViewModel.insertFood(savedItem)
                    dismiss()



                    val intent = Intent()
                    intent.putExtra("saved_food_item", itemSave.toString())

                    val targetFrag = targetFragment
                    targetFrag?.onActivityResult(2, Activity.RESULT_OK, intent)

                    // Print this if I want to see what the JSON toString looks like
                    //println(foodSave)

                }else{
                    // Else if the itemSave is a Recipe item, insert it into the viewmodel as Recipe
                    val savedItem = SavedRecipe(itemSave.toString())

                    // INSERT THE JSON OBJECT AFTER I APPEND THE NEW NOTES N SHIT
                    savedItemViewModel.insertRecipe(savedItem)
                    dismiss()

                    val intent = Intent()
                    intent.putExtra("saved_recipe_item", itemSave.toString())

                    val targetFrag = targetFragment
                    targetFrag?.onActivityResult(1, Activity.RESULT_OK, intent)
                }

            }
        }


        // Set adapter and buttons for Tags
        var tagAdapter = CustomListAdapter(
            tagList.context,
            R.layout.tag_list_view,
            tagsArray,
            "tag"
        )
        tagList.adapter = tagAdapter


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
            }

            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        })

        expandableButton_tags.setOnClickListener{
            expandableLayout_tags.toggle()
        }

        btnAddTag.setOnClickListener{

            val entry = add_tag_entry.text

            if (entry.isNotEmpty()){
                val trimmed = entry.trim()
                val entryArray = (trimmed).split(" ")

                if (entryArray.size == 1){
                    tagsArray.add(trimmed.toString())


                    // Sets the length of the List depending on how many items there are
                    Utils.setListViewHeightBasedOnChildren(tagList)

                    add_tag_entry.text.clear()
                }else {
                    println("NOPE")
                }
            }
        }


        // Set adapter and buttons for Notes
        var directionAdapter = CustomListAdapter(
            directionList.context,
            R.layout.directions_list_view, directionsArray, "addedDirection"
        )
        directionList.adapter = directionAdapter


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
            }

            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        })

        expandableButton_directions.setOnClickListener{
            expandableLayout_directions.toggle()
        }

        btnAddDirection.setOnClickListener{
            if (add_direction_entry.text.isNotEmpty()){
                directionsArray.add(add_direction_entry.text.toString())

                //// Sets the length of the List depending on how many items there are
                Utils.setListViewHeightBasedOnChildren(directionList)

                add_direction_entry.text.clear()
            }
        }


    }

    // gets called when the category is chosen from the
    // ChooseCategoryFragment listener
    override fun onCategoryChoice(category: String) {
        Toast.makeText(activity, "$category chosen for category", Toast.LENGTH_SHORT).show()
        chooseCategory.text = category
        categoryChosen = true
    }


    companion object {

        const val ITEM_JSON_STRING = "itemJSON"
        const val FOOD_NAME_STRING = "food_name"
        const val RECIPE_NAME_STRING = "recipe_name"

        fun newInstance(itemJSON : JSONObject): AddNotesBeforeItemSave {
            val fragmentDialog = AddNotesBeforeItemSave()

             //Supply num input as an argument.
            val args = Bundle()
            args.putString(ITEM_JSON_STRING, itemJSON.toString())
            fragmentDialog.arguments = args

            return fragmentDialog
        }
    }
}