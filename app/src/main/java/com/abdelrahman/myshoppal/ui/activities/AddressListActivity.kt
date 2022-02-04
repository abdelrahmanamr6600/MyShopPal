package com.abdelrahman.myshoppal.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.Address
import com.abdelrahman.myshoppal.ui.adapters.AddressListAdapter
import com.abdelrahman.myshoppal.ui.ui.adapters.MyProductsListAdapter
import com.abdelrahman.myshoppal.utils.Constants
import com.myshoppal.utils.SwipeToDeleteCallback
import com.myshoppal.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

class AddressListActivity : BaseActivity() {

   var mSelectedAddress:Boolean = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            getAddressesList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        setupActionBar()
        goToAddAddress()

        if (intent.hasExtra(Constants.Extra_SELECT_ADDRESS)){
            mSelectedAddress = intent.getBooleanExtra(Constants.Extra_SELECT_ADDRESS,false)
        }

        getAddressesList()
        if (mSelectedAddress){
            tv_add_address.setText("Select Address")
        }


    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_address_list_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_address_list_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    private fun goToAddAddress(){
        tv_add_address.setOnClickListener {
            val intent = Intent(this, AddEditAddressActivity::class.java)
            startActivityForResult(intent,Constants.Add_Address_REQUEST_CODE)
        }
    }
  fun getAddressesList(){
        showProgressBar("Please Wait")
        FirestoreClass().getAddresses(this)
    }

   fun getAddressesSuccessful(list: ArrayList<Address>) {
        hideDialog()

        if (list.size > 0){
            rv_address_list.visibility = View.VISIBLE
            tv_no_address_found.visibility = View.GONE
            rv_address_list.layoutManager = LinearLayoutManager(this)
            val adapter = AddressListAdapter(this, list,mSelectedAddress)
            Collections.reverse(list)
            rv_address_list.setHasFixedSize(true)
            rv_address_list.adapter = adapter
            adapter.notifyDataSetChanged()

            if (!mSelectedAddress){

                val ediSwipeHandler = object:SwipeToEditCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val listAdapter  = rv_address_list.adapter as AddressListAdapter
                        listAdapter.notifyEditItem(this@AddressListActivity,viewHolder.adapterPosition)
                    }
                }

                val deleteSwipeHandler = object:SwipeToDeleteCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showProgressBar("please wait")
                        val listAdapter  = rv_address_list.adapter as AddressListAdapter
                        listAdapter.notifyDeleteItem(viewHolder.adapterPosition)

                    }
                }

                var editItemTouchHelper = ItemTouchHelper(ediSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(rv_address_list)
                var deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(rv_address_list)
            }




        }
        else
        {
            rv_address_list.visibility = View.GONE
            tv_no_address_found.visibility = View.VISIBLE
        }
    }
fun deleteAddressSuccessful(){
    hideDialog()
    getAddressesList()
}

}