package com.mattmiss.kotlinfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.simple_tabs.*
import org.json.JSONObject


class SearchResultViewPagerParent : androidx.fragment.app.DialogFragment(){

    private val searchCode = 1
    private val savedCode = 2

    var instanceCode : Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.simple_tabs, container)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        instanceCode = arguments?.getInt("instanceCode")

        val adapter = SearchResultsPagerAdapter(childFragmentManager)

        if (instanceCode == searchCode){
            adapter.setJSONObject(1,JSONObject(arguments?.getString("foodJSON")))
        }else if (instanceCode == savedCode){
            adapter.setJSONObject(2,JSONObject(arguments?.getString("foodJSON")))
        }
        pager.adapter = adapter
    }


    companion object {

        /**
         * Create a new instance of CustomDialogFragment, providing "num" as an
         * argument.
         */
        fun newInstance(instanceCode: Int, foodJSON : JSONObject): SearchResultViewPagerParent {
            val fragmentDialog = SearchResultViewPagerParent()

             //Supply num input as an argument.
            val args = Bundle()
            args.putInt("instanceCode", instanceCode)
            args.putString("foodJSON", foodJSON.toString())
            fragmentDialog.arguments = args

            return fragmentDialog
        }
    }
}