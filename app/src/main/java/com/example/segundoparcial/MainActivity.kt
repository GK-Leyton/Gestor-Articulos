package com.example.segundoparcial

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import modelosDatos.URL
import org.json.JSONArray
import popups.PopupHelperAutores
import popups.PopupHelperVerificarUsuarioParaCambioDeContrasena

class MainActivity : AppCompatActivity() {
    private lateinit var hiddenGifImageView: ImageView
    private lateinit var txtUsuario: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var btnEntrar: Button
    private lateinit var btnCrearUsuario: ImageButton
    private lateinit var txtCambioContrasena: TextView
    private lateinit var btnDesarrolladores: ImageView

    var url = URL.BASE_URL+"login.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /////////////////////////////
        txtUsuario = findViewById(R.id.txtUsuario)
        txtContrasena = findViewById(R.id.txtContrasena)
        txtCambioContrasena = findViewById(R.id.txtCambiarContrasena)
        btnDesarrolladores = findViewById(R.id.btnDesarrolladores)
        ///////////////////////////

        val backgroundGifImageView: ImageView = findViewById(R.id.background_gif)
        Glide.with(this)
            .asGif()
            .load(R.drawable.oceano_de_nubes)
            .override(410, 750)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(backgroundGifImageView)


        hiddenGifImageView = findViewById(R.id.hidden_gif)
        Glide.with(this)
            .asGif()
            .load(R.drawable.ballena_1)
            .override(410, 710)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(hiddenGifImageView)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_desarrolladores)
            .override(410, 710)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(btnDesarrolladores)


        btnEntrar = findViewById(R.id.btnEntrar)
        btnCrearUsuario = findViewById(R.id.btnAgregarUsuario)


        btnEntrar.setOnClickListener {

            login()

        }
        btnCrearUsuario.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "crearUsuario")
            }
            startActivity(intent)
        }
        txtCambioContrasena.setOnClickListener {
            /*val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "cambiarContrasena")
            }
            startActivity(intent)*/

            val popupHelper = PopupHelperVerificarUsuarioParaCambioDeContrasena(this)
            popupHelper.showPopup(txtCambioContrasena)

        }
        btnDesarrolladores.setOnClickListener {
            val popupHelper = PopupHelperAutores(this)
            popupHelper.showPopup(btnDesarrolladores)
        }









        val rootLayout = findViewById<ConstraintLayout>(R.id.main_constraint_layout)
        rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootLayout.getWindowVisibleDisplayFrame(r)
            val heightDiff = rootLayout.height - (r.bottom - r.top)


            val keyboardThreshold = 300

            if (heightDiff > keyboardThreshold) {
                rootLayout.setPadding(0, 0, 0, 500)
            } else {
                rootLayout.setPadding(0, 0, 0, 0)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_constraint_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    fun login() {
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                when (response) {
                    "ERROR 1" -> {
                        Toast.makeText(applicationContext, "Se deben llenar todos los campos", Toast.LENGTH_SHORT).show()
                    }
                    "ERROR 2" -> {
                        Toast.makeText(applicationContext, "Alguno de los campos es erroneo", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                        btnEntrar.setClickable(false)
                        hiddenGifImageView.visibility = ImageView.VISIBLE
                        hiddenGifImageView.postDelayed({
                            hiddenGifImageView.visibility = ImageView.INVISIBLE
                        }, 1450)

                        val jsonArray = JSONArray(response)
                        //Toast.makeText(applicationContext, "Tipo Usuario " + jsonArray.getJSONObject(0).getString("tipo_usuario"), Toast.LENGTH_SHORT).show()
                        val tipoUsuario = jsonArray.getJSONObject(0).getString("tipo_usuario")
                        val idUsuario = jsonArray.getJSONObject(0).getString("id_usuario")
                        //Toast.makeText(applicationContext, "ID Usuario " + idUsuario, Toast.LENGTH_SHORT).show()
                        //Toast.makeText(applicationContext, "Bienvenido", Toast.LENGTH_SHORT).show()
                        Thread {
                            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                                putExtra("proximaPagina", "menuPrincipal")
                                putExtra("tipoUsuario" , tipoUsuario)
                                putExtra("idUsuario" , idUsuario)
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                            }
                            Thread.sleep(1450)
                            startActivity(intent)
                        }.start()
                    }
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(applicationContext, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }) {

            override fun getParams(): Map<String, String> {
                return mapOf(
                    "usuario" to txtUsuario.text.toString(),
                    "contrasena" to txtContrasena.text.toString()
                )
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    override fun onStop() {
        super.onStop()
        // Detener el servicio de sonido cuando la app va al segundo plano
        stopService(Intent(this, SoundService::class.java))
    }

    override fun onStart() {
        super.onStart()
        // Reiniciar el servicio cuando la app vuelve al primer plano
        startService(Intent(this, SoundService::class.java))
    }

    override fun onPause() {
        super.onPause()
        // Enviar la acción PAUSE_SOUND al servicio para pausar el sonido
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.action = "PAUSE_SOUND"
        startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        // Enviar la acción RESUME_SOUND al servicio para reanudar el sonido
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.action = "RESUME_SOUND"
        startService(serviceIntent)
    }
    override fun onBackPressed() {
    }

}
