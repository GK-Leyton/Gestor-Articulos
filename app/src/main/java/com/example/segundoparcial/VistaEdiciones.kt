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
import popups.PopupHelperEditarEdicionRevista
import kotlin.random.Random

class VistaEdiciones : AppCompatActivity() {

    private val articulosList = mutableListOf<ArticuloDesdeActa>()

    private val url = URL.BASE_URL + "Revistas/Edicion/ObtenerLaInformacionDeunaEdicionPorNumeroEdicinYIdRevista.php"
    private val url2 = URL.BASE_URL + "Revistas/Edicion/ObtenerArticulosDeUnaEdicionPorIdRevistaYIdEdicion.php"


    private val url3 = URL.BASE_URL + "Revistas/Edicion/EliminarEdicion.php"
    private lateinit var txtNombreEdicion: TextView
    private lateinit var txtNombreEditor: TextView
    private lateinit var txtPaginaInicio: TextView
    private lateinit var txtPaginaFinal: TextView
    private lateinit var txtNombreRevista: TextView
    private lateinit var btnEditar: ImageView
    private lateinit var btnEliminar: ImageView
    private lateinit var layoutEliminar: ConstraintLayout

    private var idUsuarioGlobal = ""
    private var tipoUsuarioGlobal = ""
    private var idRevistaGlobal = ""
    private var idEdicionGlobal = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        val intentSonido = Intent(this, SoundService::class.java)
        startService(intentSonido)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_ediciones)


        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
        val random = Random.nextInt(1, 5)
        when (random) {
            1 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_1)
            2 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_2)
            3 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_3)
            4 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_4)

        }

        val idEdicion = intent.getStringExtra("idEdicion")
        val idRevista = intent.getStringExtra("idRevista")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")

        idUsuarioGlobal = idUsuario.toString()
        tipoUsuarioGlobal = tipoUsuario.toString()
        idRevistaGlobal = idRevista.toString()
        idEdicionGlobal = idEdicion.toString()


        txtNombreEdicion = findViewById(R.id.txtNombreEdicion)
        txtNombreEditor = findViewById(R.id.txtNombreEditor)
        txtPaginaInicio = findViewById(R.id.txtNumeroPaginaInicio)
        txtPaginaFinal = findViewById(R.id.txtNumeroPaginaFinal)
        txtNombreRevista = findViewById(R.id.txtNombreRevista)

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
        val layoutEdiciones = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layoutEdiciones)

        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "VistaRevistas")
                putExtra("tipoUsuario", tipoUsuario)
                putExtra("idUsuario", idUsuario)
                putExtra("idRevista", idRevista)
            }
            startActivity(intent)
        }

        btnOjo.setOnClickListener {
            if (layoutEdiciones.visibility == View.VISIBLE) {
                layoutEdiciones.visibility = View.GONE
            } else {
                layoutEdiciones.visibility = View.VISIBLE
            }
        }


        btnEditar.setOnClickListener {
            val popupHelper = PopupHelperEditarEdicionRevista(this , idRevista.toString() , idEdicion.toString() , idUsuario.toString() , tipoUsuario.toString())
            popupHelper.showPopup(btnEditar)
        }

        obtenerInformacionDeLaEdicion(idRevista.toString().toInt() , idEdicion.toString().toInt())
        obtenerArticulosDeLaEdicion(idRevista.toString().toInt() , idEdicion.toString().toInt() , adapter)


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
                        eliminarEdicion(idEdicionGlobal)
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "VistaRevistas")
                            putExtra("tipoUsuario", tipoUsuarioGlobal)
                            putExtra("idUsuario", idUsuarioGlobal)
                            putExtra("idRevista", idRevistaGlobal)
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


    fun obtenerInformacionDeLaEdicion(idRevista: Int , idEdicion : Int) {
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
                        val revista = jsonArray.getJSONObject(i)
                        val titulo = revista.getString("titulo")
                        val editor = revista.getString("editor")
                        val pagina_inicio = revista.getString("pagina_inicio")
                        val pagina_final = revista.getString("pagina_final")
                        val nombre_revista = revista.getString("nombre_revista")


                        // Asignar los valores a las vistas
                        txtNombreEdicion.text = titulo
                        txtNombreEditor.text = editor
                        txtPaginaInicio.text =  "Pagina Nro. " + pagina_inicio
                        txtPaginaFinal.text = "Pagina Nro. " + pagina_final
                        txtNombreRevista.text = nombre_revista




                    }



                    //Toast.makeText(applicationContext, "Préstamos obtenidos con éxito", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    ///Toast.makeText(applicationContext, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
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
                params["idEdicion"] = idEdicion.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun obtenerArticulosDeLaEdicion(idRevista: Int , idEdicion: Int , adapter: EdicionAdapter) {
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
                params["idRevista"] = idRevista.toString()
                params["idEdicion"] = idEdicion.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest)
    }


    fun eliminarEdicion(idEdicion: String) {
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
                params["idEdicion"] = idEdicion

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
