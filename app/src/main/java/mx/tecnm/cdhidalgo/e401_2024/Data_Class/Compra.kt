package mx.tecnm.cdhidalgo.e401_2024.Data_Class

data class Compra(
    var correo:String?,
    var total:String?,
    var numeroProductos:String?,
    var producto:ArrayList<Producto>)
