package com.devmasterteam.tasks.service.constants

/**
 * Constantes usadas na aplicação
 */
class TaskConstants private constructor() {

    // SharedPreferences
    object SHARED {
        const val TOKEN_KEY = "GRXd/el4tZ2xbJOne4IcT8iWhRedRjXufkBHRZnuPNZZr1g69/8DqQ=="
        const val PERSON_KEY = "xCFBiyvL4z1VYg8WbF5PenNiUIPYQTlB84iEYeECFVU="
        const val PERSON_NAME = "João Victor"
    }

    // Requisições API
    object HEADER {
        const val TOKEN = "Token"
        const val PERSON_KEY = "PersonKey"
    }

    object HTTP {
        const val SUCCESS = 200
    }

    object BUNDLE {
        const val TASKID = "taskid"
        const val TASKFILTER = "taskFilter"
    }

    object FILTER {
        const val ALL = 0
        const val NEXT = 1
        const val EXPIRED = 2
    }

}