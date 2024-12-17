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
import modelosDatos.Ciudad
import modelosDatos.URL
import modelosDatos.temas
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PopupHelperEditarCentroInvestigacion(private val context: Context , private val idCentro : String , private val idUsuario : String , private val tipoUsuario : String) {

    val url = URL.BASE_URL+"Tema/ObtenerTemas.php"
    val url2 = URL.BASE_URL+"Ciudad/ObtenerTodasLasCiudades.php"
    val url3 = URL.BASE_URL+"Centro/EditarCentro.php"
    val url4 = URL.BASE_URL+"Centro/ObtenerInformacionDeUnCentroPorIdCentro.php"

    private val temasList = mutableListOf<temas>()
    private val ciudadList = mutableListOf<Ciudad>()
    private lateinit var txtNombreCentroInvestigacion : EditText

    var nombreOriginal = ""
    var temaOriginal = ""
    var temaOriginalLimpio = ""
    var ciudadOriginal = ""
    var ciudadOriginalLimpio = ""

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_centro_investigacion, null)

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
        val spinnerCiudad: Spinner = popupView.findViewById(R.id.spinnerCiudad)



        obtenerInformacionDelCentro(idCentro.toInt() , spinnerTematica , spinnerCiudad)
        /*Thread.sleep(200)
        obtenerTemas(spinnerTematica)
        obtenerCiudades(spinnerCiudad)*/

        // Configurar TextViews y EditTexts
        txtNombreCentroInvestigacion = popupView.findViewById(R.id.txtNombreCentroInvestigacion)

        // Configurar DatePicker

        // Botón crear revista
        val btnCrearCentroInvestigacion: Button = popupView.findViewById(R.id.btnEditarCentroInvestigacion)
        btnCrearCentroInvestigacion.isEnabled = false

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtNombreCentroInvestigacion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields(txtNombreCentroInvestigacion, spinnerTematica, btnCrearCentroInvestigacion , spinnerCiudad)
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        // Configurar listener para el Spinner
        spinnerTematica.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                checkInputFields(txtNombreCentroInvestigacion, spinnerTematica, btnCrearCentroInvestigacion , spinnerCiudad)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerCiudad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                checkInputFields(txtNombreCentroInvestigacion, spinnerTematica, btnCrearCentroInvestigacion , spinnerCiudad)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Manejar la creación de la revista
        btnCrearCentroInvestigacion.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {


                val nombre = if (txtNombreCentroInvestigacion.text.toString().trim().equals(nombreOriginal.trim(), ignoreCase = true) || txtNombreCentroInvestigacion.text.toString().trim().isEmpty()) "null" else txtNombreCentroInvestigacion.text.toString().trim()
                val ciudadAux = spinnerCiudad.selectedItem.toString()
                val idCiudad = if(spinnerCiudad.selectedItemPosition == 0 || spinnerCiudad.selectedItem.toString().trim().equals(ciudadOriginalLimpio.trim(), ignoreCase = true )) "null" else ciudadList.find { it.ciudad == ciudadAux }?.id
                val temaAux = spinnerTematica.selectedItem.toString()
                val idTema = if(spinnerTematica.selectedItemPosition == 0 || spinnerTematica.selectedItem.toString().trim().equals(temaOriginalLimpio.trim(), ignoreCase = true )) "null" else temasList.find { it.tema == temaAux }?.id

            if (nombre.isNotEmpty() || spinnerTematica.selectedItemPosition != 0 || spinnerCiudad.selectedItemPosition != 0 ) {

               /* Toast.makeText(context, "Nombre : $nombre", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Tema : $idCiudad", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Ciudad : $idTema", Toast.LENGTH_SHORT).show()*/

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    // Lógica para el botón "Aceptar"
                    editarCentro(nombre, idCiudad.toString() , idTema.toString())
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "VistaCentros")
                        putExtra("idCentro" , idCentro)
                        putExtra("tipoUsuario" , tipoUsuario)
                        putExtra("idUsuario" , idUsuario)
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
        spinnerTematica: Spinner,
        btnCrearRevista: Button,
        spinnerCiudad: Spinner
    ) {
        val isEnabled = txtNombreRevista.text.isNotEmpty() &&
                spinnerTematica.selectedItemPosition != 0 &&
                spinnerCiudad.selectedItemPosition != 0

        btnCrearRevista.isEnabled = isEnabled
        btnCrearRevista.setBackgroundColor(
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

    fun obtenerCiudades(spinner: Spinner) {
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


    fun editarCentro(nombre: String , id_ciudad: String , id_tema: String ) {
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
                             //   else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
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
                params["nombreCentro"] = nombre
                params["idTema"] = id_tema
                params["idCiudad"] = id_ciudad
                params["idCentro"] = idCentro
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun obtenerInformacionDelCentro(idCentro: Int , spinnerTematica: Spinner , spinnerCiudad: Spinner ) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url4,
            Response.Listener<String> { response ->
                try {
                    // Imprimir la respuesta completa para depuración
                    println("Respuesta del servidor: $response")

                    if (response == "ERROR 2") {
                      //  Toast.makeText(context, "No se encontraron préstamos", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }


                    // Procesar la respuesta JSON
                    val jsonArray = JSONArray(response)
                    val ultimoIndice = jsonArray.length() - 1
                    for (i in 0 until jsonArray.length()) {
                        val centro = jsonArray.getJSONObject(i)
                        val nombre = centro.getString("nombre")
                        val idCiudad = centro.getString("id_ciudad")
                        val idTema = centro.getString("id_tema")

                        txtNombreCentroInvestigacion.setText(nombre)
                        nombreOriginal = nombre
                        ciudadOriginal = idCiudad
                        temaOriginal = idTema

                        if(i == ultimoIndice){
                            obtenerTemas(spinnerTematica)
                            obtenerCiudades(spinnerCiudad)
                        }
                    }

                   // Toast.makeText(context, "Préstamos obtenidos con éxito", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                   // Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
              //  Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idCentro"] = idCentro.toString()
                return params
            }
        }

        // Añadir la solicitud a la cola de solicitudes
        Volley.newRequestQueue(context).add(stringRequest)
    }

}
