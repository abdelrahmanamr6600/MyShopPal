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
import com.abdelrahman.myshoppal.ui.ui.ui.fragments.ProductsFragment
import com.abdelrahman.myshoppal.utils.Constants
import com.abdelrahman.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class MyProductsListAdapter(val context: Context, private val productsList:ArrayList<Product>,val fragment:ProductsFragment) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var product = productsList[position]
        if (holder is MyViewHolder)
        {
            GlideLoader(context).loadProductPicture(product.image,holder.productImage)
            holder.productName.text = product.title
            holder.productPrice.text = "$${product.price}"

            holder.deleteButton.setOnClickListener {
             fragment.deleteProduct(product.product_id)
            }

            holder.itemView.setOnClickListener {
                var intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.PRODUCT_ID,product.product_id)
                intent.putExtra(Constants.USER_ID,product.user_id)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
             return  productsList.size
    }
    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
       var productImage = itemView.iv_item_image!!
        var productName = itemView.tv_item_name!!
        var productPrice = itemView.tv_item_price!!
        var deleteButton = itemView.ib_delete_product!!


    }
}


