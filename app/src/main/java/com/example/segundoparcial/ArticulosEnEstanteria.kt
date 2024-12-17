package com.example.segundoparcial

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
import modelosDatos.Articulo
import modelosDatos.URL
import org.json.JSONArray
import kotlin.random.Random

class ArticulosEnEstanteria : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var searchBar: EditText
    private val articleList = mutableListOf<Articulo>()
    var url = URL.BASE_URL+"Articulos/ObtenerTodosLosArticulosDisponibles.php"


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_scrolleable_plantilla)



        val logo = findViewById<ImageView>(R.id.imageView2)
        val titulo = findViewById<TextView>(R.id.textView)
        val subtitulo = findViewById<TextView>(R.id.textView1)
        obtenerArticulosDisponibles();
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
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val btnAtras = findViewById<ImageView>(R.id.btnVoler)
        searchBar = findViewById(R.id.searchBar)
        articleAdapter = ArticleAdapter(articleList, this , idUsuario.toString() , tipoUsuario.toString() , "ArticulosEnEstanteria")


        Glide.with(this)
            .asGif()
            .load(R.drawable.icono_ojo_animado)
            .into(btnOjo)


        btnOjo.setOnClickListener {
            if (searchBar.visibility == EditText.VISIBLE || recyclerView.visibility == RecyclerView.VISIBLE) {
                searchBar.visibility = EditText.GONE
                recyclerView.visibility = RecyclerView.GONE
            } else {
                searchBar.visibility = EditText.VISIBLE
                recyclerView.visibility = RecyclerView.VISIBLE
            }
        }



        /*for (i in 1..50) {
            articleList.add(
                Articulo(
                    i,
                    "Articulo $i",
                    "Tipo ${i % 7}",
                    "Localización $i"
                )
            )
        }*/
        recyclerView.addItemDecoration(ItemSpacingDecoration(spacingInPixels))
        recyclerView.adapter = articleAdapter

        btnAtras.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menuPrincipal")
                putExtra("tipoUsuario", tipoUsuario)
                putExtra("idUsuario", idUsuario)
            }
            startActivity(intent)
        }


        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                articleAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun obtenerArticulosDisponibles() {
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
                            articleList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val articulo = jsonArray.getJSONObject(i)
                                val idArticulo = articulo.getString("id_articulo").toInt()
                                val nombreCorto = articulo.getString("nombre_corto")
                                val localizacion = articulo.getString("localizacion")
                                val tipoArticulo = articulo.getString("nombre_tipo_articulo")
                                // Agregar a la lista de artículos
                                articleList.add(
                                    Articulo(
                                        idArticulo,
                                        nombreCorto,
                                        tipoArticulo,
                                        localizacion
                                    )
                                )
                            }

                            // Asignar adaptador al RecyclerView
                            recyclerView.adapter = articleAdapter

                            // Notificar que los datos han cambiado
                            articleAdapter.notifyDataSetChanged()

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