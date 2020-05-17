package com.techm.employeemanagement.data.model

import com.techm.employeemanagement.utils.ResponseStatus
/*
* Data class for delete employee response
* */
class DeleteEmployee(var status:String, var apiStatus:ResponseStatus, var message:String)