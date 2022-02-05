package com.abdelrahman.myshoppal.ui.fragments

import android.os.BaseBundle
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.SoldProduct
import com.abdelrahman.myshoppal.ui.adapters.SoldProductsListAdapter
import com.abdelrahman.myshoppal.ui.ui.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_sold_products.*


class SoldProductsFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }
    private fun getSoldProductsList() {

        showProgressBar(resources.getString(R.string.please_wait))

        FirestoreClass().getSoldProductsList(this@SoldProductsFragment)
    }
    fun successSoldProductsList(list: ArrayList<SoldProduct>) {
        hideDialog()

        if (list.size > 0) {
            rv_sold_product_items.visibility = View.VISIBLE
            tv_no_sold_products_found.visibility = View.GONE

            rv_sold_product_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_product_items.setHasFixedSize(true)

            val soldProductsListAdapter =
                SoldProductsListAdapter(requireActivity(), list)
            rv_sold_product_items.adapter = soldProductsListAdapter
        } else {
            rv_sold_product_items.visibility = View.GONE
            tv_no_sold_products_found.visibility = View.VISIBLE
        }
    }
}