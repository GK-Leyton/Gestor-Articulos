package popups

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import modelosDatos.Autor
import modelosDatos.Palabra
import modelosDatos.TipoArticulo
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class PopupHelperEditarInformacionArticulo(private val context: Context , private val idUsuario : String , private val tipoUsuario : String , private val idArticulo : String , private val desde : String) {

    private val selectedItems = ArrayList<Palabra>() // ArrayList para almacenar items seleccionados



    var url2 = URL.BASE_URL+"Articulos/EditarInformacionArticulo.php"
    var url3 = URL.BASE_URL+"Autores/ObtenerTodosLosAutores.php"
    var url4 = URL.BASE_URL+"Articulos/ObtenerInformacionDeUnArticuloPorIdArticulo.php"
    private val palabrasList = mutableListOf<Palabra>()
    private val tipoarticulosList = mutableListOf<TipoArticulo>()
    private val autoresList = mutableListOf<Autor>()


    // Declaración global de componentes
    private lateinit var txtTituloArticuloAbreviado: EditText
    private lateinit var txtTituloArticulo: EditText
    private lateinit var btnCrearArticulo: Button
    private lateinit var spinnerAutor: Spinner
    private lateinit var txtCorreoContacto: EditText
    private lateinit var txtSacadoDe: EditText
    private lateinit var txtURL: EditText

    var tituloOiginal = ""
    var tituloAbreviadoOriginal = ""
    var autorOriginal = ""
    var autorOriginalLimpio = ""
    var correoContactoOriginal = ""
    var localizacionOriginal = ""
    var urlOriginal = ""

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_informacion_articulo_parte_1, null)

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

        popupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN


        // Mostrar popup centrado
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)



        AnimationHelper.animatePopupView(popupView, 200) {
            // Aquí puedes agregar código adicional si es necesario después de la animación
        }

        setPopupBackGroundHelper(popupView, context)

        txtTituloArticuloAbreviado = popupView.findViewById(R.id.txtTituloArticuloAbreviado)
        txtTituloArticulo = popupView.findViewById(R.id.txtTituloArticulo)
        btnCrearArticulo = popupView.findViewById(R.id.btnEditarArticulo)
        spinnerAutor = popupView.findViewById(R.id.spinnerAutores)
        txtCorreoContacto = popupView.findViewById(R.id.txtCorreoContacto)
        txtSacadoDe = popupView.findViewById(R.id.txtSacadoDe)
        txtURL = popupView.findViewById(R.id.txtURL)


        ObtenerInformacionDeUnArticuloPorIdArticulo(idArticulo.toInt() , spinnerAutor)
        /*Thread.sleep(200)
        obtenerAutores(spinnerAutor)*/



        btnCrearArticulo.isEnabled = false // Deshabilitar inicialmente

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtTituloArticuloAbreviado.addTextChangedListener(createTextWatcher())
        txtTituloArticulo.addTextChangedListener(createTextWatcher())
        txtCorreoContacto.addTextChangedListener(createTextWatcher())
        txtSacadoDe.addTextChangedListener(createTextWatcher())
        txtURL.addTextChangedListener(createTextWatcher())

        // Configurar el listener para los Spinners


        // Manejar la creación del artículo
        btnCrearArticulo.setOnClickListener {
            //Toast.makeText(context, "Artículo creado: ${txtTituloArticulo.text}", Toast.LENGTH_SHORT).show()
            val titulo = if (txtTituloArticulo.text.toString().trim().equals(tituloOiginal.trim(), ignoreCase = true) || txtTituloArticulo.text.toString().trim().isEmpty()) "null" else txtTituloArticulo.text.toString().trim()
            val tituloAbreviado = if (txtTituloArticuloAbreviado.text.toString().trim().equals(tituloAbreviadoOriginal.trim(), ignoreCase = true) || txtTituloArticuloAbreviado.text.toString().trim().isEmpty()) "null" else txtTituloArticuloAbreviado.text.toString().trim()
            val correoContacto = if (txtCorreoContacto.text.toString().trim().equals(correoContactoOriginal.trim(), ignoreCase = true) || txtCorreoContacto.text.toString().trim().isEmpty()) "null" else txtCorreoContacto.text.toString().trim()
            val localizacion = if (txtSacadoDe.text.toString().trim().equals(localizacionOriginal.trim(), ignoreCase = true) || txtSacadoDe.text.toString().trim().isEmpty()) "null" else txtSacadoDe.text.toString().trim()
            val url = if (txtURL.text.toString().trim().equals(urlOriginal.trim(), ignoreCase = true) || txtURL.text.toString().trim().isEmpty()) "null" else txtURL.text.toString().trim()
            val autorAux = spinnerAutor.selectedItem.toString()
            val idAutor = if(spinnerAutor.selectedItemPosition == 0 || spinnerAutor.selectedItem.toString().trim().equals(autorOriginalLimpio.trim(), ignoreCase = true )) "null" else autoresList.find { it.nombre == autorAux }?.id

            if(titulo.isNotEmpty() || tituloAbreviado.isNotEmpty() || correoContacto.isNotEmpty() || localizacion.isNotEmpty() || url.isNotEmpty() || spinnerAutor.selectedItemPosition != 0){

                /*Toast.makeText(context, "Titulo : $titulo", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Titulo Abreviado : $tituloAbreviado", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Correo Contacto : $correoContacto", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Localizacion : $localizacion", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "URL : $url", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Autor : $idAutor", Toast.LENGTH_SHORT).show()*/

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    // Lógica para el botón "Aceptar"
                    editarArticulo(titulo , tituloAbreviado , idAutor.toString() , correoContacto , localizacion , url)
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "vistaArticulos")
                        putExtra("tipoUsuario" , tipoUsuario)
                        putExtra("idUsuario" , idUsuario)
                        putExtra("idArticulo" , idArticulo)
                        putExtra("desde" , desde)
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



    private fun checkInputFields() {
        val isText1NotEmpty = txtTituloArticuloAbreviado.text.isNotEmpty()
        val isText2NotEmpty = txtTituloArticulo.text.isNotEmpty()

        // Validar que el correo sea válido
        val isCorreoContactoValid = txtCorreoContacto.text.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(txtCorreoContacto.text).matches()

        val isSacadoDeNotEmpty = txtSacadoDe.text.isNotEmpty()
        val isURLNotEmpty = txtURL.text.isNotEmpty()


        val isAutorSelected = spinnerAutor.selectedItem != "Selecciona un autor"

        // Activar/desactivar el botón según las validaciones
        btnCrearArticulo.isEnabled = isText1NotEmpty &&
                isText2NotEmpty &&
                isCorreoContactoValid &&
                isSacadoDeNotEmpty &&
                isURLNotEmpty &&
                isAutorSelected

        if (btnCrearArticulo.isEnabled) {
            btnCrearArticulo.setBackgroundColor(context.getColor(R.color.colorButtonEnabled))
        } else {
            btnCrearArticulo.setBackgroundColor(context.getColor(R.color.colorButtonDefault))
        }

        if (!isCorreoContactoValid && txtCorreoContacto.text.isNotEmpty()) {
            txtCorreoContacto.error = "Correo no válido"
        }
    }



    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields()
            }

            override fun afterTextChanged(s: Editable?) {}
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

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }



    fun obtenerAutores (spinner: Spinner) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url3,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        //Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            autoresList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val autor = jsonArray.getJSONObject(i)
                                val id_investigador = autor.getString("id_investigador").toInt()
                                val nombre = autor.getString("nombre")

                                // Agregar a la lista de temas
                                autoresList.add(Autor(nombre , id_investigador , "" , ""))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Selecciona una opción") + autoresList.map { it.nombre }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter


                            val nombreAutor = autoresList.find { it.id == autorOriginal.toInt() }?.nombre.toString()
                            autorOriginalLimpio = nombreAutor.trim().toLowerCase(Locale.ROOT)
                            val indice = autoresList.indexOfFirst { it.nombre.trim().toLowerCase(
                                Locale.ROOT) == autorOriginalLimpio }
                            spinner.setSelection(indice + 1)

                           // Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                           // Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
               // Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun ObtenerInformacionDeUnArticuloPorIdArticulo(idArticulo: Int , spinnerAutor: Spinner) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url4,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                        //Toast.makeText(context, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                    // Limpiar la lista de artículos si existe

                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    val ultimoIndice = jsonArray.length() - 1
                    for (i in 0 until jsonArray.length()) {
                        val articulo = jsonArray.getJSONObject(i)
                        val tituloArticulo = articulo.getString("titulo") // Correcto
                        val tituloAbreviado = articulo.getString("nombre_corto")
                        val correoContacto = articulo.getString("correo")
                        val urlArticulo = articulo.getString("url")
                        val localizacion = articulo.getString("localizacion")
                        val id_autor = articulo.getString("id_autor")

                        txtTituloArticuloAbreviado.setText(tituloAbreviado.toString())
                        txtTituloArticulo.setText(tituloArticulo.toString())
                        txtCorreoContacto.setText(correoContacto.toString())
                        txtSacadoDe.setText(localizacion.toString())
                        txtURL.setText(urlArticulo.toString())

                        tituloOiginal = tituloArticulo.toString()
                        tituloAbreviadoOriginal = tituloAbreviado.toString()
                        autorOriginal = id_autor
                        correoContactoOriginal = correoContacto.toString()
                        localizacionOriginal = localizacion.toString()
                        urlOriginal = urlArticulo.toString()

                        if (i == ultimoIndice) {
                            obtenerAutores(spinnerAutor)
                        }

                    }


                   // Toast.makeText(context, "Préstamos obtenidos con éxito", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                   // Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
               // Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idArticulo"] = idArticulo.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun editarArticulo( titulo: String , tituloAbreviado: String , autor: String , correo: String , localizacion: String , url: String ) {
        val stringRequest = object : StringRequest(
            Method.POST, url2,
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
                               // 1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                               // 2 -> Toast.makeText(context, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                               // else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
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
                val params = java.util.HashMap<String, String>()
                params["titulo"] = titulo.toString()
                params["tituloAbreviado"] = tituloAbreviado.toString()
                params["idAutor"] = autor.toString()
                params["correoElectronico"] = correo.toString()
                params["sacadoDe"] = localizacion.toString()
                params["url"] = url.toString()
                params["idArticulo"] = idArticulo.toString()

                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }
}
