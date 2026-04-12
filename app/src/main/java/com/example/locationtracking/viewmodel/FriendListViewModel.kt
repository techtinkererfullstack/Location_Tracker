package com.example.locationtracking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.locationtracking.AppUsers
import com.example.locationtracking.repo.UserRepository

class FriendListViewModel(private val repo: UserRepository): ViewModel() {
    private val _userList = MutableLiveData<List<AppUsers>>()
    val userList: LiveData<List<AppUsers>> get() = _userList

    fun fetchUsers() {
        repo.getAllUsers { users ->
            _userList.value = users
        }
    }
}