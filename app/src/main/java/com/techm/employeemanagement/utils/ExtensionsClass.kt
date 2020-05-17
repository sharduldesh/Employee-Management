package com.techm.employeemanagement.utils

import android.content.Context
import android.widget.Toast

/**
 *  extension function
 * */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()}