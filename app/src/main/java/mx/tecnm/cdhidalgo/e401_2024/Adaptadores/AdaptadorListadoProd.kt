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

class AdaptadorListadoProd(val lista:ArrayList<Producto>)
: RecyclerView.Adapter<AdaptadorListadoProd.ListaViewHolder>() {
    private lateinit var mListener:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ListaViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_producto2,parent,false)
        return ListaViewHolder(vista, mListener)
    }

    override fun onBindViewHolder(holder: ListaViewHolder,position: Int) {
        holder.render(lista[position])
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ListaViewHolder(itemView: View, listener: onItemClickListener):
        RecyclerView.ViewHolder(itemView) {
        val _foto: ImageView = itemView.findViewById(R.id.fotoProd)
        val _nomCorto: TextView = itemView.findViewById(R.id.nombreCortoProd)
        val _descripcion: TextView = itemView.findViewById(R.id.descripcionProd)
        val _categoria: TextView = itemView.findViewById(R.id.categoriaProd)
        val _precio: TextView = itemView.findViewById(R.id.precioProd)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

        fun render(producto: Producto){
            Glide.with(itemView)
                .load(producto.imagen)
                .override(170,170)
                .into(_foto)
            _nomCorto.text = "${producto.nombreCorto}"
            _descripcion.text = "${producto.descripcion}"
            _categoria.text = "${producto.categoria}"
            val numberFormat = NumberFormat.getCurrencyInstance()
            val cantidadFormateada = numberFormat.format(producto?.precio)
            _precio.text = "${cantidadFormateada}"
        }
    }
}