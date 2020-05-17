package com.techm.employeemanagement.data.network

import com.techm.employeemanagement.data.model.DeleteEmployee
import com.techm.employeemanagement.data.model.CreateEmployeeRequest
import com.techm.employeemanagement.data.model.CreateEmployeeResponse
import com.techm.employeemanagement.data.model.EmployeeDataModel
import com.techm.employeemanagement.utils.Constant.Companion.createEmployee
import com.techm.employeemanagement.utils.Constant.Companion.deleteEmployee
import com.techm.employeemanagement.utils.Constant.Companion.employeeInformationUrl
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface for calling API methods
 */
interface ApiInterface {
    /**
     * get employee list
     */
    @GET(employeeInformationUrl)
    fun getEmployeeInformation(): Call<EmployeeDataModel>

    /**
     * create employee
     * */
    @POST(createEmployee)
    fun createEmployee(@Body body: CreateEmployeeRequest): Call<CreateEmployeeResponse>

    /**
     * delete employee
     * */
    @DELETE(deleteEmployee)
    fun deleteEmployee(@Path("id") id: String): Call<DeleteEmployee>
}