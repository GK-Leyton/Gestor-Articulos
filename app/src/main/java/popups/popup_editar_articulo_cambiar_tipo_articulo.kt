package popups

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import modelosDatos.Acta
import modelosDatos.Congreso
import modelosDatos.EdicionDesdeRevista
import modelosDatos.InformeTecnico
import modelosDatos.Revista
import modelosDatos.TipoArticulo
import modelosDatos.URL
import modelosDatos.centros
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PopupHelperEditarArticuloCambiarTipoArticulo(private val context: Context , private val idArticulo : String , private val idUsuario : String , private val tipoUsuario : String , private val desde : String , private val idTipoArticulo : String){

    var url = URL.BASE_URL+"TipoArticulo/ObtenerTiposArticulo.php"
    var url2 = URL.BASE_URL+"Congresos/ObtenerTodosLosCongresos.php"
    var url3 = URL.BASE_URL+"Revistas/ObtenerTodasLasRevistas.php"
    var url4 = URL.BASE_URL+"Centro/ObtenerCentros.php"
    var url5 = URL.BASE_URL+"Congresos/Actas/ObtenerTodasLasActasDeUnCongresoPorIdCongreso.php"
    var url6 = URL.BASE_URL+"Revistas/Edicion/ObtenerLasEdicionesDeUnaRevista.php"
    var url7 = URL.BASE_URL+"Centro/ObtenerInformesTecnicosDeUnCentroPorIdCentro.php"


    var url8 = URL.BASE_URL+"Articulos/UpdateCambiarDeActaDeCongreso.php"
    var url9 = URL.BASE_URL+"Articulos/UpdateCambiarDeEdicionDeRevista.php"
    var url10 = URL.BASE_URL+"Articulos/UpdateCambiarDeInformeTecnicoDeCentro.php"

    var url11 = URL.BASE_URL+"Articulos/UpdateCambiarTipoArticuloEnTablaArticulos.php"

    var url12 = URL.BASE_URL+"Articulos/DeleteEliminarRelacionEnArticulo_Acta.php"
    var url13 = URL.BASE_URL+"Articulos/DeleteEliminarRelacionEnArticulo_Edicion.php"
    var url14 = URL.BASE_URL+"Articulos/DeleteEliminarRelacionEnArticulo_InformeTecnico.php"


    var url15 = URL.BASE_URL+"Articulos/CreateCrearRelacionEnArticuloActa.php"
    var url16 = URL.BASE_URL+"Articulos/CreateCrearRelacionEnArticuloEdicion.php"
    var url17 = URL.BASE_URL+"Articulos/CreateCrearRelacionEnArticuloInformeTecnico.php"



    private val revistasList = mutableListOf<Revista>()
    private val edicionesList = mutableListOf<EdicionDesdeRevista>()
    private val tipoarticulosList = mutableListOf<TipoArticulo>()
    private val centrosList = mutableListOf<centros>()
    private val informeTecnicoList = mutableListOf<InformeTecnico>()
    private val congresosList = mutableListOf<Congreso>()
    private val actasList = mutableListOf<Acta>()


    private lateinit var spinnerTipoArticulo: Spinner
    private lateinit var spinnerRevistaCentro: Spinner
    private lateinit var spinnerEdicionActa: Spinner
    private lateinit var txtTipoArticulo: TextView
    private lateinit var txtRevistaCentro: TextView
    private lateinit var txtEdicionActa: TextView
    private lateinit var btnCrearArticuloAsignarCongreso: Button

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_editar_articulo_cambiar_tipo_articulo, null)

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
        }

        btnCrearArticuloAsignarCongreso = popupView.findViewById(R.id.btnEditarArticulo)
        btnCrearArticuloAsignarCongreso.isEnabled = false

        btnCrearArticuloAsignarCongreso.setOnClickListener {
            AnimationHelper.dismissPopupView(popupView, 200) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de completar la acción?")

                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    // Lógica para el botón "Aceptar"


                    var aux = ""
                    aux = spinnerTipoArticulo.selectedItem.toString().trim()
                    val idNuevoTipoArticulo = tipoarticulosList.find { it.tipoArticulo == aux }?.id.toString()
                    aux = spinnerTipoArticulo.selectedItem.toString()


                    if(idNuevoTipoArticulo == idTipoArticulo){ //si el tipo de articulo se mantiene
                        if(aux == "Congres"){
                            //hacer la nueva relacion con el nuevo congreso con un alter table
                            //cambiando la tabla articulo acta
                            val idActa = actasList.find { it.nombre == spinnerEdicionActa.selectedItem.toString() }?.idActa
                            UpdateCambiarDeActaDeCongreso(idArticulo , idActa.toString())



                        }else if(aux == "Revista"){
                            //hacer la nueva relacion con la nueva revista con un alter table
                            //cambiando la tabla articulo_edicion
                            val idEdicion = edicionesList.find { it.nombre == spinnerEdicionActa.selectedItem.toString() }?.id
                            UpdateCambiarDeEdicionDeRevista(idArticulo , idEdicion.toString());



                        }else if(aux == "Informe Tecnico"){
                            //hacer la nueva relacion con la nueva revista con un alter table
                            //cambiando la tabla articulo_informe_tecnico
                            val idInforme = spinnerEdicionActa.selectedItem.toString()
                            UpdateCambiarDeInformeTecnicoDeCentro(idArticulo , idInforme);



                        }


                        val intent = Intent(context, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "vistaArticulos")
                            putExtra("tipoUsuario" , tipoUsuario)
                            putExtra("idUsuario" , idUsuario)
                            putExtra("idArticulo" , idArticulo)
                            putExtra("desde" , desde)
                        }
                        context.startActivity(intent)
                        popupWindow.dismiss()



                    }
                    else{ //si el tipo de articulo no se mantiene

                        val aux2 = tipoarticulosList.find { it.id.toString() == idTipoArticulo }?.tipoArticulo
                        UpdateCambiarTipoArticuloEnTablaArticulos(idArticulo , idNuevoTipoArticulo)

                        //Eliminando la relacion que queda obsoleta
                        if(aux2 == "Congres"){
                            //eliminar la relacion con el congreso anterior usando el tipo articulo original como referencia
                            DeleteEliminarRelacionEnArticulo_Acta(idArticulo  )
                        }else if(aux2 == "Revista"){
                            //eliminar la relacion con la revista anterior usando el tipo articulo original como referencia
                            DeleteEliminarRelacionEnArticulo_Edicion(idArticulo )
                        }else if(aux2 == "Informe Tecnico"){
                            //eliminar la relacion con el informe tecnico anterior usando el tipo articulo original como referencia
                            DeleteEliminarRelacionEnArticulo_InformeTecnico(idArticulo)
                        }

                        //Creando la nueva relacion

                        if(aux == "Congres"){
                            //crear la relacion con el congreso anterior usando el tipo articulo original como referencia
                            val idActa = actasList.find { it.nombre == spinnerEdicionActa.selectedItem.toString() }?.idActa
                            CreateCrearRelacionEnArticuloActa(idArticulo , idActa.toString())



                        }else if(aux == "Revista"){
                            //crear la relacion con la revista anterior usando el tipo articulo original como referencia
                            val idEdicion = edicionesList.find { it.nombre == spinnerEdicionActa.selectedItem.toString() }?.id
                            CreateCrearRelacionEnArticuloEdicion(idArticulo , idEdicion.toString())



                        }else if(aux == "Informe Tecnico"){
                            //crear la relacion con el informe tecnico anterior usando el tipo articulo original como referencia
                            val idInforme = spinnerEdicionActa.selectedItem.toString()
                            CreateCrearRelacionEnArticuloInformeTecnico(idArticulo , idInforme);



                        }

                        val intent = Intent(context, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "vistaArticulos")
                            putExtra("tipoUsuario" , tipoUsuario)
                            putExtra("idUsuario" , idUsuario)
                            putExtra("idArticulo" , idArticulo)
                            putExtra("desde" , desde)
                        }
                        context.startActivity(intent)
                        popupWindow.dismiss()


                    }

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

        setPopupBackGroundHelper(popupView, context)



        // Configurar Spinner
        spinnerTipoArticulo = popupView.findViewById(R.id.spinnerTipoArticulo)
        spinnerRevistaCentro = popupView.findViewById(R.id.spinnerRevistaCentro)
        spinnerEdicionActa = popupView.findViewById(R.id.spinnerEdicionActa)
        txtTipoArticulo = popupView.findViewById(R.id.txtTipoArticulo)
        txtRevistaCentro = popupView.findViewById(R.id.txt_revista_centro)
        txtEdicionActa = popupView.findViewById(R.id.txt_edicion_acta)







        obtenerTipoArticulo(spinnerTipoArticulo)


        // Configurar listener para el Spinner
        spinnerTipoArticulo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Seleccionaste: ${parent.getItemAtPosition(position)}", Toast.LENGTH_SHORT).show()
                checkInputFields(spinnerTipoArticulo, spinnerRevistaCentro, spinnerEdicionActa , btnCrearArticuloAsignarCongreso)

                if(spinnerTipoArticulo.selectedItem.toString().equals("Congres")) {
                    obtenerCongresos(spinnerRevistaCentro)
                    txtRevistaCentro.text = "Congresos"
                    spinnerRevistaCentro.background = ContextCompat.getDrawable(context, R.drawable.rounded_gray_5dp)
                }else if(spinnerTipoArticulo.selectedItem.toString().equals("Revista")) {
                    obtenerRevistas(spinnerRevistaCentro)
                    txtRevistaCentro.text = "Revistas"
                    spinnerRevistaCentro.background = ContextCompat.getDrawable(context, R.drawable.rounded_gray_5dp)
                }else if(spinnerTipoArticulo.selectedItem.toString().equals("Informe Tecnico")) {
                    obtenerCentros(spinnerRevistaCentro)
                    txtRevistaCentro.text = "Centros"
                    spinnerRevistaCentro.background = ContextCompat.getDrawable(context, R.drawable.rounded_gray_5dp)
                }else{
                    clearSpinner(spinnerRevistaCentro)
                    clearSpinner(spinnerEdicionActa)
                    txtRevistaCentro.text = ""
                    txtEdicionActa.text = ""
                    spinnerRevistaCentro.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    spinnerEdicionActa.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerRevistaCentro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               // Toast.makeText(context, "Seleccionaste: ${parent.getItemAtPosition(position)}", Toast.LENGTH_SHORT).show()
                checkInputFields(spinnerTipoArticulo, spinnerRevistaCentro, spinnerEdicionActa , btnCrearArticuloAsignarCongreso)


                if(spinnerTipoArticulo.selectedItem.toString().equals("Congres")) {
                    val idCongreso = congresosList.find { it.nombre == spinnerRevistaCentro.selectedItem.toString() }?.id
                    obtenerActas(spinnerEdicionActa , idCongreso.toString())
                    txtEdicionActa.text = "Actas"
                    spinnerEdicionActa.background = ContextCompat.getDrawable(context, R.drawable.rounded_gray_5dp)
                }else if(spinnerTipoArticulo.selectedItem.toString().equals("Revista")) {
                    val idRevista = revistasList.find { it.nombre == spinnerRevistaCentro.selectedItem.toString() }?.id
                    obtenerEdiciones(spinnerEdicionActa , idRevista.toString())
                    txtEdicionActa.text = "Ediciones"
                    spinnerEdicionActa.background = ContextCompat.getDrawable(context, R.drawable.rounded_gray_5dp)
                }else if(spinnerTipoArticulo.selectedItem.toString().equals("Informe Tecnico")) {
                    val idCentro = centrosList.find { it.centro == spinnerRevistaCentro.selectedItem.toString() }?.id
                    obtenerInformesTecnicos(spinnerEdicionActa , idCentro.toString())
                    txtEdicionActa.text = "Informes Tecnicos"
                    spinnerEdicionActa.background = ContextCompat.getDrawable(context, R.drawable.rounded_gray_5dp)
                }else{
                    clearSpinner(spinnerEdicionActa)
                    txtEdicionActa.text = ""
                    spinnerEdicionActa.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerEdicionActa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "Seleccionaste: ${parent.getItemAtPosition(position)}", Toast.LENGTH_SHORT).show()
                checkInputFields(spinnerTipoArticulo, spinnerRevistaCentro, spinnerEdicionActa , btnCrearArticuloAsignarCongreso)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    private fun checkInputFields(spinnerTipoArticulo: Spinner, spinnerRevistaCentro: Spinner, spinnerEdicionActa: Spinner, btnCrearRevista: Button    ) {
        val isEnabled = spinnerTipoArticulo.selectedItemPosition != 0 &&
                spinnerRevistaCentro.selectedItemPosition != 0 &&
                spinnerEdicionActa.selectedItemPosition != 0

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

    private fun clearSpinner(spinner: Spinner) {
        val emptyAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mutableListOf())
        spinner.adapter = emptyAdapter
    }

    fun obtenerTipoArticulo (spinner: Spinner) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                      //  Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            tipoarticulosList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val tipoArticulo = jsonArray.getJSONObject(i)
                                val id_tipo = tipoArticulo.getString("id_tipo").toInt()
                                val tipo_articulo = tipoArticulo.getString("tipo_articulo")

                                // Agregar a la lista de temas
                                tipoarticulosList.add(TipoArticulo(id_tipo, tipo_articulo))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Selecciona una opción") + tipoarticulosList.map { it.tipoArticulo }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

                        //    Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                        //    Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
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
                      //  Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
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

                        //    Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                         //   Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
              //  Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun obtenerRevistas(spinner: Spinner) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url3,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                      //  Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
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

                          //  Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                           // Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
              //  Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
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
            Method.POST, url4,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                       // Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            centrosList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val centro = jsonArray.getJSONObject(i)
                                val id_centro = centro.getString("id_centro").toInt()
                                val nombre = centro.getString("nombre")


                                // Agregar a la lista de artículos
                                centrosList.add(
                                    centros(
                                        id_centro,
                                        nombre,
                                    )
                                )
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + centrosList.map { it.centro }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

                          //  Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                          //  Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
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

    fun obtenerActas(spinner: Spinner , idCongreso: String ) {
        // Crear la solicitud

        val stringRequest = object : StringRequest(
            Method.POST, url5,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        actasList.clear()
                        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listOf(""))
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter

                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
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

                          //  Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                          //  Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
              //  Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
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

    fun obtenerEdiciones(spinner: Spinner , idRevista: String ) {
        // Crear la solicitud

        val stringRequest = object : StringRequest(
            Method.POST, url6,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        edicionesList.clear()
                        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listOf(""))
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter

                       // Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            edicionesList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val edicion = jsonArray.getJSONObject(i)
                                val titulo = edicion.getString("titulo")
                                val editor = edicion.getString("editor")
                                val numero_edicion = edicion.getString("numero_edicion")


                                // Añade cada acta a la lista
                                edicionesList.add(
                                    EdicionDesdeRevista(
                                        titulo,
                                        editor,
                                        numero_edicion

                                    )
                                )
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + edicionesList.map { it.nombre }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

                           // Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                          //  Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
              //  Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                return mapOf("idRevista" to idRevista)
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun obtenerInformesTecnicos(spinner: Spinner , idCentro: String ) {
        // Crear la solicitud

        val stringRequest = object : StringRequest(
            Method.POST, url7,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        informeTecnicoList.clear()
                        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listOf(""))
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter

                      //  Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            informeTecnicoList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val informe = jsonArray.getJSONObject(i)
                                val numero_informe = informe.getString("numero_informe")
                                val id_centro = informe.getString("id_centro")
                                val tema = informe.getString("tema")


                                // Añade cada acta a la lista
                                informeTecnicoList.add(
                                    InformeTecnico(
                                        id_centro,
                                        numero_informe.toInt(),
                                        tema
                                    )
                                )
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Seleccione una opción") + informeTecnicoList.map { it.idInforme }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

                         //   Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                         //   Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
              //  Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                return mapOf("idCentro" to idCentro)
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

///////////////////////////////////////////////////////////////////////////////////////

    fun UpdateCambiarDeActaDeCongreso( idArticulo: String , idActa: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url8,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                       //     Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                         //       1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                          //      else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
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
                //Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idArticulo"] = idArticulo
                params["idActa"] = idActa
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun UpdateCambiarDeEdicionDeRevista( idArticulo: String , idEdicion: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url9,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                           // Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                              //  1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                              //  else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                       // else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                    //Toast.makeText(context, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo
                params["idEdicion"] = idEdicion
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun UpdateCambiarDeInformeTecnicoDeCentro( idArticulo: String , idInformeTecnico: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url10,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                           // Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                              //  1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                              //  else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo
                params["idInformeTecnico"] = idInformeTecnico
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun UpdateCambiarTipoArticuloEnTablaArticulos( idArticulo: String , tipoArticulo: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url11,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                          //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                           //     1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                           //     else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                      //  else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                  //  Toast.makeText(context, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo
                params["tipoArticulo"] = tipoArticulo
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun DeleteEliminarRelacionEnArticulo_Acta( idArticulo: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url12,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                          //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido Eliminando Articulo Acta")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                            //    1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                            //    else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                      //  else -> Toast.makeText(context, "Respuesta inesperada del servidor Eliminando Articulo Acta", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                   // Toast.makeText(context, "Error al procesar la respuesta del servidor Eliminando Articulo Acta ", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun DeleteEliminarRelacionEnArticulo_Edicion( idArticulo: String ) {
        val stringRequest = object : StringRequest(
            Method.POST, url13,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                          //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido Eliminando articulo Edicion")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                              //  1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                             //   else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                      //  else -> Toast.makeText(context, "Respuesta inesperada del servidor Eliminando articulo Edicion", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                  //  Toast.makeText(context, "Error al procesar la respuesta del servidor Eliminando articulo Edicion", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun DeleteEliminarRelacionEnArticulo_InformeTecnico( idArticulo: String ) {
        val stringRequest = object : StringRequest(
            Method.POST, url14,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                           // Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                            //    1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                            //    else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                      //  else -> Toast.makeText(context, "Respuesta inesperada del servidor Eliminando articulo Informe", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                   // Toast.makeText(context, "Error al procesar la respuesta del servidor Eliminando articulo Informe", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Error en la solicitud
               // Toast.makeText(context, "Error en la solicitud Eliminando articulo Informe: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idArticulo"] = idArticulo
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun CreateCrearRelacionEnArticuloActa( idArticulo: String , idActa: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url15,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                          //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                          //      1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                          //      else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
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
                val params = HashMap<String, String>()
                params["idArticulo"] = idArticulo
                params["idActa"] = idActa
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun CreateCrearRelacionEnArticuloEdicion( idArticulo: String , idEdicion: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url16,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                           // Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                             //   1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                             //   else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
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
                val params = HashMap<String, String>()
                params["idArticulo"] = idArticulo
                params["idEdicion"] = idEdicion
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun CreateCrearRelacionEnArticuloInformeTecnico( idArticulo: String , idInformeTecnico: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url17,
            Response.Listener<String> { response ->
                try {
                    Log.d("DEBUG", "Respuesta del servidor: $response") // Agrega esta línea para depuración

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                         //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.optInt("code", -1)
                            val message = jsonResponse.optString("message", "Error desconocido")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                            //    1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                            //    else -> Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                      //  else -> Toast.makeText(context, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error al analizar la respuesta
                  //  Toast.makeText(context, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show()
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
                params["idArticulo"] = idArticulo
                params["idInformeTecnico"] = idInformeTecnico
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }
}
