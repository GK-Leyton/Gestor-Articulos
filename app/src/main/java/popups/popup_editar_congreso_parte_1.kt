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
import modelosDatos.TipoCongreso
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PopupHelperEditarCongreso(private val context: Context , private val idCongreso: String , private val idUsuario: String , private val tipoUsuario: String) {

    var url = URL.BASE_URL+"Congresos/TipoCongreso/ObtenerTiposCongreso.php"
    var url2 = URL.BASE_URL+"Congresos/EditarCongreso.php"
    var url3 = URL.BASE_URL+"Congresos/ObtenerInformacionDeUnCongresoPorIdCongreso.php"
    private val TiposList = mutableListOf<TipoCongreso>()
    private lateinit var txtNombreRevista : EditText
    private lateinit var txtFrecuencia : EditText
    private lateinit var textViewDate : TextView
    private var nombreOriginal = ""
    private var frecuenciaOriginal = ""
    private var fechaOriginal = ""
    private var tipoOriginal = ""
    private var tipoOriginalLimpio = ""


    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_congreso_parte1, null)

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

        //Toast.makeText(context, "idCongreso " + idCongreso, Toast.LENGTH_SHORT).show()


        // Configurar Spinner
        val spinnerTematica: Spinner = popupView.findViewById(R.id.spinnerTipoCongreso)



        obtenerInformacionDelCongreso(idCongreso.toInt() , spinnerTematica)
        /*Thread.sleep(200)
        obtenerTemas(spinnerTematica)*/



        // Configurar TextViews y EditTexts
        txtNombreRevista = popupView.findViewById(R.id.txtNombreCongreso)
        txtFrecuencia = popupView.findViewById(R.id.txtFrecuencia)
        textViewDate = popupView.findViewById(R.id.textViewDate)

        // Configurar DatePicker
        textViewDate.setOnClickListener {
            showDatePicker(textViewDate)
        }

        // Botón crear Congreso
        val btnEditarCongreso: Button = popupView.findViewById(R.id.btnEditarCongreso)
        btnEditarCongreso.isEnabled = false // Deshabilitar inicialmente

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtNombreRevista.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreRevista, txtFrecuencia, spinnerTematica, btnEditarCongreso)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        txtFrecuencia.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreRevista, txtFrecuencia, spinnerTematica, btnEditarCongreso)

                // Validar el valor máximo de txtFrecuencia
                if (s.toString().isNotEmpty()) {
                    val frecuencia = s.toString().toIntOrNull() ?: 0
                    if (frecuencia > 3650) {
                        txtFrecuencia.error = "La frecuencia no puede ser mayor a 10 años"
                    } else {
                        txtFrecuencia.error = null // Limpiar el error si está dentro del rango
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // Configurar listener para el Spinner
        spinnerTematica.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                checkInputFields(txtNombreRevista, txtFrecuencia, spinnerTematica, btnEditarCongreso)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Manejar la creación de la Congreso
        btnEditarCongreso.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                val nombre = if (txtNombreRevista.text.toString().trim().equals(nombreOriginal.trim(), ignoreCase = true) || txtNombreRevista.text.toString().trim().isEmpty()) "null" else txtNombreRevista.text.toString().trim()
                val frecuencia = if (txtFrecuencia.text.toString().trim().equals(frecuenciaOriginal.trim(), ignoreCase = true) || txtFrecuencia.text.toString().trim().isEmpty()) "null" else txtFrecuencia.text.toString().trim()
                val fechaCreacion = if (textViewDate.text.toString().trim().equals(fechaOriginal.trim(), ignoreCase = true) || textViewDate.text.toString().trim().isEmpty()) "null" else textViewDate.text.toString().trim()
                val temaAux = spinnerTematica.selectedItem.toString()
                val idTema = if(spinnerTematica.selectedItemPosition == 0 || spinnerTematica.selectedItem.toString().trim().equals(tipoOriginalLimpio.trim(), ignoreCase = true )) "null" else TiposList.find { it.tipoCongreso == temaAux }?.id



            if (nombre.isNotEmpty() || frecuencia.isNotEmpty() && fechaCreacion.isNotEmpty() || spinnerTematica.selectedItemPosition != 0 || fechaCreacion.isNotEmpty()) {
                /*Toast.makeText(context, "Nombre " + nombre, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Frecuencia " + frecuencia, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Fecha " + fechaCreacion, Toast.LENGTH_SHORT).show()*/
                //Toast.makeText(context, "Tema " + idTema, Toast.LENGTH_SHORT).show()


                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    // Lógica para el botón "Aceptar"
                    editarCongreso(nombre, idTema.toString(), frecuencia, fechaCreacion)
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina" , "VistaCongreso")
                        putExtra("tipoUsuario" , tipoUsuario)
                        putExtra("idUsuario" , idUsuario)
                        putExtra("idCongreso" , idCongreso)
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
        txtNombreRevista: EditText,
        txtFrecuencia: EditText,
        spinnerTematica: Spinner,
        btnCrearRevista: Button
    ) {
        val isEnabled = txtNombreRevista.text.isNotEmpty() ||
                txtFrecuencia.text.isNotEmpty() ||
                spinnerTematica.selectedItemPosition != 0

        btnCrearRevista.isEnabled = isEnabled
        btnCrearRevista.setBackgroundColor(
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


    fun obtenerTemas(spinner: Spinner) {
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
                            TiposList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val tipo = jsonArray.getJSONObject(i)
                                val id_tipo = tipo.getString("id_tipo").toInt()
                                val tipo_congreso = tipo.getString("tipo_congreso")

                                // Agregar a la lista de temas
                                TiposList.add(TipoCongreso(id_tipo, tipo_congreso))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + TiposList.map { it.tipoCongreso }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

                            val nombreTipo = TiposList.find { it.id == tipoOriginal.toInt() }?.tipoCongreso.toString()
                            tipoOriginalLimpio = nombreTipo.trim().toLowerCase(Locale.ROOT)
                            val indice = TiposList.indexOfFirst { it.tipoCongreso.trim().toLowerCase(Locale.ROOT) == tipoOriginalLimpio }
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


    fun editarCongreso(nombre: String, tipo: String, frecuencia: String, fecha: String) {
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
                            //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                      //  else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al procesar la respuesta
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
                params["nombreCongreso"] = nombre
                params["tipoCongreso"] = tipo
                params["primerCongreso"] = fecha
                params["frecuenciaCongreso"] = frecuencia
                params["idCongreso"] = idCongreso
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }



    fun obtenerInformacionDelCongreso(idCongreso: Int , spinnerTematica: Spinner) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url3,
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
                        val congreso = jsonArray.getJSONObject(i)
                        val nombre = congreso.getString("nombre")
                        val frecuencia = congreso.getString("frecuencia")
                        val nombre_tipo_congreso = congreso.getString("nombre_tipo_congreso")
                        val primer_congreso = congreso.getString("primer_congreso")
                        val tipoCongreso = congreso.getString("tipo")


                        // Asignar los valores a las vistas
                        txtNombreRevista.setText(nombre)
                        txtFrecuencia.setText(frecuencia)
                        textViewDate.setText(primer_congreso)
                        tipoOriginal = tipoCongreso
                        nombreOriginal = nombre
                        frecuenciaOriginal = frecuencia
                        fechaOriginal = primer_congreso


                        if (i == ultimoIndice) {
                            obtenerTemas(spinnerTematica)
                        }

                    }

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
                params["idCongreso"] = idCongreso.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }
}
