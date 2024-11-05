package mx.tecnm.cdhidalgo.e401_2024.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.R
import java.text.NumberFormat
import java.util.ArrayList

class AdaptadorCarrito (private val lista: ArrayList<Producto>)
    : RecyclerView.Adapter<AdaptadorCarrito.ListaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            :ListaViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_carrito,parent,false)
        return ListaViewHolder(vista)
    }

    override fun onBindViewHolder(holder:ListaViewHolder, position: Int) {
        holder.render(lista[position])
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ListaViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        val _foto: ImageView = itemView.findViewById(R.id.vista_imagen)
        val _nombreCorto: TextView = itemView.findViewById(R.id.vista_nombre_producto)
        val _categoria: TextView = itemView.findViewById(R.id.vista_categoria_producto)
        val _precio: TextView = itemView.findViewById(R.id.vista_precio_producto)

        fun render(producto: Producto){
            Glide.with(itemView)
                .load(producto.imagen)
                .override(200,200)
                .into(_foto)
            _nombreCorto.text = producto.nombreCorto
            _categoria.text = "Categoría: ${producto.categoria.toString()}"
            val numberFormat = NumberFormat.getCurrencyInstance()
            val cantidadFormateada = numberFormat.format(producto?.precio)
            _precio.text = "$cantidadFormateada"
        }
    }
}