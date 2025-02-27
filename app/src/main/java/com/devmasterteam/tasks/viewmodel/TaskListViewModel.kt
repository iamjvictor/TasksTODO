package com.devmasterteam.tasks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application, ) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var taskFilter = 0



    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    fun list(filter: Int){
        taskFilter = filter
        val listener = object : APIListener<List<TaskModel>>{
            override fun onSuccess(result: List<TaskModel>) {

                result.forEach{
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }
                _tasks.value = result

            }

            override fun onFailure(message: String) {
                Log.d("TaslList result", "Error: $message")
            }

        }

        if (filter == TaskConstants.FILTER.ALL){
            taskRepository.listAll(listener)
            Log.d("AllTasksFragment", "Task Filter: $taskFilter")

        }else if(filter == TaskConstants.FILTER.NEXT){
            taskRepository.listNext(listener)
            Log.d("AllTasksFragment", "Task Filter: $taskFilter")

        }else{
            taskRepository.listOverdue(listener)
            Log.d("AllTasksFragment", "Task Filter: $taskFilter")

        }
    }

    fun delete(id: Int){
        taskRepository.delete(id, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }

        })


    }

    fun complete (id: Int){
        taskRepository.complete(id, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }

        })
    }

    fun undo (id: Int){
        taskRepository.undo(id, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }

        })
    }


}