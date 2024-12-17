package com.example.segundoparcial

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
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

class CrearUsuario : AppCompatActivity() {
    private lateinit var hiddenGifImageView: ImageView
    private lateinit var txtNombreUsuario: EditText
    private lateinit var txtUsuario: EditText
    private lateinit var spinnerPreguntaSegura: Spinner
    private lateinit var txtRespuesta: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var txtConfirmarContrasena: EditText
    private lateinit var btnCrearUsuario: Button

    var url = URL.BASE_URL+"PreguntasRecuperacion/ObtenerPreguntasRecuperacion.php"
    var url2 = URL.BASE_URL+"Usuario/CrearUsuario.php"
    var url3 = URL.BASE_URL+"Usuario/ObtenerUsuarios.php"

    private val preguntasList = mutableListOf<PreguntaRecuperacion>()
    private val usuariosList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_crear_usuario)

        // Configurar GIFs con Glide
        val backgroundGifImageView: ImageView = findViewById(R.id.background_gif)
        Glide.with(this)
            .asGif()
            .load(R.drawable.oceano_de_nubes)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(backgroundGifImageView)



        // Referencias a EditText
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario)
        txtUsuario = findViewById(R.id.txtUsuario)
        spinnerPreguntaSegura = findViewById(R.id.spinnerPreguntaSegura)
        txtRespuesta = findViewById(R.id.txtPreguntaSegura)
        txtContrasena = findViewById(R.id.txtContrasena)
        txtConfirmarContrasena = findViewById(R.id.txtConfirmarContrasena)
        btnCrearUsuario = findViewById(R.id.btnCrearUsuario)

        obtenerPreguntas(spinnerPreguntaSegura)

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
        ObtenerTodosLosUsuarios()

        btnCrearUsuario.setOnClickListener {
            val nombreUsuario = txtNombreUsuario.text.toString()
            val usuario = txtUsuario.text.toString()
            val pregunta = spinnerPreguntaSegura.selectedItem.toString()
            val idPregunta = preguntasList.find { it.pregunta == pregunta }?.id ?: ""
            val respuesta = txtRespuesta.text.toString()
            val contrasena = txtContrasena.text.toString()
            val confirmarContrasena = txtConfirmarContrasena.text.toString()

            if (nombreUsuario.isEmpty() || usuario.isEmpty() || spinnerPreguntaSegura.selectedItemPosition == 0 || respuesta.isEmpty() ||
                contrasena.isEmpty() || confirmarContrasena.isEmpty()
            ) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (contrasena != confirmarContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{



                //Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    // Lógica para el botón "Aceptar"
                    var isUserValid = true;
                    for (usu in usuariosList) {
                        if(usu == usuario) {
                            isUserValid = false;
                        }
                    }
                    if(isUserValid){
                        crearUsuario(usuario , nombreUsuario , contrasena , "1" , idPregunta , respuesta)
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "login")
                        }
                        startActivity(intent)
                    }else{
                        txtUsuario.error = "El usuario ya existe"
                    }

                    dialog.dismiss()
                }

                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    // Lógica para el botón "Cancelar"
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()



            }


        }
    }

    fun obtenerPreguntas(spinner: Spinner) {
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
                            // Limpiar la lista antes de agregar los temas
                            preguntasList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val preguntaRecuperacion = jsonArray.getJSONObject(i)
                                val id_pregunta = preguntaRecuperacion.getString("id_pregunta").toInt()
                                val pregunta = preguntaRecuperacion.getString("pregunta")


                                // Agregar a la lista de temas
                                preguntasList.add(
                                    PreguntaRecuperacion(
                                        id_pregunta.toString(),
                                        pregunta
                                    )
                                )
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + preguntasList.map { it.pregunta }
                            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

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
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(this).add(stringRequest)
    }
    fun crearUsuario( usuario : String , nombre : String , contrasena : String , tipoUsuario : String , idPreguntaSegura : String , respuesta : String) {
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
                               // else -> Toast.makeText(this, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                       // else -> Toast.makeText(this, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                    //Toast.makeText(this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Error en la solicitud
               // Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["usuario"] = usuario
                params["nombre"] = nombre
                params["contrasena"] = contrasena
                params["tipoUsuario"] = tipoUsuario
                params["idPreguntaSegura"] = idPreguntaSegura
                params["respuesta"] = respuesta

                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(this).add(stringRequest)
    }


    fun ObtenerTodosLosUsuarios() {

        val stringRequest = object : StringRequest(
            Request.Method.POST, url3,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        // Toast.makeText(context , "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                    usuariosList.clear()

                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val Usuario = jsonArray.getJSONObject(i)
                        val usuario = Usuario.getString("usuario")

                        usuariosList.add(usuario)
                    }


                } catch (e: Exception) {
                    //  Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Añadir la solicitud a la cola de solicitudes
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



