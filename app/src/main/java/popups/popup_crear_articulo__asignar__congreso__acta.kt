package popups

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
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
import modelosDatos.Acta
import modelosDatos.Congreso
import modelosDatos.CrearArticulo
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PopupHelperCrearArticuloAsignarCongresoActa(private val context: Context , private val datosInsercion: CrearArticulo) {



    var url = URL.BASE_URL+"Congresos/ObtenerTodosLosCongresos.php"
    var url2 = URL.BASE_URL+"Congresos/Actas/ObtenerTodasLasActasDeUnCongresoPorIdCongreso.php"
    var url3 = URL.BASE_URL+"Articulos/CrearArticuloDeCongreso.php"
    var url4 = URL.BASE_URL+"Articulos/CrearRelacionArticuloPalabraClave.php"

    private val congresosList = mutableListOf<Congreso>()
    private val actasList = mutableListOf<Acta>()
    private lateinit var spinnerCongresos: Spinner
    private lateinit var spinnerActas: Spinner
    private lateinit var btnCrearArticuloAsignarCongreso: Button



    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_crear_articulo__asignar__congreso__acta, null)

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
        spinnerCongresos = popupView.findViewById(R.id.spinnerCongresos)
        spinnerActas = popupView.findViewById(R.id.spinnerActas)
        btnCrearArticuloAsignarCongreso = popupView.findViewById(R.id.btnCrearArticuloAsignarCongresoActa)
        btnCrearArticuloAsignarCongreso.isEnabled = false

        //val temas = arrayOf("Seleccione una opción", "Centro 1", "Centro 2", "Centro 3") // Agrega un ítem neutro
        //val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temas)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //spinnerCentros.adapter = adapter
        spinnerActas.visibility = View.GONE
        obtenerCongresos(spinnerCongresos)


        // Configurar TextViews y EditTexts






        // Configurar DatePicker




        // Agregar TextWatcher para monitorear cambios en los EditTexts




        // Configurar listener para el Spinner
        spinnerCongresos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Seleccionaste: ${parent.getItemAtPosition(position)}", Toast.LENGTH_SHORT).show()
                checkInputFields(spinnerCongresos, spinnerActas, btnCrearArticuloAsignarCongreso)


                spinnerActas.visibility = View.VISIBLE
                val idCongreso = congresosList.find { it.nombre == parent.getItemAtPosition(position) }?.id.toString()
                obtenerActas(spinnerActas , idCongreso)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerActas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Seleccionaste: ${parent.getItemAtPosition(position)}", Toast.LENGTH_SHORT).show()
                checkInputFields(spinnerCongresos, spinnerActas, btnCrearArticuloAsignarCongreso)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        // Manejar la creación de la revista
        btnCrearArticuloAsignarCongreso.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                //imprimir los datos del objeto recibido por parametros
                /*
                Toast.makeText(context, "titulo: ${datosInsercion.titulo}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "tituloAbreviado: ${datosInsercion.tituloAbreviado}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "tipoArticulo: ${datosInsercion.tipoArticulo}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "autor: ${datosInsercion.autor}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "correo: ${datosInsercion.correo}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "sacadoDe: ${datosInsercion.localizacion}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "url: ${datosInsercion.url}", Toast.LENGTH_SHORT).show()
                */


                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->

                    val id_acta = actasList.find { it.nombre == spinnerActas.selectedItem.toString() }?.idActa
                    crearArticulo(datosInsercion.titulo, datosInsercion.tituloAbreviado, datosInsercion.tipoArticulo, datosInsercion.autor, datosInsercion.correo, datosInsercion.localizacion, datosInsercion.url , id_acta.toString() , datosInsercion.autor)
                    for (palabra in datosInsercion.palabrasClave) {
                        //Toast.makeText(context, "palabraClave: ${palabra.id}", Toast.LENGTH_SHORT).show()
                        crearRelacionArticuloPlabra(palabra.id.toString());
                    }
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

    private fun checkInputFields(
        spinnerCongresos: Spinner,
        spinnerActas: Spinner,
        btnCrearRevista: Button
    ) {
        val isEnabled = spinnerCongresos.selectedItemPosition != 0 &&
                spinnerActas.selectedItemPosition != 0

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


    fun obtenerActas(spinner: Spinner , idCongreso: String ) {
        // Crear la solicitud

        val stringRequest = object : StringRequest(
            Method.POST, url2,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        actasList.clear()
                        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listOf(""))
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter

                        //Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            actasList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val acta = jsonArray.getJSONObject(i)
                                val id_acta = acta.getString("id_acta").toInt()
                                val nombre = acta.getString("nombre")
                                val fecha_inicio = acta.getString("fecha_inicio")
                                val nombre_ciudad = acta.getString("nombre_ciudad")

                                // Agregar a la lista de temas
                                actasList.add(Acta(id_acta.toString(), nombre, fecha_inicio, nombre_ciudad))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + actasList.map { it.nombre }
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
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idCongreso"] = idCongreso.toString()
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun obtenerCongresos(spinner: Spinner) {
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

    fun crearArticulo( titulo: String , tituloAbreviado: String , tipoArticulo: String , autor: String , correo: String , localizacion: String , url: String , id_acta: String , id_autor: String ) {
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
                               // 1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                               // 2 -> Toast.makeText(context, "Error: Falló la inserción ($message)", Toast.LENGTH_SHORT).show()
                                //else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
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
                params["titulo"] = titulo
                params["tituloAbreviado"] = tituloAbreviado
                params["tipoArticulo"] = tipoArticulo
                params["autor"] = autor
                params["correo"] = correo
                params["ubicacion"] = localizacion
                params["url"] = url
                params["idActa"] = id_acta
                params["idAutor"] = id_autor
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun crearRelacionArticuloPlabra(idPalabra: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url4,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                            //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                               // 1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                               // else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                        //else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
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
                params["idPalabra"] = idPalabra
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


}