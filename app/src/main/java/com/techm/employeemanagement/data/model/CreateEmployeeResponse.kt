package com.techm.employeemanagement.data.model

import com.techm.employeemanagement.utils.ResponseStatus

/**
 * Data class for add employee API response
 * */
class CreateEmployeeResponse(
    var error: String,
    var status: ResponseStatus,
    var data: CreateEmployeeRequest
)