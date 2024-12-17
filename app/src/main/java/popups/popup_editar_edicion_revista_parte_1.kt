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
import modelosDatos.Revista
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap
import java.util.Locale

class PopupHelperEditarEdicionRevista( private val context: Context, private val idRevistaOriginal: String , private val idEdicionOriginal : String , private val idUsuario : String , private val tipoUsuario : String) {


    var url = URL.BASE_URL+"Revistas/ObtenerTodasLasRevistas.php"
    val url2 = URL.BASE_URL+"Revistas/Edicion/ObtenerLaInformacionDeunaEdicionPorNumeroEdicinYIdRevista.php"
    var url3 = URL.BASE_URL+"Revistas/Edicion/EditarEdicion.php"

    private lateinit var txtNombreActa: EditText
    private lateinit var txtEditor: TextView
    private lateinit var txtPaginaInicio: TextView
    private lateinit var txtPaginaFin: TextView

    var nombreOriginal = ""
    var editorOriginal = ""
    var paginaInicioOriginal = ""
    var paginaFinOriginal = ""
    var revistaOriginal = ""
    var revistaOriginalLimpio = ""



    private val revistasList = mutableListOf<Revista>()

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_edicion_revista_parte1, null)

        // Crear PopupWindow con el ancho deseado
        val popupWindow = PopupWindow(
            popupView,
            dpToPx(350),  // Convertir dp a píxeles
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
       /* Toast.makeText(context, "Edicion editada con éxito", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "idEdicion para intent " + idEdicionOriginal, Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "idRevista para intent" + idRevistaOriginal, Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "idUsuario para intent" + idUsuario, Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "tipoUsuario para intent" + tipoUsuario, Toast.LENGTH_SHORT).show()*/

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


        val spinnerRevista: Spinner = popupView.findViewById(R.id.spinnerRevista)


        obtenerInformacionDeLaEdicion(idRevistaOriginal.toInt() , idEdicionOriginal.toInt() , spinnerRevista)
        /*Thread.sleep(200)
        obtenerRevistas(spinnerRevista)*/
        

        // Configurar TextViews y EditTexts
        txtNombreActa = popupView.findViewById(R.id.txtTituloEdicion)
        txtEditor = popupView.findViewById(R.id.txtEditor)
        txtPaginaInicio = popupView.findViewById(R.id.txtPaginaInicio)
        txtPaginaFin = popupView.findViewById(R.id.txtPaginaFin)

        



        // Botón crear Congreso
        val btnCrearEdicion: Button = popupView.findViewById(R.id.btnEditarEdicion)
        btnCrearEdicion.isEnabled = false // Deshabilitar inicialmente

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtNombreActa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreActa, txtEditor, txtPaginaInicio , txtPaginaFin ,spinnerRevista, btnCrearEdicion)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        txtEditor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreActa, txtEditor, txtPaginaInicio, txtPaginaFin, spinnerRevista, btnCrearEdicion)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        txtPaginaInicio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreActa, txtEditor, txtPaginaInicio, txtPaginaFin, spinnerRevista, btnCrearEdicion)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        txtPaginaFin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreActa, txtEditor, txtPaginaInicio, txtPaginaFin, spinnerRevista, btnCrearEdicion)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // Configurar listener para el Spinner
        spinnerRevista.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Genero seleccionado: ${spinnerRevista.selectedItem}", Toast.LENGTH_SHORT).show()
                checkInputFields(txtNombreActa, txtEditor, txtPaginaInicio, txtPaginaFin , spinnerRevista , btnCrearEdicion)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        // Manejar la creación de la Congreso
        btnCrearEdicion.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {

                val nombre = if (txtNombreActa.text.toString().trim().equals(nombreOriginal.trim(), ignoreCase = true) || txtNombreActa.text.toString().trim().isEmpty()) "null" else txtNombreActa.text.toString().trim()
                val fechaInicio = if (txtPaginaInicio.text.toString().trim().equals(paginaInicioOriginal.trim(), ignoreCase = true) || txtPaginaInicio.text.toString().trim().isEmpty()) "null" else txtPaginaInicio.text.toString().trim()
                val fechaFin = if (txtPaginaFin.text.toString().trim().equals(paginaFinOriginal.trim(), ignoreCase = true) || txtPaginaFin.text.toString().trim().isEmpty()) "null" else txtPaginaFin.text.toString().trim()
                val editor = if (txtEditor.text.toString().trim().equals(editorOriginal.trim(), ignoreCase = true) || txtEditor.text.toString().trim().isEmpty()) "null" else txtEditor.text.toString().trim()
                val auxRevista = spinnerRevista.selectedItem.toString()
                val idRevista = if(spinnerRevista.selectedItemPosition == 0 || spinnerRevista.selectedItem.toString().trim().equals(revistaOriginalLimpio.trim(), ignoreCase = true )) "null" else revistasList.find { it.nombre == auxRevista }?.id





            if (nombre.isNotEmpty() && fechaInicio.isNotEmpty() && fechaFin.isNotEmpty() && spinnerRevista.selectedItemPosition != 0 && editor.isNotEmpty()) {
               /* Toast.makeText(context, "nombre " + nombre, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "fechaInicio " + fechaInicio, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "fechaFin " + fechaFin, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "idCiudad " + idRevista, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "editor " + editor, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "idRevista " + idRevista, Toast.LENGTH_SHORT).show()*/


                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    // Lógica para el botón "Aceptar"
                    editarEdicionRevista(nombre , editor , fechaInicio , fechaFin , idRevista.toString())
                    val idRevistaAux = if (idRevista != "null") idRevista.toString() else idRevistaOriginal

                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina" , "vistaEdiciones")
                        putExtra("tipoUsuario" , tipoUsuario)
                        putExtra("idUsuario" , idUsuario)
                        putExtra("idEdicion" , idEdicionOriginal)
                        putExtra("idRevista" , idRevistaAux)
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
        txtNombreActa: EditText,
        txtEditor: TextView,
        txtPaginaInicio: TextView,
        txtPaginaFin: TextView,
        spinnerTematica: Spinner,
        btnCrearEdicion: Button
    ) {

        val paginaI = if (txtPaginaInicio.text.isEmpty()) 0 else txtPaginaInicio.text.toString().toInt()
        val paginaF = if (txtPaginaFin.text.isEmpty()) 0 else txtPaginaFin.text.toString().toInt()

        val isEnabled = txtNombreActa.text.isNotEmpty() &&
                txtEditor.text.isNotEmpty() &&
                txtPaginaInicio.text.isNotEmpty() &&
                txtPaginaFin.text.isNotEmpty() &&
                spinnerTematica.selectedItemPosition != 0 &&
                paginaI < paginaF



        btnCrearEdicion.isEnabled = isEnabled
        btnCrearEdicion.setBackgroundColor(
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

    fun obtenerRevistas(spinner: Spinner) {
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
                            revistasList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val revista = jsonArray.getJSONObject(i)
                                val id_revista = revista.getString("id_revista").toInt()
                                val nombre_revista = revista.getString("nombre")
                                val temaRevista = revista.getString("tema")

                                // Agregar a la lista de artículos
                                revistasList.add(
                                    Revista(
                                        nombre_revista,
                                        id_revista,
                                        temaRevista
                                    )
                                )
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + revistasList.map { it.nombre }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

                            val nombreRevista = revistasList.find { it.id == revistaOriginal.toInt() }?.nombre.toString()
                            revistaOriginalLimpio = nombreRevista.trim().toLowerCase(Locale.ROOT)
                            val indice = revistasList.indexOfFirst { it.nombre.trim().toLowerCase(Locale.ROOT) == revistaOriginalLimpio }
                            spinner.setSelection(indice + 1)


                          //  Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
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



    fun editarEdicionRevista(nombre : String , editor : String , paginaInicio : String , paginaFin : String , idRevista : String ) {
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
                            //    1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                            //    2 -> Toast.makeText(context, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                            //    else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
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
               // Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["tituloEdicion"] = nombre
                params["editor"] = editor
                params["paginaInicio"] = paginaInicio
                params["paginaFin"] = paginaFin
                params["idRevista"] = idRevista
                params["idEdicion"] = idEdicionOriginal
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun obtenerInformacionDeLaEdicion(idRevista: Int , idEdicion : Int , spinnerRevista : Spinner) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                       // Toast.makeText(context, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }


                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    val ultimoIndice = jsonArray.length() - 1
                    for (i in 0 until jsonArray.length()) {
                        val revista = jsonArray.getJSONObject(i)
                        val titulo = revista.getString("titulo")
                        val editor = revista.getString("editor")
                        val pagina_inicio = revista.getString("pagina_inicio")
                        val pagina_final = revista.getString("pagina_final")
                        val id_revista = revista.getString("id_revista")

                        txtNombreActa.setText(titulo)
                        txtEditor.setText(editor)
                        txtPaginaInicio.setText(pagina_inicio)
                        txtPaginaFin.setText(pagina_final)

                        nombreOriginal = titulo
                        editorOriginal = editor
                        paginaInicioOriginal = pagina_inicio
                        paginaFinOriginal = pagina_final
                        revistaOriginal = id_revista

                        if (i == ultimoIndice) {
                            obtenerRevistas(spinnerRevista)
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
                params["idRevista"] = idRevista.toString()
                params["idEdicion"] = idEdicion.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }

}
