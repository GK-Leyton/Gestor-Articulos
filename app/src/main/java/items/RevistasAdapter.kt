package com.example.segundoparcial

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import modelosDatos.Revista

class RevistaAdapter(private var revistas: List<Revista>, private val context: Context , private val idUsuario : String , private val tipoUsuario : String) : RecyclerView.Adapter<RevistaAdapter.ViewHolder>() {

    private var allRevistas: List<Revista> = revistas // Lista original

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemRevista: View = itemView.findViewById(R.id.itemRevista)
        val txtIdRevista: TextView = itemView.findViewById(R.id.txtIdRevista) // ID de la revista
        val txtNombreRevista: TextView = itemView.findViewById(R.id.txtNombreRevista) // Nombre de la revista
        val frame: FrameLayout = itemView.findViewById(R.id.frameRevista)
        val txtTemaRevista: TextView = itemView.findViewById(R.id.txtTemaRevista)

        fun bind(revista: Revista) {
            // Asignar valores a los elementos de la vista
            txtIdRevista.text = revista.id.toString()
            txtNombreRevista.text = revista.nombre
            txtTemaRevista.text = revista.temaRevista

            when (revista.temaRevista) {
                "Inteligencia Artificial" -> frame.setBackgroundResource(R.drawable.icono_revista_inteligencia_artificial)
                "Aprendizaje Automático" -> frame.setBackgroundResource(R.drawable.icono_revista_aprendizaje_automatico)
                "Big Data" -> frame.setBackgroundResource(R.drawable.icono_revista_big_data)
                "Ciencia de Datos" -> frame.setBackgroundResource(R.drawable.icono_revista_ciencia_de_datos)
                "Desarrollo Web" -> frame.setBackgroundResource(R.drawable.icono_revista_desarrollo_web)
                "Seguridad Informática" -> frame.setBackgroundResource(R.drawable.icono_revista_seguridad_informatica)
                "Blockchain" -> frame.setBackgroundResource(R.drawable.icono_revista_blockchain)
                "Internet de las Cosas" -> frame.setBackgroundResource(R.drawable.icono_revista_internet_de_las_cosas)
                "Realidad Aumentada" -> frame.setBackgroundResource(R.drawable.icono_revista_realidad_aumentada)
                "Realidad Virtual" -> frame.setBackgroundResource(R.drawable.icono_revista_realidad_virtual)
                "Computación en la Nube" -> frame.setBackgroundResource(R.drawable.icono_revista_computacion_en_la_nube)
                "Análisis Predictivo" -> frame.setBackgroundResource(R.drawable.icono_revista_analisis_predictivo)
                "Automatización de Procesos" -> frame.setBackgroundResource(R.drawable.icono_revista_automatizacion_de_procesos)
                "Robótica" -> frame.setBackgroundResource(R.drawable.icono_revista_robotica)
                "Sistemas Distribuidos" -> frame.setBackgroundResource(R.drawable.icono_revista_sistemas_distribuidos)
            }

            // Evento de clic para mostrar un mensaje con el ID de la revista
            itemView.setOnClickListener {
                //Toast.makeText(context, "ID de la revista: ${revista.id}", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "VistaRevistas")
                    putExtra("idRevista" , revista.id.toString())
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                }
                context.startActivity(intent)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_revista, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(revistas[position])
    }

    override fun getItemCount(): Int {
        return revistas.size
    }

    fun filter(query: String) {
        revistas = if (query.isEmpty()) {
            allRevistas // Si la búsqueda está vacía, mostrar todas las revistas
        } else {
            allRevistas.filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged() // Notificar al RecyclerView de los cambios
    }
}
