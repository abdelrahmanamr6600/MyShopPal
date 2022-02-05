package com.abdelrahman.myshoppal.ui.ui.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.Order
import com.abdelrahman.myshoppal.ui.adapters.OrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*
import java.util.ArrayList

class OrdersFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_orders, container, false)

        return root
    }

    fun populateOrdersListinUi(ordersList:ArrayList<Order>){
        hideDialog()
        if (ordersList.size > 0){
            tv_no_orders_found.visibility = View.GONE
            rv_my_order_items.visibility = View.VISIBLE

        }
        var adapter = OrdersListAdapter(this.requireContext(),ordersList)
        rv_my_order_items.layoutManager = LinearLayoutManager(activity)
        rv_my_order_items.setHasFixedSize(true)
        rv_my_order_items.adapter = adapter


    }

    private fun getMyOrdersList(){
        showProgressBar("Please Wait")
        FirestoreClass().getMyOrdersList(this)
    }
}