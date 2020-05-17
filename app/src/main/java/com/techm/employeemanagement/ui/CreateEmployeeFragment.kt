package com.techm.employeemanagement.ui

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techm.employeemanagement.R
import com.techm.employeemanagement.data.model.CreateEmployeeRequest
import com.techm.employeemanagement.utils.ResponseStatus
import com.techm.employeemanagement.utils.toast
import com.techm.employeemanagement.ui.viewmodel.ViewModelAddEmployee
import kotlinx.android.synthetic.main.fragment_create_employee.*
import kotlinx.android.synthetic.main.fragment_create_employee.view.*

/**
 * add employee fragment
 */
class CreateEmployeeFragment : Fragment() {
    private lateinit var mDataViewModel: ViewModelAddEmployee
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_create_employee, container, false)
        activity?.title = getString(R.string.create_employee)

        mDataViewModel = ViewModelProvider(this).get(ViewModelAddEmployee::class.java)
        setupProgressDialog()

        view.buttonSave.setOnClickListener {
            createEmployee()
        }
        /**
         * Observer for API response
         * */
        mDataViewModel.createEmployeeResponse.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    hideProgressDialog()
                    context?.toast(getString(R.string.employee_created))
                    clearText()
                }
                ResponseStatus.FAIL -> {
                    hideProgressDialog()
                    context?.toast(getString(R.string.serviceFailureError))
                }
                ResponseStatus.LOADING ->
                    showProgressDialog()
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem( R.id.action_add ).isVisible=false
    }

    /*
    * clear text in search field*/
    private fun clearText() {
        textFieldName.editText!!.setText("")
        textFieldSalary.editText!!.setText("")
        textFieldAge.editText!!.setText("")
    }

    /**
     * call create employee **/
    private fun createEmployee() {
        val employeeName = textFieldName.editText!!.text.toString()
        val employeeSalary = textFieldSalary.editText!!.text.toString()
        val employeeAge = textFieldAge.editText!!.text.toString()
        when {
            TextUtils.isEmpty(employeeName) -> {
                textFieldName.error = getString(R.string.required_field)
                textFieldName.isErrorEnabled = true
            }
            TextUtils.isEmpty(employeeSalary) -> {
                textFieldSalary.error = getString(R.string.required_field)
                textFieldSalary.isErrorEnabled = true
            }
            TextUtils.isEmpty(employeeAge) -> {
                textFieldAge.error = getString(R.string.required_field)
                textFieldAge.isErrorEnabled = true
            }
            else -> {
                showProgressDialog()
                var mModelEmployeeRegistration =
                    CreateEmployeeRequest(
                        employeeName,
                        employeeSalary,
                        employeeAge
                    )
                mDataViewModel.createEmployee(mModelEmployeeRegistration)
            }
        }
    }


    /**
     * Set up progress dialog
     * **/
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupProgressDialog() {
        builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_dialog)
        alertDialog = builder.create()
    }

    /*
    * Show progress dialogue
    * */
    private fun showProgressDialog() {
        if (alertDialog != null && !alertDialog.isShowing) {
            alertDialog.show()
        }
    }

    /*
   * Hide progress dialogue
   * */
    private fun hideProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing) {
            alertDialog.hide()
            alertDialog.dismiss()
        }
    }

    /**
     * Hide dialog on destroy**/
    override fun onDestroy() {
        super.onDestroy()
        if (alertDialog != null && alertDialog.isShowing) {
            alertDialog.hide()
            alertDialog.dismiss()
        }
    }
}
