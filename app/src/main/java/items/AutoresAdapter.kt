package com.example.segundoparcial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import modelosDatos.Autor
import popups.PopupHelperQueHacerVerAutor

class AutorAdapter(private var autores: List<Autor>, private val context: Context , private var idUsuario : String , private var tipoUsuario : String) : RecyclerView.Adapter<AutorAdapter.ViewHolder>() {

    private var allAutores: List<Autor> = autores // Lista original

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemAutor: View = itemView.findViewById(R.id.itemAutor)
        val txtIdAutor: TextView = itemView.findViewById(R.id.txtIdAutor) // ID del autor
        val txtNombreAutor: TextView = itemView.findViewById(R.id.txtNombreAutor) // Nombre del autor
        val txtGenero: TextView = itemView.findViewById(R.id.txtGenero)
        val frame: FrameLayout = itemView.findViewById(R.id.frameAutor)
        val txtCentro: TextView = itemView.findViewById(R.id.txtCentroAutor)

        fun bind(autor: Autor) {
            // Asignar valores a los elementos de la vista
            txtIdAutor.text = autor.id.toString()
            txtNombreAutor.text = autor.nombre
            txtGenero.text = autor.genero
            txtCentro.text = autor.centro

            // Cambiar el fondo según si el ID del autor es par

            when (autor.genero) {
                "Femenino" -> frame.setBackgroundResource(R.drawable.icono_autores_cientifica)
                "Masculino" -> frame.setBackgroundResource(R.drawable.icono_autores_cientifico)
            }


            // Evento de clic para mostrar un mensaje con el ID del autor
            itemView.setOnClickListener {
                //Toast.makeText(context, "Genero:" + txtGenero.text.toString() , Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "ID del autor: ${autor.id}", Toast.LENGTH_SHORT).show()
                val popupHelper = PopupHelperQueHacerVerAutor(context , autor.id.toString() , idUsuario , tipoUsuario)
                popupHelper.showPopup(itemView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_autor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(autores[position])
    }

    override fun getItemCount(): Int {
        return autores.size
    }

    fun filter(query: String) {
        autores = if (query.isEmpty()) {
            allAutores // Si la búsqueda está vacía, mostrar todos los autores
        } else {
            allAutores.filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged() // Notificar al RecyclerView de los cambios
    }
}

