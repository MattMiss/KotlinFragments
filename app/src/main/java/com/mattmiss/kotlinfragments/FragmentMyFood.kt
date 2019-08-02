package com.mattmiss.kotlinfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_a.*
import kotlinx.android.synthetic.main.fragment_b.*
import org.json.JSONObject


class FragmentMyFood : androidx.fragment.app.Fragment() {

    private lateinit var savedItemViewModel: SavedItemViewModel



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Database stuff
        //val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerview)

        val adapter = SavedItemListAdapter(context!!, { savedItem: JSONObject -> foodSaveClicked(savedItem)})

        recyclerview.adapter = adapter

        recyclerview.layoutManager = LinearLayoutManager(recyclerview.context)

        // Add a line in between each item
        recyclerview.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerview.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        savedItemViewModel = ViewModelProviders.of(this).get(SavedItemViewModel::class.java)
        savedItemViewModel.allSavedItems.observe(this, Observer { savedItems ->
            savedItems.let { adapter.setSavedItems(it) }
        })


    }

    companion object {
        fun newInstance(): FragmentMyFood {
            val fragment = FragmentMyFood()
            return fragment
        }
    }


    private fun foodSaveClicked(savedItem : JSONObject) {
        val ft = activity!!.supportFragmentManager.beginTransaction()

        val newFragment = SavedFoodViewPagerParent.newInstance(savedItem)
        newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)

        ft.addToBackStack("fragment_dialog")
        newFragment.show(ft, "fragment_dialog")

    }
}