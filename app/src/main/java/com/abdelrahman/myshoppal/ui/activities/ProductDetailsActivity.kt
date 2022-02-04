package com.abdelrahman.myshoppal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.CartItem
import com.abdelrahman.myshoppal.models.Product
import com.abdelrahman.myshoppal.utils.Constants
import com.abdelrahman.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {
    private var mProduct_id :String = " "
    private var mProductUserId :String = " "
    lateinit var mProductDetails : Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionBar()
        getProductDetails()
        if (checkProduct()){
            btn_add_to_cart.visibility = View.GONE

        }
        else
            btn_add_to_cart.visibility = View.VISIBLE

        btn_add_to_cart.setOnClickListener {
            addToCart()
        }
        btn_go_to_cart.setOnClickListener {
            startActivity(Intent(this, CartListActivity::class.java))
        }
    }

     private fun getProductDetails(){
        if (intent.hasExtra(Constants.PRODUCT_ID))
        {
            showProgressBar("please Wait")
            mProduct_id = intent.getStringExtra(Constants.PRODUCT_ID)!!
            mProductUserId = intent.getStringExtra(Constants.USER_ID)!!
           FirestoreClass().getProductDetails(this,mProduct_id)
        }


    }


    private fun setupActionBar(){
        setSupportActionBar(toolbar_product_details_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_product_details_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    fun successProductDetails(product:Product) {

        GlideLoader(this).loadProductPicture(product.image,iv_product_detail_image)
        tv_product_details_title.text = product.title
        tv_product_details_description.text = product.description
        tv_product_details_title.text = product.title
        tv_product_details_price.text = "$${product.price}"
        tv_product_details_available_quantity.text = product.stock_quantity
        mProductDetails = product
        if (FirestoreClass().getCurrentUser()==product.user_id)
        {
            hideDialog()
        }
        else
            FirestoreClass().checkIfItemExistInCart(this,product.product_id)

    }

    fun addToCartSuccess(){
        hideDialog()
        Toast.makeText(this,R.string.success_message_added_to_cart,Toast.LENGTH_LONG).show()
        btn_go_to_cart.visibility = View.VISIBLE
        btn_add_to_cart.visibility = View.GONE
    }
    private fun checkProduct() :Boolean{
        return mProductUserId == FirestoreClass().getCurrentUser()
    }
    private fun addToCart(){
        val cartItem = CartItem(
            FirestoreClass().getCurrentUser(),
            mProduct_id,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        ,mProductDetails.stock_quantity
        )
        showProgressBar("please wait")
        FirestoreClass().addProductToCart(this,cartItem)
    }

    fun productExistInCart(){
        btn_add_to_cart.visibility =View.GONE
        btn_go_to_cart.visibility =View.VISIBLE
        hideDialog()
    }
}