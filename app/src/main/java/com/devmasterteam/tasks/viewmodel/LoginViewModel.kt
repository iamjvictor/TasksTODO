package com.devmasterteam.tasks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {


    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val personRepository = PersonRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser


    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        Log.d("LoginViewModel", "Iniciando login com email: $email")

        personRepository.login(email, password, object : APIListener<PersonModel> {
            override fun onSuccess(result: PersonModel) {
                Log.d("LoginViewModel", "Login bem-sucedido")

                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                RetrofitClient.addHeaders(result.token, result.personKey)

                _login.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                Log.e("LoginViewModel", "Erro no login: $message")
                _login.value = ValidationModel(message)
            }
        })
    }


    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeaders(token, personKey)

        val logged = (token != "" && personKey != "")
        _loggedUser.value = logged

        if (!logged){
            priorityRepository.list(object : APIListener<List<PriorityModel>>{
                override fun onSuccess(result: List<PriorityModel>) {
                    priorityRepository.save(result) // Salva a lista completa no banco de dados

                    // Verifica o conteúdo do banco após salvar
                    val savedPriorities = priorityRepository.list()
                    savedPriorities.forEach { priority ->
                        Log.d("PriorityRepository", "Database Check - ID: ${priority.id}, Description: ${priority.description}")
                    }
                }
                override fun onFailure(message: String) {
                    val s = ""
                }
            })
        }

    }

}