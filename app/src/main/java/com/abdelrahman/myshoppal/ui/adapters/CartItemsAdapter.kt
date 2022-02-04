package com.abdelrahman.myshoppal.ui.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.CartItem
import com.abdelrahman.myshoppal.ui.activities.BaseActivity
import com.abdelrahman.myshoppal.ui.activities.CartListActivity
import com.abdelrahman.myshoppal.ui.activities.CheckoutActivity
import com.abdelrahman.myshoppal.utils.Constants
import com.abdelrahman.myshoppal.utils.GlideLoader
import com.abdelrahman.myshoppal.utils.MSPTextView
import com.abdelrahman.myshoppal.utils.MSPTextViewBold
import kotlinx.android.synthetic.main.item_cart_layout.view.*

open class CartItemsAdapter( private var context:Context , private var cartItemsList:ArrayList<CartItem>,val updateCartItems:Boolean) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return MyCartItemsHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item = cartItemsList[position]
        if (holder is MyCartItemsHolder)
        {
            GlideLoader(context).loadProductPicture(item.image,holder.cartImage)
            holder.cartTitle.text = item.title
            holder.cartPrice.text = "$${item.price}"
            holder.cartQuantity.text = item.cart_Quantity

            Log.i("quntity",item.stock_Quantity)
            Log.d("title",item.title)

            if(item.stock_Quantity =="0"){

                holder.add.visibility = View.GONE
                holder.remove.visibility = View.GONE
                holder.itemView.tv_cart_quantity.text =
                        context.resources.getString(R.string.lbl_out_of_stock)
            }else
            {
                holder.add.visibility = View.VISIBLE
                holder.remove.visibility = View.VISIBLE
            }

            holder.deleteItem.setOnClickListener {
                when(context){
                    is CartListActivity ->{
                        (context as CartListActivity).deleteCartItem(item.id)
                    }

                }


            }
            holder.remove.setOnClickListener {
                if (item.cart_Quantity=="1"){
                    FirestoreClass().deleteCartItem(context as CartListActivity,item.id)
                }
                else {
                    val cartQuantity: Int = item.cart_Quantity.toInt()
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()
                    if (context is CartListActivity) {
                        (context as CartListActivity).showProgressBar("please wait")
                    }
                    FirestoreClass().updateMyCart(context,item.id,itemHashMap)
                }

            }
            holder.add.setOnClickListener {
                val cartQuantity: Int = item.cart_Quantity.toInt()
                if (cartQuantity < item.stock_Quantity.toInt()) {
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()
                    if (context is CartListActivity) {
                        (context as CartListActivity).showProgressBar("please wait")
                    }
                    FirestoreClass().updateMyCart(context, item.id, itemHashMap)

                } else {
                    Toast.makeText(
                        context,
                        "we Have${item.stock_Quantity} only on stock",
                        Toast.LENGTH_LONG
                    ).show()


                }
            }

            if (!updateCartItems){
                holder.remove.visibility = View.GONE
                holder.add.visibility = View.GONE
                holder.deleteItem.visibility = View.GONE
            }

        }
    }

    override fun getItemCount(): Int {
       return cartItemsList.size
    }
}

class MyCartItemsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var cartTitle: MSPTextView = itemView.tv_cart_item_title
    var cartImage: ImageView = itemView.iv_cart_item_image
    var cartPrice: MSPTextViewBold = itemView.tv_cart_item_price
    var cartQuantity: MSPTextView = itemView.tv_cart_quantity
    var deleteItem: ImageButton = itemView.ib_delete_cart_item
    var add: ImageButton = itemView.ib_add_cart_item
    var remove : ImageButton = itemView.ib_remove_cart_item




}