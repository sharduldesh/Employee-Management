package com.techm.employeemanagement.viewmodel

import org.junit.Assert.*

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.techm.employeemanagement.data.network.ApiInterface
import com.techm.employeemanagement.data.model.EmployeeDataModel
import com.techm.employeemanagement.data.repository.RepositoryViewModel
import com.techm.employeemanagement.ui.viewmodel.ViewModelEmployeeInformation
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Spy

import org.mockito.*
import org.mockito.Mockito.*
import java.net.SocketException

@RunWith(JUnit4::class)
class ViewModelEmployeeInformationTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Spy
    lateinit var mApiInterface: ApiInterface

    lateinit var mViewModelEmployeeInformation: ViewModelEmployeeInformation

    lateinit var mRepositoryViewModel: RepositoryViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.mRepositoryViewModel =
            RepositoryViewModel()

        val application = mock(Application::class.java)
        this.mViewModelEmployeeInformation =
            ViewModelEmployeeInformation(
                application
            )
    }

    //Unit test for checking successful API call
    @Test
    fun getEmployeeListSuccessTest() {
        `when`(this.mApiInterface.getEmployeeInformation()).thenAnswer {
            return@thenAnswer Maybe.just(ArgumentMatchers.any<RepositoryViewModel>())
        }

        val observer = mock(Observer::class.java) as Observer<EmployeeDataModel>
        this.mViewModelEmployeeInformation.mEmployeeInformationData.observeForever(observer)

        this.mViewModelEmployeeInformation.getEmployeeList()

        assertNotNull(this.mViewModelEmployeeInformation.mEmployeeInformationData.value)
    }

    //Unit test for checking error condition handled for API call
    @Test
    fun getEmployeeListErrorTest() {
        `when`(this.mApiInterface.getEmployeeInformation()).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException("No network here"))
        }

        val observer = mock(Observer::class.java) as Observer<EmployeeDataModel>
        this.mViewModelEmployeeInformation.mEmployeeInformationData.observeForever(observer)

        this.mViewModelEmployeeInformation.getEmployeeList()

        assertNotNull(this.mViewModelEmployeeInformation.mEmployeeInformationData.value)
        assert(this.mViewModelEmployeeInformation.mEmployeeInformationData.value?.errorMessage is String)
    }
}