package com.example.segundoparcial

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
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
import popups.PopupHelperEditarUsuario
import kotlin.random.Random

class VistaPerfilUsuario : AppCompatActivity() {

    private var url = URL.BASE_URL+"Usuario/ObtenerInformacionDeUnUsuario.php"
    private var url2 = URL.BASE_URL+"Autores/EliminarAutores.php"
    private var url3 = URL.BASE_URL+"Usuario/ObtenerLibrosEnPrestamoActivo.php"
    private var url4 = URL.BASE_URL+"Usuario/ObtenerTodosLosLibrosPrestados.php"

    private lateinit var txtNombreUsuario: TextView
    private lateinit var txtUsuario: TextView
    private lateinit var txtTotalArticulosLeidos: TextView
    private lateinit var txtCantidadArticulosPrestados: TextView

    private lateinit var btnEditar: ImageView
    private lateinit var btnCerrarSesion: ImageView
    private lateinit var layoutCerrarSesion: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        val intentSonido = Intent(this, SoundService::class.java)
        startService(intentSonido)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_perfil_usuario)
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


        val idUsuario = intent.getStringExtra("idUsuario")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")


        val btnVoler = findViewById<ImageView>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val layoutPerfil = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layoutPerfilAutor)

        txtNombreUsuario = findViewById(R.id.txtNombreUsuario)
        txtUsuario = findViewById(R.id.txtUsuario)
        txtTotalArticulosLeidos = findViewById(R.id.txtTotalArticulosLeidos)
        txtCantidadArticulosPrestados = findViewById(R.id.txtCantidadArticulosPrestados)

        btnEditar = findViewById(R.id.btnEditar)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        layoutCerrarSesion = findViewById(R.id.layoutCerrarSesion)



        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_editar)
            .override(410, 710)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(btnEditar)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_log_out)
            .override(410, 710)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(btnCerrarSesion)

        ObtenerInformacionDelUsuario(idUsuario.toString().toInt())
        obtenerCantidadDeArticulosEnPrestamoActivo(idUsuario.toString().toInt())
        obtenerCantidadDeArticulosEnPrestamoDeTodoLosTiempos(idUsuario.toString().toInt())

        btnVoler.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menuPrincipal")
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
            val popupHelper = PopupHelperEditarUsuario(this, idUsuario.toString(), tipoUsuario.toString())
            popupHelper.showPopup(btnEditar)
        }

        for (i in 0 until layoutCerrarSesion.childCount) {
            val child = layoutCerrarSesion.getChildAt(i)
            child.setOnClickListener {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de que deseas Cerrar Sesion?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    Runtime.getRuntime().exit(0)
                    dialog.dismiss()
                }


                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    //evento cancelar
                    dialog.dismiss()

                }

                val dialog = builder.create()
                dialog.show()


            }
        }


    }


    fun ObtenerInformacionDelUsuario(idUsuario: Int) {

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }



                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val Usuario = jsonArray.getJSONObject(i)
                        val nombre = Usuario.getString("nombre")
                        val usuario = Usuario.getString("usuario")


                        // Agregar el artículo a la lista
                        txtNombreUsuario.setText(nombre)
                        txtUsuario.setText(usuario)



                    }

                    // Notificar al adaptador del RecyclerView



                } catch (e: Exception) {
                    //Toast.makeText(applicationContext, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(applicationContext, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idUsuario"] = idUsuario.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun obtenerCantidadDeArticulosEnPrestamoActivo(idUsuario: Int) {

        val stringRequest = object : StringRequest(
            Request.Method.POST, url3,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }



                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val usuario = jsonArray.getJSONObject(i)
                        val cantidad = usuario.getString("cantidad")

                        // Agregar el artículo a la lista
                        txtCantidadArticulosPrestados.setText(cantidad + " Articulos en despacho")

                    }

                    // Notificar al adaptador del RecyclerView



                } catch (e: Exception) {
                    //Toast.makeText(applicationContext, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(applicationContext, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idUsuario"] = idUsuario.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }


    fun obtenerCantidadDeArticulosEnPrestamoDeTodoLosTiempos(idUsuario: Int) {

        val stringRequest = object : StringRequest(
            Request.Method.POST, url4,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }



                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val usuario = jsonArray.getJSONObject(i)
                        val cantidad = usuario.getString("cantidad")

                        // Agregar el artículo a la lista
                        txtTotalArticulosLeidos.setText(cantidad + " Articulos leidos")

                    }

                    // Notificar al adaptador del RecyclerView



                } catch (e: Exception) {
                    //Toast.makeText(applicationContext, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(applicationContext, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idUsuario"] = idUsuario.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
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