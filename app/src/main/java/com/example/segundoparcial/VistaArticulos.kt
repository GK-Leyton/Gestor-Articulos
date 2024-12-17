package com.example.segundoparcial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
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
import popups.PopupHelperQueEditarArticulos
import kotlin.random.Random

class VistaArticulos : AppCompatActivity() {

    lateinit var txtCorreoAutor: TextView
    lateinit var txtUrl: TextView
    lateinit var txtInvestigador: TextView
    lateinit var txtLocalizacion: TextView
    lateinit var txtPalabrasClave: TextView
    lateinit var txtTipoArticulo: TextView
    lateinit var txtTituloArticulo: TextView
    lateinit var txtTituloAbreviado: TextView
    lateinit var WVArticulo: WebView
    private lateinit var btnEditar: ImageView
    private lateinit var btnEliminar: ImageView
    private lateinit var layoutEliminar: ConstraintLayout

    private var idTipoArticulo = ""

    val url = URL.BASE_URL+"Articulos/ObtenerInformacionDeUnArticuloPorIdArticulo.php"
    val url2 = URL.BASE_URL + "Prestamos/DevolverArticulo_FinalizarPrestamo.php"
    val url3 = URL.BASE_URL + "Prestamos/PedirrArticulo_IniciarPrestamo.php"
    val url4 = URL.BASE_URL + "Articulos/EliminarArticulo.php"

    private var idUsuarioGlobal = ""
    private var tipoUsuarioGlobal = ""
    private var idArticuloGlobal = ""




    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_articulos)
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

        val idArticulo = intent.getStringExtra("idArticulo")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        val desde = intent.getStringExtra("desde")

        idUsuarioGlobal = idUsuario.toString()
        tipoUsuarioGlobal = tipoUsuario.toString()
        idArticuloGlobal = idArticulo.toString()

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


        val btnCerrarWv = findViewById<ImageButton>(R.id.btnCerrarWV)
        val btnVerWv = findViewById<ImageView>(R.id.btnVerWV)
        val btnVoler = findViewById<ImageView>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val layoutArticulo = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layoutArticulo)
        val btnQueHacer = findViewById<Button>(R.id.btnQueHacer)

        if(desde.equals("TodosLosArticulos")){
            btnQueHacer.visibility = Button.GONE
        }else if(desde.equals("ArticulosEnDespacho")){
            btnQueHacer.text = "Devolver"
            btnQueHacer.setOnClickListener {
                DevolverArticulo(idUsuario.toString() , idArticulo.toString())

                val intent = Intent(this, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menuPrincipal")
                    putExtra("tipoUsuario", tipoUsuario)
                    putExtra("idUsuario", idUsuario)
                }
                startActivity(intent)

            }
        }
        if(!desde.equals("TodosLosArticulos") && !desde.equals("ArticulosEnDespacho")){
            btnQueHacer.text = "Pedir"
            btnQueHacer.setOnClickListener {
                PedirArticulo(idUsuario.toString() , idArticulo.toString())

                val intent = Intent(this, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menuPrincipal")
                    putExtra("tipoUsuario", tipoUsuario)
                    putExtra("idUsuario", idUsuario)
                }
                startActivity(intent)

            }
        }

        txtCorreoAutor = findViewById(R.id.txtCorreoAutor)
        txtUrl = findViewById(R.id.txtUrl)
        txtInvestigador = findViewById(R.id.txtInvestigador)
        txtLocalizacion = findViewById(R.id.txtLocalizacion)
        txtPalabrasClave = findViewById(R.id.txtPalabrasClave)
        txtTipoArticulo = findViewById(R.id.txtTipoArticulo)
        txtTituloArticulo = findViewById(R.id.txtTituloArticulo)
        txtTituloAbreviado = findViewById(R.id.txtTituloAbreviado)
        WVArticulo = findViewById(R.id.WVArticulo)







        WVArticulo.webViewClient = WebViewClient()
        WVArticulo.settings.javaScriptEnabled = true
        WVArticulo.settings.userAgentString ="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
        WVArticulo.settings.domStorageEnabled = true
        WVArticulo.settings.databaseEnabled = true
        WVArticulo.settings.cacheMode = WebSettings.LOAD_DEFAULT
        WVArticulo.settings.loadWithOverviewMode = true
        WVArticulo.settings.useWideViewPort = true
        WVArticulo.settings.builtInZoomControls = true
        WVArticulo.settings.displayZoomControls = false

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        WVArticulo.settings.mediaPlaybackRequiresUserGesture = false

       // WVArticulo.loadUrl("https://www.scielo.pt/scielo.php?script=sci_arttext&pid=S1645-00862023000100148&lang=es")

        WVArticulo.visibility = WebView.GONE;
        btnCerrarWv.visibility = ImageButton.GONE;

        btnCerrarWv.setOnClickListener {
            WVArticulo.visibility = WebView.GONE;
            btnCerrarWv.visibility = ImageButton.GONE;
        }
        btnVerWv.setOnClickListener {
            WVArticulo.visibility = WebView.VISIBLE;
            btnCerrarWv.visibility = ImageButton.VISIBLE;
        }
        btnVoler.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menuPrincipal")
                putExtra("tipoUsuario", tipoUsuario)
                putExtra("idUsuario", idUsuario)
            }
            startActivity(intent)
        }
        btnOjo.setOnClickListener {
            if (WVArticulo.visibility == WebView.VISIBLE || layoutArticulo.visibility == WebView.VISIBLE || btnQueHacer.visibility == Button.VISIBLE) {
                WVArticulo.visibility = WebView.GONE
                layoutArticulo.visibility = WebView.GONE
                btnQueHacer.visibility = Button.GONE

            } else {
                layoutArticulo.visibility = WebView.VISIBLE
                btnQueHacer.visibility = Button.VISIBLE
            }

        }

        btnEditar.setOnClickListener {
            val popupHelper = PopupHelperQueEditarArticulos(this , idUsuario.toString() , tipoUsuario.toString() , idTipoArticulo , idArticulo.toString() , desde.toString())
            popupHelper.showPopup(btnEditar)
        }

        ObtenerInformacionDeUnArticuloPorIdArticulo(idArticulo.toString().toInt());
        setVisibleOrInvisibleBtnEliminar()

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_libro)
            .override(100, 100)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(btnVerWv)
    }



    fun setVisibleOrInvisibleBtnEliminar(){


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
                        eliminarArticulo(idArticuloGlobal)
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "menuPrincipal")
                            putExtra("tipoUsuario", tipoUsuarioGlobal)
                            putExtra("idUsuario", idUsuarioGlobal)
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

    }

    fun ObtenerInformacionDeUnArticuloPorIdArticulo(idArticulo: Int) {
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

                    // Limpiar la lista de artículos si existe

                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val articulo = jsonArray.getJSONObject(i)
                        val tituloArticulo = articulo.getString("titulo") // Correcto
                        val tituloAbreviado = articulo.getString("nombre_corto")
                        val palabrasClave = articulo.getString("palabras_clave")
                        val tipoArticulo = articulo.getString("nombre_tipo_articulo")
                        val nombreInvestigador = articulo.getString("nombre_investigador")
                        val correoContacto = articulo.getString("correo")
                        val urlArticulo = articulo.getString("url")
                        val localizacion = articulo.getString("localizacion")
                        idTipoArticulo = articulo.getString("tipo_articulo")

                        WVArticulo.loadUrl(urlArticulo)

                        // Agregar el artículo a la lista

                        txtTituloArticulo.text = tituloArticulo.toString()
                        txtTituloAbreviado.text = tituloAbreviado.toString()
                        txtPalabrasClave.text = palabrasClave.toString()
                        txtTipoArticulo.text = tipoArticulo.toString()
                        txtInvestigador.text = nombreInvestigador.toString()
                        txtCorreoAutor.text = correoContacto.toString()
                        txtUrl.text = urlArticulo.toString()
                        txtLocalizacion.text = localizacion.toString()

                    }



                    //Toast.makeText(applicationContext, "Préstamos obtenidos con éxito", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }



    fun DevolverArticulo(idUsuario: String, idArticulo: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url2,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                                //1 -> Toast.makeText(this, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                                //else -> Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo
                params["idUsuario"] = idUsuario
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(this).add(stringRequest)
    }


    fun PedirArticulo(idUsuario: String, idArticulo: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url3,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                                //1 -> Toast.makeText(this, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                                //else -> Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo
                params["idUsuario"] = idUsuario
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun eliminarArticulo(idArticulo: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url4,
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
                params["idArticulo"] = idArticulo

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