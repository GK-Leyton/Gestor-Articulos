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

class PopupHelperCrearParte1(private val context: Context) {

    fun showPopup(anchorView: View) {
        // Inflar el diseño del popup
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_crear_parte1, null)

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

        // Manejo de eventos para los botones
        val btnCrearRevista: ImageButton = popupView.findViewById(R.id.btnCrearRevista)
        val btnCrearCongreso: ImageButton = popupView.findViewById(R.id.btnCrearCongreso)
        val btnCrearArticulo: ImageButton = popupView.findViewById(R.id.btnCrearArticulo)
        val btnCrearInformeTecnico : ImageButton = popupView.findViewById(R.id.btnCrearInformeTecnico)
        val btnCrearCentroInvestigacion : ImageButton = popupView.findViewById(R.id.btnCrearCentroInvestigacion)
        val btnCrearInvestigador : ImageButton = popupView.findViewById(R.id.btnCrearInvestigador)
        val btnCrearTipoCongreso : ImageButton = popupView.findViewById(R.id.btnCrearTipoCongreso)
        val btnCrearTema : ImageButton = popupView.findViewById(R.id.btnCrearTema)

        btnCrearRevista.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperCrearRevista(context)
                popupHelper.showPopup(anchorView)
            }
        }
        btnCrearCongreso.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperCrearCongreso(context)
                popupHelper.showPopup(anchorView)
            }
        }
        btnCrearArticulo.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperCrearArticulo(context)
                popupHelper.showPopup(anchorView)
            }
        }
        btnCrearInformeTecnico.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperCrearInformeTecnico(context)
                popupHelper.showPopup(anchorView)
            }
        }
        btnCrearCentroInvestigacion.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperCrearCentroInvestigacion(context)
                popupHelper.showPopup(anchorView)
            }
        }
        btnCrearInvestigador.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperCrearErudito(context)
                popupHelper.showPopup(anchorView)
            }
        }
        btnCrearTipoCongreso.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperCrearTipoCongreso(context)
                popupHelper.showPopup(anchorView)
            }
        }
        btnCrearTema.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                popupWindow.dismiss()
                val popupHelper = PopupHelperCrearTemasEdicionesParte1(context)
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
