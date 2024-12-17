package com.example.segundoparcial

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import items.ArticulosEnEdicionesAdapter
import modelosDatos.EdicionDesdeRevista
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import popups.PopupHelperEditarRevista
import kotlin.random.Random

class VistaRevistas : AppCompatActivity() {


    private lateinit var txtNombreRevista: TextView
    private lateinit var txtFrecuencia: TextView
    private lateinit var txtPrimerCongreso: TextView
    private lateinit var txtTemaRevista: TextView
    private lateinit var btnEditar: ImageView
    private lateinit var btnEliminar: ImageView
    private lateinit var layoutEliminar: ConstraintLayout

    private val edicionList = mutableListOf<EdicionDesdeRevista>()

    private var tipoUsuarioGlobal = ""
    private var idUsuarioGlobal = ""
    private var idRevistaGlobal = ""


    private val url = URL.BASE_URL + "Revistas/Edicion/ObtenerLasEdicionesDeUnaRevista.php"
    private val url2 = URL.BASE_URL + "Revistas/ObtenerInformacionSobreUnaRevistaPorIdRevista.php"


    private val url3 = URL.BASE_URL + "Revistas/EliminarRevista.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        val intentSonido = Intent(this, SoundService::class.java)
        startService(intentSonido)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_revistas)



        // Ajuste de márgenes para tener un diseño sin interferencias con las barras del sistema
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

        // Referencias a los elementos de la interfaz
        val btnVolver = findViewById<ImageView>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val layoutCongreso = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layoutRevista)





        val idRevista = intent.getStringExtra("idRevista")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")

        idUsuarioGlobal = idUsuario.toString()
        tipoUsuarioGlobal = tipoUsuario.toString()
        idRevistaGlobal = idRevista.toString()

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

        txtNombreRevista = findViewById(R.id.txtNombreRevista)
        txtFrecuencia = findViewById(R.id.txtFrecuencia)
        txtPrimerCongreso = findViewById(R.id.txtFechaCreacion)
        txtTemaRevista = findViewById(R.id.txtTemaRevista)


        // Acción para volver a la pantalla de carga
        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "revistas")
                putExtra("tipoUsuario" , tipoUsuario)
                putExtra("idUsuario" , idUsuario)
            }
            startActivity(intent)
        }

        // Mostrar u ocultar el layoutCongreso al hacer clic en el botón de ojo
        btnOjo.setOnClickListener {
            if (layoutCongreso.visibility == android.view.View.VISIBLE) {
                layoutCongreso.visibility = android.view.View.GONE
            } else {
                layoutCongreso.visibility = android.view.View.VISIBLE
            }
        }



        btnEditar.setOnClickListener {
            val popupHelper = PopupHelperEditarRevista(this , idRevista.toString() , tipoUsuario.toString() , idUsuario.toString())
            popupHelper.showPopup(btnEditar)
        }

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)


        // Configuramos el RecyclerView con el LayoutManager y el Adapter
        val adapter = ArticulosEnEdicionesAdapter(edicionList, this , idUsuario.toString() , tipoUsuario.toString() , idRevista.toString())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        obtenerInformacionDeLaRevista(idRevista.toString().toInt())
        obtenerEdiciones(idRevista.toString().toInt() , adapter)


    }

    fun setVisibleOrInvisibleBtnEliminar(){
        val isDeleteable = edicionList.isEmpty()
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
                        eliminarRevista(idRevistaGlobal)
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "revistas")
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



    fun obtenerEdiciones(idRevista: Int, adapter: ArticulosEnEdicionesAdapter) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron actas", Toast.LENGTH_SHORT).show()
                        setVisibleOrInvisibleBtnEliminar()
                        return@Listener
                    }

                    // Limpia la lista antes de rellenarla con nuevos datos
                    edicionList.clear()

                    // Procesa la respuesta JSON
                    val jsonArray = JSONArray(response)
                    val ultimoIndice = jsonArray.length() - 1
                    for (i in 0 until jsonArray.length()) {
                        val edicion = jsonArray.getJSONObject(i)
                        val titulo = edicion.getString("titulo")
                        val editor = edicion.getString("editor")
                        val numero_edicion = edicion.getString("numero_edicion")


                        // Añade cada acta a la lista
                        edicionList.add(
                            EdicionDesdeRevista(
                            titulo,
                            editor,
                            numero_edicion

                        )
                        )

                        if(i == ultimoIndice){
                            setVisibleOrInvisibleBtnEliminar()
                        }
                    }

                    // Notifica al adaptador sobre los cambios
                    adapter.notifyDataSetChanged()



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
                return mapOf("idRevista" to idRevista.toString())
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }



    fun obtenerInformacionDeLaRevista(idRevista: Int) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url2,
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
                        val revista = jsonArray.getJSONObject(i)
                        val nombre = revista.getString("nombre")
                        val primera_edicion = revista.getString("primera_edicion")
                        val frecuencia = revista.getString("frecuencia")
                        val nombre_tema = revista.getString("nombre_tema")


                        // Asignar los valores a las vistas

                        txtNombreRevista.text = nombre
                        txtPrimerCongreso.text = primera_edicion
                        txtFrecuencia.text = "Cada " + frecuencia + " dias"
                        txtTemaRevista.text = nombre_tema


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
                params["idRevista"] = idRevista.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }


    fun eliminarRevista(idRevista: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url3,
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
                params["idRevista"] = idRevista

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
