package items

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import modelosDatos.ArticuloDesdeActa

class ActasAdapter(private val articulosList: List<ArticuloDesdeActa>, private val context: Context , private val idUsuario: String , private val tipoUsuario: String) :
    RecyclerView.Adapter<ActasAdapter.ArticuloViewHolder>() {

    inner class ArticuloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreArticulo: TextView = itemView.findViewById(R.id.txtNombreArticulo)
        val tipoArticulo: TextView = itemView.findViewById(R.id.txtTipoArticulo)
        val idArticulo: TextView = itemView.findViewById(R.id.txtIdArticulo)

        fun bind(articulo: ArticuloDesdeActa) {
            nombreArticulo.text = articulo.nombre
            tipoArticulo.text = articulo.localizacion
            idArticulo.text = articulo.id.toString()

            // Evento de clic para mostrar el nombre del artículo
            itemView.setOnClickListener {
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "vistaArticulos")
                    putExtra("idArticulo", articulo.id)
                    putExtra("idUsuario", idUsuario)
                    putExtra("tipoUsuario", tipoUsuario)
                }
                context.startActivity(intent)
                // Aquí puedes agregar más lógica, como abrir un popup, actividad, etc.
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_articulo_desde_actas, parent, false)
        return ArticuloViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        val articulo = articulosList[position]
        holder.bind(articulo)
    }

    override fun getItemCount(): Int {
        return articulosList.size
    }
}
