package popups


import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import modelosDatos.URL
import org.json.JSONArray

class PopupHelperVerificarUsuarioParaCambioDeContrasena(private val context: Context) {

    var url = URL.BASE_URL+"Usuario/ObtenerIdUsuarioPorUsuario.php"
    private lateinit var btnValidarusuario : Button
    private lateinit var txtUsuario : EditText

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_verificar_usuario_para_cambio_de_contrasena, null)

        // Crear PopupWindow con el ancho deseado
        val popupWindow = PopupWindow(
            popupView,
            dpToPx(350),  // Convertir dp a píxeles
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Configurar fondo transparente
        popupWindow.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = false

        // Atenuar el fondo
        setActivityBackgroundAlpha(0.5f)
        popupWindow.setOnDismissListener {
            setActivityBackgroundAlpha(1.0f)
        }

        // Mostrar popup centrado
        popupWindow.showAtLocation(anchorView, android.view.Gravity.CENTER, 0, 0)

        AnimationHelper.animatePopupView(popupView, 200) {
            // Aquí puedes agregar código adicional si es necesario después de la animación
        }

       // setPopupBackGroundHelper(popupView, context)




        // Configurar TextViews y EditTexts
        txtUsuario = popupView.findViewById(R.id.txtUsuario)

        // Configurar DatePicker

        // Botón crear revista
        btnValidarusuario = popupView.findViewById(R.id.btnValidarUsuario)
        btnValidarusuario.isEnabled = false

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtUsuario.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtUsuario , btnValidarusuario)
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        // Manejar la creación de la revista
        btnValidarusuario.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
            val nombre = btnValidarusuario.text.toString()

            if (nombre.isNotEmpty()) {
                verificarUsuario(popupWindow)
                popupWindow.dismiss()
            } else {
                //Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
            }
        }


    }

    private fun checkInputFields(
        txtTipoCongreso: EditText,
        btnCrearTipoCongreso: Button
    ) {
        val isEnabled = txtTipoCongreso.text.isNotEmpty()
        btnCrearTipoCongreso.isEnabled = isEnabled
        btnCrearTipoCongreso.setBackgroundColor(
            if (isEnabled) context.getColor(R.color.colorButtonEnabled) // Color cuando está habilitado
            else context.getColor(R.color.colorButtonDefault) // Color cuando está deshabilitado
        )
    }



    private fun setActivityBackgroundAlpha(alpha: Float) {
        val activity = context as? AppCompatActivity
        activity?.window?.apply {
            val params = attributes
            params.alpha = alpha
            attributes = params
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }


    fun verificarUsuario( popupWindow: PopupWindow ) {
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                when (response) {
                    "ERROR 1" -> {
                        //Toast.makeText(context, "Se deben llenar todos los campos", Toast.LENGTH_SHORT).show()
                    }
                    "ERROR 2" -> {
                        //Toast.makeText(context, "El usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                        btnValidarusuario.setClickable(false)
                        val jsonArray = JSONArray(response)
                        val idUsuario = jsonArray.getJSONObject(0).getString("id_usuario")
                        //Toast.makeText(context, "ID Usuario " + idUsuario, Toast.LENGTH_SHORT).show()
                        //Toast.makeText(applicationContext, "Bienvenido", Toast.LENGTH_SHORT).show()

                            val intent = Intent(context, PantallaDeCarga::class.java).apply {
                                putExtra("proximaPagina", "cambiarContrasena")
                                putExtra("idUsuario", idUsuario)
                            }
                        context.startActivity(intent) // Usar el contexto para iniciar la actividad
                        popupWindow.dismiss()




                    }
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }) {

            override fun getParams(): Map<String, String> {
                return mapOf(
                    "usuario" to txtUsuario.text.toString(),

                )
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}
