package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.util.Log
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context) : BaseRepository(context) {
    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun listAll(listener: APIListener<List<TaskModel>>) {
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.list()
        executeCall(call, listener)
    }

    fun listNext(listener: APIListener<List<TaskModel>>) {
        val call = remote.listNext()
        executeCall(call, listener)
    }

    fun listOverdue(listener: APIListener<List<TaskModel>>) {
        val call = remote.listOverdue()
        executeCall(call, listener)
    }

    fun load(id: Int, listener: APIListener<TaskModel>) {
        val call = remote.load(id)
        executeCall(call, listener)
    }

    fun create(task: TaskModel, listener: APIListener<Boolean>) {
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }

    fun update(task: TaskModel, listener: APIListener<Boolean>) {
        val call = remote.update(task.id, task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }


    fun delete(id: Int, listener: APIListener<Boolean>) {
        val call = remote.delete(id)
        executeCall(call, listener)
    }

    fun complete(id: Int, listener: APIListener<Boolean>) {
        val call = remote.complete(id)
        executeCall(call, listener)
    }

    fun undo(id: Int, listener: APIListener<Boolean>) {
        val call = remote.undo(id)
        executeCall(call, listener)
    }

}