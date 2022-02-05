package com.abdelrahman.myshoppal.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.models.Order
import com.abdelrahman.myshoppal.ui.activities.MyOrdersDetailsActivity
import com.abdelrahman.myshoppal.ui.ui.adapters.MyProductsListAdapter
import com.abdelrahman.myshoppal.utils.Constants
import com.abdelrahman.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_cart_layout.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class OrdersListAdapter(
        private var context: Context,
       private var ordersList:ArrayList<Order>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyOrdersViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if(holder is MyOrdersViewHolder){
           var order = ordersList[position]
           holder.itemView.tv_item_name.text = order.title
           holder.itemView.tv_item_price.text = order.sub_total_amount
         GlideLoader(context).loadProductPicture(order.image,holder.itemView.iv_item_image)
           holder.itemView.ib_delete_product.visibility = View.GONE

           holder.itemView.setOnClickListener {
               val intent = Intent(context,MyOrdersDetailsActivity::class.java)
               intent.putExtra(Constants.EXTRA_MY_ORDER_DETAILS,order)
               context.startActivity(intent)
           }
       }

    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class MyOrdersViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

    }
}