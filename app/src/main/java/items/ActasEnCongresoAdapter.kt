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
import modelosDatos.Acta

// Adaptador para el RecyclerView
class ActasCongresoAdapter(
    private val actasList: List<Acta>,
    private val context: Context,
    idCongreso: String,
    idUsuario: String,
    tipoUsuario: String
) : RecyclerView.Adapter<ActasCongresoAdapter.ActaViewHolder>() {

    // Variables globales
    private val idCongresoGlobal = idCongreso
    private val idUsuarioGlobal = idUsuario
    private val tipoUsuarioGlobal = tipoUsuario

    // ViewHolder para cada ítem en el RecyclerView
    class ActaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreActa: TextView = itemView.findViewById(R.id.txtNombreActa)
        val fechaInicio: TextView = itemView.findViewById(R.id.txtFechaInicio)
        val ciudad: TextView = itemView.findViewById(R.id.txtCiudad)

        // Método para enlazar los datos y configurar el clic
        fun bind(
            acta: Acta,
            context: Context,
            idCongreso: String,
            idUsuario: String,
            tipoUsuario: String
        ) {
            // Enlazar los datos
            nombreActa.text = acta.nombre
            fechaInicio.text = acta.fechaInicio
            ciudad.text = acta.ciudad

            // Evento de clic en el ítem
            itemView.setOnClickListener {
                // Intent para cambiar de pantalla
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "VistaActas")
                    putExtra("idCongreso", idCongreso)
                    putExtra("idUsuario", idUsuario)
                    putExtra("tipoUsuario", tipoUsuario)
                    putExtra("idActa", acta.idActa)
                }
                context.startActivity(intent)

                // Mostrar un mensaje con el nombre del acta
                Toast.makeText(context, "Acta: ${acta.nombre}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActaViewHolder {
        // Inflar el diseño del ítem
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_actas, parent, false)
        return ActaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActaViewHolder, position: Int) {
        // Obtener el acta actual
        val acta = actasList[position]

        // Enlazar el ViewHolder con los datos y variables globales
        holder.bind(acta, context, idCongresoGlobal, idUsuarioGlobal, tipoUsuarioGlobal)
    }

    override fun getItemCount(): Int {
        return actasList.size
    }
}
