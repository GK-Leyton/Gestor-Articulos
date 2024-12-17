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
import modelosDatos.InformeTecnico
import modelosDatos.Revista

class InformeTecnicoAdapter(private var informesTecnicos: List<InformeTecnico>, private val context: Context , private val idUsuario : String , private val tipoUsuario : String , private val idCentro : String) : RecyclerView.Adapter<InformeTecnicoAdapter.ViewHolder>() {

    private var allInformeTecnico: List<InformeTecnico> = informesTecnicos // Lista original

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemInformeTecnico: View = itemView.findViewById(R.id.itemInformeTecnico)
        val txtTemaInformeTecnico: TextView = itemView.findViewById(R.id.txtTemaInformeTecnico) // ID de la revista
        val txtFechaInformeTEcnico: TextView = itemView.findViewById(R.id.txtFechaInformeTEcnico) // Nombre de la revista
        val txtNumeroInformeTecnico: TextView = itemView.findViewById(R.id.txtNumeroInformeTecnico)

        fun bind(informeTecnico: InformeTecnico) {
            // Asignar valores a los elementos de la vista
            txtNumeroInformeTecnico.text = informeTecnico.idInforme.toString()
            txtTemaInformeTecnico.text = informeTecnico.temaInforme
            txtFechaInformeTEcnico.text = informeTecnico.nombreCentro

            // Evento de clic para mostrar un mensaje con el ID de la revista
            itemView.setOnClickListener {
                //Toast.makeText(context, "ID de la revista: ${revista.id}", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "VistaInformesTecnicos")
                    putExtra("idInforme" , informeTecnico.idInforme.toString())
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idCentro" , idCentro)
                }
                context.startActivity(intent)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_informe_tecnico, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informesTecnicos[position])
    }

    override fun getItemCount(): Int {
        return informesTecnicos.size
    }

    fun filter(query: String) {
        informesTecnicos = if (query.isEmpty()) {
            allInformeTecnico // Si la búsqueda está vacía, mostrar todas las revistas
        } else {
            allInformeTecnico.filter {
                it.nombreCentro.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged() // Notificar al RecyclerView de los cambios
    }
}
