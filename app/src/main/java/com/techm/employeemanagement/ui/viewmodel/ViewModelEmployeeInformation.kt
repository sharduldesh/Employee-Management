package com.techm.employeemanagement.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.techm.employeemanagement.data.callbacks.ResponseCallback
import com.techm.employeemanagement.data.model.DeleteEmployee
import com.techm.employeemanagement.data.model.EmployeeDataModel
import com.techm.employeemanagement.data.repository.RepositoryViewModel
import com.techm.employeemanagement.utils.ResponseStatus
import org.jetbrains.annotations.NotNull

class ViewModelEmployeeInformation(@NotNull application: Application) :
    AndroidViewModel(application),
    ResponseCallback<Any> {
    private var repositoryViewModel: RepositoryViewModel =
        RepositoryViewModel()
    var mEmployeeInformationData: MutableLiveData<EmployeeDataModel> =
        MutableLiveData<EmployeeDataModel>()

    var mEmployeeDeleteStatus: MutableLiveData<DeleteEmployee> =
        MutableLiveData<DeleteEmployee>()

    /**
     * one time initialize
     * */
    init {
        mEmployeeInformationData.value =
            EmployeeDataModel(
                ArrayList(),
                "",
                ResponseStatus.LOADING
            )
        repositoryViewModel.retrieveEmployeeList(this)
    }

    /**
     * Calling API
     */
    fun getEmployeeList() {
        repositoryViewModel.retrieveEmployeeList(this)
    }

    /**
     * Delete employee API
     * */
    fun deleteEmployee(employeeId: String) {
        repositoryViewModel.deleteEmployee(employeeId, this)
    }

    /**
     * API success response
     * */

    override fun onSuccess(data: Any) {
        if (data is EmployeeDataModel) {
            mEmployeeInformationData.value = data
        } else if (data is DeleteEmployee) {
            mEmployeeDeleteStatus.value = data
        }
    }

    /**
     * API failure response
     * */
    override fun onError(error: String?) {
        mEmployeeInformationData.value =
            EmployeeDataModel(
                ArrayList(),
                error.toString(),
                ResponseStatus.FAIL
            )
    }


}