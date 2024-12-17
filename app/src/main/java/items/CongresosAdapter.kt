package com.example.segundoparcial

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import modelosDatos.Congreso

class CongresoAdapter(private var congresos: List<Congreso>, private val context: Context , private val idUsuario: String , private val tipoUsuario: String) : RecyclerView.Adapter<CongresoAdapter.ViewHolder>() {

    private var allCongresos: List<Congreso> = congresos

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemCongreso: View = itemView.findViewById(R.id.itemCongreso)
        val txtIdCongreso: TextView = itemView.findViewById(R.id.txtIdCongreso)
        val txtNombreCongreso: TextView = itemView.findViewById(R.id.txtNombreCongreso)
        val txtTipoCongreso: TextView = itemView.findViewById(R.id.txtTipoCongreso)

        val frame: FrameLayout = itemView.findViewById(R.id.frameCongreso)

        fun bind(congreso: Congreso) {

            txtIdCongreso.text = congreso.id.toString()
            txtNombreCongreso.text = congreso.nombre
            txtTipoCongreso.text = congreso.tipoCongreso


            when (congreso.tipoCongreso) {
                "Académicos" -> frame.setBackgroundResource(R.drawable.icono_congreso_academico)
                "Científicos" -> frame.setBackgroundResource(R.drawable.icono_congreso_cientifico)
                "Médicos" -> frame.setBackgroundResource(R.drawable.icono_congreso_medico)
                "Tecnológicos" -> frame.setBackgroundResource(R.drawable.icono_congreso_tecnologico)
                "Empresariales" -> frame.setBackgroundResource(R.drawable.icono_congreso_empresarial)
                "Políticos" -> frame.setBackgroundResource(R.drawable.icono_congreso_politico)
                "Sociales" -> frame.setBackgroundResource(R.drawable.icono_congreso_social)
                "Culturales" -> frame.setBackgroundResource(R.drawable.icono_congreso_cultural)
            }



            itemView.setOnClickListener {
                Toast.makeText(context, "ID del congreso: ${congreso.id}", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "VistaCongreso")
                    putExtra("idCongreso", congreso.id.toString())
                    putExtra("idUsuario", idUsuario)
                    putExtra("tipoUsuario", tipoUsuario)
                }
                // Usamos ContextCompat para iniciar la actividad desde el contexto
                startActivity(context, intent, null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_congreso, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(congresos[position])
    }

    override fun getItemCount(): Int {
        return congresos.size
    }

    fun filter(query: String) {
        congresos = if (query.isEmpty()) {
            allCongresos
        } else {
            allCongresos.filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}
