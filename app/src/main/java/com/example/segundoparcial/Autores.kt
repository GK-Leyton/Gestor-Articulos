package com.example.segundoparcial

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import items.ItemSpacingDecoration
import modelosDatos.Autor
import modelosDatos.Congreso
import modelosDatos.URL
import org.json.JSONArray
import kotlin.random.Random

class Autores : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var autorAdapter: AutorAdapter
    private lateinit var searchBar: EditText
    private val autorList = mutableListOf<Autor>()
    var url = URL.BASE_URL+"Autores/ObtenerTodosLosAutores.php"


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_scrolleable_plantilla)

        val logo = findViewById<ImageView>(R.id.imageView2)
        val titulo = findViewById<TextView>(R.id.textView)
        val subtitulo = findViewById<TextView>(R.id.textView1)


        obtenerAutores();

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
        autorAdapter = AutorAdapter(autorList, this , idUsuario.toString() , tipoUsuario.toString())
        val btnVoler = findViewById<ImageButton>(R.id.btnVoler)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)



        btnOjo.setOnClickListener {
            if (searchBar.visibility == EditText.VISIBLE || recyclerView.visibility == RecyclerView.VISIBLE) {
                searchBar.visibility = EditText.GONE
                recyclerView.visibility = RecyclerView.GONE
            } else {
                searchBar.visibility = EditText.VISIBLE
                recyclerView.visibility = RecyclerView.VISIBLE
            }
        }

        btnVoler.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menuPrincipal")
                putExtra("tipoUsuario" , tipoUsuario)
                putExtra("idUsuario" , idUsuario)
            }
            startActivity(intent)

        }


        Glide.with(this)
            .asGif()
            .load(R.drawable.icono_ojo_animado)
            .into(btnOjo)
        
        // Simulamos algunos datos de autores
        /*for (i in 1..50) {
            autorList.add(
                Autor(
                    "Autor $i",
                    i,
                    "Femenino"
                )
            )
        }*/

        recyclerView.addItemDecoration(ItemSpacingDecoration(spacingInPixels))
        recyclerView.adapter = autorAdapter

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                autorAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun obtenerAutores() {
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
                            autorList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val autores = jsonArray.getJSONObject(i)
                                val nombre_autor = autores.getString("nombre")
                                val id_autor = autores.getString("id_investigador")
                                val genero_autor = autores.getString("nombre_genero")
                                val centro_autor = autores.getString("nombre_centro")

                                // Agregar a la lista de artículos
                                autorList.add(
                                    Autor(
                                        nombre_autor,
                                        id_autor.toInt(),
                                        genero_autor,
                                        centro_autor
                                    )
                                )
                            }

                            // Asignar adaptador al RecyclerView
                            recyclerView.adapter = autorAdapter

                            // Notificar que los datos han cambiado
                            autorAdapter.notifyDataSetChanged()

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
