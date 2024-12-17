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
import items.ActasCongresoAdapter
import modelosDatos.Acta
import modelosDatos.Articulo
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import popups.PopupHelperEditarCongreso
import kotlin.random.Random

class VistaCongresos : AppCompatActivity() {

    private val url = URL.BASE_URL + "Congresos/Actas/ObtenerTodasLasActasDeUnCongresoPorIdCongreso.php"
    private val url2 = URL.BASE_URL + "Congresos/ObtenerInformacionDeUnCongresoPorIdCongreso.php"

    private val url3 = URL.BASE_URL + "Congresos/EliminarCongresos.php"
    private lateinit var txtNombreCongreso: TextView
    private lateinit var txtFrecuencia: TextView
    private lateinit var txtPrimerCongreso: TextView
    private lateinit var txtTipoCongreso: TextView
    private lateinit var btnEditar: ImageView
    private lateinit var btnEliminar: ImageView
    private lateinit var layoutEliminar: ConstraintLayout

    private val articleList = mutableListOf<Articulo>()

    private var idUsuarioGlobal = ""
    private var tipoUsuarioGlobal = ""
    private var idCongresoGlobal = ""

    // Lista mutable para almacenar las actas
    private val actasList = mutableListOf<Acta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_congresos)

        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
        val random = Random.nextInt(1, 5)
        when (random) {
            1 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_1)
            2 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_2)
            3 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_3)
            4 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_4)

        }


        // Ajuste de márgenes para diseño sin interferencias con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutPrincipal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idUsuario = intent.getStringExtra("idUsuario")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idCongreso = intent.getStringExtra("idCongreso")

        idUsuarioGlobal = idUsuario.toString()
        tipoUsuarioGlobal = tipoUsuario.toString()
        idCongresoGlobal = idCongreso.toString()


        // Referencias a elementos de la interfaz
        txtNombreCongreso = findViewById(R.id.txtNombreCongreso)
        txtFrecuencia = findViewById(R.id.txtFrecuencia)
        txtPrimerCongreso = findViewById(R.id.txtFechaCreacion)
        txtTipoCongreso = findViewById(R.id.txtTipoCongreso)


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

        // Referencias a elementos de la interfaz
        val btnVolver = findViewById<ImageView>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val layoutCongreso = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layoutCongreso)



        // Acción para volver a la pantalla de carga
        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "congresos")
                putExtra("idUsuario", idUsuario)
                putExtra("tipoUsuario", tipoUsuario)
            }
            startActivity(intent)
        }

        // Mostrar u ocultar el layoutCongreso al hacer clic en el botón de ojo
        btnOjo.setOnClickListener {
            layoutCongreso.visibility = if (layoutCongreso.visibility == android.view.View.VISIBLE) {
                android.view.View.GONE
            } else {
                android.view.View.VISIBLE
            }
        }



        btnEditar.setOnClickListener {
            val popupHelper = PopupHelperEditarCongreso(this , idCongreso.toString() , idUsuario.toString() , tipoUsuario.toString())
            popupHelper.showPopup(btnEditar)
        }

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val adapter = ActasCongresoAdapter(actasList, this , idCongreso.toString() , idUsuario.toString() , tipoUsuario.toString())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Llamar al método para obtener las actas si `idCongreso` no es nulo
        if (!idCongreso.isNullOrEmpty()) {
            obtenerActas(idCongreso.toInt(), adapter)
            obtenerInformacionDelCongreso(idCongreso.toInt())
        }


    }

    fun setVisibleOrInvisibleBtnEliminar(){
        val isDeleteable = articleList.isEmpty();
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
                        eliminarCongreso(idCongresoGlobal)
                        val intent = Intent(this , PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "congresos")
                            putExtra("idUsuario" , idUsuarioGlobal)
                            putExtra("tipoUsuario" , tipoUsuarioGlobal)
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

    fun obtenerActas(idCongreso: Int, adapter: ActasCongresoAdapter) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron actas", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                    // Limpia la lista antes de rellenarla con nuevos datos
                    actasList.clear()

                    // Procesa la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val actaJson = jsonArray.getJSONObject(i)
                        val idActa = actaJson.getString("id_acta")
                        val nombre = actaJson.getString("nombre")
                        val fechaInicio = actaJson.getString("fecha_inicio")
                        val nombreCiudad = actaJson.getString("nombre_ciudad")

                        // Añade cada acta a la lista
                        actasList.add(Acta(idActa , nombre, fechaInicio, nombreCiudad))
                    }

                    // Notifica al adaptador sobre los cambios
                    adapter.notifyDataSetChanged()

                    //Toast.makeText(applicationContext, "Actas obtenidas con éxito", Toast.LENGTH_SHORT).show()
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
                return mapOf("idCongreso" to idCongreso.toString())
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }



    fun obtenerInformacionDelCongreso(idCongreso: Int) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url2,
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
                    val ultimoIndice = jsonArray.length() - 1
                    for (i in 0 until jsonArray.length()) {
                        val congreso = jsonArray.getJSONObject(i)
                        val nombre = congreso.getString("nombre")
                        val frecuencia = congreso.getString("frecuencia")
                        val nombre_tipo_congreso = congreso.getString("nombre_tipo_congreso")
                        val primer_congreso = congreso.getString("primer_congreso")


                        // Asignar los valores a las vistas
                        txtNombreCongreso.text = nombre
                        txtFrecuencia.text = frecuencia
                        txtPrimerCongreso.text = primer_congreso
                        txtTipoCongreso.text = nombre_tipo_congreso


                        if (i == ultimoIndice) {
                            setVisibleOrInvisibleBtnEliminar()
                        }

                    }


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
                params["idCongreso"] = idCongreso.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }


    fun eliminarCongreso(idCongreso: String) {
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
                                //1 -> Toast.makeText(this, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                                //2 -> Toast.makeText(this, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                                // -> Toast.makeText(this, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
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
                params["idCongreso"] = idCongreso

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
