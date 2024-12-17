package com.example.segundoparcial

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import popups.PopupHelperPreguntaFechaReporte
import popups.TextViewTiembleManager
import kotlin.random.Random

class Reportes : AppCompatActivity() {

    private var tipoReporte = ""
    private var tiempoReporte = ""
    private var losMasoLosMenos = ""
    private var cantidadLosMasLosMenos = ""
    private lateinit var txtTipoReporte : TextView
    private lateinit var linearLayout1: LinearLayout
    private lateinit var linearLayout2: LinearLayout
    private lateinit var linearLayout3: LinearLayout
    private lateinit var txtLosMasoLosMenos: TextView
    private lateinit var txtCantidadLosMasLosMenos: EditText
    private lateinit var layoutGenerar: ConstraintLayout
    val temblor = TextViewTiembleManager()

    private var selectConsulta = ""
    private var fromConsulta = ""
    private var whereConsulta = ""
    private var groupByConsulta = ""
    private var orderByConsulta = ""
    private var limitConsulta = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)


        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipal)
        val random = Random.nextInt(1, 5)
        when (random) {
            1 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_1)
            2 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_2)
            3 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_3)
            4 -> layoutPrincipal.setBackgroundResource(R.drawable.paisaje_4)

        }

        val idUsuario = intent.getStringExtra("idUsuario")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")

        // Inicializa los RadioButtons de tipo de reporte
        val btnReporteDeRevistas: RadioButton = findViewById(R.id.btnReporteDeRevistas)
        val btnReporteDeCongresos: RadioButton = findViewById(R.id.btnReporteDeCongresos)
        val btnReporteDeInformesTecnicos: RadioButton = findViewById(R.id.btnReporteDeInformesTecnicos)
        val btnReporteDeArticulos: RadioButton = findViewById(R.id.btnReporteDeArticulos)
        val btnVoler: ImageButton = findViewById(R.id.btnVoler)

        // Inicializa los RadioButtons de tiempo de reporte
        val btnReporteAnual: RadioButton = findViewById(R.id.btnReporteAnual)
        val btnReporteMensual: RadioButton = findViewById(R.id.btnReporteMensual)
        val btnReporteSemanal: RadioButton = findViewById(R.id.btnReporteSemanal)
        val btnReporteHistorico: RadioButton = findViewById(R.id.btnReporteHistorico)
        layoutGenerar = findViewById(R.id.layoutGenerar)
        val layoutReporte = findViewById<ConstraintLayout>(R.id.layoutReporte)

        val btnLosMas: RadioButton = findViewById(R.id.btnLosMas)
        val btnLosMenos: RadioButton = findViewById(R.id.btnlosMenos)
        val btnOjo = findViewById<ImageView>(R.id.btnOjo)
        val btnGenerar = findViewById<ImageView>(R.id.btnGenerar)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_generar)
            .override(410, 710)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(btnGenerar)


        txtTipoReporte = findViewById(R.id.txtTipoReporte)
        linearLayout1 = findViewById(R.id.linearLayout)
        linearLayout2 = findViewById(R.id.linearLayout2)
        linearLayout3 = findViewById(R.id.linearLayout3)
        txtLosMasoLosMenos = findViewById(R.id.txtLosMasoLosMenos)
        txtCantidadLosMasLosMenos = findViewById(R.id.txtCantidadLosMasLosMenos)

        layoutGenerar.visibility = View.GONE

        btnOjo.setOnClickListener {
            if (layoutReporte.visibility == View.VISIBLE) {
                layoutReporte.visibility = View.GONE
            } else {
                layoutReporte.visibility = View.VISIBLE
            }
        }

        Glide.with(this)
            .asGif()
            .load(R.drawable.icono_ojo_animado)
            .into(btnOjo)

        for (i in 0 until layoutGenerar.childCount) {
            val child = layoutGenerar.getChildAt(i)
            child.setOnClickListener {
                val popupHelper = PopupHelperPreguntaFechaReporte(this, selectConsulta , fromConsulta , whereConsulta , groupByConsulta , orderByConsulta , limitConsulta , tiempoReporte , losMasoLosMenos , cantidadLosMasLosMenos , idUsuario.toString() , tipoUsuario.toString())
                popupHelper.showPopup(child)

            }
        }

        txtCantidadLosMasLosMenos.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                actualizarMensajeDetalles()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cantidadLosMasLosMenos = " " + s.toString() // Actualiza el valor de cantidadLosMasLosMenos en tiempo real
                actualizarMensajeDetalles()
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    actualizarMensajeDetalles()
                }
            }
        })

        // Agrega listeners para los RadioButtons de tipo de reporte
        btnReporteDeRevistas.setOnClickListener {
            tipoReporte = " Revistas"

            selectConsulta = "SELECT \n" +
                    "    revista.id_revista AS id_padre,\n" +
                    "    revista.nombre AS nombre_padre, \n" +
                    "    edicion.numero_edicion AS id_hijo, \n" +
                    "    edicion.titulo AS nombre_hijo, \n" +
                    "    COUNT(\n" +
                    "        CASE \n" +
                    "            WHEN revista.id_revista = edicion.id_revista \n" +
                    "            AND edicion.numero_edicion = articulo_edicion.numero_edicion \n" +
                    "            AND articulo_edicion.id_articulo = articulo.id_articulo \n" +
                    "            AND articulo.id_articulo = prestamo.id_articulo\n" +
                    "        THEN 1 \n" +
                    "        END\n" +
                    "    ) AS cantidad_prestamos \n"
            fromConsulta = "FROM \n" +
                    "    revista \n" +
                    "INNER JOIN edicion \n" +
                    "    ON revista.id_revista = edicion.id_revista\n" +
                    "INNER JOIN articulo_edicion \n" +
                    "    ON edicion.numero_edicion = articulo_edicion.numero_edicion\n" +
                    "INNER JOIN articulo \n" +
                    "    ON articulo_edicion.id_articulo = articulo.id_articulo\n" +
                    "LEFT JOIN prestamo \n" +
                    "    ON articulo.id_articulo = prestamo.id_articulo \n"
            whereConsulta = "WHERE prestamo.fecha_prestamo BETWEEN '@' AND '^' \n"
            groupByConsulta = "GROUP BY \n" +
                    "    revista.id_revista, \n" +
                    "    revista.nombre, \n" +
                    "    edicion.numero_edicion, \n" +
                    "    edicion.titulo \n"
            orderByConsulta ="ORDER BY \n" +
                    "    cantidad_prestamos ° "
            limitConsulta = " \n LIMIT * ;"



            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout1 , btnReporteDeRevistas)
        }

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        btnReporteDeCongresos.setOnClickListener {
            tipoReporte = " Congresos"

            selectConsulta = "SELECT \n" +
                    "    congreso.id_congreso AS id_padre, \n" +
                    "    congreso.nombre AS nombre_padre, \n" +
                    "    acta_edicion.id_acta AS id_hijo, \n" +
                    "    acta_edicion.nombre AS nombre_hijo, \n" +
                    "    COUNT(\n" +
                    "        CASE \n" +
                    "            WHEN congreso.id_congreso = acta_edicion.id_congreso \n" +
                    "            AND acta_edicion.id_acta = articulo_acta.id_acta \n" +
                    "            AND articulo_acta.id_articulo = articulo.id_articulo \n" +
                    "            AND articulo.id_articulo = prestamo.id_articulo\n" +
                    "        THEN 1 \n" +
                    "        END\n" +
                    "    ) AS cantidad_prestamos\n"

            fromConsulta = "FROM \n" +
                    "    congreso \n" +
                    "INNER JOIN acta_edicion \n" +
                    "    ON congreso.id_congreso = acta_edicion.id_congreso  -- Cambié 'acta' por 'acta_edicion' para hacer la relación correcta\n" +
                    "INNER JOIN articulo_acta\n" +
                    "    ON acta_edicion.id_acta = articulo_acta.id_acta\n" +
                    "INNER JOIN articulo \n" +
                    "    ON articulo_acta.id_articulo = articulo.id_articulo\n" +
                    "LEFT JOIN prestamo \n" +
                    "    ON articulo.id_articulo = prestamo.id_articulo\n"

            whereConsulta = "WHERE prestamo.fecha_prestamo BETWEEN '@' AND '^'\n"

            groupByConsulta = "GROUP BY \n" +
                    "    congreso.id_congreso, \n" +
                    "    congreso.nombre, \n" +
                    "    acta_edicion.id_acta, \n" +
                    "    acta_edicion.nombre\n"

            orderByConsulta ="ORDER BY \n" +
                    "    cantidad_prestamos ° "
            limitConsulta = " \n LIMIT * ;"

            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout1 , btnReporteDeCongresos)
        }

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        btnReporteDeInformesTecnicos.setOnClickListener {
            tipoReporte = " Informes Técnicos"

            selectConsulta = "SELECT \n" +
                    "    centro.id_centro, \n" +
                    "    centro.nombre, \n" +
                    "    informe_tecnico.numero_informe, \n" +
                    "    informe_tecnico.tema, \n" +
                    "    COUNT(\n" +
                    "        CASE \n" +
                    "            WHEN centro.id_centro = informe_tecnico.id_centro \n" +
                    "            AND informe_tecnico.numero_informe = articulo_informe_tecnico.numero_informe \n" +
                    "            AND articulo_informe_tecnico.id_articulo = articulo.id_articulo \n" +
                    "            AND articulo.id_articulo = prestamo.id_articulo\n" +
                    "        THEN 1 \n" +
                    "        END\n" +
                    "    ) AS cantidad_prestamos \n"
            fromConsulta = "FROM \n" +
                    "    centro \n" +
                    "INNER JOIN informe_tecnico\n" +
                    "\tON centro.id_centro = informe_tecnico.id_centro\n" +
                    "INNER JOIN articulo_informe_tecnico \n" +
                    "    ON informe_tecnico.numero_informe = articulo_informe_tecnico.numero_informe     \n" +
                    "INNER JOIN articulo \n" +
                    "    ON articulo_informe_tecnico.id_articulo = articulo.id_articulo\n" +
                    "LEFT JOIN prestamo \n" +
                    "    ON articulo.id_articulo = prestamo.id_articulo\n"
            whereConsulta = "WHERE prestamo.fecha_prestamo BETWEEN '@' AND '^'\n"
            groupByConsulta = "GROUP BY \n" +
                    "    centro.id_centro, \n" +
                    "    centro.nombre, \n" +
                    "    informe_tecnico.numero_informe, \n" +
                    "    informe_tecnico.tema\n"
            orderByConsulta ="ORDER BY \n" +
                    "    cantidad_prestamos ° "
            limitConsulta = " \n LIMIT * ;"

            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout1 , btnReporteDeInformesTecnicos)
        }

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        btnReporteDeArticulos.setOnClickListener {
            tipoReporte = " Articulos"

            selectConsulta= "SELECT \n" +
                    "\tCOALESCE(revista.id_revista, congreso.id_congreso , centro.id_centro) AS id_padre,\n" +
                    "\tCOALESCE(revista.nombre, congreso.nombre, centro.nombre) AS nombre_padre,\n" +
                    "\tarticulo.id_articulo AS id_hijo,\n" +
                    "articulo.titulo AS nombre_hijo,\n" +
                    "COUNT(prestamo.id_prestamo) AS cantidad_prestamos \n"

            fromConsulta = "FROM \n" +
                    "    articulo\n" +
                    "INNER JOIN prestamo \n" +
                    "    ON articulo.id_articulo = prestamo.id_articulo\n" +
                    "LEFT JOIN articulo_edicion \n" +
                    "    ON articulo.id_articulo = articulo_edicion.id_articulo\n" +
                    "LEFT JOIN edicion \n" +
                    "    ON articulo_edicion.numero_edicion = edicion.numero_edicion\n" +
                    "LEFT JOIN revista \n" +
                    "    ON edicion.id_revista = revista.id_revista\n" +
                    "LEFT JOIN articulo_acta \n" +
                    "    ON articulo.id_articulo = articulo_acta.id_articulo\n" +
                    "LEFT JOIN acta_edicion \n" +
                    "    ON articulo_acta.id_acta = acta_edicion.id_acta\n" +
                    "LEFT JOIN congreso \n" +
                    "    ON acta_edicion.id_congreso = congreso.id_congreso\n" +
                    "LEFT JOIN articulo_informe_tecnico \n" +
                    "    ON articulo.id_articulo = articulo_informe_tecnico.id_articulo\n" +
                    "LEFT JOIN informe_tecnico \n" +
                    "    ON articulo_informe_tecnico.numero_informe = informe_tecnico.numero_informe\n" +
                    "LEFT JOIN centro\n" +
                    "\tON informe_tecnico.id_centro = centro.id_centro\n"
            whereConsulta = "WHERE prestamo.fecha_prestamo BETWEEN '@' AND '^'\n"
            groupByConsulta = "GROUP BY \n" +
                    "    articulo.id_articulo, \n" +
                    "    articulo.titulo, \n" +
                    "    revista.nombre, \n" +
                    "    congreso.nombre, \n" +
                    "    informe_tecnico.numero_informe\n"
            orderByConsulta ="ORDER BY \n" +
                    "    cantidad_prestamos ° "
            limitConsulta = " \n LIMIT * ;"


            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout1 , btnReporteDeArticulos)
        }

        // Agrega listeners para los RadioButtons de tiempo de reporte
        btnReporteAnual.setOnClickListener {
            tiempoReporte = " Anual"
            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout2 , btnReporteAnual)
        }

        btnReporteMensual.setOnClickListener {
            tiempoReporte = " Mensual"
            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout2 , btnReporteMensual)
        }

        btnReporteSemanal.setOnClickListener {
            tiempoReporte = " Semanal"
            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout2 , btnReporteSemanal)
        }

        btnReporteHistorico.setOnClickListener {
            tiempoReporte = " Historico"
            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout2 , btnReporteHistorico)
        }

        btnLosMas.setOnClickListener {
            losMasoLosMenos = " más"
            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout3 , btnLosMas)
        }

        btnLosMenos.setOnClickListener {
            losMasoLosMenos = " menos"
            actualizarMensajeDetalles()
            setColorradioButtons(linearLayout3 , btnLosMenos)
        }

        btnVoler.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menuPrincipal")
                putExtra("tipoUsuario" , tipoUsuario)
                putExtra("idUsuario" , idUsuario)
            }
            startActivity(intent)
        }

    }

    /*fun animarTextViewTiemblo(textView: TextView) {
        val animator = ValueAnimator.ofFloat(0f, 10f, -10f, 0f).apply {
            duration = 100 // Duración corta para un temblor rápido
            repeatCount = 3 // Número de veces que tiembla
            interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                textView.translationX = value
            }
        }
        animator.start()
    }*/

    fun actualizarMensajeDetalles(){

        var cantidad = "El"

        if(tipoReporte == "Revistas"){
            cantidad = "La"
        }else{
            cantidad = "El"
        }

        if(cantidadLosMasLosMenos != "1"){
            if(tipoReporte == "Revistas"){
                cantidad = "Las"
            }else{
                cantidad = "Los"
            }

            }
        if(cantidadLosMasLosMenos != "" && losMasoLosMenos != ""){
            val mensajeAux= cantidad +cantidadLosMasLosMenos+losMasoLosMenos
            txtLosMasoLosMenos.setText(mensajeAux)
        }

        if(cantidadLosMasLosMenos != "" && tipoReporte != "" && losMasoLosMenos != "" && tiempoReporte != "" && txtCantidadLosMasLosMenos.text.toString() != ""){
            val mensaje = cantidad + cantidadLosMasLosMenos + tipoReporte + " con" + losMasoLosMenos + " movimiento" + tiempoReporte
            txtTipoReporte.setText(mensaje)
            txtTipoReporte.setTypeface(null, Typeface.BOLD)
            txtTipoReporte.setTextSize(16f)
            temblor.detenerTiemble(txtTipoReporte)
            layoutGenerar.visibility = View.VISIBLE

        }else{
            temblor.iniciarTiemble(txtTipoReporte)
            txtTipoReporte.setText("Calculando el tipo de tu reporte...")
            layoutGenerar.visibility = View.GONE

        }





    }

    fun setColorradioButtons(layout: ViewGroup, radioButton: RadioButton) {
        // Recorre todos los hijos del layout, incluidos los hijos de los hijos
        for (i in 0 until layout.childCount) {
            val child: View = layout.getChildAt(i)

            // Si el hijo es un RadioButton, elimínale el fondo
            if (child is RadioButton) {
                child.setBackgroundResource(0)  // Elimina el fondo
            }

            // Si el hijo es otro ViewGroup (como un LinearLayout o un RadioGroup),
            // llama recursivamente para recorrer sus hijos
            if (child is ViewGroup) {
                setColorradioButtons(child, radioButton)
            }
        }

        // Asigna un fondo al RadioButton seleccionado
        radioButton.setBackgroundResource(R.drawable.rounded_transparent_lighter_salmon_5dp)
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
