package com.techm.employeemanagement.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.techm.employeemanagement.data.callbacks.ResponseCallback
import com.techm.employeemanagement.data.model.CreateEmployeeRequest
import com.techm.employeemanagement.data.model.CreateEmployeeResponse
import com.techm.employeemanagement.data.repository.RepositoryViewModel
import com.techm.employeemanagement.utils.ResponseStatus
import org.jetbrains.annotations.NotNull

class ViewModelAddEmployee(@NotNull application: Application) :
    AndroidViewModel(application),
    ResponseCallback<CreateEmployeeResponse> {
    private var repositoryViewModel: RepositoryViewModel =
        RepositoryViewModel()
    var createEmployeeResponse: MutableLiveData<CreateEmployeeResponse> =
        MutableLiveData<CreateEmployeeResponse>()
    /**
     * Calling API
     */
    fun createEmployee(employeeInformation: CreateEmployeeRequest) {
        repositoryViewModel.createEmployee(employeeInformation, this)
    }

    /**
     * API success response
     * */

    override fun onSuccess(data: CreateEmployeeResponse) {
        createEmployeeResponse.value = data
    }

    /**
     * API failure response
     * */
    override fun onError(error: String?) {
        createEmployeeResponse.value =
            CreateEmployeeResponse(
                error.toString(),
                ResponseStatus.FAIL,
                CreateEmployeeRequest(
                    "",
                    "",
                    ""
                )
            )
    }


}