package popups

import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupWindow
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.segundoparcial.R
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.segundoparcial.PantallaDeCarga

class PopupHelperQueHacerVerAutor(private val context: Context , private val idAutor : String , private val idUsuario : String , private val tipoUsuario : String) {

    fun showPopup(anchorView: View) {
        // Inflar el diseño del popup
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_que_hacer_ver_autor, null)

        // Crear PopupWindow con el tamaño específico
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Configurar fondo transparente fuera del popup
        popupWindow.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = true

        // Atenuar el fondo de la actividad
        setActivityBackgroundAlpha(0.5f)

        // Detectar cuando el popup se cierra para restaurar el fondo
        popupWindow.setOnDismissListener {
            setActivityBackgroundAlpha(1.0f)
        }

        // Mostrar el popup centrado en la pantalla
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)

        // Llamar a la función de animación de entrada
        AnimationHelper.animatePopupView(popupView, 200) {
            // Aquí puedes agregar código adicional si es necesario después de la animación
        }

        val btnVerArticulosAutor : ImageButton = popupView.findViewById(R.id.btnVerArticulosAutor)
        val btnPerfilAutor : ImageButton = popupView.findViewById(R.id.btnPerfilAutor)

        btnVerArticulosAutor.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "ArticulosPorAutor")
                    putExtra("idAutor" , idAutor)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("tipoUsuario" , tipoUsuario)
                }
                // Usamos ContextCompat para iniciar la actividad desde el contexto
                startActivity(context, intent, null)

                popupWindow.dismiss()
            }
        }

        btnPerfilAutor.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "PerfilAutor")
                    putExtra("idAutor" , idAutor)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("tipoUsuario" , tipoUsuario)
                }
                // Usamos ContextCompat para iniciar la actividad desde el contexto
                startActivity(context, intent, null)
                popupWindow.dismiss()
            }
        }




    }

    private fun setActivityBackgroundAlpha(alpha: Float) {
        val activity = context as? AppCompatActivity
        activity?.window?.apply {
            val params: WindowManager.LayoutParams = this.attributes
            params.alpha = alpha
            this.attributes = params
        }
    }
}
