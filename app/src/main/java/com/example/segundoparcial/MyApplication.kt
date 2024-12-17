package com.example.segundoparcial

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Verifica si el servicio ya está corriendo antes de iniciarlo
        if (!isServiceRunning(SoundService::class.java)) {
            startService(Intent(applicationContext, SoundService::class.java))
        }
    }

    // Función para verificar si el servicio ya está en ejecución
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)
        for (service in services) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}

