package items

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import modelosDatos.EdicionDesdeRevista

class ArticulosEnEdicionesAdapter(private val edicionesList: List<EdicionDesdeRevista>, private val context: Context , private val idUsuario: String , private val tipoUsuario: String , private val idRevista: String ) :
    RecyclerView.Adapter<ArticulosEnEdicionesAdapter.ArticuloViewHolder>() {

    inner class ArticuloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreArticulo: TextView = itemView.findViewById(R.id.txtTituloEdicion)
        val tipoArticulo: TextView = itemView.findViewById(R.id.txtTematicaEdicion)
        val idArticulo: TextView = itemView.findViewById(R.id.txtIdEdicion)

        fun bind(edicion: EdicionDesdeRevista) {
            nombreArticulo.text = edicion.nombre
            tipoArticulo.text = edicion.editor
            idArticulo.text = edicion.id

            // Evento de clic para mostrar el nombre del artículo
            itemView.setOnClickListener {
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "vistaEdiciones")
                    putExtra("idRevista", idRevista)
                    putExtra("idUsuario", idUsuario)
                    putExtra("tipoUsuario", tipoUsuario)
                    putExtra("idEdicion", edicion.id)
                }
                context.startActivity(intent)
                 Toast.makeText(context, "Articulodasd: ${edicion.nombre}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ediciones_desde_revistas, parent, false)
        return ArticuloViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        val edicion = edicionesList[position]
        holder.bind(edicion)
    }

    override fun getItemCount(): Int {
        return edicionesList.size
    }
}
