package com.techm.employeemanagement.data.callbacks

/**Common Interface for handling callbacks*/

interface ResponseCallback<T>
{
    fun onSuccess(data: T)
    fun onError(error:String?)
}