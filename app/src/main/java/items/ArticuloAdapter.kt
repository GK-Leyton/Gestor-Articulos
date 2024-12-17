package com.example.segundoparcial

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import modelosDatos.Articulo

class ArticleAdapter(private var articles: List<Articulo>, private val context: Context , private var idUsuario: String , private var tipoUsuario: String , private var desde: String) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var allArticles: List<Articulo> = articles // Mantiene la lista original

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemArticulo: View = itemView.findViewById(R.id.itemArticulo)
        val txtId: TextView = itemView.findViewById(R.id.txtIdArticulo)
        val editTextNombre: TextView = itemView.findViewById(R.id.txtNombreArticulo)
        val editTextTipo: TextView = itemView.findViewById(R.id.txtTipoArticulo)
        val editTextLocalizacion: TextView = itemView.findViewById(R.id.txtLocalizacion)
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)

        fun bind(articulo: Articulo) {
            // Asignar valores a los elementos de la vista
            txtId.text = articulo.id.toString()
            editTextNombre.setText(articulo.nombre)
            editTextTipo.setText(articulo.tipo)
            editTextLocalizacion.setText(articulo.localizacion)

            // Cambiar el fondo según el tipo de artículo
            when (articulo.tipo) {
                "Congres" -> itemArticulo.setBackgroundResource(R.drawable.portada_libro1)
                "Informe Tecnico" -> itemArticulo.setBackgroundResource(R.drawable.portada_libro2)
                "Revista" -> itemArticulo.setBackgroundResource(R.drawable.portada_libro3)
                //"Tipo 4" -> itemArticulo.setBackgroundResource(R.drawable.portada_libro4)
                //"Tipo 5" -> itemArticulo.setBackgroundResource(R.drawable.portada_libro5)
                //"Tipo 6" -> itemArticulo.setBackgroundResource(R.drawable.portada_libro6)
            }

            // Configurar el evento de clic para mostrar el Toast con el ID del artículo
            itemView.setOnClickListener {
                val idArticulo = articulo.id
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "vistaArticulos")
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idArticulo" , idArticulo.toString())
                    putExtra("desde" , desde)
                }
                context.startActivity(intent)

                Toast.makeText(context, "ID del artículo: ${articulo.id}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_articulo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun filter(query: String) {
        articles = if (query.isEmpty()) {
            allArticles // Si la búsqueda está vacía, muestra todos los artículos
        } else {
            allArticles.filter {
                it.nombre.contains(query, ignoreCase = true) || it.tipo.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged() // Notifica a RecyclerView que los datos han cambiado
    }
}
