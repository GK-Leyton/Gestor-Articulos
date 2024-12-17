package com.example.segundoparcial

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import modelosDatos.PreguntaRecuperacion
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap
import java.util.UUID

class CambiarContraseña : AppCompatActivity() {
    private lateinit var hiddenGifImageView: ImageView
    private lateinit var txtCadenaRandom: TextView
    private lateinit var btnRecargar: ImageButton
    private lateinit var txtInsertarCadenaRandom: EditText
    private lateinit var txtPreguntaSegura: TextView
    private lateinit var textView8 : TextView
    private lateinit var textView5 : TextView
    private lateinit var textView6 : TextView
    private lateinit var txtRespuestaSegura: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var txtConfirmarContrasena: EditText
    private lateinit var btnCambiarContrasena: Button
    private var respuestaSegura = "";


    var url = URL.BASE_URL+"Usuario/ObtenerPreguntaYRespuestaSeguraPorIdUsuario.php"
    var url2 = URL.BASE_URL+"Usuario/CambiarContraseñaPorIdUsuario.php"

    private val preguntasList = mutableListOf<PreguntaRecuperacion>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_cambiar_contrasena)

        val backgroundGifImageView: ImageView = findViewById(R.id.background_gif)
        Glide.with(this)
            .asGif()
            .load(R.drawable.oceano_de_nubes)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(backgroundGifImageView)


        // Referencias a EditText
        txtCadenaRandom = findViewById(R.id.txtCadenaRandom)
        btnRecargar = findViewById(R.id.btnRecargar)
        txtInsertarCadenaRandom = findViewById(R.id.txtInsertarCadenaRandom)
        txtPreguntaSegura = findViewById(R.id.txtPreguntaSegura)
        txtRespuestaSegura = findViewById(R.id.txtRespuestaSegura)
        txtContrasena = findViewById(R.id.txtContrasena)
        txtConfirmarContrasena = findViewById(R.id.txtConfirmarContrasena)
        btnCambiarContrasena = findViewById(R.id.btnCrearUsuario)
        textView8 = findViewById(R.id.textView8)
        textView5 = findViewById(R.id.textView5)
        textView6 = findViewById(R.id.textView6)

        val idUsuario = intent.getStringExtra("idUsuario")
        //Toast.makeText(this, "ID Usuario: $idUsuario", Toast.LENGTH_SHORT).show()

        txtPreguntaSegura.visibility = TextView.INVISIBLE
        txtRespuestaSegura.visibility = EditText.INVISIBLE
        txtContrasena.visibility = EditText.INVISIBLE
        txtConfirmarContrasena.visibility = EditText.INVISIBLE
        textView8.visibility = TextView.INVISIBLE
        textView5.visibility = TextView.INVISIBLE
        textView6.visibility = TextView.INVISIBLE


        var cadenaRandom = UUID.randomUUID().toString()
        var cadenaLimitada = cadenaRandom.substring(0, 8)

        txtCadenaRandom.text = cadenaLimitada
        btnRecargar.setOnClickListener {
            cadenaRandom = UUID.randomUUID().toString()
            cadenaLimitada = cadenaRandom.substring(0, 8)
            txtCadenaRandom.text = cadenaLimitada
        }

        // Manejo del botón volver
        val btnVolver = findViewById<ImageButton>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "login")
            }
            startActivity(intent)
        }

        // Manejo de padding cuando el teclado aparece
        val rootLayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main_constraint_layout)
        /*rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootLayout.getWindowVisibleDisplayFrame(r)
            val heightDiff = rootLayout.height - (r.bottom - r.top)
            if (heightDiff > 300) {
                rootLayout.setPadding(0, 0, 0, 500)
            } else {
                rootLayout.setPadding(0, 0, 0, 0)
            }
        }*/

        // Manejo de *WindowInsets* para ajuste visual
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Validar datos al crear usuario
        btnCambiarContrasena.setOnClickListener {

        if(txtContrasena.text.toString().isNotEmpty() && txtConfirmarContrasena.text.toString().isNotEmpty() && (txtConfirmarContrasena.text.toString() == txtContrasena.text.toString())){

            cambiarContrasena(idUsuario.toString(), txtContrasena.text.toString())
            //Toast.makeText(this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "login")
            }
            startActivity(intent)
        }else{
            //Toast.makeText(this, "Las contraseñas no coinciden o no rellenaste los campos", Toast.LENGTH_SHORT).show()
        }




        }






        txtInsertarCadenaRandom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Mientras el texto está cambiando
                if (s.toString() == cadenaLimitada) {
                    println("El texto coincide con la cadena objetivo")
                    txtPreguntaSegura.visibility = TextView.VISIBLE
                    txtRespuestaSegura.visibility = EditText.VISIBLE
                    textView8.visibility = TextView.VISIBLE
                    txtInsertarCadenaRandom.isEnabled = false;
                    obtenerPreguntas(idUsuario.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Después de que el texto ha cambiado
            }
        })

        txtRespuestaSegura.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Mientras el texto está cambiando
                if (s.toString() == respuestaSegura) {
                    println("El texto coincide con la cadena objetivo")
                    txtContrasena.visibility = EditText.VISIBLE
                    txtConfirmarContrasena.visibility = EditText.VISIBLE
                    textView5.visibility = TextView.VISIBLE
                    textView6.visibility = TextView.VISIBLE
                    txtRespuestaSegura.isEnabled = false;
                    obtenerPreguntas(idUsuario.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Después de que el texto ha cambiado
            }
        })

    }



    fun obtenerPreguntas(idUsuario: String) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        //Toast.makeText(this, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            //Toast.makeText(this, "Id usuario en la funcion " + idUsuario, Toast.LENGTH_SHORT).show()
                            for (i in 0 until jsonArray.length()) {
                                val preguntaRecuperacion = jsonArray.getJSONObject(i)
                                val respuesta = preguntaRecuperacion.getString("respuesta")
                                val pregunta = preguntaRecuperacion.getString("pregunta")

                                txtPreguntaSegura.setText(pregunta)
                                respuestaSegura = respuesta.toString()

                            }

                            // Actualizar el adaptador del Spinner
                            //Toast.makeText(this, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            //Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idUsuario"] = idUsuario
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(this).add(stringRequest)
    }
    fun cambiarContrasena(idUsuario: String , nuevaContrasena: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url2,
            Response.Listener<String> { response ->
                try {
                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.getInt("code")
                            val message = jsonResponse.getString("message")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                               // 1 -> Toast.makeText(this, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                               // 2 -> Toast.makeText(this, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                                //else -> Toast.makeText(this, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                        //else -> Toast.makeText(this, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                    //Toast.makeText(this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Error en la solicitud
                //Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idUsuario"] = idUsuario
                params["nuevaContrasena"] = nuevaContrasena


                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(this).add(stringRequest)
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



