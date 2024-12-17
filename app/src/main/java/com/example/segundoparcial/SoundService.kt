package com.example.segundoparcial

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class SoundService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        // Manejar la acción que llega
        when (action) {
            "PAUSE_SOUND" -> pauseSound()
            "RESUME_SOUND" -> resumeSound()
            else -> startSound()
        }

        return START_STICKY
    }

    private fun startSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sonido_olas_de_mar)
            mediaPlayer?.isLooping = true // Repetir el sonido
            mediaPlayer?.start()
        }
    }

    private fun pauseSound() {
        mediaPlayer?.pause()  // Pausa la reproducción
    }

    private fun resumeSound() {
        mediaPlayer?.start()  // Reanuda la reproducción
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

}
