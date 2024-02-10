package com.abdelrahman.myshoppal.ui.ui.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.abdelrahman.myshoppal.R
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseFragment : Fragment() {

 private lateinit var mProgressBar:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }
    fun showProgressBar(text : String){
        mProgressBar = Dialog(requireContext())
        mProgressBar.setContentView(R.layout.dialog_progress)
        mProgressBar.setCancelable(false)
        mProgressBar.setCanceledOnTouchOutside(false)
        mProgressBar.tv_progress_text.text = text
        mProgressBar.show()
    }

    fun hideDialog()
    {
        mProgressBar.cancel()
    }

}