package com.example.randomuserapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp.domain.model.User
import com.example.randomuserapp.domain.usecase.GetUsersUseCase
import com.example.randomuserapp.utils.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchUsers(count: Int) {
        if (networkUtil.isNetworkAvailable()) {
            _error.value = ""
            viewModelScope.launch {
                val result = getUsersUseCase(count)
                result.onSuccess {
                    _users.value = it
                }.onFailure {
                    _error.value = "Failed to fetch users: ${it.message}"
                }
            }
        }
        else {
            _error.value = "No internet connection"
        }

    }
}