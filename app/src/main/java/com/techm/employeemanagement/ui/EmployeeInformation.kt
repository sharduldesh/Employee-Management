package com.techm.employeemanagement.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techm.employeemanagement.R
import com.techm.employeemanagement.ui.adapter.EmployeeListAdapter
import com.techm.employeemanagement.data.model.EmployeeInformation
import com.techm.employeemanagement.utils.*
import com.techm.employeemanagement.ui.viewmodel.ViewModelEmployeeInformation
import kotlinx.android.synthetic.main.fragment_employee_list.*
import kotlinx.android.synthetic.main.fragment_employee_list.view.*


/**
 * Fragment for showing employee list.
 */
class EmployeeInformation : Fragment(), View.OnClickListener {

    private lateinit var empViewModel: ViewModelEmployeeInformation
    private lateinit var empAdapter: EmployeeListAdapter
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private var isSwipeToRefreshCall: Boolean = false
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var employeeList = ArrayList<EmployeeInformation>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_employee_list, container, false)
        activity?.title = getString(R.string.employee_information)
        empViewModel = ViewModelProvider(this).get(ViewModelEmployeeInformation::class.java)
        setupProgressDialog()
        view.searchView.queryHint = getString(R.string.search_hint)


        view.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                newText.let { empAdapter.filter(it) }
                return true
            }
        })

        view.swipeToRefresh.setOnRefreshListener {
            isSwipeToRefreshCall = true
            getEmployeeList(context)
        }

        view.employee_list.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        /**
         * Setting blank adapter for initialize
         */
        empAdapter =
            EmployeeListAdapter(
                ArrayList(),
                context
            )
        linearLayoutManager = LinearLayoutManager(activity)
        view.employee_list.layoutManager = linearLayoutManager
        view.employee_list.adapter = empAdapter

        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            /**function for handling swipe action**/
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var mModelEmployeeInformation =
                    empAdapter.getItemAtPosition(viewHolder.adapterPosition)
                empAdapter.removeAt(viewHolder.adapterPosition)
                empViewModel.deleteEmployee(mModelEmployeeInformation.id)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(view.employee_list)

        /**
         * API Live data observer
         * */
        empViewModel.mEmployeeInformationData.observe(viewLifecycleOwner, Observer {
            if (isSwipeToRefreshCall) {
                isSwipeToRefreshCall = false
                swipeToRefresh.isRefreshing = false
            } else
                hideProgressDialog()

            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    employeeList.clear()
                    employeeList = it.data
                    empAdapter.setList(it.data)
                }
                ResponseStatus.FAIL -> {
                    hideProgressDialog()
                    context?.toast(getString(R.string.serviceFailureError))

                }

                ResponseStatus.LOADING ->
                    showProgressDialog()
            }
        })

        empViewModel.mEmployeeDeleteStatus.observe(viewLifecycleOwner, Observer {
            when (it.apiStatus) {
                ResponseStatus.SUCCESS -> {
                    context?.toast(getString(R.string.employee_deleted))

                }
                ResponseStatus.FAIL -> {
                    context?.toast(getString(R.string.serviceFailureError))
                    //empAdapter.setList(employeeList)
                }
                ResponseStatus.LOADING ->
                    showProgressDialog()
            }
        })
        return view
    }

    /**oncreate activity method*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**onViewCreated activity method*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**onCreateOptionsMenu activity method*/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     *  Handle action bar item clicks here
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_add -> {
                findNavController().navigate(R.id.action_EmployeeInformation_to_AddEmployee)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     *  get data from the viewModel
     */
    private fun getEmployeeList(context: Context?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CheckNetworkConnection.isNetworkConnected()) {
                showProgressDialog()
                empViewModel.getEmployeeList()
                hideProgressDialog()
            } else {
                if (swipeToRefresh.isRefreshing) {
                    swipeToRefresh.isRefreshing = false
                }
                hideProgressDialog()
                context?.toast(getString(R.string.no_internet_connection))
            }
        } else {
            if (CheckNetworkConnection.isNetworkConnectedKitkat()) {
                showProgressDialog()
                empViewModel.getEmployeeList()
                hideProgressDialog()

            } else {
                if (swipeToRefresh.isRefreshing) {
                    swipeToRefresh.isRefreshing = false
                }
                hideProgressDialog()
                context?.toast(getString(R.string.no_internet_connection))
            }
        }
    }

    /**
     * Showing dialog when api call
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupProgressDialog() {
        builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_dialog)
        dialog = builder.create()
    }

    private fun showProgressDialog() {
        if (dialog != null && !dialog.isShowing) {
            dialog.show()
        }
    }

    private fun hideProgressDialog() {
        if (dialog != null && dialog.isShowing) {
            dialog.hide()
            dialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null && dialog.isShowing) {
            dialog.hide()
            dialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        getEmployeeList(context)
    }

    override fun onClick(v: View?) {

    }

}
