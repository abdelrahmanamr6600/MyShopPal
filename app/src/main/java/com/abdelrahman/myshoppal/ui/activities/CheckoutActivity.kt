package com.abdelrahman.myshoppal.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.Address
import com.abdelrahman.myshoppal.models.CartItem
import com.abdelrahman.myshoppal.models.Order
import com.abdelrahman.myshoppal.models.Product
import com.abdelrahman.myshoppal.ui.ui.adapters.CartItemsAdapter
import com.abdelrahman.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity() {
    private var mSelectedAddress:Address?= null
    private lateinit var mProductsList:ArrayList<Product>
    private lateinit var mCartItemList:ArrayList<CartItem>
    private var mSubTotal:Double = 0.0
    private var mTotalAmount:Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setupActionBar()
        getProductList()
        if (intent.hasExtra(Constants.Extra_SELECTED_ADDRESS))
        {
            mSelectedAddress = intent.getParcelableExtra<Address>(Constants.Extra_SELECTED_ADDRESS)
        }
        if (mSelectedAddress!=null){
            tv_checkout_address_type.text = mSelectedAddress?.type
            tv_checkout_address.text = "${mSelectedAddress?.address},${mSelectedAddress?.zipCode}"
            tv_checkout_additional_note.text=mSelectedAddress?.additionalNote
            tv_checkout_full_name.text = mSelectedAddress?.name
            if (mSelectedAddress!!.otherDetails?.isNotEmpty())
            {
                tv_checkout_other_details.text = mSelectedAddress?.otherDetails
            }
            tv_checkout_mobile_number.text = mSelectedAddress?.mobileNumber

        }
        btn_place_order.setOnClickListener {
            placeAnOrder()
        }

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_checkout_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_checkout_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    private fun getProductList() {

        // Show the progress dialog.
        showProgressBar(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

    private fun getCartItemsList(){
        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        mProductsList = productsList
       getCartItemsList()
    }

    fun successCartItemsList(cartList:ArrayList<CartItem>){
        hideDialog()
        for (product in mProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.productId) {

                    cartItem.stock_Quantity = product.stock_quantity
                }
            }
        }
        mCartItemList = cartList
        rv_cart_list_items.layoutManager = LinearLayoutManager(this)
        rv_cart_list_items.setHasFixedSize(true)
        var cartItemsListAdapter = CartItemsAdapter(this,mCartItemList,false)
        rv_cart_list_items.adapter = cartItemsListAdapter

        for (item in mCartItemList){
            val availableQuantity = item.stock_Quantity.toInt()
            if (availableQuantity>0){
                val price = item.price.toDouble()
                 val quantity = item.cart_Quantity.toInt()
                mSubTotal+=(price * quantity)

            }
        }

        tv_checkout_sub_total.text = "$${mSubTotal}"
        tv_checkout_shipping_charge.text = "$10.0"
        if (mSubTotal >0){
            ll_checkout_place_order.visibility = View.VISIBLE
           mTotalAmount = mSubTotal + 10.0
            tv_checkout_total_amount.text = "$${mTotalAmount}"
        }
        else
        {
            ll_checkout_place_order.visibility = View.GONE
        }
    }

    private fun placeAnOrder(){
        showProgressBar(resources.getString(R.string.please_wait))
        if (mSelectedAddress!=null){
            val order = Order(
                FirestoreClass().getCurrentUser(),
                mCartItemList,
                mSelectedAddress!!,
                "MyOrder${System.currentTimeMillis()}",
                mCartItemList[0].image,
                mSubTotal.toString(),
                "10",
                mTotalAmount.toString(),
            )
            FirestoreClass().placeOrder(this,order )
        }
    }
    fun orderPlacedSuccessful(){
  FirestoreClass().updateAllDetails(this,mCartItemList)
    }
    fun allDetailsUpdatedSuccessfully(){
        hideDialog()
        Toast.makeText(this,"Yout Order was Placed Successfully",Toast.LENGTH_LONG).show()
        val intent = Intent(this,DashBoardActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}