package com.example.segundoparcial

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import items.ItemSpacingDecoration
import modelosDatos.Congreso
import modelosDatos.URL
import org.json.JSONArray
import kotlin.random.Random

class Congresos : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var congresoAdapter: CongresoAdapter
    private lateinit var searchBar: EditText
    private val congresoList = mutableListOf<Congreso>()
    var url = URL.BASE_URL+"Congresos/ObtenerTodosLosCongresos.php"

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_scrolleable_plantilla) // Cambiar el layout si es necesario

        val logo = findViewById<ImageView>(R.id.imageView2)
        val titulo = findViewById<TextView>(R.id.textView)
        val subtitulo = findViewById<TextView>(R.id.textView1)

        obtenerCongresos();


        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        //Toast.makeText(this,"id Usuario" + idUsuario, Toast.LENGTH_SHORT).show()


        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
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

        val gridLayoutManager = GridLayoutManager(this, 2)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = gridLayoutManager
        searchBar = findViewById(R.id.searchBar)
        congresoAdapter = CongresoAdapter(congresoList, this , idUsuario.toString() , tipoUsuario.toString())
        val btnVolver = findViewById<ImageButton>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)

        // Lógica para mostrar/ocultar la barra de búsqueda y el RecyclerView
        btnOjo.setOnClickListener {
            if (searchBar.visibility == EditText.VISIBLE || recyclerView.visibility == RecyclerView.VISIBLE) {
                searchBar.visibility = EditText.GONE
                recyclerView.visibility = RecyclerView.GONE
            } else {
                searchBar.visibility = EditText.VISIBLE
                recyclerView.visibility = RecyclerView.VISIBLE
            }
        }

        // Lógica para volver al menú principal
        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menuPrincipal")
                putExtra("tipoUsuario" , tipoUsuario)
                putExtra("idUsuario" , idUsuario)
            }
            startActivity(intent)
        }

        // Animación del botón ojo
        Glide.with(this)
            .asGif()
            .load(R.drawable.icono_ojo_animado)
            .into(btnOjo)

        // Simulamos algunos datos de congresos
        /*for (i in 1..50) {
            congresoList.add(
                Congreso(
                    "Congreso $i",
                    i
                )
            )
        }*/

        recyclerView.addItemDecoration(ItemSpacingDecoration(spacingInPixels))
        recyclerView.adapter = congresoAdapter

        // Filtrar congresos con la barra de búsqueda
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                congresoAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    fun obtenerCongresos() {
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
                            congresoList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val congreso = jsonArray.getJSONObject(i)
                                val nombreCongreso = congreso.getString("nombre")
                                val idCongreso = congreso.getString("id_congreso").toInt()
                                val tipoConcongreso = congreso.getString("nombre_tipo_congreso")

                                // Agregar a la lista de artículos
                                congresoList.add(
                                    Congreso(
                                        nombreCongreso,
                                        idCongreso,
                                        tipoConcongreso
                                    )
                                )
                            }

                            // Asignar adaptador al RecyclerView
                            recyclerView.adapter = congresoAdapter

                            // Notificar que los datos han cambiado
                            congresoAdapter.notifyDataSetChanged()

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
