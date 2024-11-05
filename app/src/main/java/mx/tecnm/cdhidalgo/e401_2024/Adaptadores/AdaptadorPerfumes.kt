package mx.tecnm.cdhidalgo.e401_2024.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.R
import mx.tecnm.cdhidalgo.e401_2024.miCarrito
import java.text.NumberFormat
import java.util.ArrayList

class AdaptadorPerfumes (private val lista: ArrayList<Producto>)
    : RecyclerView.Adapter<AdaptadorPerfumes.ListaViewHolder>(){

    private lateinit var mListener:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            :ListaViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_producto,parent,false)
        return ListaViewHolder(vista, mListener)
    }

    override fun onBindViewHolder(holder:ListaViewHolder, position: Int) {
        holder.render(lista[position])
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ListaViewHolder(itemView: View, listener:onItemClickListener):
        RecyclerView.ViewHolder(itemView) {
        val _foto: ImageView = itemView.findViewById(R.id.foto_producto)
        val _nombreCorto: TextView = itemView.findViewById(R.id.nombreCorto_producto)
        val _descripcion: TextView = itemView.findViewById(R.id.descripcion_producto)
        val _precio: TextView = itemView.findViewById(R.id.precio_producto)
        val _comprar: ImageButton = itemView.findViewById(R.id.btnAgregarProducto)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

        fun render(producto: Producto){
            Glide.with(itemView)
                .load(producto.imagen)
                .override(380,380)
                .into(_foto)
            _nombreCorto.text = producto.nombreCorto
            _descripcion.text = producto.descripcion
            val numberFormat = NumberFormat.getCurrencyInstance()
            val cantidadFormateada = numberFormat.format(producto?.precio)
            _precio.text = "$cantidadFormateada"
            _comprar.setOnClickListener {
                miCarrito.add(producto)
            }
        }
    }
}