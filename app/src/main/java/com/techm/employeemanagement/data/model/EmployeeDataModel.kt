package com.techm.employeemanagement.data.model

import com.techm.employeemanagement.utils.ResponseStatus

/**
 * Data class for employee list response
 * */
class EmployeeDataModel(
    var data: ArrayList<EmployeeInformation>,
    var errorMessage: String = "",
    var status: ResponseStatus
)