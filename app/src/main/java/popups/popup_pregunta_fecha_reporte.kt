package popups

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.segundoparcial.PantallaDeCarga
import com.example.segundoparcial.R
import modelosDatos.URL
import java.util.*

class PopupHelperPreguntaFechaReporte(private val context: Context , private val selectConsulta : String , private val fromConsulta : String , private val whereConsulta : String , private val groupByConsulta : String, private val orderByConsulta : String , private val limitConsulta : String , private val tiempoReporte : String , private val losMasoLosMenos : String , private val cantidadLosMasLosMenos : String , private val idUsuario : String , private val tipoUsuario : String) {

    var url = URL.BASE_URL+"Tema/CrearTemaRevista.php"
    private var primerDia = ""
    private var ultimoDia = ""
    private lateinit var txtFechaReporte: TextView
    private lateinit var btnGenerarReporte : Button

    fun showPopup(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_pregunta_fecha_reporte, null)




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


        txtFechaReporte  = popupView.findViewById(R.id.txtFechaReporte)
        btnGenerarReporte = popupView.findViewById(R.id.btnGenerarReporte)
        btnGenerarReporte.isEnabled = false

        checkInputFields(txtFechaReporte, btnGenerarReporte)

        if (tiempoReporte.equals(" Historico") ) {
            // No desplegar DatePicker
        } else if (tiempoReporte.equals(" Semanal")) {
            txtFechaReporte.setOnClickListener{
                showWeeklyDatePicker(txtFechaReporte)
                //Toast.makeText(context, "Semana: $primerDia - $ultimoDia", Toast.LENGTH_SHORT).show()
            }

        } else if (tiempoReporte.equals(" Mensual")) {
            txtFechaReporte.setOnClickListener{
                showMonthlyDatePicker(txtFechaReporte)
                //Toast.makeText(context, "Mes: $primerDia - $ultimoDia", Toast.LENGTH_SHORT).show()
            }
        } else if (tiempoReporte.equals(" Anual")) {
            txtFechaReporte.setOnClickListener{
                showYearlyDatePicker(txtFechaReporte)
                //Toast.makeText(context, "Año: $primerDia - $ultimoDia", Toast.LENGTH_SHORT).show()
            }
        }



        txtFechaReporte.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas hacer nada aquí
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Ejecutar la función cada vez que el texto cambia
                checkInputFields(txtFechaReporte, btnGenerarReporte)

            }

            override fun afterTextChanged(editable: Editable?) {
                // También puedes usarlo si necesitas alguna acción después de que se cambió el texto
            }
        })

        
        btnGenerarReporte.setOnClickListener {
           // AnimationHelper.dismissPopupView(popupView, 200) {
               // Toast.makeText(context, "primer dia $primerDia ultimo dia $ultimoDia", Toast.LENGTH_SHORT).show()
               // Toast.makeText(context, "ultimo dia $ultimoDia", Toast.LENGTH_SHORT).show()
                val whereconsultaAuxiliar1 = if (tiempoReporte.equals(" Historico")) "" else whereConsulta.replace("'@'", "'"+primerDia+"'")
                val whereconsultaAuxiliar2 = if (tiempoReporte.equals(" Historico")) "" else whereconsultaAuxiliar1.replace("^", ultimoDia)
                var auxiliarOrderBy = if (losMasoLosMenos == " más") "ASC" else "DESC"
                val orderbyconsultaAuxiliar = orderByConsulta.replace("°", auxiliarOrderBy)
                val limitConsultaAuxiliar = limitConsulta.replace("*", cantidadLosMasLosMenos)

                val consultaFinal = selectConsulta + fromConsulta + whereconsultaAuxiliar2 + groupByConsulta + orderbyconsultaAuxiliar + limitConsultaAuxiliar

            val intent = Intent(context, PantallaDeCarga::class.java).apply {
                //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("proximaPagina" , "vistaReportes")
                putExtra("idUsuario" , idUsuario)
                putExtra("tipoUsuario" , tipoUsuario)
                putExtra("consulta" , consultaFinal)
            }
            context.startActivity(intent)
            popupWindow.dismiss()

       // }


    }
    }

    private fun checkInputFields(
        txtFechaReporte: TextView,
        btnCrearTipoCongreso: Button
    ) {
        val isEnabled = if (tiempoReporte.equals(" Historico") ) true else txtFechaReporte.text.isNotEmpty()

        if (tiempoReporte.equals(" Historico") ){
            txtFechaReporte.text = "Todos los tiempos"
        }

        btnCrearTipoCongreso.isEnabled = isEnabled
        btnCrearTipoCongreso.setBackgroundColor(
            if (isEnabled) context.getColor(R.color.colorButtonEnabled)
            else context.getColor(R.color.colorButtonDefault)
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


    private fun showWeeklyDatePicker(txtFechaReporte: TextView) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Establecer la fecha seleccionada
                calendar.set(year, month, dayOfMonth)

                // Establecer el primer día de la semana (lunes)
                val diaSemana = calendar.get(Calendar.DAY_OF_WEEK)
                var diferencia = 0

                // Si el día seleccionado no es lunes, ajustamos para que sea lunes
                if (diaSemana != Calendar.MONDAY) {
                    // Restamos los días necesarios para llegar al lunes
                    diferencia = Calendar.MONDAY - diaSemana
                    if (diferencia > 0) {
                        diferencia -= 7  // Si el día es después de lunes (martes, miércoles...), retrocedemos
                    }
                }

                calendar.add(Calendar.DAY_OF_WEEK, diferencia)

                // Guardar el primer día (lunes) en formato año-mes-día
                primerDia = String.format(
                    "%04d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                // Guardar el último día (domingo) de la semana, sumamos 6 días
                calendar.add(Calendar.DAY_OF_WEEK, 6)
                ultimoDia = String.format(
                    "%04d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                // Mostrar el rango de fechas en el TextView
                txtFechaReporte.text = "Semana: $primerDia - $ultimoDia"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }




    private fun showMonthlyDatePicker(txtFechaReporte: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, _ ->
                // Calcular el primer y último día del mes
                calendar.set(year, month, 1)
                primerDia = String.format(
                    "%04d-%02d-%02d",
                    year,
                    month + 1,
                    1
                )
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                ultimoDia = String.format(
                    "%04d-%02d-%02d",
                    year,
                    month + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                txtFechaReporte.text = "Mes: $primerDia - $ultimoDia"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.findViewById<ViewGroup>(
            Resources.getSystem().getIdentifier("android:id/day", null, null)
        )?.visibility = View.GONE
        datePickerDialog.show()
    }


    private fun showYearlyDatePicker(txtFechaReporte: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, _, _ ->
                // Calcular el primer y último día del año
                primerDia = String.format(
                    "%04d-%02d-%02d",
                    year,
                    1,
                    1
                )
                ultimoDia = String.format(
                    "%04d-%02d-%02d",
                    year,
                    12,
                    31
                )

                txtFechaReporte.text = "Año: $primerDia - $ultimoDia"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.findViewById<ViewGroup>(
            Resources.getSystem().getIdentifier("android:id/month", null, null)
        )?.visibility = View.GONE
        datePickerDialog.datePicker.findViewById<ViewGroup>(
            Resources.getSystem().getIdentifier("android:id/day", null, null)
        )?.visibility = View.GONE
        datePickerDialog.show()
    }




}
