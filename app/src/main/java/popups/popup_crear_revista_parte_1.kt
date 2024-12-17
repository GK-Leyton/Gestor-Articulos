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
import modelosDatos.URL
import modelosDatos.temas
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PopupHelperCrearRevista(private val context: Context) {

    var url = URL.BASE_URL+"Tema/ObtenerTemas.php"
    var url2 = URL.BASE_URL+"Revistas/CrearRevista.php"

    private val temasList = mutableListOf<temas>()


    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_crear_revista_parte1, null)

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
        val spinnerTematica: Spinner = popupView.findViewById(R.id.spinnerTematica)
        //val temas = arrayOf("Seleccione una opción", "Tema 1", "Tema 2", "Tema 3") // Agrega un ítem neutro
        //val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temas)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //spinnerTematica.adapter = adapter
        obtenerTemas(spinnerTematica)

        // Configurar TextViews y EditTexts
        val txtNombreRevista: EditText = popupView.findViewById(R.id.txtNombreRevista)
        val txtFrecuencia: EditText = popupView.findViewById(R.id.txtFrecuencia)
        val textViewDate: TextView = popupView.findViewById(R.id.textViewDate)

        // Configurar DatePicker
        textViewDate.setOnClickListener {
            showDatePicker(textViewDate)
        }

        // Botón crear revista
        val btnCrearRevista: Button = popupView.findViewById(R.id.btnCrearRevista)
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

        // Manejar la creación de la revista
        btnCrearRevista.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                val nombre = txtNombreRevista.text.toString()
                val frecuencia = txtFrecuencia.text.toString()
                val fechaCreacion = textViewDate.text.toString()
                val tematica = spinnerTematica.selectedItem.toString()
                val tematicaId = temasList.find { it.tema == tematica }?.id ?: 0


                if (nombre.isNotEmpty() && frecuencia.isNotEmpty() && fechaCreacion.isNotEmpty() && spinnerTematica.selectedItemPosition != 0) {
                    /*Toast.makeText(context, "Nombre : $nombre", Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "Frecuencia : $frecuencia", Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "Fecha : $fechaCreacion", Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "Tematica : $tematicaId", Toast.LENGTH_SHORT).show()
                     */

                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Confirmación")
                    builder.setMessage("¿Estás seguro de completar la acción?")

                    builder.setPositiveButton("Aceptar") { dialog, _ ->
                        // Lógica para el botón "Aceptar"
                        crearRevista(nombre, tematicaId.toString(), frecuencia, fechaCreacion)
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
                    Toast.makeText(
                        context,
                        "Por favor completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
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
        // Verificar si todos los EditTexts están llenos
        val isEditTextsFilled = txtNombreRevista.text.isNotEmpty() &&
                txtFrecuencia.text.isNotEmpty()

        // Verificar si el Spinner tiene una selección válida
        val isSpinnerSelected = spinnerTematica.selectedItemPosition != 0

        // Determinar si el botón debe estar habilitado
        val isEnabled = isEditTextsFilled && isSpinnerSelected
        btnCrearRevista.isEnabled = isEnabled

        // Cambiar el color del botón dependiendo de su estado
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
                            temasList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val temas = jsonArray.getJSONObject(i)
                                val id_tema = temas.getString("id_tema").toInt()
                                val temaNombre = temas.getString("tema")

                                // Agregar a la lista de temas
                                temasList.add(temas(id_tema, temaNombre))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + temasList.map { it.tema }
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

    fun crearRevista(nombre: String , tema: String , frecuencia: String , primera_edicion: String ) {
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
                                //1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                               // 2 -> Toast.makeText(context, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                               // else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
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
                params["tema"] = tema
                params["frecuencia"] = frecuencia
                params["primera_edicion"] = primera_edicion
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }
}
