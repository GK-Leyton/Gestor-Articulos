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
import items.EdicionAdapter
import modelosDatos.ArticuloDesdeActa
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import popups.PopupHelperEditarInformeTecnico
import kotlin.random.Random

class VistaInformeTecnico : AppCompatActivity() {

    private val articulosList = mutableListOf<ArticuloDesdeActa>()

    private val url = URL.BASE_URL + "InformesTecnicos/ObtenerLaInformacionDeUnInformeTecnicoPorNumeroInforme.php"
    private val url2 = URL.BASE_URL + "InformesTecnicos/ObtenerTodosLosArticulosDeUnInformeTecnicoPorNumeroInforme.php"

    private val url3 = URL.BASE_URL + "InformesTecnicos/EliminarInformeTecnico.php"
    private lateinit var txtNombreDelCentroDelInformeTecnico: TextView
    private lateinit var txtNumeroInforme: TextView
    private lateinit var txtTemaInformeTecnico: TextView
    private lateinit var txtFechaInformeTecnico: TextView
    private lateinit var btnEditar: ImageView
    private lateinit var btnEliminar: ImageView
    private lateinit var layoutEliminar: ConstraintLayout

    private var idUsuarioGlobal = ""
    private var tipoUsuarioGlobal = ""
    private var idCentroGlobal = ""
    private var idInformeGlobal = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        val intentSonido = Intent(this, SoundService::class.java)
        startService(intentSonido)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_informe_tecnico)


        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
        val random = Random.nextInt(1, 5)
        when (random) {
            1 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_1)
            2 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_2)
            3 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_3)
            4 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_4)

        }

        //val idEdicion = intent.getStringExtra("idEdicion")
        val idInforme = intent.getStringExtra("idInforme")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        val idCentro = intent.getStringExtra("idCentro")

        idUsuarioGlobal = idUsuario.toString()
        tipoUsuarioGlobal = tipoUsuario.toString()
        idCentroGlobal = idCentro.toString()
        idInformeGlobal = idInforme.toString()


        txtNombreDelCentroDelInformeTecnico = findViewById(R.id.txtNombreDelCentroDelInformeTecnico)
        txtNumeroInforme = findViewById(R.id.txtNumeroInforme)
        txtTemaInformeTecnico = findViewById(R.id.txtTemaInformeTecnico)
        txtFechaInformeTecnico = findViewById(R.id.txtFechaInformeTecnico)

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

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView) // Asegúrate de que el ID sea correcto
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = EdicionAdapter(articulosList, this , idUsuario.toString() , tipoUsuario.toString())
        recyclerView.adapter = adapter

        // Configuración de los botones
        val btnVolver = findViewById<ImageView>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val layoutInformeTecnico = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layoutInformeTecnico)

        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
            putExtra("proximaPagina", "VistaCentros")
            putExtra("idCentro" , idCentro.toString() )
            putExtra("tipoUsuario" , tipoUsuario)
            putExtra("idUsuario" , idUsuario)
        }
            startActivity(intent)
        }

        btnOjo.setOnClickListener {
            if (layoutInformeTecnico.visibility == View.VISIBLE) {
                layoutInformeTecnico.visibility = View.GONE
            } else {
                layoutInformeTecnico.visibility = View.VISIBLE
            }
        }


        btnEditar.setOnClickListener {
            //Toast.makeText(this, "ID informe que se envia a editar: $idInforme", Toast.LENGTH_SHORT).show()
            val popupHelper = PopupHelperEditarInformeTecnico(this , idInforme.toString().toInt() , idUsuario.toString() , tipoUsuario.toString() , idCentro.toString())
            popupHelper.showPopup(btnEditar)
        }

        obtenerInformacionDeLaEdicion(idInforme.toString().toInt())
        obtenerArticulosDeLaEdicion(idInforme.toString().toInt() , adapter)


    }

    fun setVisibleOrInvisibleBtnEliminar(){
        val isDeleteable = articulosList.isEmpty()
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
                        eliminarInformeTecnico(idInformeGlobal)
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "VistaCentros")
                            putExtra("idCentro" , idCentroGlobal)
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


    fun obtenerInformacionDeLaEdicion(numeroInforme: Int ) {
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
                        val informe = jsonArray.getJSONObject(i)
                        val nombre_centro = informe.getString("nombre_centro")
                        val nombre_tema = informe.getString("nombre_tema")
                        val numero_informe = informe.getString("numero_informe")
                        val fecha = informe.getString("fecha")



                        // Asignar los valores a las vistas
                        txtNombreDelCentroDelInformeTecnico.text = nombre_centro
                        txtNumeroInforme.text = numero_informe
                        txtTemaInformeTecnico.text = nombre_tema
                        txtFechaInformeTecnico.text = fecha




                    }
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
                params["idInformeTecnico"] = numeroInforme.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun obtenerArticulosDeLaEdicion(idInforme: Int , adapter: EdicionAdapter) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener<String> { response ->
                try {
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        //Toast.makeText(applicationContext, "No se encontraron actas", Toast.LENGTH_SHORT).show()
                        setVisibleOrInvisibleBtnEliminar()
                        return@Listener
                    }

                    // Limpia la lista antes de rellenarla con nuevos datos
                    articulosList.clear()

                    // Procesa la respuesta JSON
                    val jsonArray = JSONArray(response)
                    val ultimoIndice = jsonArray.length() - 1
                    for (i in 0 until jsonArray.length()) {
                        val articulo = jsonArray.getJSONObject(i)
                        val id_articulo = articulo.getString("id_articulo")
                        val nombre_corto = articulo.getString("nombre_corto")
                        val tipo_articulo = articulo.getString("tipo_articulo")


                        // Añade cada acta a la lista
                        articulosList.add(
                            ArticuloDesdeActa(
                                id_articulo,
                                nombre_corto,
                                tipo_articulo

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
                val params = HashMap<String, String>()
                params["idInformeTecnico"] = idInforme.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }


    fun eliminarInformeTecnico(idInformeTecnico: String) {
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
                params["idInformeTecnico"] = idInformeTecnico

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
