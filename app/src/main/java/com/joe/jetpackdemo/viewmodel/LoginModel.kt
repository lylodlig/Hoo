package com.joe.jetpackdemo.viewmodel

import android.text.Editable
import androidx.lifecycle.*
import androidx.work.ListenableWorker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.joe.jetpackdemo.common.BaseApplication
import com.joe.jetpackdemo.common.listener.SimpleWatcher
import com.joe.jetpackdemo.db.RepositoryProvider
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.data.User
import com.joe.jetpackdemo.db.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginModel constructor(
    private val repository: UserRepository
) : ViewModel() {

    val n = MutableLiveData<String>("")
    val p = MutableLiveData<String>("")
    //lateinit var lifecycleOwner: LifecycleOwner

    /**
     * 用户名改变回调的函数
     */
    fun onNameChanged(s: CharSequence) {
        //n.set(s.toString())
        n.value = s.toString()
    }

    /**
     * 密码改变的回调函数
     */
    fun onPwdChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //p.set(s.toString())
        p.value = s.toString()
    }

    fun login(): LiveData<User?>? {
        val pwd = p.value!!
        val account = n.value!!
        return repository.login(account, pwd)
    }

    // SimpleWatcher 是简化了的TextWatcher
    val nameWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)

            n.value = s.toString()
        }
    }

    val pwdWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)

            //p.set(s.toString())
            p.value = s.toString()
        }
    }

    /**
     * 第一次启动的时候调用
     */
    fun onFirstLaunch():String {
        val context = BaseApplication.context
        context.assets.open("shoes.json").use {
            JsonReader(it.reader()).use {
                val shoeType = object : TypeToken<List<Shoe>>() {}.type
                val shoeList: List<Shoe> = Gson().fromJson(it, shoeType)

                val shoeDao = RepositoryProvider.providerShoeRepository(context)
                shoeDao.insertShoes(shoeList)
                for (i in 0..2) {
                    for (shoe in shoeList) {
                        shoe.id += shoeList.size
                    }
                    shoeDao.insertShoes(shoeList)
                }
            }

        }
        return "初始化数据成功！"
    }
}