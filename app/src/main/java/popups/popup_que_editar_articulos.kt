package popups

import PopupHelperEditarPalabrasClaveArticulo
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

class PopupHelperQueEditarArticulos(private val context: Context , private val idUsuario : String , private val tipoUsuario : String , private val tipoArticulo : String , private val idArticulo : String , private val desde : String) {

    fun showPopup(anchorView: View) {
        // Inflar el diseño del popup
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_que_editar_articulos, null)

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


        val btnEditarInformacion : ImageButton = popupView.findViewById(R.id.btnEditarInformacion)
        val btnEditarPalabraClave : ImageButton = popupView.findViewById(R.id.btnEditarPalabraClave)
        val btnEditarTipoArticulo : ImageButton = popupView.findViewById(R.id.btnEditarTipoArticulo)






        btnEditarInformacion.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperEditarInformacionArticulo(context , idUsuario , tipoUsuario , idArticulo , desde)
                popupHelper.showPopup(anchorView)
            }
        }
        btnEditarPalabraClave.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                //val popupHelper = PopupHelperEditarPalabrasClaveArticulo(context, idUsuario, tipoUsuario, idArticulo, desde)
                val popupHelper = PopupHelperEditarPalabrasClaveArticulo(context , idUsuario , tipoUsuario , idArticulo , desde)
                popupHelper.showPopup(anchorView)

            }
        }
        btnEditarTipoArticulo.setOnClickListener {
           // Toast.makeText(context, "Tipo articulo " + tipoArticulo, Toast.LENGTH_SHORT).show()
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperEditarArticuloCambiarTipoArticulo(context , idArticulo , idUsuario , tipoUsuario , desde , tipoArticulo)
                popupHelper.showPopup(anchorView)

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
