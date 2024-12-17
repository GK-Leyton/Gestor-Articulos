
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import modelosDatos.Palabra
import modelosDatos.URL
import org.json.JSONArray
import org.json.JSONObject
import popups.AnimationHelper
import popups.setPopupBackGroundHelper
import java.util.HashMap

class PopupHelperEditarPalabrasClaveArticulo(
    private val context: Context,
    private val idUsuario: String,
    private val tipoUsuario: String,
    private val idArticulo: String,
    private val desde: String
) {

    private val palabrasRegistradasList = mutableListOf<String>()
    private val palabrasCandidatasList = mutableListOf<String>()
    private val esquemaPalabrasOriginalesConId = mutableListOf<Palabra>()
    private val esquemaTodasLasPalabrasConId = mutableListOf<Palabra>()
    private val esquemaPalabrasPorEliminar = mutableListOf<Palabra>()
    private val esquemaPalabrasPorAgregar = mutableListOf<Palabra>()

    private lateinit var popupView: View
    private lateinit var scrollViewPalabrasRegistradas: ScrollView
    private lateinit var scrollViewTodasLasPalabras: ScrollView
    private lateinit var btnEditarPalabrasClave: Button

    var url = URL.BASE_URL+"Articulos/ObtenerTodasLasPalabrasDeUnArticuloPorIdArticulo.php"
    var url2 = URL.BASE_URL+"Palabra/ObtenerPalabras.php"
    var url3 = URL.BASE_URL+"Articulos/EditarPalabrasClaveAgregar.php"
    var url4 = URL.BASE_URL+"Articulos/EditarPalabrasClaveEliminar.php"

    fun showPopup(anchorView: View) {
        // Inflar el layout del popup
        popupView =
            LayoutInflater.from(context).inflate(R.layout.popup_editar_palabras_clave_articulo_parte_1, null)

        // Medir la vista inflada para obtener su altura real
        val popupWindow = PopupWindow(
            popupView,
            dpToPx(350),  // Convertir dp a píxeles
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Configurar el fondo transparente
        popupWindow.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = false

        // Atenuar el fondo de la actividad
        setActivityBackgroundAlpha(0.5f)
        popupWindow.setOnDismissListener {
            setActivityBackgroundAlpha(1.0f)
        }

        // Mostrar el popup centrado en la pantalla
        popupWindow.showAtLocation(anchorView, android.view.Gravity.CENTER, 0, 0)

        // Animar el popup
        AnimationHelper.animatePopupView(popupView, 200) {
            // Aquí puedes agregar código adicional si es necesario después de la animación
        }

        // Configurar el fondo del popup si es necesario
        setPopupBackGroundHelper(popupView, context)

        // Obtener el botón de editar
        btnEditarPalabrasClave = popupView.findViewById(R.id.btnEditarPalabrasClave)

        // Manejar el click del botón
        btnEditarPalabrasClave.setOnClickListener {
            // Animar y cerrar el popup cuando el botón es presionado
            AnimationHelper.dismissPopupView(popupView, 200) {
                // Código adicional al cerrar el popup si es necesario
            }
        }

        // Obtener los ScrollViews
        scrollViewPalabrasRegistradas = popupView.findViewById(R.id.scrollViewPalabrasRegistradas)
        scrollViewTodasLasPalabras = popupView.findViewById(R.id.scrollViewTodasLasPalabras)

        // Llamar a la función para llenar los ScrollViews con datos estáticos
        obtenerPalabrasOriginalesArticulo(idArticulo)


        btnEditarPalabrasClave.setOnClickListener {


            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de completar la acción?")

            builder.setPositiveButton("Aceptar") { dialog, _ ->
                // Lógica para el botón "Aceptar"

                esquemaPalabrasPorEliminar.clear()
                esquemaPalabrasPorAgregar.clear()

                for (palabraOriginal in esquemaPalabrasOriginalesConId) {
                    if (!palabrasRegistradasList.contains(palabraOriginal.palabra)) {
                        esquemaPalabrasPorEliminar.add(palabraOriginal)
                    }
                }

                for (palabraOriginal in palabrasRegistradasList) {
                    val idPalabra = esquemaTodasLasPalabrasConId.find { it.palabra == palabraOriginal }?.id
                    if (!esquemaPalabrasOriginalesConId.contains(Palabra(idPalabra.toString().toInt() , palabraOriginal))) {
                        esquemaPalabrasPorAgregar.add(Palabra(idPalabra.toString().toInt() , palabraOriginal))
                    }
                }

                val esquemaPalabrasOriginalesSinIdAux = esquemaPalabrasOriginalesConId.map { it.palabra }
                if (palabrasRegistradasList.toSet() == esquemaPalabrasOriginalesSinIdAux.toSet()) {
                    Toast.makeText(context, "No hay cambios", Toast.LENGTH_SHORT).show()
                }else{
                    AnimationHelper.dismissPopupView(popupView, 200) {

                        for(palabra in esquemaPalabrasPorAgregar){
                            editarPalabrasAgregar(idArticulo, palabra.id.toString())
                        }
                        for(palabra in esquemaPalabrasPorEliminar){
                            editarPalabrasEliminar(idArticulo, palabra.id.toString())
                        }

                        val intent = Intent(context, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "vistaArticulos")
                            putExtra("tipoUsuario" , tipoUsuario)
                            putExtra("idUsuario" , idUsuario)
                            putExtra("idArticulo" , idArticulo.toString())
                            putExtra("desde" , desde)
                        }
                        context.startActivity(intent)


                        popupWindow.dismiss()
                    }
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


    private fun cargarPalabrasQuemadas() {
        // Limpiar la lista de palabras registradas
        palabrasRegistradasList.clear()
        palabrasCandidatasList.clear()
        // Recorrer la lista de palabras originales (esquemaPalabrasOriginalesConId)
        for (palabrasOriginales in esquemaPalabrasOriginalesConId) {
            // Agregar las palabras a la lista palabrasRegistradasList
            palabrasRegistradasList.add(palabrasOriginales.palabra)
        }
        for (palabrasOriginales in esquemaTodasLasPalabrasConId) {
            // Agregar las palabras a la lista palabrasRegistradasList
            palabrasCandidatasList.add(palabrasOriginales.palabra)
        }

        actualizarScrollViews(scrollViewPalabrasRegistradas , scrollViewTodasLasPalabras, popupView)
    }


    // Función para actualizar los ScrollViews con las palabras en los ArrayLists
    private fun actualizarScrollViews(
        scrollViewPalabrasRegistradas: ScrollView,
        scrollViewTodasLasPalabras: ScrollView,
        popupView: View // Agregar popupView como parámetro
    ) {
        // Limpiar los ScrollViews
        val layoutPalabrasRegistradas: LinearLayout = scrollViewPalabrasRegistradas.getChildAt(0) as LinearLayout
        val layoutPalabrasCandidatas: LinearLayout = scrollViewTodasLasPalabras.getChildAt(0) as LinearLayout
        layoutPalabrasRegistradas.removeAllViews()
        layoutPalabrasCandidatas.removeAllViews()

        // Llenar el ScrollView de palabras registradas
        for (palabra in palabrasRegistradasList) {
            val textView = crearTextView(palabra)
            textView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val dragShadowBuilder = View.DragShadowBuilder(v)
                    v.startDragAndDrop(null, dragShadowBuilder, v, 0)
                    true
                } else {
                    false
                }
            }
            layoutPalabrasRegistradas.addView(textView)
        }

        // Llenar el ScrollView de palabras candidatas
        for (palabra in palabrasCandidatasList) {
            val textView = crearTextView(palabra)
            textView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val dragShadowBuilder = View.DragShadowBuilder(v)
                    v.startDragAndDrop(null, dragShadowBuilder, v, 0)
                    true
                } else {
                    false
                }
            }
            layoutPalabrasCandidatas.addView(textView)
        }

        // Configurar los ScrollViews para recibir palabras arrastradas

        scrollViewPalabrasRegistradas.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    //scrollViewPalabrasRegistradas.setBackgroundColor(Color.LTGRAY)
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    //scrollViewPalabrasRegistradas.setBackgroundColor(Color.WHITE)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val draggedView = event.localState as? TextView
                    if (draggedView != null) {
                        moverPalabraEntreScrollViews(draggedView, palabrasCandidatasList, palabrasRegistradasList, popupView)
                        //scrollViewPalabrasRegistradas.setBackgroundColor(Color.WHITE)
                    }
                    true
                }
                else -> true
            }
        }

        scrollViewTodasLasPalabras.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    //scrollViewTodasLasPalabras.setBackgroundColor(Color.LTGRAY)
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                   // scrollViewTodasLasPalabras.setBackgroundColor(Color.WHITE)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val draggedView = event.localState as? TextView
                    if (draggedView != null) {
                        moverPalabraEntreScrollViews(draggedView, palabrasRegistradasList, palabrasCandidatasList, popupView)
                        //scrollViewTodasLasPalabras.setBackgroundColor(Color.WHITE)
                    }
                    true
                }
                else -> true
            }
        }

        // Asegurarse de que los ScrollViews sigan aceptando eventos
        if (layoutPalabrasRegistradas.childCount == 0 || layoutPalabrasCandidatas.childCount == 0) {
            // Volver a configurar los DragListeners si los layouts están vacíos
            scrollViewPalabrasRegistradas.invalidate()
            scrollViewTodasLasPalabras.invalidate()
        }
    }

    // Función para mover una palabra entre los ArrayLists y actualizar los ScrollViews
    private fun moverPalabraEntreScrollViews(
        textView: TextView,
        fromList: MutableList<String>,
        toList: MutableList<String>,
        rootView: View // Pasamos la vista raíz del popup
    ) {
        val palabra = textView.text.toString()

        if (palabra.isNotEmpty()) {
            // Eliminar la palabra de la lista de origen y agregarla a la lista de destino
            if (fromList.remove(palabra)) {
                toList.add(palabra)

                // Actualizar las vistas
                val parentView = textView.parent as ViewGroup
                parentView.removeView(textView)

                // Buscar el layout de destino desde la vista raíz
                val targetLayout = if (toList === palabrasRegistradasList) {
                    rootView.findViewById<LinearLayout>(R.id.layoutPalabrasRegistradas)
                } else {
                    rootView.findViewById<LinearLayout>(R.id.layoutPalabrasCandidatas)
                }

                // Verificar que el layout de destino no sea nulo
                if (targetLayout != null) {
                    targetLayout.addView(textView)
                } else {
                    Log.e("MoverPalabra", "No se encontró el layout de destino.")
                }
            }
        }
    }

    // Función para crear un TextView con el estilo adecuado
    private fun crearTextView(palabra: String): TextView {
        val textView = TextView(context)
        textView.text = palabra
        textView.tag = palabra // Para identificar el TextView
        textView.background = context.getDrawable(R.drawable.rounded_transparent_lighter_white_5dp)
        textView.setTextColor(Color.BLACK)
        textView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(20)
        ).apply {
            setMargins(0, dpToPx(7), 0, dpToPx(7))
        }
        textView.layoutParams = params
        return textView
    }


    // Función para convertir dp a px
    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    // Función para atenuar el fondo de la actividad
    private fun setActivityBackgroundAlpha(alpha: Float) {
        val activity = context as? AppCompatActivity
        activity?.window?.apply {
            val params = attributes
            params.alpha = alpha
            attributes = params
        }
    }


    fun obtenerPalabrasOriginalesArticulo( idArticulo: String ) {
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
                            esquemaPalabrasOriginalesConId.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val palabra = jsonArray.getJSONObject(i)
                                val id_palabra = palabra.getString("id_palabra").toInt()
                                val nombre_palabra = palabra.getString("palabra")

                                esquemaPalabrasOriginalesConId.add(Palabra(
                                    id_palabra,
                                    nombre_palabra
                                ))
                            }

                            obtenerTodasLasPalabras()


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
                params["idArticulo"] = idArticulo
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun obtenerTodasLasPalabras() {
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
                            esquemaTodasLasPalabrasConId.clear()

                            // Convertir la respuesta en JSON Array
                            val jsonArray = JSONArray(response)
                            for (i in 0 until jsonArray.length()) {
                                val palabra = jsonArray.getJSONObject(i)
                                val id_palabra = palabra.getString("id_palabra").toInt()
                                val palabraNombre = palabra.getString("palabra")

                                // Agregar a la lista de temas
                                esquemaTodasLasPalabrasConId.add(Palabra(id_palabra, palabraNombre))
                            }

                            cargarPalabrasQuemadas()

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
            override fun getParams(): Map<String, String> = emptyMap()
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }


    fun editarPalabrasAgregar(idArticulo: String , idPalabra: String) {
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
                          //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                params["idPalabra"] = idPalabra
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun editarPalabrasEliminar(idArticulo: String , idPalabra: String) {
        val stringRequest = object : StringRequest(
            Method.POST, url4,
            Response.Listener<String> { response ->
                try {
                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")

                    when (status) {
                        "success" -> {
                            val message = jsonResponse.getString("message")
                         //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            val code = jsonResponse.getInt("code")
                            val message = jsonResponse.getString("message")

                            // Mostrar mensaje de error basado en el código
                            when (code) {
                           //     1 -> Toast.makeText(context, "Error: Tema vacío ($message)", Toast.LENGTH_SHORT).show()
                           //     2 -> Toast.makeText(context, "Error: Falló la Eliminacion ($message)", Toast.LENGTH_SHORT).show()
                           //     else -> Toast.makeText(context, "Error desconocido: $message", Toast.LENGTH_SHORT).show()
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
                params["idPalabra"] = idPalabra
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }
}
