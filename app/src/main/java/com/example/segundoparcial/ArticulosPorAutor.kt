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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import items.ItemSpacingDecoration
import modelosDatos.Articulo
import modelosDatos.URL
import org.json.JSONArray
import kotlin.random.Random

class ArticulosPorAutor : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var searchBar: EditText
    private val articleList = mutableListOf<Articulo>()

    var url = URL.BASE_URL+"Autores/ObtenerTodosLosArticulosDeUnAutorPorIDAutor.php"


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_scrolleable_plantilla)

        val logo = findViewById<ImageView>(R.id.imageView2)
        val titulo = findViewById<TextView>(R.id.textView)
        val subtitulo = findViewById<TextView>(R.id.textView1)


        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        val idAutor = intent.getStringExtra("idAutor")
        //Toast.makeText(this,"id Usuario" + idUsuario, Toast.LENGTH_SHORT).show()
        //Toast.makeText(this,"id Autor" + idAutor, Toast.LENGTH_SHORT).show()

        ObtenerArticulosPorAutor(idAutor.toString().toInt())
        //ObtenerArticulosPorAutor(1)

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
        articleAdapter = ArticleAdapter(articleList, this , idUsuario.toString() , tipoUsuario.toString() , "ArticulosPorAutor")


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
                putExtra("proximaPagina", "autores")
                putExtra("tipoUsuario" , tipoUsuario)
                putExtra("idUsuario" , idUsuario)
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


    fun ObtenerArticulosPorAutor(idAutor: Int) {

        //Toast.makeText(this, "Empezando: $idAutor", Toast.LENGTH_SHORT).show()
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

                    // Limpiar la lista de artículos si existe
                    articleList.clear()

                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val articulo = jsonArray.getJSONObject(i)
                        val idArticulo = articulo.getInt("id_articulo")
                        val nombreCorto = articulo.getString("nombre_corto")
                        val localizacion = articulo.getString("localizacion")
                        val tipoArticulo = articulo.getString("nombre_tipo_articulo")

                        // Agregar el artículo a la lista
                        articleList.add(
                            Articulo(
                                idArticulo,
                                nombreCorto,
                                tipoArticulo,
                                localizacion
                            )
                        )
                    }

                    // Notificar al adaptador del RecyclerView
                    recyclerView.adapter = articleAdapter
                    articleAdapter.notifyDataSetChanged()

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
                params["idAutor"] = idAutor.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
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