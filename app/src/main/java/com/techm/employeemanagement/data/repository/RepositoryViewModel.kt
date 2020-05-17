package com.techm.employeemanagement.data.repository


import com.techm.employeemanagement.data.callbacks.ResponseCallback
import com.techm.employeemanagement.data.model.DeleteEmployee
import com.techm.employeemanagement.data.model.CreateEmployeeRequest
import com.techm.employeemanagement.data.model.CreateEmployeeResponse
import com.techm.employeemanagement.data.model.EmployeeDataModel
import com.techm.employeemanagement.data.network.ApiClient
import com.techm.employeemanagement.utils.ResponseStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryViewModel {
    /**
     * method for get employee list
     * */
    fun retrieveEmployeeList(objCallback: ResponseCallback<Any>) {
        var listResponseEmployeeData: EmployeeDataModel;

        val data: Call<EmployeeDataModel>? = ApiClient.build()?.getEmployeeInformation()
        val enqueue = data?.enqueue(object : Callback<EmployeeDataModel> {
            override fun onResponse(
                call: Call<EmployeeDataModel>,
                responseEmployeeData: Response<EmployeeDataModel>
            ) {
                if (responseEmployeeData.isSuccessful) {

                    listResponseEmployeeData = responseEmployeeData.body()!!
                    listResponseEmployeeData.status = ResponseStatus.SUCCESS

                    objCallback.onSuccess(listResponseEmployeeData)
                }
            }

            override fun onFailure(call: Call<EmployeeDataModel>, t: Throwable) {
                objCallback.onError(t.message)
            }
        })
    }

    /**
     *  method for delete employee
     * */
    fun deleteEmployee(employeeID: String, objCallback: ResponseCallback<Any>) {
        var listResponse: DeleteEmployee;

        val data: Call<DeleteEmployee>? = ApiClient.build()?.deleteEmployee(employeeID)
        val enqueue = data?.enqueue(object : Callback<DeleteEmployee> {
            override fun onResponse(
                call: Call<DeleteEmployee>,
                response: Response<DeleteEmployee>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.status.equals("success")) {
                        listResponse = response.body()!!
                        listResponse.apiStatus = ResponseStatus.SUCCESS

                        objCallback.onSuccess(listResponse)
                    } else {
                        objCallback.onError(response.body()?.message)
                    }
                }
            }

            override fun onFailure(call: Call<DeleteEmployee>, t: Throwable) {
                objCallback.onError(t.message)
            }
        })
    }

    /**
     * method for create employee
     * */
    fun createEmployee(
        employeeData: CreateEmployeeRequest,
        objCallback: ResponseCallback<CreateEmployeeResponse>
    ) {
        var listResponse: CreateEmployeeResponse;
        val data: Call<CreateEmployeeResponse>? =
            ApiClient.build()?.createEmployee(employeeData)
        val enqueue = data?.enqueue(object : Callback<CreateEmployeeResponse> {
            override fun onResponse(
                call: Call<CreateEmployeeResponse>,
                response: Response<CreateEmployeeResponse>
            ) {
                if (response.isSuccessful) {

                    listResponse = response.body()!!
                    listResponse.status = ResponseStatus.SUCCESS
                    objCallback.onSuccess(listResponse)
                }
            }

            override fun onFailure(call: Call<CreateEmployeeResponse>, t: Throwable) {
                objCallback.onError(t.message)
            }
        })
    }
}
