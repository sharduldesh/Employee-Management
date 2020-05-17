package com.techm.employeemanagement.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techm.employeemanagement.R
import com.techm.employeemanagement.data.model.EmployeeInformation
import kotlinx.android.synthetic.main.item_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adapter class for handling list item
 * */
class EmployeeListAdapter : RecyclerView.Adapter<ViewHolder> {
    private var itemsList = ArrayList<EmployeeInformation>()
    private var items = ArrayList<EmployeeInformation>()
    lateinit var context: Context

    constructor(items: ArrayList<EmployeeInformation>, context: Context?) {
        this.items = items
        if (context != null) {
            this.context = context
        }
        itemsList.addAll(items)
    }

    /**
     * return the size of the list.
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Inflates the item views**/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.item_layout, parent, false)
        )
    }

    /**
     * Binds object in the ArrayList to a view
     */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.employeeName.text = items[position].employee_name
        holder.employeeSalary.text = items[position].employee_salary
        holder.employeeAge.text = items[position].employee_age
    }

    /***
     * remove item at selected position
     * */
    fun removeAt(position: Int) {
        this.items.removeAt(position)
        notifyItemRemoved(position)
    }


    /**
     * get item postion
     * **/
    fun getItemAtPosition(position: Int): EmployeeInformation {
        return this.items[position]
    }

    /**
     * set list to current list from another class
     */
    fun setList(dataInformation: ArrayList<EmployeeInformation>) {
        this.items = dataInformation
        itemsList.addAll(items)
        notifyDataSetChanged()
    }

    /**
     * filter employee list
     * **/
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        items.clear()
        if (charText.isEmpty()) {
            items.addAll(itemsList)
        } else {
            for (wp in itemsList) {
                if (wp.employee_name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    items.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val employeeName: TextView = view.employeeName
    val employeeSalary: TextView = view.employee_salary
    val employeeAge: TextView = view.employee_age
}