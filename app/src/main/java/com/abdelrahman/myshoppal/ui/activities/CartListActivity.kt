package com.abdelrahman.myshoppal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.CartItem
import com.abdelrahman.myshoppal.models.Product
import com.abdelrahman.myshoppal.ui.ui.adapters.CartItemsAdapter
import com.abdelrahman.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.item_cart_layout.*

class CartListActivity : BaseActivity() {

    // A global variable for the product list.
    private lateinit var mProductsList: ArrayList<Product>

    // A global variable for the cart list items.
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()
        btn_checkout.setOnClickListener {
            val intent = Intent(this,AddressListActivity::class.java)
            intent.putExtra(Constants.Extra_SELECT_ADDRESS,true)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
      getProductList()
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_cart_list_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_cart_list_activity.setNavigationOnClickListener{ onBackPressed() }
    }
    private fun getProductList() {

        // Show the progress dialog.
        showProgressBar(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProductsList(this@CartListActivity)
    }
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList

        getCartItemsList()
    }
    private fun getCartItemsList(){
        FirestoreClass().getCartList(this)
    }

    fun successCartItemList(list: ArrayList<CartItem>) {
        hideDialog()

        for (product in mProductsList) {
            for (cart in list) {
                if (product.product_id == cart.productId) {

                    cart.stock_Quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0) {
                        cart.cart_Quantity = product.stock_quantity
                    }
                }
            }
        }
        mCartListItems = list

        if (mCartListItems.size > 0){
            rv_cart_items_list.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE
            ll_checkout.visibility = View.VISIBLE
            var subTotal: Double = 0.0

            for (item in list) {

                val price = item.price.toDouble()
                val quantity = item.cart_Quantity.toInt()

                subTotal += (price * quantity)
            }

            tv_sub_total.text = "$$subTotal"

            tv_shipping_charge.text = "$10.0"

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 10
                tv_total_amount.text = "$$total"
            }else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE

        }
        Log.i("size",list.size.toString())
        rv_cart_items_list.layoutManager = LinearLayoutManager(this)
        var adapter  = CartItemsAdapter(this,mCartListItems,true)
        rv_cart_items_list.setHasFixedSize(true)

        rv_cart_items_list.adapter = adapter



    }

    fun productDeleteSuccess() {
       hideDialog()
        FirestoreClass().getCartList(this)

    }



    private fun showAlertDialogToDeleteProduct(cart_Id: String) {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->


            // START
            // Show the progress dialog.
//            showProgressBar(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteCartItem(this, cart_Id)

            // END

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)

        alertDialog.show()
    }

    fun deleteCartItem(cart_Id:String){
        showAlertDialogToDeleteProduct(cart_Id)

    }

    fun itemUpdateSuccess() {

        hideDialog()

        getCartItemsList()
    }

}