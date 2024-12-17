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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import items.ActasAdapter
import modelosDatos.ArticuloDesdeActa
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import popups.PopupHelperEditarActaCongreso
import kotlin.random.Random

class VistaActas : AppCompatActivity() {

    private val url = URL.BASE_URL + "Congresos/Actas/ObtenerInformacionDeUnaActaPorIdActa.php"
    private val url2 = URL.BASE_URL + "Congresos/Actas/ObtenerArticulosDeUnaActaPorIdActa.php"

    private val url3 = URL.BASE_URL + "Congresos/Actas/EliminarActa.php"
    private lateinit var txtFechaInicio: TextView
    private lateinit var txtFechaFin: TextView
    private lateinit var txtNombreCiudad: TextView
    private lateinit var txtNombreCongreso: TextView
    private lateinit var txtNombreActa: TextView
    private var articleList = mutableListOf<ArticuloDesdeActa>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ActasAdapter

    private lateinit var btnEditar: ImageView
    private lateinit var btnEliminar: ImageView
    private lateinit var layoutEliminar: ConstraintLayout

    private var idActaGlobal = ""
    private var idCongresoGlobal = ""
    private var idUsuarioGlobal = ""
    private var tipoUsuarioGlobal = ""


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_actas)


        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
        val random = Random.nextInt(1, 5)
        when (random) {
            1 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_1)
            2 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_2)
            3 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_3)
            4 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_4)

        }



        // Obtener datos del Intent
        val idCongreso = intent.getStringExtra("idCongreso") ?: "" // Valor por defecto vacío si es nulo
        val idActa = intent.getStringExtra("idActa")?.toIntOrNull() ?: -1  // Convertir a Int o -1 si es nulo
        val tipoUsuario = intent.getStringExtra("tipoUsuario") ?: ""
        val idUsuario = intent.getStringExtra("idUsuario") ?: ""

        idActaGlobal = idActa.toString()
        idCongresoGlobal = idCongreso
        idUsuarioGlobal = idUsuario
        tipoUsuarioGlobal = tipoUsuario

        // Mostrar mensaje con los valores obtenidos

        // Configuración de las vistas
        txtFechaInicio = findViewById(R.id.txtFechaInicio)
        txtFechaFin = findViewById(R.id.txtFechaFin)
        txtNombreCiudad = findViewById(R.id.txtNombreCiudad)
        txtNombreCongreso = findViewById(R.id.txtNombreCongreso)
        txtNombreActa = findViewById(R.id.txtNombreActa)


        // Verificar si idActa es válido antes de obtener la información



        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicialización del adaptador (después de cargar los datos)
        articleAdapter = ActasAdapter(articleList, this, idUsuario, tipoUsuario)
        recyclerView.adapter = articleAdapter

        // Configuración de los botones
        val btnVolver = findViewById<ImageView>(R.id.btnVolver)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val layoutActas = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layoutActas)




        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)
        layoutEliminar = findViewById(R.id.layoutEliminar)

        if(tipoUsuario == "1"){
            btnEditar.visibility = ImageButton.INVISIBLE
            btnEliminar.visibility = ImageButton.INVISIBLE
        }



        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_editar)
            .override(410, 710)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(btnEditar)

        btnVolver.setOnClickListener {
            // Navegar a la pantalla de carga
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "VistaCongreso")
                putExtra("idUsuario", idUsuario)
                putExtra("tipoUsuario", tipoUsuario)
                putExtra("idCongreso", idCongreso)
            }
            startActivity(intent)
        }

        btnOjo.setOnClickListener {
            // Alternar visibilidad del layoutActas
            if (layoutActas.visibility == View.VISIBLE) {
                layoutActas.visibility = View.GONE
            } else {
                layoutActas.visibility = View.VISIBLE
            }
        }


        btnEditar.setOnClickListener {
            val popupHelper = PopupHelperEditarActaCongreso(this, idActa.toString() , idUsuario, tipoUsuario , idCongreso)
            popupHelper.showPopup(btnEditar)
        }

        obtenerInformacionDelaActa(idActa)
        obtenerArticulosDeUnaActa(idActa)



    }

    fun setVisibleOrInvisibleBtnEliminar(){

        val isDeleteable = articleList.isEmpty()
        //Toast.makeText(this, "isDeleteable: $isDeleteable", Toast.LENGTH_SHORT).show()
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
                        eliminarActa(idActaGlobal)

                        //Toast.makeText(this, "idActa: $idActaGlobal", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "VistaCongreso")
                            putExtra("idCongreso", idCongresoGlobal)
                            putExtra("idUsuario", idUsuarioGlobal)
                            putExtra("tipoUsuario", tipoUsuarioGlobal)
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

    fun obtenerInformacionDelaActa(idActa: Int) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    println("Respuesta del servidor: $response")
                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val acta = jsonArray.getJSONObject(i)
                        val nombre_acta = acta.getString("nombre")
                        val fecha_inicio = acta.getString("fecha_inicio")
                        val fecha_fin = acta.getString("fecha_fin")
                        val nombre_ciudad = acta.getString("nombre_ciudad")
                        val nombre_congreso = acta.getString("nombre_congreso")

                        txtFechaInicio.text = fecha_inicio
                        txtFechaFin.text = fecha_fin
                        txtNombreCiudad.text = nombre_ciudad
                        txtNombreCongreso.text = nombre_congreso
                        txtNombreActa.text = nombre_acta
                    }
                    //Toast.makeText(applicationContext, "Acta obtenida con éxito", Toast.LENGTH_SHORT).show()
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
                params["idActa"] = idActa.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun obtenerArticulosDeUnaActa(idActa: Int) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener<String> { response ->
                try {
                    println("Respuesta del servidor: $response")
                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron artículos", Toast.LENGTH_SHORT).show()
                        setVisibleOrInvisibleBtnEliminar()
                        return@Listener
                    }

                    // Limpiar la lista de artículos antes de agregar los nuevos
                    articleList.clear()

                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    val ultimoIndice = jsonArray.length() - 1

                    for (i in 0 until jsonArray.length()) {
                        val articulo = jsonArray.getJSONObject(i)
                        val id_articulo = articulo.getInt("id_articulo")
                        val titulo = articulo.getString("nombre_corto")
                        val tipo_articulo = articulo.getString("nombre_tipo_articulo")

                        // Agregar el artículo a la lista
                        articleList.add(ArticuloDesdeActa(id_articulo.toString(), titulo, tipo_articulo))


                        if (i == ultimoIndice) {
                            setVisibleOrInvisibleBtnEliminar()
                        }
                    }

                    // Notificar al adaptador que los datos han cambiado
                    articleAdapter.notifyDataSetChanged()



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
                params["idActa"] = idActa.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)


    }

    fun eliminarActa(idActa: String) {
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
                                //2 -> Toast.makeText(this, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
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
                params["idActa"] = idActa

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

