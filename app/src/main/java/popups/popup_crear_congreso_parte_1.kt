package popups

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.segundoparcial.R
import modelosDatos.TipoCongreso
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PopupHelperCrearCongreso(private val context: Context) {

    var url = URL.BASE_URL+"Congresos/TipoCongreso/ObtenerTiposCongreso.php"
    var url2 = URL.BASE_URL+"Congresos/CrearCongreso.php"
    private val TiposList = mutableListOf<TipoCongreso>()

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_crear_congreso_parte1, null)

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



        // Configurar Spinner
        val spinnerTematica: Spinner = popupView.findViewById(R.id.spinnerTipoCongreso)
        //val temas = arrayOf("Seleccione una opción", "Tema 1", "Tema 2", "Tema 3") // Agrega un ítem neutro
        //val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temas)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //spinnerTematica.adapter = adapter

        obtenerTemas(spinnerTematica)

        // Configurar TextViews y EditTexts
        val txtNombreRevista: EditText = popupView.findViewById(R.id.txtNombreCongreso)
        val txtFrecuencia: EditText = popupView.findViewById(R.id.txtFrecuencia)
        val textViewDate: TextView = popupView.findViewById(R.id.textViewDate)

        // Configurar DatePicker
        textViewDate.setOnClickListener {
            showDatePicker(textViewDate)
        }

        // Botón crear Congreso
        val btnCrearRevista: Button = popupView.findViewById(R.id.btnCrearCongreso)
        btnCrearRevista.isEnabled = false // Deshabilitar inicialmente

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtNombreRevista.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreRevista, txtFrecuencia, spinnerTematica, btnCrearRevista)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        txtFrecuencia.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreRevista, txtFrecuencia, spinnerTematica, btnCrearRevista)

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
                checkInputFields(txtNombreRevista, txtFrecuencia, spinnerTematica, btnCrearRevista)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Manejar la creación de la Congreso
        btnCrearRevista.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                val nombre = txtNombreRevista.text.toString()
                val frecuencia = txtFrecuencia.text.toString()
                val fechaCreacion = textViewDate.text.toString()
                val auxTema = spinnerTematica.selectedItem.toString()
                val idTema = TiposList.find { it.tipoCongreso == auxTema }?.id


            if (nombre.isNotEmpty() && frecuencia.isNotEmpty() && fechaCreacion.isNotEmpty() && spinnerTematica.selectedItemPosition != 0) {
                /*Toast.makeText(context, "Nombre " + nombre, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Frecuencia " + frecuencia, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Fecha " + fechaCreacion, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Tema " + idTema, Toast.LENGTH_SHORT).show()*/

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    // Lógica para el botón "Aceptar"
                    crearCongreo(nombre, idTema.toString(), frecuencia, fechaCreacion)
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
        val isEnabled = txtNombreRevista.text.isNotEmpty() &&
                txtFrecuencia.text.isNotEmpty() &&
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
                        //Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
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


    fun crearCongreo(nombre: String , tipo: String , frecuencia: String , fecha: String ) {
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
                            //    1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                            //    2 -> Toast.makeText(context, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                            //    else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                        //else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                    //Toast.makeText(context, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show()
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
                params["nombre"] = nombre
                params["tipo"] = tipo
                params["frecuencia"] = frecuencia
                params["primerCongreso"] = fecha
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }
}
