package popups

import android.app.DatePickerDialog
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
import modelosDatos.Ciudad
import modelosDatos.Congreso
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.HashMap
import java.util.Locale

class PopupHelperEditarActaCongreso(private val context: Context , private val idActa: String , private val idUsuario: String , private val tipoUsuario: String , private val idCongresoPadre: String) {


    var url = URL.BASE_URL+"Ciudad/ObtenerTodasLasCiudades.php"
    var url2 = URL.BASE_URL+"Congresos/ObtenerTodosLosCongresos.php"
    var url3 = URL.BASE_URL+"Congresos/Actas/EditarActa.php"
    var url4 = URL.BASE_URL+"Congresos/Actas/ObtenerInformacionDeUnaActaPorIdActa.php"

    private lateinit var txtNombreActa: EditText
    private lateinit var txtFechaInicio: TextView
    private lateinit var txtFechaFin: TextView

    private val ciudadList = mutableListOf<Ciudad>()
    private val congresosList = mutableListOf<Congreso>()

    var nombreActaOriginal = ""
    var fechaInicioOriginal = ""
    var fechaFinOriginal = ""
    var ciudadOriginal = ""
    var ciudadOriginalLimpio = ""
    var congresoOriginal = ""
    var congresoOriginalLimpio = ""



    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_acta_congreso_parte1, null)

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


        val spinnerCiudad: Spinner = popupView.findViewById(R.id.spinnerCiudad)
        val spinnerCongreso: Spinner = popupView.findViewById(R.id.spinnerCongreso)



        obtenerInformacionDelaActa(idActa.toInt() , spinnerCiudad , spinnerCongreso)
        /*Thread.sleep(200)
        obtenerCiudades(spinnerCiudad)
        obtenerCongresos(spinnerCongreso)*/

        // Configurar TextViews y EditTexts
        txtNombreActa = popupView.findViewById(R.id.txtNombreActa)
        txtFechaInicio = popupView.findViewById(R.id.txtFechaInicio)
        txtFechaFin = popupView.findViewById(R.id.txtFechaFin)

        // Botón crear Congreso
        val btnCrearActa: Button = popupView.findViewById(R.id.btnEditarActa)
        btnCrearActa.isEnabled = false // Deshabilitar inicialmente


        txtFechaInicio.setOnClickListener {
            showDatePicker(txtFechaInicio)
        }
        txtFechaFin.setOnClickListener {
            showDatePicker(txtFechaFin)
        }






        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtNombreActa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreActa, txtFechaInicio, txtFechaFin , spinnerCiudad, spinnerCongreso, btnCrearActa)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        txtFechaInicio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreActa, txtFechaInicio, txtFechaFin, spinnerCiudad, spinnerCongreso, btnCrearActa)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // Configurar listener para el Spinner
        spinnerCiudad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Genero seleccionado: ${spinnerCiudad.selectedItem}", Toast.LENGTH_SHORT).show()
                checkInputFields(txtNombreActa, txtFechaInicio, txtFechaFin, spinnerCiudad , spinnerCongreso, btnCrearActa)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerCongreso.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Centro seleccionado: ${spinnerCongreso.selectedItem}", Toast.LENGTH_SHORT).show()
                checkInputFields(txtNombreActa, txtFechaInicio, txtFechaFin, spinnerCiudad, spinnerCongreso, btnCrearActa)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Manejar la creación de la Congreso
        btnCrearActa.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {


                val nombre = if (txtNombreActa.text.toString().trim().equals(nombreActaOriginal.trim(), ignoreCase = true) || txtNombreActa.text.toString().trim().isEmpty()) "null" else txtNombreActa.text.toString().trim()
                val fechaInicio = if (txtFechaInicio.text.toString().trim().equals(fechaInicioOriginal.trim(), ignoreCase = true) || txtFechaInicio.text.toString().trim().isEmpty()) "null" else txtFechaInicio.text.toString().trim()
                val fechaFin = if (txtFechaFin.text.toString().trim().equals(fechaFinOriginal.trim(), ignoreCase = true) || txtFechaFin.text.toString().trim().isEmpty()) "null" else txtFechaFin.text.toString().trim()
                val ciudadAux = spinnerCiudad.selectedItem.toString()
                val congresoAux = spinnerCongreso.selectedItem.toString()
                val idCiudad = if(spinnerCiudad.selectedItemPosition == 0 || spinnerCiudad.selectedItem.toString().trim().equals(ciudadOriginalLimpio.trim(), ignoreCase = true )) "null" else ciudadList.find { it.ciudad == ciudadAux }?.id
                val idCongreso = if(spinnerCongreso.selectedItemPosition == 0 || spinnerCongreso.selectedItem.toString().trim().equals(congresoOriginalLimpio.trim(), ignoreCase = true )) "null" else congresosList.find { it.nombre == congresoAux }?.id


                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) // Formato esperado con /

                val areDatesValid = try {
                    val fechaInicioTexto = txtFechaInicio.text.toString()
                    val fechaFinTexto = txtFechaFin.text.toString()
                    val fechaInicio = dateFormat.parse(fechaInicioTexto)
                    val fechaFin = dateFormat.parse(fechaFinTexto)
                    fechaInicio != null && fechaFin != null && (fechaInicio.before(fechaFin) || fechaInicio.equals(fechaFin))
                } catch (e: Exception) {
                    false // Considera inválido si hay error al analizar las fechas
                }



                if (nombre.isNotEmpty() && fechaInicio.isNotEmpty() && fechaFin.isNotEmpty() && spinnerCiudad.selectedItemPosition != 0 && spinnerCongreso.selectedItemPosition != 0 && areDatesValid) {
                /*Toast.makeText(context, "nombre " + nombre, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "fechaInicio " + fechaInicio, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "fechaFin " + fechaFin, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "idCiudad " + idCiudad, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "idCongreso " + idCongreso, Toast.LENGTH_SHORT).show()*/


                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Confirmación")
                    builder.setMessage("¿Estás seguro de completar la acción?")

                    builder.setPositiveButton("Aceptar") { dialog, _ ->
                        // Lógica para el botón "Aceptar"
                        editarActaCongreso(nombre, fechaInicio, fechaFin, idCiudad.toString(), idCongreso.toString())
                        val idCongresoAux = if (idCongreso != "null") idCongreso.toString () else idCongresoPadre
                        val intent = Intent(context, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina" , "VistaActas")
                            putExtra("tipoUsuario" , tipoUsuario)
                            putExtra("idUsuario" , idUsuario)
                            putExtra("idCongreso" , idCongresoAux)
                            putExtra("idActa" , idActa)
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
                Toast.makeText(context, "Por favor completa todos los campos o revisa sus valores", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }
            }
        }


    }

    private fun checkInputFields(
        txtNombreActa: EditText,
        txtFechaInicio: TextView,
        txtFechaFin: TextView,
        spinnerTematica: Spinner,
        spinnerCentro: Spinner,
        btnCrearActa: Button
    ) {


        val isEnabled = txtNombreActa.text.isNotEmpty() &&
                txtFechaInicio.text.isNotEmpty() &&
                txtFechaFin.text.isNotEmpty() &&
                spinnerTematica.selectedItemPosition != 0 &&
                spinnerCentro.selectedItemPosition != 0


        btnCrearActa.isEnabled = isEnabled
        btnCrearActa.setBackgroundColor(
            if (isEnabled) context.getColor(R.color.colorButtonEnabled) // Color cuando está habilitado
            else context.getColor(R.color.colorButtonDefault) // Color cuando está deshabilitado
        )
    }


    private fun showDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            textView.text = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay)
        }, year, month, day)

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
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

    fun obtenerCiudades(spinner: Spinner) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        //Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            ciudadList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val ciudades = jsonArray.getJSONObject(i)
                                val id_ciudad = ciudades.getString("id_ciudad").toInt()
                                val ciudad = ciudades.getString("ciudad")

                                // Agregar a la lista de temas
                                ciudadList.add(Ciudad(id_ciudad, ciudad))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + ciudadList.map { it.ciudad }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter


                            val nombreCiudad = ciudadList.find { it.id == ciudadOriginal.toInt() }?.ciudad.toString()
                            ciudadOriginalLimpio = nombreCiudad.trim().toLowerCase(Locale.ROOT)
                            val indice = ciudadList.indexOfFirst { it.ciudad.trim().toLowerCase(Locale.ROOT) == ciudadOriginalLimpio }
                            spinner.setSelection(indice + 1)

                            //Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            //Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun obtenerCongresos(spinner: Spinner) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url2,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        //Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            congresosList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val congreso = jsonArray.getJSONObject(i)
                                val id_congreso = congreso.getString("id_congreso").toInt()
                                val nombre = congreso.getString("nombre")
                                val tipo = congreso.getString("tipo")

                                // Agregar a la lista de temas
                                congresosList.add(Congreso(nombre, id_congreso, tipo))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + congresosList.map { it.nombre }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter


                            val nombreCongreso = congresosList.find { it.id == congresoOriginal.toInt() }?.nombre.toString()
                            congresoOriginalLimpio = nombreCongreso.trim().toLowerCase(Locale.ROOT)
                            val indice = congresosList.indexOfFirst { it.nombre.trim().toLowerCase(Locale.ROOT) == congresoOriginalLimpio }
                            spinner.setSelection(indice + 1)

                           // Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            //Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun editarActaCongreso(nombre: String , fechaInicio: String , fechaFIn: String , idCiudad: String , idCongreso: String ) {
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
                            //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                //Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["nombreActa"] = nombre
                params["fechaInicio"] = fechaInicio
                params["fechaFin"] = fechaFIn
                params["idCiudad"] = idCiudad
                params["idCongreso"] = idCongreso
                params["idActa"] = idActa
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }



    fun obtenerInformacionDelaActa(idActa: Int , spinnerCiudad : Spinner , spinnerCongreso : Spinner) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url4,
            Response.Listener<String> { response ->
                try {
                    println("Respuesta del servidor: $response")
                    if (response == "ERROR 2") {
                       // Toast.makeText(context, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    val ultimoIndice = jsonArray.length() - 1
                    for (i in 0 until jsonArray.length()) {
                        val acta = jsonArray.getJSONObject(i)
                        val nombre_acta = acta.getString("nombre")
                        val fecha_inicio = acta.getString("fecha_inicio")
                        val fecha_fin = acta.getString("fecha_fin")
                        val idCiudad = acta.getString("id_ciudad")
                        val idCongreso = acta.getString("id_congreso")

                        txtFechaInicio.setText(fecha_inicio.replace("-", "/"))
                        txtFechaFin.setText(fecha_fin.replace("-", "/"))
                        txtNombreActa.setText(nombre_acta)
                        nombreActaOriginal = nombre_acta
                        fechaInicioOriginal = fecha_inicio
                        fechaFinOriginal = fecha_fin
                        ciudadOriginal = idCiudad
                        congresoOriginal = idCongreso

                        if (i == ultimoIndice) {
                            obtenerCiudades(spinnerCiudad)
                            obtenerCongresos(spinnerCongreso)
                        }
                    }
                   // Toast.makeText(context, "Acta obtenida con éxito", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                  //  Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
               // Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idActa"] = idActa.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }

}