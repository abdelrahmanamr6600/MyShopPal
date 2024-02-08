package com.abdelrahman.myshoppal.ui.ui.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.Product
import com.abdelrahman.myshoppal.ui.activities.CartListActivity
import com.abdelrahman.myshoppal.ui.activities.SettingsActivity
import com.abdelrahman.myshoppal.ui.ui.adapters.MyDashBoardAdapter
import com.abdelrahman.myshoppal.ui.ui.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getProductsFromFireStore()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
      //  dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_setting,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.settings ->
            {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true

            }
            R.id.cartList ->{
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private  fun getProductsFromFireStore(){
        showProgressBar("pleas Wait")
        FirestoreClass().getDashboardItems(this)
    }

    fun successProductsFromFireStore(productsList: ArrayList<Product>) {
        hideDialog()
        if (productsList.size >0)
        {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE
        }
        rv_dashboard_items.layoutManager = GridLayoutManager(activity,2)
        val Myadapter = MyDashBoardAdapter(requireContext(),productsList)
        rv_dashboard_items.setHasFixedSize(true)
        rv_dashboard_items.adapter=Myadapter


    }
}


