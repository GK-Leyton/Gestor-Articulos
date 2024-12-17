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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap


class PopupHelperEditarUsuario(private val context: Context ,  private val idUsuario: String , private val tipoUsuario: String) {



    var url = URL.BASE_URL+"Usuario/ObtenerInformacionDeUnUsuario.php"
    var url2 = URL.BASE_URL+"Usuario/ObtenerUsuarios.php"
    var url3 = URL.BASE_URL+"Usuario/EditarInformacionBasicaUsuario.php"

    private val usuariosList = mutableListOf<String>()

    var nombreOriginal = ""
    var usuarioOriginal = ""
    
    private lateinit var txtNombreUsuario : EditText
    private lateinit var txtUsuario : EditText
    private lateinit var btnCrearErudito : Button

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_usuario_parte1, null)

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

        setPopupBackGroundHelper(popupView, context)





        ObtenerInformacionDelUsuario(idUsuario.toInt())
        ObtenerTodosLosUsuarios()





        // Configurar TextViews y EditTexts
        txtNombreUsuario = popupView.findViewById(R.id.txtNombreUsuario)
        txtUsuario = popupView.findViewById(R.id.txtUsuario)



        // Botón crear Congreso
        btnCrearErudito = popupView.findViewById(R.id.btnEditarUsuario)
        btnCrearErudito.isEnabled = false

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtNombreUsuario.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreUsuario, txtUsuario, btnCrearErudito)
            }

            override fun afterTextChanged(s: Editable?) {
                checkInputFields(txtNombreUsuario, txtUsuario, btnCrearErudito)
            }

        })

        txtUsuario.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                checkInputFields(txtNombreUsuario, txtUsuario, btnCrearErudito)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreUsuario, txtUsuario, btnCrearErudito)
            }

            override fun afterTextChanged(s: Editable?) {
                checkInputFields(txtNombreUsuario, txtUsuario, btnCrearErudito)
            }
        })



        // Manejar la creación de la Congreso
        btnCrearErudito.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {

                val nombre = if (txtNombreUsuario.text.toString().trim().equals(nombreOriginal.trim(), ignoreCase = true) || txtNombreUsuario.text.toString().trim().isEmpty()) "null" else txtNombreUsuario.text.toString().trim()
                val usuario = if (txtUsuario.text.toString().trim().equals(usuarioOriginal.trim(), ignoreCase = true) || txtUsuario.text.toString().trim().isEmpty()) "null" else txtUsuario.text.toString().trim()

                if (nombre.isNotEmpty() || usuario.isNotEmpty()) {

                    /*Toast.makeText(context, "nombre " + nombre, Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "usuario " + usuario, Toast.LENGTH_SHORT).show()*/

                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Confirmación")
                    builder.setMessage("¿Estás seguro de completar la acción?")

                    builder.setPositiveButton("Aceptar") { dialog, _ ->
                        // Lógica para el botón "Aceptar"
                        EditarUsuario(idUsuario, nombre, usuario)
                        val intent = Intent(context, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "perfilUsuario")
                            putExtra("tipoUsuario", tipoUsuario)
                            putExtra("idUsuario", idUsuario)
                        }
                        context.startActivity(intent)
                        popupWindow.dismiss()
                        dialog.dismiss()
                    }

                    builder.setNegativeButton("Cancelar") { dialog, _ ->
                        // Lógica para el botón "Cancelar"
                        popupWindow.dismiss()
                        dialog.dismiss()
                    }

                    val dialog = builder.create()
                    dialog.show()


                }

            }
        }


    }

    private fun checkInputFields(
        txtNombreUsuario: EditText,
        txtUsuario: EditText,
        btnCrearErudito: Button
    ) {

        var isUserNameValid = true;

        if(usuariosList.isNotEmpty() && usuarioOriginal != ""){
            for (usuario in usuariosList) {

                if(usuario.lowercase() != usuarioOriginal.lowercase()){
                    if(usuario.lowercase() == txtUsuario.text.toString().lowercase()) {
                        txtUsuario.error = "El usuario ya existe"
                        isUserNameValid = false
                        break
                    }

                }
            }
            }



        val isEnabled = (txtNombreUsuario.text.isNotEmpty() ||
                txtUsuario.text.isNotEmpty()) && isUserNameValid



        btnCrearErudito.isEnabled = isEnabled
        btnCrearErudito.setBackgroundColor(
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



    fun EditarUsuario(idUsuario: String , nombre: String , usuario: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url3,
            Response.Listener<String> { response ->
                try {
                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                           // Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.getInt("code")
                            val message = jsonResponse.getString("message")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                              //  1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                              //  2 -> Toast.makeText(context, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                              //  else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                       // else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                   // Toast.makeText(context, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Error en la solicitud
               // Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idUsuario"] = idUsuario
                params["nombre"] = nombre
                params["usuario"] = usuario

                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun ObtenerInformacionDelUsuario(idUsuario: Int ) {

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                       // Toast.makeText(context , "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }



                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    val ultimoIndice = jsonArray.length() - 1
                    for (i in 0 until jsonArray.length()) {
                        val Usuario = jsonArray.getJSONObject(i)
                        val nombre = Usuario.getString("nombre")
                        val usuario = Usuario.getString("usuario")


                        // Agregar el artículo a la lista
                        txtNombreUsuario.setText(nombre)
                        txtUsuario.setText(usuario)

                        nombreOriginal = nombre
                        usuarioOriginal = usuario

                        if (i == ultimoIndice) {
                            checkInputFields(txtNombreUsuario, txtUsuario, btnCrearErudito)
                        }
                    }

                    // Notificar al adaptador del RecyclerView


                  //  Toast.makeText(context, "Préstamos obtenidos con éxito", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                  //  Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
              //  Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idUsuario"] = idUsuario.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun ObtenerTodosLosUsuarios() {

        val stringRequest = object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                       // Toast.makeText(context , "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                    usuariosList.clear()

                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val Usuario = jsonArray.getJSONObject(i)
                        val usuario = Usuario.getString("usuario")

                        usuariosList.add(usuario)
                    }


                } catch (e: Exception) {
                  //  Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
               // Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }

}
