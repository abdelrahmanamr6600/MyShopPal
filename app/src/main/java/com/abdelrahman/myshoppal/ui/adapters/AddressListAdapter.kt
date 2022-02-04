package com.abdelrahman.myshoppal.ui.adapters

import android.app.Activity
import android.content.Intent
import android.provider.SyncStateContract
import android.text.BoringLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.Address
import com.abdelrahman.myshoppal.ui.activities.AddEditAddressActivity
import com.abdelrahman.myshoppal.ui.activities.AddressListActivity
import com.abdelrahman.myshoppal.ui.activities.CheckoutActivity
import com.abdelrahman.myshoppal.utils.Constants
import com.google.api.Context
import kotlinx.android.synthetic.main.item_address_layout.view.*

open class AddressListAdapter(private var context: android.content.Context, private var addressList:ArrayList<Address>,private var mSelectAddress:Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyAddressesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val address = addressList[position]
        if (holder is MyAddressesViewHolder){
            holder.itemView.tv_address_full_name.text = address.name
            holder.itemView.tv_address_mobile_number.text = address.mobileNumber
            holder.itemView.tv_address_type.text = address.type
            holder.itemView.tv_address_details.text ="${address.address}, ${address.zipCode}"

            if (mSelectAddress)
            {
                holder.itemView.setOnClickListener {
                val intent = Intent(context,CheckoutActivity::class.java)
                    intent.putExtra(Constants.Extra_SELECTED_ADDRESS,address)
                    context.startActivity(intent)
                }
            }

        }
    }

    override fun getItemCount(): Int {
     return addressList.size
    }

    fun notifyEditItem(activity: Activity,position:Int){
        val intent = Intent(context,AddEditAddressActivity::class.java)
          intent.putExtra(Constants.Extra_Address_Details,addressList[position])
           activity.startActivityForResult(intent,Constants.Add_Address_REQUEST_CODE)
        notifyItemChanged(position)
    }
    fun notifyDeleteItem(position: Int){
        FirestoreClass().deleteAddress(context as AddressListActivity,addressList[position].id)
        notifyItemChanged(position)
    }

    private class MyAddressesViewHolder(view: View) : RecyclerView.ViewHolder(view)

}