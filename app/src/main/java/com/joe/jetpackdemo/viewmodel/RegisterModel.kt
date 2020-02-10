package com.joe.jetpackdemo.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joe.jetpackdemo.db.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterModel constructor(
    private val repository: UserRepository
) : ViewModel() {

    var n = ""
    val p = MutableLiveData<String>("")
    val mail = MutableLiveData<String>("")
    var isEnable = ObservableBoolean(false)
        set(value) {
            field=value
            field.notifyChange()
        }

    /**
     * 用户名改变回调的函数
     */
    fun onNameChanged(s: CharSequence) {
        //n.set(s.toString())
        n = s.toString()
        isEnable.set(n!!.isNotBlank() && p.value!!.isNotBlank() && mail.value!!.isNotBlank())
    }

    /**
     * 邮箱改变的时候
     */
    fun onEmailChanged(s: CharSequence) {
        //n.set(s.toString())
        mail.value = s.toString()
        isEnable.set(n!!.isNotBlank() && p.value!!.isNotBlank() && mail.value!!.isNotBlank())
    }

    /**
     * 密码改变的回调函数
     */
    fun onPwdChanged(s: CharSequence) {
        //p.set(s.toString())
        p.value = s.toString()
        isEnable.set(n!!.isNotBlank() && p.value!!.isNotBlank() && mail.value!!.isNotBlank())
    }

    fun register() {
        viewModelScope.launch {
            repository.register(mail.value!!, n!!, p.value!!)
        }
    }
}