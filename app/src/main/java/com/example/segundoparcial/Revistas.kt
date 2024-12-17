package com.example.segundoparcial

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import items.ItemSpacingDecoration
import modelosDatos.Revista
import modelosDatos.URL
import org.json.JSONArray
import kotlin.random.Random

class Revistas : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var revistaAdapter: RevistaAdapter
    private lateinit var searchBar: EditText
    private val revistaList = mutableListOf<Revista>()
    var url = URL.BASE_URL+"Revistas/ObtenerTodasLasRevistas.php"

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_scrolleable_plantilla)


        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        //Toast.makeText(this,"id Usuario" + idUsuario, Toast.LENGTH_SHORT).show()

        obtenerRevistas();


        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
        val random = Random.nextInt(1, 5)
        when (random) {
            1 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_1)
            2 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_2)
            3 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_3)
            4 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_4)

        }

        val gridLayoutManager = GridLayoutManager(this, 2)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = gridLayoutManager
        searchBar = findViewById(R.id.searchBar)
        revistaAdapter = RevistaAdapter(revistaList, this , idUsuario.toString() , tipoUsuario.toString()) // Adaptador de Revistas
        val btnVoler = findViewById<ImageButton>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)

        // Evento para mostrar u ocultar la barra de búsqueda y el RecyclerView
        btnOjo.setOnClickListener {
            if (searchBar.visibility == EditText.VISIBLE || recyclerView.visibility == RecyclerView.VISIBLE) {
                searchBar.visibility = EditText.GONE
                recyclerView.visibility = RecyclerView.GONE
            } else {
                searchBar.visibility = EditText.VISIBLE
                recyclerView.visibility = RecyclerView.VISIBLE
            }
        }

        // Botón para volver al menú principal
        btnVoler.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menuPrincipal")
                putExtra("tipoUsuario" , tipoUsuario)
                putExtra("idUsuario" , idUsuario)
            }
            startActivity(intent)
        }

        // Cargar imagen de un GIF en el botón con Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.icono_ojo_animado)
            .into(btnOjo)

        // Simulamos algunos datos de revistas
        /*for (i in 1..50) {
            revistaList.add(
                Revista(
                    "Revista $i",
                    i
                )
            )
        }*/

        // Añadir decoración y configurar el adaptador
        recyclerView.addItemDecoration(ItemSpacingDecoration(spacingInPixels))
        recyclerView.adapter = revistaAdapter

        // Filtro en la barra de búsqueda
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                revistaAdapter.filter(s.toString()) // Filtrar las revistas por nombre
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    fun obtenerRevistas() {
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
                            revistaList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val revista = jsonArray.getJSONObject(i)
                                val id_revista = revista.getString("id_revista").toInt()
                                val nombre_revista = revista.getString("nombre")
                                val temaRevista = revista.getString("tema")

                                // Agregar a la lista de artículos
                                revistaList.add(
                                    Revista(
                                        nombre_revista,
                                        id_revista,
                                        temaRevista
                                    )
                                )
                            }

                            // Asignar adaptador al RecyclerView
                            recyclerView.adapter = revistaAdapter

                            // Notificar que los datos han cambiado
                            revistaAdapter.notifyDataSetChanged()

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
