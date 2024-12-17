package popups

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.segundoparcial.R
import modelosDatos.Autor
import modelosDatos.CrearArticulo
import modelosDatos.Palabra
import modelosDatos.TipoArticulo
import modelosDatos.URL
import org.json.JSONArray

class PopupHelperCrearArticulo(private val context: Context) {

    private val selectedItems = ArrayList<Palabra>() // ArrayList para almacenar items seleccionados



    var url = URL.BASE_URL+"TipoArticulo/ObtenerTiposArticulo.php"
    var url2 = URL.BASE_URL+"Palabra/ObtenerPalabras.php"
    var url3 = URL.BASE_URL+"Autores/ObtenerTodosLosAutores.php"
    private val palabrasList = mutableListOf<Palabra>()
    private val tipoarticulosList = mutableListOf<TipoArticulo>()
    private val autoresList = mutableListOf<Autor>()


    // Declaración global de componentes
    private lateinit var txtTituloArticuloAbreviado: EditText
    private lateinit var txtTituloArticulo: EditText
    private lateinit var btnCrearArticulo: Button
    private lateinit var spinnerPalabraClave: Spinner
    private lateinit var spinnerTipoArticulo: Spinner
    private lateinit var spinnerAutor: Spinner
    private lateinit var linearLayoutContainer: LinearLayout
    private lateinit var txtCorreoContacto: EditText
    private lateinit var txtSacadoDe: EditText
    private lateinit var txtURL: EditText

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_crear_articulo_parte_1, null)

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

        popupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN


        // Mostrar popup centrado
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)



        AnimationHelper.animatePopupView(popupView, 200) {
            // Aquí puedes agregar código adicional si es necesario después de la animación
        }

        setPopupBackGroundHelper(popupView, context)

        // Inicializar componentes
        initializeComponents(popupView)

        // Configurar los Adapters para los Spinners
        /*val adapterPalabrasClave = ArrayAdapter(context, android.R.layout.simple_spinner_item, palabrasClave)
        adapterPalabrasClave.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPalabraClave.adapter = adapterPalabrasClave


        val adapterCategorias = ArrayAdapter(context, android.R.layout.simple_spinner_item, categorias)
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapterCategorias

        val adapterAutores = ArrayAdapter(context, android.R.layout.simple_spinner_item, autores)
        adapterAutores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAutor.adapter = adapterAutores*/

        obtenerPalabras(spinnerPalabraClave)
        obtenerTipoArticulo(spinnerTipoArticulo)
        obtenerAutores(spinnerAutor)

        btnCrearArticulo.isEnabled = false // Deshabilitar inicialmente

        // Agregar TextWatcher para monitorear cambios en los EditTexts
        txtTituloArticuloAbreviado.addTextChangedListener(createTextWatcher())
        txtTituloArticulo.addTextChangedListener(createTextWatcher())
        txtCorreoContacto.addTextChangedListener(createTextWatcher())
        txtSacadoDe.addTextChangedListener(createTextWatcher())
        txtURL.addTextChangedListener(createTextWatcher())

        // Configurar el listener para los Spinners
        configureSpinnerListener(spinnerPalabraClave)
        configureSpinnerListener(spinnerTipoArticulo)
        configureSpinnerListener(spinnerAutor)

        // Manejar la creación del artículo
        btnCrearArticulo.setOnClickListener {
            //Toast.makeText(context, "Artículo creado: ${txtTituloArticulo.text}", Toast.LENGTH_SHORT).show()



                val titulo = txtTituloArticulo.text.toString()
                val tituloAbreviado = txtTituloArticuloAbreviado.text.toString()
                val correo = txtCorreoContacto.text.toString()
                val url = txtURL.text.toString()
                val sacadoDe = txtSacadoDe.text.toString()
                val autor = spinnerAutor.selectedItem.toString()
                val idAutor = autoresList.find { it.nombre == autor }?.id
                val tipo = spinnerTipoArticulo.selectedItem.toString()
                val idTipo = tipoarticulosList.find { it.tipoArticulo == tipo }?.id



                val datosInsercion = CrearArticulo(titulo, tituloAbreviado, selectedItems, idTipo.toString() , idAutor.toString() , correo, sacadoDe, url)

                if(tipo.equals("Congres")) {
                    val popupHelper = PopupHelperCrearArticuloAsignarCongresoActa(context, datosInsercion)
                    popupHelper.showPopup(anchorView)

                    popupWindow.dismiss()
                }
                else if(tipo.equals("Revista")) {
                    val popupHelper = PopupHelperCrearArticuloAsignarRevistaEdicion(context, datosInsercion)
                    popupHelper.showPopup(anchorView)

                    popupWindow.dismiss()
                }
                else if(tipo.equals("Informe Tecnico")) {
                    val popupHelper = PopupHelperCrearArticuloAsignarInformeTecnico(context, datosInsercion)
                    popupHelper.showPopup(anchorView)

                    popupWindow.dismiss()
                }





        }
    }

    private fun initializeComponents(popupView: View) {
        txtTituloArticuloAbreviado = popupView.findViewById(R.id.txtTituloArticuloAbreviado)
        txtTituloArticulo = popupView.findViewById(R.id.txtTituloArticulo)
        btnCrearArticulo = popupView.findViewById(R.id.btnCrearArticulo_parte_1)
        spinnerPalabraClave = popupView.findViewById(R.id.spinnerPalabraClave)
        spinnerTipoArticulo = popupView.findViewById(R.id.spinnerTipoArticulo)
        spinnerAutor = popupView.findViewById(R.id.spinnerAutores)
        linearLayoutContainer = popupView.findViewById(R.id.linearLayoutContainer)
        txtCorreoContacto = popupView.findViewById(R.id.txtCorreoContacto)
        txtSacadoDe = popupView.findViewById(R.id.txtSacadoDe)
        txtURL = popupView.findViewById(R.id.txtURL)
    }

    private fun configureSpinnerListener(spinner: Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String

                // Verificar que el spinner sea el de Palabra Clave
                if (spinner.id == R.id.spinnerPalabraClave && selectedItem != "Selecciona una opción") {
                    // Comprobar que el elemento no esté ya en el ScrollView
                    if (!isItemAlreadyAdded(selectedItem, linearLayoutContainer)) {
                        addTextViewToScrollView(selectedItem, linearLayoutContainer)
                        val id_palabra = palabrasList.find { it.palabra == selectedItem }?.id
                        selectedItems.add(
                            Palabra(
                                id_palabra.toString().toInt(),
                                selectedItem
                            )

                        ) // Agregar a ArrayList
                    }
                }

                // Llamar a checkInputFields cada vez que se selecciona un item en el spinner
                checkInputFields()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // Función para verificar si el item ya está añadido al LinearLayout
    private fun isItemAlreadyAdded(item: String, container: LinearLayout): Boolean {
        for (i in 0 until container.childCount) {
            val textView = container.getChildAt(i) as TextView
            if (textView.text.toString() == item) {
                return true // El item ya existe en el ScrollView
            }
        }
        return false // El item no está en el ScrollView
    }

    private fun addTextViewToScrollView(text: String, container: LinearLayout) {
        val textView = TextView(context).apply {
            this.text = text
            this.textSize = 16f
            this.setPadding(16, 16, 16, 16)
            this.setTextColor(android.graphics.Color.BLACK)
            this.setTypeface(this.typeface, android.graphics.Typeface.BOLD)
            this.gravity = Gravity.CENTER
        }
        container.addView(textView) // Agregar el TextView al LinearLayout dentro del ScrollView
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputFields()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun checkInputFields() {
        val isText1NotEmpty = txtTituloArticuloAbreviado.text.isNotEmpty()
        val isText2NotEmpty = txtTituloArticulo.text.isNotEmpty()

        // Validar que el correo sea válido
        val isCorreoContactoValid = txtCorreoContacto.text.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(txtCorreoContacto.text).matches() && palabrasList.isNotEmpty()

        val isSacadoDeNotEmpty = txtSacadoDe.text.isNotEmpty()
        val isURLNotEmpty = txtURL.text.isNotEmpty()
        val hasSelectedItems = linearLayoutContainer.childCount > 0 // Comprobar si hay elementos en el LinearLayout

        // Comprobar los valores seleccionados en los spinners
        val isPalabraClaveSelected = spinnerPalabraClave.selectedItem != "Selecciona una opción"
        val isCategoriaSelected = spinnerTipoArticulo.selectedItem != "Selecciona una categoría"
        val isAutorSelected = spinnerAutor.selectedItem != "Selecciona un autor"

        // Activar/desactivar el botón según las validaciones
        btnCrearArticulo.isEnabled = isText1NotEmpty &&
                isText2NotEmpty &&
                hasSelectedItems &&
                isCorreoContactoValid &&
                isSacadoDeNotEmpty &&
                isURLNotEmpty &&
                isPalabraClaveSelected &&
                isCategoriaSelected &&
                isAutorSelected

        if (btnCrearArticulo.isEnabled) {
            btnCrearArticulo.setBackgroundColor(context.getColor(R.color.colorButtonEnabled))
        } else {
            btnCrearArticulo.setBackgroundColor(context.getColor(R.color.colorButtonDefault))
        }

        if (!isCorreoContactoValid && txtCorreoContacto.text.isNotEmpty()) {
            txtCorreoContacto.error = "Correo no válido"
        }
    }



    private fun setActivityBackgroundAlpha(alpha: Float) {
        val activity = context as AppCompatActivity
        val window = activity.window
        val params = window.attributes
        params.alpha = alpha
        window.attributes = params
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }


    fun obtenerPalabras(spinner: Spinner) {
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
                            palabrasList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val palabra = jsonArray.getJSONObject(i)
                                val id_palabra = palabra.getString("id_palabra").toInt()
                                val palabraNombre = palabra.getString("palabra")

                                // Agregar a la lista de temas
                                palabrasList.add(Palabra(id_palabra, palabraNombre))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Selecciona una opción") + palabrasList.map { it.palabra }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

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

    fun obtenerTipoArticulo (spinner: Spinner) {
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

                           // Toast.makeText(context, "Temas obtenidos con éxito", Toast.LENGTH_SHORT).show()
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


    fun obtenerAutores (spinner: Spinner) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url3,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        //Toast.makeText(context, "No se encontraron temas", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los temas
                            autoresList.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val autor = jsonArray.getJSONObject(i)
                                val id_investigador = autor.getString("id_investigador").toInt()
                                val nombre = autor.getString("nombre")

                                // Agregar a la lista de temas
                                autoresList.add(Autor(nombre , id_investigador , "" , ""))
                            }

                            // Actualizar el adaptador del Spinner
                            val temasNombres = listOf("Selecciona una opción") + autoresList.map { it.nombre }
                            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, temasNombres)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter

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

}
