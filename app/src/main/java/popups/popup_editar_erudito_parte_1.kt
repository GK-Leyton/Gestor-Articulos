package popups

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
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
import modelosDatos.centros
import modelosDatos.Genero
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap
import java.util.Locale

class PopupHelperEditarErudito(private val context: Context , private val idAutor: String , private val idUsuario: String , private val tipoUsuario: String) {

    var url4 = URL.BASE_URL+"Autores/ObtenerLaInformacionDeUnAutor.php"
    var url = URL.BASE_URL+"Genero/ObtenerGenero.php"
    var url2 = URL.BASE_URL+"Centro/ObtenerCentros.php"
    var url3 = URL.BASE_URL+"Autores/EditarAutor.php"
    var nombreOriginal = ""
    var correoOriginal = ""
    var generoOriginal = ""
    var centroOriginal = ""
    var generoOriginalLimpio = ""
    var centroOriginalLimpio = ""

    private val generoList = mutableListOf<Genero>()
    private val centroList = mutableListOf<centros>()
    private lateinit var txtNombreErudito : EditText
    private lateinit var txtCorreo : EditText

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_erudito_parte1, null)

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

        val spinnerGenero: Spinner = popupView.findViewById(R.id.spinnerGeneroErudito)
        val spinnerCentro: Spinner = popupView.findViewById(R.id.spinnerCentroErudito)




        ObtenerInformacionDelAutor(idAutor.toInt() , spinnerGenero , spinnerCentro)
        /*Thread.sleep(200)
        obtenerGeneros(spinnerGenero)
        obtenerCentros(spinnerCentro)*/




        // Configurar TextViews y EditTexts
        txtNombreErudito = popupView.findViewById(R.id.txtNombreErudito)
        txtCorreo = popupView.findViewById(R.id.txtCorreoErudito)



        // Botón crear Congreso
        val btnCrearErudito: Button = popupView.findViewById(R.id.btnCrearErudito)
        btnCrearErudito.isEnabled = false // Deshabilitar inicialmente

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtNombreErudito.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreErudito, txtCorreo, spinnerGenero, spinnerCentro, btnCrearErudito)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        txtCorreo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreErudito, txtCorreo, spinnerGenero, spinnerCentro, btnCrearErudito)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // Configurar listener para el Spinner
        spinnerGenero.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               // Toast.makeText(context, "Genero seleccionado: ${spinnerGenero.selectedItem}", Toast.LENGTH_SHORT).show()
                checkInputFields(txtNombreErudito, txtCorreo, spinnerGenero, spinnerCentro, btnCrearErudito)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerCentro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               // Toast.makeText(context, "Centro seleccionado: ${spinnerCentro.selectedItem}", Toast.LENGTH_SHORT).show()
                checkInputFields(txtNombreErudito, txtCorreo, spinnerGenero, spinnerCentro, btnCrearErudito)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Manejar la creación de la Congreso
        btnCrearErudito.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                val auxGenero = spinnerGenero.selectedItem.toString()
                val auxCentro = spinnerCentro.selectedItem.toString()

                val nombre = if (txtNombreErudito.text.toString().trim().equals(nombreOriginal.trim(), ignoreCase = true) || txtNombreErudito.text.toString().trim().isEmpty()) "null" else txtNombreErudito.text.toString().trim()
                val correo = if (txtCorreo.text.toString().trim().equals(correoOriginal.trim(), ignoreCase = true) || txtCorreo.text.toString().trim().isEmpty()) "null" else txtCorreo.text.toString().trim()
                val idGenero = if(spinnerGenero.selectedItemPosition == 0 || spinnerGenero.selectedItem.toString().trim().equals(generoOriginalLimpio.trim(), ignoreCase = true )) "null" else generoList.find { it.genero == auxGenero }?.id
                val idCentro = if(spinnerCentro.selectedItemPosition == 0 || spinnerCentro.selectedItem.toString().trim().equals(centroOriginalLimpio.trim(), ignoreCase = true )) "null" else centroList.find { it.centro == auxCentro }?.id

                if (nombre.isNotEmpty() || correo.isNotEmpty() || spinnerGenero.selectedItemPosition != 0 || spinnerCentro.selectedItemPosition != 0) {
                   /* Toast.makeText(context, "nombre " + nombre, Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "correo " + correo, Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "idGenero " + idGenero, Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "idCentro " + idCentro, Toast.LENGTH_SHORT).show()*/
                    //crearAutor(nombre, correo, idCentro.toString(), idGenero.toString())


                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Confirmación")
                    builder.setMessage("¿Estás seguro de completar la acción?")

                    builder.setPositiveButton("Aceptar") { dialog, _ ->
                        // Lógica para el botón "Aceptar"
                        EditarAutor(idAutor, nombre, correo, idCentro.toString(), idGenero.toString())
                        val intent = Intent(context, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina" , "PerfilAutor")
                            putExtra("tipoUsuario" , tipoUsuario)
                            putExtra("idUsuario" , idUsuario)
                            putExtra("idAutor" , idAutor)
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




                } else {
                    Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun checkInputFields(
        txtNombreErudito: EditText,
        txtCorreo: EditText,
        spinnerTematica: Spinner,
        spinnerCentro: Spinner,
        btnCrearErudito: Button
    ) {
        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(txtCorreo.text.toString()).matches()

        val isEnabled = txtNombreErudito.text.isNotEmpty() ||
                txtCorreo.text.isNotEmpty() ||
                isValidEmail ||
                spinnerTematica.selectedItemPosition != 0 ||
                spinnerCentro.selectedItemPosition != 0

        btnCrearErudito.isEnabled = isEnabled
        btnCrearErudito.setBackgroundColor(
            if (isEnabled) context.getColor(R.color.colorButtonEnabled) // Color cuando está habilitado
            else context.getColor(R.color.colorButtonDefault) // Color cuando está deshabilitado
        )

        // Mostrar error en el campo de correo si no es válido
        if (!isValidEmail && txtCorreo.text.isNotEmpty()) {
            txtCorreo.error = "Correo no válido"
        }
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

    fun obtenerGeneros(spinner: Spinner) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                       // Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            generoList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val genero = jsonArray.getJSONObject(i)
                                val id_genero = genero.getString("id_genero").toInt()
                                val nombre_genero = genero.getString("genero")

                                // Agregar a la lista de temas
                                generoList.add(Genero(id_genero, nombre_genero))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + generoList.map { it.genero }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

                            // Obtener el índice correcto para el género


                            val nombreGenero = generoList.find { it.id == generoOriginal.toInt() }?.genero.toString()
                            generoOriginalLimpio = nombreGenero.trim().toLowerCase(Locale.ROOT)
                            val indice = generoList.indexOfFirst { it.genero.trim().toLowerCase(Locale.ROOT) == generoOriginalLimpio }
                            spinner.setSelection(indice + 1)

                            //Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
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


    fun obtenerCentros(spinner: Spinner) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url2,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                       // Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            centroList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val centro = jsonArray.getJSONObject(i)
                                val id_centro = centro.getString("id_centro").toInt()
                                val nombre = centro.getString("nombre")

                                // Agregar a la lista de temas
                                centroList.add(centros(id_centro, nombre))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + centroList.map { it.centro }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

                            val nombreCentro = centroList.find { it.id == centroOriginal.toInt() }?.centro.toString()
                            centroOriginalLimpio = nombreCentro.trim().toLowerCase(Locale.ROOT)
                            val indice = centroList.indexOfFirst { it.centro.trim().toLowerCase(Locale.ROOT) == centroOriginalLimpio }
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

    fun EditarAutor(idAutor: String, nombre: String, correo: String, centro: String, genero: String) {
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
                      //  else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                   // Toast.makeText(context, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Error en la solicitud
              //  Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idInvestigador"] = idAutor
                params["nombreInvestigador"] = nombre
                params["correoInvestigador"] = correo
                params["generoInvestigador"] = genero
                params["centroInvestigador"] = centro
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun ObtenerInformacionDelAutor(idAutor: Int , spinnerGenero: Spinner , spinnerCentro: Spinner) {

        val stringRequest = object : StringRequest(
            Request.Method.POST, url4,
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
                        val Autor = jsonArray.getJSONObject(i)
                        val nombreAutor = Autor.getString("nombre")
                        val correoAutor = Autor.getString("correo")
                        val generoAutor = Autor.getString("genero")
                        val centroAutor = Autor.getString("centro")

                        // Agregar el artículo a la lista
                        txtNombreErudito.setText(nombreAutor)
                        txtCorreo.setText(correoAutor)

                        nombreOriginal = nombreAutor
                        correoOriginal = correoAutor
                        generoOriginal = generoAutor
                        centroOriginal = centroAutor

                        if(i == ultimoIndice){
                            obtenerGeneros(spinnerGenero)
                            obtenerCentros(spinnerCentro)
                        }


                    }

                    // Notificar al adaptador del RecyclerView


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
                params["idAutor"] = idAutor.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }

}
