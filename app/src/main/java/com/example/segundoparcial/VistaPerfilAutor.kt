package com.example.segundoparcial

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import popups.PopupHelperEditarErudito
import kotlin.random.Random

class VistaPerfilAutor : AppCompatActivity() {

    private var url = URL.BASE_URL+"Autores/ObtenerLaInformacionDeUnAutor.php"
    private var url2 = URL.BASE_URL+"Autores/EliminarAutores.php"

    private lateinit var txtNombreAutor: TextView
    private lateinit var txtCorreoAutor: TextView
    private lateinit var txtPublicaciones: TextView
    private lateinit var txtCentroDeInvestigador: TextView
    private lateinit var imgAutor: ImageView
    private lateinit var btnEditar: ImageView
    private lateinit var btnEliminar: ImageView
    private lateinit var layoutEliminar: ConstraintLayout

    private var idUsuarioGlobal = ""
    private var tipoUsuarioGlobal = ""
    private var cantidadPublicaciones = ""
    private var idAutorGlobal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val intentSonido = Intent(this, SoundService::class.java)
        startService(intentSonido)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_perfil_autor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutPrincipal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
        val random = Random.nextInt(1, 5)
        when (random) {
            1 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_1)
            2 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_2)
            3 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_3)
            4 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_4)

        }


        val idAutor = intent.getStringExtra("idAutor")
        val idUsuario = intent.getStringExtra("idUsuario")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")

        idUsuarioGlobal = idUsuario.toString()
        tipoUsuarioGlobal = tipoUsuario.toString()
        idAutorGlobal = idAutor.toString()

        val btnVoler = findViewById<ImageView>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val layoutPerfil = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layoutPerfilAutor)

        txtNombreAutor = findViewById(R.id.txtNombreAutor)
        txtCorreoAutor = findViewById(R.id.txtCorreoAutor)
        txtPublicaciones = findViewById(R.id.txtCantidadPublicaciones)
        txtCentroDeInvestigador = findViewById(R.id.txtCentroDeInvestigador)
        imgAutor = findViewById(R.id.imgAutor)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)
        layoutEliminar = findViewById(R.id.layoutEliminar)

        if(tipoUsuario == "1"){
            btnEditar.visibility = ImageButton.INVISIBLE
        }

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_editar)
            .override(410, 710)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(btnEditar)

        ObtenerInformacionDelAutor(idAutor.toString().toInt())

        btnVoler.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "autores")
                putExtra("tipoUsuario" , tipoUsuario)
                putExtra("idUsuario" , idUsuario)
            }
            startActivity(intent)
        }
        btnOjo.setOnClickListener {
            if (layoutPerfil.visibility == WebView.VISIBLE ) {

                layoutPerfil.visibility = WebView.GONE


            } else {
                layoutPerfil.visibility = WebView.VISIBLE
            }

        }
        btnEditar.setOnClickListener {
            val popupHelper = PopupHelperEditarErudito(this , idAutor.toString() , idUsuario.toString() , tipoUsuario.toString())
            popupHelper.showPopup(btnEditar)
        }


    }

    fun setVisibleOrInvisibleBtnEliminar(){
        val isDeleteable = if (cantidadPublicaciones.toInt() == 0) true else false

        if (isDeleteable) {

            btnEliminar.visibility = View.VISIBLE
            Glide.with(this)
                .asGif()
                .load(R.drawable.gif_eliminar)
                .override(410, 710)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(btnEliminar)

            for (i in 0 until layoutEliminar.childCount) {
                val child = layoutEliminar.getChildAt(i)
                child.setOnClickListener {


                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Confirmación")
                    builder.setMessage("¿Estás seguro de completar la acción?")

                    builder.setPositiveButton("Aceptar") { dialog, _ ->
                        // Lógica para el botón "Aceptar"
                        eliminarAutor(idAutorGlobal)
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "autores")
                            putExtra("tipoUsuario" , tipoUsuarioGlobal)
                            putExtra("idUsuario" , idUsuarioGlobal)
                        }
                        startActivity(intent)
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

        }else{
            btnEliminar.visibility = View.GONE
            btnEliminar.isEnabled = false
            layoutEliminar.visibility = View.GONE
        }
    }

    fun ObtenerInformacionDelAutor(idAutor: Int) {

        //Toast.makeText(this, "Empezando: $idAutor", Toast.LENGTH_SHORT).show()
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        setVisibleOrInvisibleBtnEliminar()
                        return@Listener
                    }



                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val Autor = jsonArray.getJSONObject(i)
                        val nombreAutor = Autor.getString("nombre")
                        val correoAutor = Autor.getString("correo")
                        val publicaciones = Autor.getString("publicaciones")
                        val centroInvestigacion = Autor.getString("nombre_centro")
                        val genero = Autor.getString("nombre_genero")

                        // Agregar el artículo a la lista
                        txtNombreAutor.text = nombreAutor
                        txtCorreoAutor.text = correoAutor
                        txtPublicaciones.text = publicaciones + " Publicaciones Vigentes"
                        txtCentroDeInvestigador.text = centroInvestigacion
                        cantidadPublicaciones = publicaciones

                        if(genero == "Masculino"){
                            imgAutor.setImageResource(R.drawable.icono_autores_cientifico)
                        }else{
                            imgAutor.setImageResource(R.drawable.icono_autores_cientifica)
                        }



                        txtCorreoAutor.setOnClickListener {
                            val email = txtCorreoAutor.text.toString()
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$email")
                                putExtra(Intent.EXTRA_SUBJECT, "Asun ")
                                putExtra(Intent.EXTRA_TEXT, "Mensaje opcional")
                            }
                            // Verifica si hay aplicaciones disponibles para manejar el Intent
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            } else {
                                //Toast.makeText(this, "No hay aplicaciones de correo instaladas", Toast.LENGTH_SHORT).show()
                            }
                        }


                        setVisibleOrInvisibleBtnEliminar()

                    }

                    // Notificar al adaptador del RecyclerView



                } catch (e: Exception) {
                    //Toast.makeText(applicationContext, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    setVisibleOrInvisibleBtnEliminar()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(applicationContext, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
                setVisibleOrInvisibleBtnEliminar()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idAutor"] = idAutor.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun eliminarAutor(idInvestigador: String) {
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
                                //1 -> Toast.makeText(this, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                                //2 -> Toast.makeText(this, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
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
                val params = java.util.HashMap<String, String>()
                params["idInvestigador"] = idInvestigador

                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(this).add(stringRequest)
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