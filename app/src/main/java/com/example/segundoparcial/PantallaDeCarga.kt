package com.example.segundoparcial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlin.random.Random


class PantallaDeCarga : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        setContentView(R.layout.pantalla_de_carga)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val proxima = intent.getStringExtra("proximaPagina")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val idUsuario = intent.getStringExtra("idUsuario")

        //Toast.makeText(this,"Valor recivido en pantalla de carga" + cuenta, Toast.LENGTH_SHORT).show()
        val txtMensajeDeCarga = findViewById<TextView>(R.id.txtMensajeDeCarga)
        val imagen = findViewById<ImageView>(R.id.gifCarga)
        var random = Random.nextInt(1, 8)
        when (random) {
            1 -> Glide.with(this)
                .load(R.drawable.icono_pantalla_de_carga_1)
                .into(imagen);
            2 -> Glide.with(this)
                .load(R.drawable.icono_pantalla_de_carga_2)
                .into(imagen);
            3 -> Glide.with(this)
                .load(R.drawable.icono_pantalla_de_carga_3)
                .into(imagen);
            4 -> Glide.with(this)
                .load(R.drawable.icono_pantalla_de_carga_4)
                .into(imagen);
            5 -> Glide.with(this)
                .load(R.drawable.icono_pantalla_de_carga_5)
                .into(imagen);
            6 -> Glide.with(this)
                .load(R.drawable.icono_pantalla_de_carga_6)
                .into(imagen);
            7 -> Glide.with(this)
                .load(R.drawable.icono_pantalla_de_carga_7)
                .into(imagen);
        }

        random = Random.nextInt(1, 8)

        when (random) {
            1 -> txtMensajeDeCarga.text = "La Primera capa es la \nCuriosidad"
            2 -> txtMensajeDeCarga.text = "Un dia o dia uno"
            3 -> txtMensajeDeCarga.text = "El ultimo paso es dar otro más"
            4 -> txtMensajeDeCarga.text = "La novena nube te espera"
            5 -> txtMensajeDeCarga.text = "Dicen que un libro al dia\nMantiene al doc lejos"
            6 -> txtMensajeDeCarga.text = "Puede que el amor de tu vida tenga un apellido que empiece con \"mono\" y termine con \"grafía\""
            7 -> txtMensajeDeCarga.text = "Hay una capa para todo el mundo!!!"
        }

        //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
        //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
        if (proxima.equals("main")) {
            val ruta = MainActivity::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("main")) {
            val ruta = MainActivity::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                Thread.sleep(2000)
                startActivity(intent)
            }.start()
        }
        if (proxima.equals("menuPrincipal")) {
            val ruta = MenuPrincipalUsuario::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }
        if (proxima.equals("articulosEnEstanteria")) {
            val ruta = ArticulosEnEstanteria::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }


        if (proxima.equals("articulosEnDespacho")) {
            val ruta = ArticulosEnDespacho::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("todosLosArticulos")) {
            val ruta = TodosLosArticulos::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("autores")) {
            val ruta = Autores::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("congresos")) {
            val ruta = Congresos::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("revistas")) {
            val ruta = Revistas::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("Centros")) {
            val ruta = Centros::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("vistaArticulos")) {
            val ruta = VistaArticulos::class.java
            val id_articulo = intent.getStringExtra("idArticulo").toString()
            val desde = intent.getStringExtra("desde").toString()
            //Toast.makeText(this,"Desde en pantalla de carga " + desde, Toast.LENGTH_SHORT).show()
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("idArticulo", id_articulo)
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("desde" , desde)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("ArticulosPorAutor")) {
            val ruta = ArticulosPorAutor::class.java
            val id_autor = intent.getStringExtra("idAutor").toString()
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idAutor" , id_autor)
                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("PerfilAutor")) {
            val ruta = VistaPerfilAutor::class.java
            val id_autor = intent.getStringExtra("idAutor").toString()
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idAutor" , id_autor)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("VistaCongreso")) {
            val ruta = VistaCongresos::class.java
            val idCongreso = intent.getStringExtra("idCongreso").toString()
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idCongreso" , idCongreso)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }



        if (proxima.equals("VistaActas")) {
            val ruta = VistaActas::class.java
            val idActa = intent.getStringExtra("idActa").toString()
            val idCongreso = intent.getStringExtra("idCongreso").toString()
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idActa" , idActa)
                    putExtra("idCongreso" , idCongreso)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("VistaRevistas")) {
            val ruta = VistaRevistas::class.java
            val idRevista = intent.getStringExtra("idRevista").toString()

            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idRevista" , idRevista)
                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("vistaEdiciones")) {
            val ruta = VistaEdiciones::class.java
            val idEdicion = intent.getStringExtra("idEdicion").toString()
            val idRevista = intent.getStringExtra("idRevista").toString()

            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idEdicion" , idEdicion)
                    putExtra("idRevista" , idRevista)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("crearUsuario")) {
            val ruta = CrearUsuario::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("login")) {
            val ruta = MainActivity::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("VistaCentros")) {
            val ruta = VistaCentros::class.java
            val idCentro = intent.getStringExtra("idCentro").toString()
            //Toast.makeText(this,"id Informe en PantallaDeCarga " + idInforme, Toast.LENGTH_SHORT).show()
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idCentro" , idCentro)
                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("VistaInformesTecnicos")) {
            val ruta = VistaInformeTecnico::class.java
            val idInformeTecnico = intent.getStringExtra("idInforme").toString()
            val idCentro = intent.getStringExtra("idCentro").toString()
            //Toast.makeText(this,"id Informe en PantallaDeCarga " + idInforme, Toast.LENGTH_SHORT).show()
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("idUsuario" , idUsuario)
                    putExtra("idInforme" , idInformeTecnico)
                    putExtra("idCentro" , idCentro)

                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }


        if (proxima.equals("cambiarContrasena")) {
            val ruta = CambiarContraseña::class.java


            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("idUsuario" , idUsuario)
                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("reportes")) {
            val ruta = Reportes::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("idUsuario" , idUsuario)
                    putExtra("tipoUsuario" , tipoUsuario)
                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("perfilUsuario")) {
            val ruta = VistaPerfilUsuario::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("idUsuario" , idUsuario)
                    putExtra("tipoUsuario" , tipoUsuario)
                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("vistaReportes")) {
            val ruta = VistaReportes::class.java
            val consultaFinal = intent.getStringExtra("consulta").toString()
            Thread {
                val intent = Intent(this, ruta).apply {
                    //flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("idUsuario" , idUsuario)
                    putExtra("tipoUsuario" , tipoUsuario)
                    putExtra("consulta" , consultaFinal)
                }
                Thread.sleep(500)
                startActivity(intent)
            }.start()
        }

    }
    override fun onBackPressed() {
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
}



