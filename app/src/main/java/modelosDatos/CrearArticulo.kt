package modelosDatos

data class CrearArticulo(
    val titulo: String,
    var tituloAbreviado: String,
    var palabrasClave: ArrayList<Palabra>,
    var tipoArticulo: String,
    var autor: String,
    var correo: String,
    var localizacion: String,
    var url: String
)
