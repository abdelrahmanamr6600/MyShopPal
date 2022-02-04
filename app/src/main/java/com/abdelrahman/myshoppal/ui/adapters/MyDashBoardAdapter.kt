package com.abdelrahman.myshoppal.ui.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.models.Product
import com.abdelrahman.myshoppal.ui.activities.ProductDetailsActivity
import com.abdelrahman.myshoppal.utils.Constants
import com.abdelrahman.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

class MyDashBoardAdapter(var context:Context , var dashboardList:ArrayList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyDashboardHolder(
                LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      var product = dashboardList[position]
        if (holder is MyDashboardHolder )
        {
            GlideLoader(context).loadProductPicture(product.image,holder.productImage)
            holder.productTitle.text = product.title
            holder.productPrice.text = "$${product.price}"
        }
        holder.itemView.setOnClickListener {
            var intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.PRODUCT_ID,product.product_id)
            intent.putExtra(Constants.USER_ID,product.user_id)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
         return dashboardList.size
    }

    class MyDashboardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       var productImage= itemView.iv_dashboard_item_image
        var productTitle = itemView.tv_dashboard_item_title
        var productPrice = itemView.tv_dashboard_item_price



    }
}