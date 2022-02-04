package com.abdelrahman.myshoppal.ui.ui.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.ui.activities.AddProductActivity
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.Product
import com.abdelrahman.myshoppal.ui.ui.adapters.MyProductsListAdapter
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : BaseFragment() {

    //private lateinit var homeViewModel: HomeViewModel
    var productsListt : ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("ppp",productsListt.size.toString())
    }

    override fun onResume() {
        super.onResume()
        getProductsFromFireStore()


    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_products, container, false)



        return root


    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_add,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id)
        {
            R.id.add_product ->{
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }
    private  fun getProductsFromFireStore(){
        showProgressBar("pleas Wait")
        FirestoreClass().getProductsList(this)

    }

    fun deleteProduct(productID: String) {

        showAlertDialogToDeleteProduct(productID)

    }

    fun productDeleteSuccess()
    {
        hideDialog()

        Toast.makeText(
                requireActivity(),
                resources.getString(R.string.product_delete_success_message),
                Toast.LENGTH_SHORT
        ).show()
        getProductsFromFireStore()


    }

     fun successProductsFromFireStore(productsList:ArrayList<Product>){
        hideDialog()
         if (productsList.size>0)
         {
             rv_my_product_items.visibility = View.VISIBLE
             tv_no_products_found.visibility = View.GONE
         }
         rv_my_product_items.layoutManager = LinearLayoutManager(activity)
         val Myadapter = MyProductsListAdapter(requireContext(),productsList,this)
         rv_my_product_items.setHasFixedSize(true)
         rv_my_product_items.adapter=Myadapter
    }


    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(requireActivity())
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
            FirestoreClass().deleteProduct(this@ProductsFragment, productID)
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





}