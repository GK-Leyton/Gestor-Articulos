import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import popups.AnimationHelper

class BotonesMenuDialogFragment(private val anchorView: View , private val idUsuario : String , private val tipoUsuario : String) : DialogFragment() {

    private lateinit var btnEnEstanteria: ImageButton
    private lateinit var btnEnDespacho: ImageButton
    private lateinit var btnTodo: ImageButton
    private lateinit var btnAutores: ImageButton
    private lateinit var btnCongresos: ImageButton
    private lateinit var btnRevistas: ImageButton
    private lateinit var btnInformeTecnico: ImageButton
    private lateinit var frameTodo: FrameLayout
    private lateinit var frameReportes: FrameLayout
    private lateinit var btnReporte: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del DialogFragment
        return inflater.inflate(R.layout.popup_botones_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnEnEstanteria = view.findViewById(R.id.btnEnEstanteria)
        btnEnDespacho = view.findViewById(R.id.btnEnDespacho)
        btnAutores = view.findViewById(R.id.btnAutor)
        btnCongresos = view.findViewById(R.id.btnCongreso)
        btnTodo = view.findViewById(R.id.btnTodo)
        btnRevistas = view.findViewById(R.id.btnRevistas)
        frameTodo = view.findViewById(R.id.frame3)
        frameReportes = view.findViewById(R.id.frame8)
        btnReporte = view.findViewById(R.id.btnReportes)

        btnInformeTecnico = view.findViewById(R.id.btnInformeTecnico)


        //Toast.makeText(requireContext(),"id Usuario" + idUsuario, Toast.LENGTH_SHORT).show()
        //Toast.makeText(requireContext(),"tipo Usuario" + tipoUsuario, Toast.LENGTH_SHORT).show()

        if(tipoUsuario != 2.toString()){
            frameTodo.visibility = FrameLayout.GONE
            frameReportes.visibility = FrameLayout.GONE
        }

        // Configura los botones dentro del Dialog
        btnEnEstanteria.setOnClickListener {
            val intent = Intent(requireContext(), PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "articulosEnEstanteria")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
            }
            startActivity(intent)
            dismiss() // Cerrar el diálogo
        }

        btnEnDespacho.setOnClickListener {
            val intent = Intent(requireContext(), PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "articulosEnDespacho")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
            }
            startActivity(intent)
            dismiss() // Cerrar el diálogo
        }

        btnTodo.setOnClickListener {
            val intent = Intent(requireContext(), PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "todosLosArticulos")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
            }
            startActivity(intent)
            dismiss() // Cerrar el diálogo
        }

        btnAutores.setOnClickListener {
            val intent = Intent(requireContext(), PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "autores")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
            }
            startActivity(intent)
            dismiss() // Cerrar el diálogo
        }

        btnCongresos.setOnClickListener {
            val intent = Intent(requireContext(), PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "congresos")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
            }
            startActivity(intent)
            dismiss() // Cerrar el diálogo
        }

        btnRevistas.setOnClickListener {
            val intent = Intent(requireContext(), PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "revistas")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
            }
            startActivity(intent)
            dismiss() // Cerrar el diálogo
        }

        btnInformeTecnico.setOnClickListener {
            val intent = Intent(requireContext(), PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "Centros")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
            }
            startActivity(intent)
            dismiss() // Cerrar el diálogo
        }

        btnReporte.setOnClickListener {
            val intent = Intent(requireContext(), PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "reportes")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
            }
            startActivity(intent)
            dismiss() // Cerrar el diálogo
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Fondo transparente
        dialog?.window?.setDimAmount(0f) // No oscurecer el fondo

        // Obtener la posición del botón
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        // Convertir dp a píxeles
        val heightInDp = 70f
        val widthInDp = 280f
        val density = resources.displayMetrics.density // Obtener la densidad de píxeles
        val heightInPx = (heightInDp * density).toInt() // Convertir a píxeles
        val widthInPx = (widthInDp * density).toInt() // Convertir a píxeles

        // Ajustar la posición del Dialog
        dialog?.window?.attributes?.x = location[0] + 50 // Desplazarse a la derecha del botón
        dialog?.window?.attributes?.y = location[1] - 1100 // Ajusta este valor para que suba más
        dialog?.window?.attributes?.width = widthInPx // Establecer el ancho del dialogo
        dialog?.window?.attributes?.height = heightInPx // Establecer la altura del dialogo

        dialog?.window?.attributes = dialog?.window?.attributes // Aplicar los cambios

        // Animar el diálogo al aparecer
        AnimationHelper.animatePopupView(requireView(), 250) {
            // Puedes agregar código adicional aquí si es necesario
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // Animar el diálogo al cerrarse
        AnimationHelper.dismissPopupView(requireView(), 250) {
            // Aquí puedes agregar cualquier lógica adicional después de que se cierre el diálogo
        }
    }
}
