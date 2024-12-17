package com.example.segundoparcial

import BotonesMenuDialogFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.webkit.CookieManager // Importar el CookieManager correcto
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import modelosDatos.URL
import org.json.JSONArray
import popups.PopupHelperCrearParte1
import kotlin.random.Random

class MenuPrincipalUsuario : AppCompatActivity() {


    var url = URL.BASE_URL+"Articulos/ObtenerElArticuloMasPrestado.php"
    private lateinit var webView: WebView
    private lateinit var txtArticulo: TextView
    private lateinit var txtNombreArticuloLibro: TextView
    private lateinit var txtNombreArticulo2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        setContentView(R.layout.menu_principal)

        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        //.makeText(this,"id Usuario" + idUsuario, Toast.LENGTH_SHORT).show()

        webView = findViewById(R.id.WVArticuloMásFamoso)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val frmarticulo = findViewById<FrameLayout>(R.id.frameLayout)
        txtArticulo = findViewById(R.id.txtNombreArticulo)
        val frmLibro = findViewById<ImageView>(R.id.btnLibroPopular)
        val btnMenos = findViewById<ImageView>(R.id.btnMenos)
        val framePadreLibro = findViewById<FrameLayout>(R.id.frameLibro)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        val btnCrear = findViewById<ImageView>(R.id.btnCrear)
        val btnPerfilUsuario = findViewById<ImageView>(R.id.btnPerfilUsuario)
        val logo = findViewById<ImageView>(R.id.imageView2)
        val titulo = findViewById<TextView>(R.id.textView)
        val subtitulo = findViewById<TextView>(R.id.textView1)
        txtNombreArticuloLibro = findViewById(R.id.txtNombreArticuloLibro)
        txtNombreArticulo2 = findViewById(R.id.txtNombreArticulo2)


        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.main)
        val random = Random.nextInt(1, 5)
        when (random) {
            1 -> {
                layoutPrincipal.setBackgroundResource(R.drawable.paisaje_1)
                logo.setImageResource(R.drawable.logo_negro)
                titulo.setTextColor(resources.getColor(R.color.black))
                subtitulo.setTextColor(resources.getColor(R.color.black))
            }
            2 -> {
                layoutPrincipal.setBackgroundResource(R.drawable.paisaje_2)
                logo.setImageResource(R.drawable.logo_blanco)
                titulo.setTextColor(resources.getColor(R.color.white))
                subtitulo.setTextColor(resources.getColor(R.color.white))
            }
            3 -> {
                layoutPrincipal.setBackgroundResource(R.drawable.paisaje_3)
                logo.setImageResource(R.drawable.logo_blanco)
                titulo.setTextColor(resources.getColor(R.color.white))
                subtitulo.setTextColor(resources.getColor(R.color.white))
            }
            4 -> {
                layoutPrincipal.setBackgroundResource(R.drawable.paisaje_4)
                logo.setImageResource(R.drawable.logo_blanco)
                titulo.setTextColor(resources.getColor(R.color.white))
                subtitulo.setTextColor(resources.getColor(R.color.white))
            }

        }

        // Configuración inicial de visibilidad
        webView.visibility = WebView.GONE
        frmarticulo.visibility = FrameLayout.GONE
        txtArticulo.visibility = TextView.GONE
        frmLibro.visibility = ImageView.VISIBLE


        if(tipoUsuario != 2.toString()){
            btnCrear.visibility = ImageView.GONE
        }

        btnMenu.setOnClickListener {
            val dialogFragment = BotonesMenuDialogFragment(btnMenu , idUsuario.toString() , tipoUsuario.toString())
            dialogFragment.show(supportFragmentManager, "BotonesMenuDialogFragment")
        }


        btnMenos.setOnClickListener {
            if (webView.visibility == WebView.VISIBLE || frmarticulo.visibility == FrameLayout.VISIBLE || txtArticulo.visibility == TextView.VISIBLE) {
                webView.visibility = WebView.GONE
                frmarticulo.visibility = FrameLayout.GONE
                txtArticulo.visibility = TextView.GONE
                btnMenos.visibility = ImageView.GONE
                frmLibro.visibility = ImageView.VISIBLE
                framePadreLibro.visibility = FrameLayout.VISIBLE
            }}
        frmLibro.setOnClickListener {
            if (webView.visibility == WebView.VISIBLE || frmarticulo.visibility == FrameLayout.VISIBLE || txtArticulo.visibility == TextView.VISIBLE) {
                webView.visibility = WebView.GONE
                frmarticulo.visibility = FrameLayout.GONE
                txtArticulo.visibility = TextView.GONE
                btnMenos.visibility = ImageView.GONE
                frmLibro.visibility = ImageView.VISIBLE
                framePadreLibro.visibility = FrameLayout.VISIBLE
            } else {
                webView.visibility = WebView.VISIBLE
                btnMenos.visibility = ImageView.VISIBLE
                frmarticulo.visibility = FrameLayout.VISIBLE
                txtArticulo.visibility = TextView.VISIBLE
                frmLibro.visibility = FrameLayout.GONE
                framePadreLibro.visibility = FrameLayout.GONE
            }
        }
        btnOjo.setOnClickListener {
            if (webView.visibility == WebView.VISIBLE || frmarticulo.visibility == FrameLayout.VISIBLE || txtArticulo.visibility == TextView.VISIBLE || frmLibro.visibility == ImageView.VISIBLE) {
                webView.visibility = WebView.GONE
                frmarticulo.visibility = FrameLayout.GONE
                txtArticulo.visibility = TextView.GONE
                btnMenos.visibility = ImageView.GONE
                frmLibro.visibility = ImageView.GONE
                framePadreLibro.visibility = FrameLayout.GONE
                if(tipoUsuario == 2.toString()){
                    btnCrear.visibility = ImageView.GONE
                }
            } else if (frmLibro.visibility == FrameLayout.GONE){
                frmLibro.visibility = ImageView.VISIBLE
                if(tipoUsuario == 2.toString()){
                    btnCrear.visibility = ImageView.VISIBLE
                }
                framePadreLibro.visibility = FrameLayout.VISIBLE
            }
        }

        btnCrear.setOnClickListener {
            val popupHelper = PopupHelperCrearParte1(this)
            popupHelper.showPopup(btnCrear)
        }

        btnPerfilUsuario.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "perfilUsuario")
                putExtra("tipoUsuario", tipoUsuario)
                putExtra("idUsuario", idUsuario)
            }
            startActivity(intent)
        }

        Glide.with(this)
            .asGif()
            .load(R.drawable.icono_ojo_animado)
            .into(btnOjo)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        webView.settings.mediaPlaybackRequiresUserGesture = false

        //webView.loadUrl("https://repository.upb.edu.co/handle/20.500.11912/4942")
        obtenerArticuloMasPrestado()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun obtenerArticuloMasPrestado() {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        //Toast.makeText(applicationContext, "No se encontraron artículos", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los artículos

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val articulo = jsonArray.getJSONObject(i)
                                val id_articulo = articulo.getString("id_articulo").toInt()
                                val nombre_corto = articulo.getString("nombre_corto")
                                val url2 = articulo.getString("url")

                                webView.loadUrl(url2)
                                txtArticulo.text = "El articulo más popular \n "+nombre_corto
                                txtNombreArticuloLibro.text = nombre_corto
                                txtNombreArticulo2.text = id_articulo.toString()
                                // Agregar a la lista de artículos
                                txtArticulo.setOnClickListener {

                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.data = Uri.parse(url2)
                                    startActivity(intent) // Inicia el navegador web
                                }

                            }



                            //Toast.makeText(applicationContext, "Artículos obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            //Toast.makeText(applicationContext, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(applicationContext, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            // Parámetros para enviar al PHP si es necesario (en este caso no los necesitas)
            override fun getParams(): Map<String, String> = emptyMap()
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


