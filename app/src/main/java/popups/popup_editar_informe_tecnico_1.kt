package popups

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
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
import modelosDatos.temas
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PopupHelperEditarInformeTecnico(private val context: Context , private val idInforme: Int , private val idUsuario: String , private val tipoUsuario: String , private val idCentroOriginal : String) {



    val url = URL.BASE_URL+"Tema/ObtenerTemas.php"
    val url2 = URL.BASE_URL+"Centro/ObtenerCentros.php"
    val url3 = URL.BASE_URL+"InformesTecnicos/EditarInformeTecnico.php"
    val url4 = URL.BASE_URL + "InformesTecnicos/ObtenerLaInformacionDeUnInformeTecnicoPorNumeroInforme.php"

    private val centroList = mutableListOf<centros>()
    private val temasList = mutableListOf<temas>()
    private lateinit var spinnerTematica: Spinner
    private lateinit var spinnerCentros: Spinner
    private lateinit var btnCrearInformeTecnico: Button
    private lateinit var textViewDate: TextView

    private var temaOriginal = ""
    private var temaOriginalLimpio = ""
    private var centroOriginal = ""
    private var centroOriginalLimpio = ""
    private var fechaOriginal = ""



    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_informe_tecnico_parte1, null)

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
        spinnerCentros = popupView.findViewById(R.id.spinnerCentros)
        spinnerTematica = popupView.findViewById(R.id.spinnerTema)
        textViewDate= popupView.findViewById(R.id.textViewDate)
        btnCrearInformeTecnico = popupView.findViewById(R.id.btnEditarInformeTecnico)
        btnCrearInformeTecnico.isEnabled = false

        obtenerInformacionDeLaEdicion(idInforme , spinnerCentros , spinnerTematica)
        /*Thread.sleep(200)
        obtenerCentros(spinnerCentros)
        obtenerTemas(spinnerTematica)*/

        // Configurar TextViews y EditTexts

        // Configurar DatePicker
        textViewDate.setOnClickListener {
            showDatePicker(textViewDate)
            checkInputFields(spinnerTematica, spinnerCentros, btnCrearInformeTecnico, textViewDate)

        }

        // Configurar listener para el Spinner
        spinnerCentros.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                checkInputFields(spinnerTematica, spinnerCentros, btnCrearInformeTecnico , textViewDate)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Manejar la creación de la revista
        btnCrearInformeTecnico.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {

                val fechaCreacion = if (textViewDate.text.toString().trim().equals(fechaOriginal.trim(), ignoreCase = true) || textViewDate.text.toString().trim().isEmpty()) "null" else textViewDate.text.toString().trim()
                val temaAux = spinnerTematica.selectedItem.toString()
                val idTema = if(spinnerTematica.selectedItemPosition == 0 || spinnerTematica.selectedItem.toString().trim().equals(temaOriginalLimpio.trim(), ignoreCase = true )) "null" else temasList.find { it.tema == temaAux }?.id
                val centroAux = spinnerCentros.selectedItem.toString()
                val idCentro = if(spinnerCentros.selectedItemPosition == 0 || spinnerCentros.selectedItem.toString().trim().equals(centroOriginalLimpio.trim(), ignoreCase = true )) "null" else centroList.find { it.centro == centroAux }?.id

            if (idTema != 0 && idCentro != 0 && fechaCreacion.isNotEmpty() && spinnerCentros.selectedItemPosition != 0 && spinnerTematica.selectedItemPosition != 0) {

                /*Toast.makeText(context, "Tema : $idTema", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Centro : $idCentro", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Fecha : $fechaCreacion", Toast.LENGTH_SHORT).show()

                Toast.makeText(context, "id informe : $idInforme", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "id usuario : $idUsuario", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "tipo usuario : $tipoUsuario", Toast.LENGTH_SHORT).show()*/

                val idCentroAux = if(idCentro != "null") idCentro.toString() else idCentroOriginal

                editarInformeTecnico(fechaCreacion , idTema.toString() , idCentro.toString())

                /*Toast.makeText(context, "idInforme : $idInforme", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Tipo Usuario : $tipoUsuario", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "idUsuario : $idUsuario", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "idCentro : $idCentroAux", Toast.LENGTH_SHORT).show()*/

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    // Lógica para el botón "Aceptar"
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "VistaInformesTecnicos")
                        putExtra("idInforme" , idInforme.toString())
                        putExtra("tipoUsuario" , tipoUsuario)
                        putExtra("idUsuario" , idUsuario)
                        putExtra("idCentro" , idCentroAux)
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
        spinnerTematica: Spinner,
        spinnerCentros: Spinner,
        btnCrearRevista: Button,
        textViewDate: TextView
    ) {
        val isEnabled = spinnerTematica.selectedItemPosition != 0 &&
                spinnerCentros.selectedItemPosition != 0 &&
                !textViewDate.text.equals("Fecha")

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
            // Verificar los campos después de seleccionar la fecha
            checkInputFields(spinnerTematica, spinnerCentros, btnCrearInformeTecnico, textViewDate)
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


    fun obtenerCentros(spinner: Spinner) {
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
                //Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
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

                            val nombreTema = temasList.find { it.id == temaOriginal.toInt() }?.tema.toString()
                            temaOriginalLimpio = nombreTema.trim().toLowerCase(Locale.ROOT)
                            val indice = temasList.indexOfFirst { it.tema.trim().toLowerCase(Locale.ROOT) == temaOriginalLimpio }
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

    fun editarInformeTecnico(fecha: String , tema: String , id_centro: String) {
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
                             //   1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                             //   2 -> Toast.makeText(context, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                                else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
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
                params["fecha"] = fecha
                params["idTema"] = tema
                params["idCentro"] = id_centro
                params["idInformeTecnico"] = idInforme.toString()
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun obtenerInformacionDeLaEdicion(numeroInforme: Int , spinnerCentros: Spinner , spinnerTematica: Spinner) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url4,
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
                        val informe = jsonArray.getJSONObject(i)

                        val id_tema = informe.getString("id_tema")
                        val id_centro = informe.getString("id_centro")
                        val fecha = informe.getString("fecha")

                        textViewDate.setText(fecha)
                        fechaOriginal = fecha
                        temaOriginal = id_tema
                        centroOriginal = id_centro

                        if (i == ultimoIndice) {
                            obtenerCentros(spinnerCentros)
                            obtenerTemas(spinnerTematica)
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
                params["idInformeTecnico"] = numeroInforme.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }

}
