package com.abdelrahman.myshoppal.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.Address
import com.abdelrahman.myshoppal.ui.adapters.AddressListAdapter
import com.abdelrahman.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*
import kotlinx.android.synthetic.main.activity_address_list.*

class AddEditAddressActivity : BaseActivity() {
    private var mAddressDetails: Address? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        setupActionBar()

        if (intent.hasExtra(Constants.Extra_Address_Details)) {
            mAddressDetails =
                    intent.getParcelableExtra(Constants.Extra_Address_Details)!!
        }


        if(mAddressDetails!=null){
            if (mAddressDetails!!.id.isNotEmpty()){
                tv_title_add.setText("Edit Address")
                btn_submit_address.setText("Edit Address")
                et_full_name.setText(mAddressDetails!!.name)
                et_phone_number.setText(mAddressDetails?.mobileNumber)
                et_address.setText(mAddressDetails?.address)
                et_zip_code.setText(mAddressDetails?.zipCode)
                et_other_details.setText(mAddressDetails?.otherDetails)
                et_additional_note.setText(mAddressDetails?.additionalNote)
                et_other_details.setText(mAddressDetails?.otherDetails)

                when(mAddressDetails?.type)
                {
                    Constants.HOME ->{
                        rb_home.isChecked = true
                    }
                    Constants.OTHER ->{
                        rb_other.isChecked = true
                    }
                    Constants.OFFICE ->{
                        rb_office.isChecked = true
                    }
                }
            }
        }

        btn_submit_address.setOnClickListener {
            saveAddressToFireStore()
        }
        rg_type.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == R.id.rb_other){
                til_other_details.visibility = View.VISIBLE
            }
            else
                til_other_details.visibility = View.GONE

        }




    }

    private fun setupActionBar(){
        setSupportActionBar( toolbar_add_edit_address_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_add_edit_address_activity.setNavigationOnClickListener{ onBackPressed() }
    }



    private fun validateData(): Boolean {
        return when {

            TextUtils.isEmpty(et_full_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_please_enter_full_name),
                        true
                )
                false
            }

            TextUtils.isEmpty(et_phone_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_please_enter_phone_number),
                        true
                )
                false
            }

            TextUtils.isEmpty(et_address.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_address), true)
                false
            }

            TextUtils.isEmpty(et_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }

            rb_other.isChecked && TextUtils.isEmpty(
                    et_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun saveAddressToFireStore(){
        val fullName = et_full_name.text.toString().trim { it <=' ' }
        val phoneNumber = et_phone_number.text.toString().trim { it <= ' ' }
        val address = et_address.text.toString().trim { it <= ' ' }
        val zipCode = et_zip_code.text.toString().trim{it <= ' '}
        val additionalNote= et_additional_note.text.toString().trim{it <= ' '}
        val otherDetails: String = et_other_details.text.toString().trim { it <= ' ' }

        if(validateData()){
            showProgressBar("please wait")
            val addressType:String = when{
                rb_home.isChecked ->{Constants.HOME
                }
                rb_office.isChecked ->{
                    Constants.OFFICE
                }
                else ->{
                    Constants.OTHER
                }
            }
            val addressModel = Address(
                    FirestoreClass().getCurrentUser(),
                    fullName,
                    phoneNumber,
                    address,
                    zipCode,
                    additionalNote,
                    addressType,
                    otherDetails
            )
            if (mAddressDetails!=null && mAddressDetails!!.id.isNotEmpty()){
                FirestoreClass().updateAddress(
                        this@AddEditAddressActivity,
                    addressModel,
                        mAddressDetails!!.id)
            }
            else{
                FirestoreClass().addAddress(this@AddEditAddressActivity,addressModel)
            }

        }
    }

    fun addUpdateAddressSuccessful() {
        hideDialog()
        Toast.makeText(this,resources.getText(R.string.err_your_address_added_successfully),Toast.LENGTH_LONG).show()
        setResult(RESULT_OK)
        finish()

    }




}