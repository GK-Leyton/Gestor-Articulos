package com.example.segundoparcial

import PDFGenerator
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import modelosDatos.PlantillaReportesSelect
import modelosDatos.URL
import org.json.JSONArray
import kotlin.random.Random

class VistaReportes : AppCompatActivity() , CoroutineScope {
    override val coroutineContext = Dispatchers.Main + Job()

    private var datosPlantillaSelectReportes = mutableListOf<PlantillaReportesSelect>()
    private lateinit var tableLayout: TableLayout
    var url = URL.BASE_URL+"PlantillaConsulta/PlantillaConsultaSelect.php"
    private lateinit var frameDatosNoEncontrados : FrameLayout
    private lateinit var constraintLayout : ConstraintLayout
    private lateinit var scrollViewTabla : ScrollView
    private lateinit var layoutGenerarPDF : ConstraintLayout
    val datosPDF = mutableListOf<List<String>>()


    private lateinit var savePDFLauncher: ActivityResultLauncher<String>
    private val pdfGenerator = PDFGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        val intentSonido = Intent(this, SoundService::class.java)
        startService(intentSonido)




        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_reportes)



        // Configura las insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutPrincipal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura fondo aleatorio
        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
        val random = Random.nextInt(1, 5)
        val backgrounds = listOf(R.drawable.paisaje_1, R.drawable.paisaje_2, R.drawable.paisaje_3, R.drawable.paisaje_4)
        layoutPrincipal.setBackgroundResource(backgrounds[random - 1])

        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        val consulta = intent.getStringExtra("consulta")
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val btnVoler = findViewById<ImageView>(R.id.btnVoler)
        val btnGenerarPDF = findViewById<ImageView>(R.id.btnGenerarPDF)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_generar_pdf)
            .override(410, 710)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(btnGenerarPDF)

        constraintLayout = findViewById(R.id.layoutGeneral)
        tableLayout = findViewById(R.id.tableLayout)
        frameDatosNoEncontrados = findViewById(R.id.frameDatosNoEncontrados)
        scrollViewTabla = findViewById(R.id.scrollViewTabla)
        layoutGenerarPDF = findViewById(R.id.layoutGenerarPDFr)

        frameDatosNoEncontrados.visibility = View.GONE
        layoutGenerarPDF.visibility = View.GONE

        Glide.with(this)
            .asGif()
            .load(R.drawable.icono_ojo_animado)
            .into(btnOjo)

        // Botón de alternar visibilidad
        btnOjo.setOnClickListener {
            constraintLayout.visibility =
                if (constraintLayout.visibility == ConstraintLayout.VISIBLE) ConstraintLayout.GONE
                else ConstraintLayout.VISIBLE
        }

        // Botón para volver
        btnVoler.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menuPrincipal")
                putExtra("tipoUsuario", tipoUsuario)
                putExtra("idUsuario", idUsuario)
            }
            startActivity(intent)
        }

        obtenerPlantillaConsultaCantidadPrestamos(consulta.toString())

        // Registra el ActivityResultLauncher al inicio
        savePDFLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
            if (uri != null) {
                try {
                    val outputStream = contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        pdfGenerator.savePDFToStream(
                            context = this,
                            outputStream,
                            "Reporte Semanal",
                            listOf("idPadre", "nombrePadre", "idHijo", "nombreHijo", "Prestamos"),
                            /*listOf(
                                listOf("Dato 1", "Dato 2", "Dato 3", "Dato 4", "Dato 9"),
                                listOf("Dato 5", "Dato 6", "Dato 7", "Dato 8", "Dato 10")
                            )*/
                            datosPDF
                        )
                        //Toast.makeText(this, "PDF guardado correctamente", Toast.LENGTH_SHORT).show()

                        // Compartir después de guardar
                        pdfGenerator.sharePDF(this, uri)
                    } else {
                        //Toast.makeText(this, "No se pudo acceder al archivo", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                   // Toast.makeText(this, "Error al guardar el PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                //Toast.makeText(this, "No se seleccionó ubicación para guardar el PDF", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura los listeners en el layoutGenerarPDF
        for (i in 0 until layoutGenerarPDF.childCount) {
            val child = layoutGenerarPDF.getChildAt(i)
            child.setOnClickListener {
                savePDFLauncher.launch("reporte_semanal.pdf")
            }
        }
    }


    private fun agregarTupla(idPadre: String, nombrePadre: String, idHijo: String, nombreHijo: String, cantidad: String){
        // Lanza una nueva coroutine
        launch {
            // Introduce un retraso de 200 milisegundos
            delay(500)

            val tableRow = TableRow(this@VistaReportes)

            val textView1 = crearTextView(idPadre , "#000000")
            val textView2 = crearTextView(nombrePadre , "#000000")
            val textView3 = crearTextView(idHijo , "#000000")
            val textView4 = crearTextView(nombreHijo , "#000000")
            val textView5 = crearTextView(cantidad , "#000000")

            tableRow.addView(textView1)
            tableRow.addView(textView2)
            tableRow.addView(textView3)
            tableRow.addView(textView4)
            tableRow.addView(textView5)

            tableLayout.addView(tableRow)


        }
    }


    private fun crearTextView(text: String , color : String): TextView {
        return TextView(this).apply {
            this.text = text
            this.gravity = View.TEXT_ALIGNMENT_CENTER
            this.setPadding(10, 10, 10, 10)
            this.setBackgroundResource(R.drawable.celda_estilo)
            this.textSize = 16f
            if (color != "#000000"){
                this.setBackground(getDrawable(R.drawable.celda_estilo))
                this.setTypeface(null, Typeface.BOLD)
            }
            this.setTextColor(Color.parseColor(color))

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel() // Cancela las coroutines cuando la actividad se destruye
    }

    fun obtenerPlantillaConsultaCantidadPrestamos( consulta : String) {
        // Crear la solicitud
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                when {
                    response == "ERROR 2" -> {
                        //Toast.makeText(applicationContext, "No se encontraron artículos", Toast.LENGTH_SHORT).show()
                        constraintLayout.visibility = View.GONE
                        frameDatosNoEncontrados.visibility = View.VISIBLE
                    }
                    else -> {
                        try {
                            // Limpiar la lista antes de agregar los artículos

                            // Convertir la respuesta en JSON Array
                                datosPlantillaSelectReportes.clear()

                                    val jsonArray = JSONArray(response)
                                    val ultimoIndice = jsonArray.length() - 1
                                    if (jsonArray.length() > 0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val plantilla = jsonArray.getJSONObject(i)
                                            val id_padre = plantilla.getString("id_padre").toInt()
                                            val nombre_padre = plantilla.getString("nombre_padre")
                                            val id_hijo = plantilla.getString("id_hijo")
                                            val nombre_hijo = plantilla.getString("nombre_hijo")
                                            val cantidad_prestamos = plantilla.getString("cantidad_prestamos")

                                            datosPlantillaSelectReportes.add(
                                                PlantillaReportesSelect(
                                                    id_padre.toString(),
                                                    nombre_padre,
                                                    id_hijo,
                                                    nombre_hijo,
                                                    cantidad_prestamos))

                                            val filaDatos = listOf(
                                                id_padre.toString(),
                                                nombre_padre.take(20),
                                                id_hijo,
                                                nombre_hijo.take(20),
                                                cantidad_prestamos
                                            )


                                            datosPDF.add(filaDatos)


                                            if(i == ultimoIndice){
                                                if(datosPlantillaSelectReportes.isEmpty()){
                                                    scrollViewTabla.visibility = ScrollView.GONE
                                                    frameDatosNoEncontrados.visibility = FrameLayout.VISIBLE
                                                }else{
                                                    layoutGenerarPDF.visibility = ConstraintLayout.VISIBLE

                                                    launch{
                                                        for(datos in datosPlantillaSelectReportes){
                                                            agregarTupla(datos.idpadre,datos.nombrePadre,datos.idHijo,datos.nombreHijo,datos.cantidadPrestamos)
                                                            delay(200)
                                                        }
                                                    }
                                                }

                                            }

                                        }

                            }

                            //Toast.makeText(applicationContext, "Artículos obtenidos con éxito", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            //Toast.makeText(applicationContext, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                            scrollViewTabla.visibility = ScrollView.GONE
                            frameDatosNoEncontrados.visibility = FrameLayout.VISIBLE
                            e.printStackTrace()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
                //Toast.makeText(applicationContext, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            // Parámetros para enviar al PHP si es necesario (en este caso no los necesitas)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["consulta"] = consulta
                return params
            }
        }

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(this).add(stringRequest)
    }

    override fun onStop() {
        super.onStop()
        // Detener el servicio de sonido cuando la app va al segundo plano
        stopService(Intent(this, SoundService::class.java))
    }

    override fun onStart() {
        super.onStart()
        // Reiniciar el servicio cuando la app vuelve al primer plano
        startService(Intent(this, SoundService::class.java))
    }

    override fun onPause() {
        super.onPause()
        // Enviar la acción PAUSE_SOUND al servicio para pausar el sonido
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.action = "PAUSE_SOUND"
        startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        // Enviar la acción RESUME_SOUND al servicio para reanudar el sonido
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.action = "RESUME_SOUND"
        startService(serviceIntent)
    }

    override fun onBackPressed() {
    }
}