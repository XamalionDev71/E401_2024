package mx.tecnm.cdhidalgo.e401_2024

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario
import java.text.NumberFormat

class DetalleProducto : AppCompatActivity() {
    private lateinit var btnRegresar: Button
    private lateinit var btnCarrito: Button
    private lateinit var btnComprar: Button
    private lateinit var nombreCliente: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRegresar = findViewById(R.id.btnRegresar)
        btnCarrito = findViewById(R.id.btnCarrito)
        btnComprar = findViewById(R.id.btnComprar)

        nombreCliente = findViewById(R.id.tvNombreCliente)

        val usuario = intent.getParcelableExtra<Usuario>("usuario")
        val producto = intent.getParcelableExtra<Producto>("producto")
        val misCompras = intent.getParcelableArrayListExtra<Producto>("misCompras")

        nombreCliente.text = "Cliente: ${usuario?.nombre} ${usuario?.apaterno} ${usuario?.amaterno}"

        if (producto != null){
            val _foto: ImageView = findViewById(R.id.detalle_foto)
            val _nombre:TextView = findViewById(R.id.detalle_nombre)
            val _descripcion:TextView = findViewById(R.id.detalle_descripcion)
            val _precio:TextView = findViewById(R.id.detalle_precio)

            Glide.with(this)
                .load(producto?.imagen)
                .override(350,350)
                .into(_foto)
            _nombre.text = producto.nombre
            _descripcion.text = producto.descripcion

            val numberFormat = NumberFormat.getCurrencyInstance()
            val cantidadFormateada = numberFormat.format(producto?.precio)
            _precio.text = "$cantidadFormateada"
        }

        btnRegresar.setOnClickListener {
            val intent = Intent(this,Tienda::class.java)
            intent.putExtra("usuario",usuario)
            intent.putExtra("misCompras",misCompras)
            finish()
            startActivity(intent)
        }

        btnCarrito.setOnClickListener {
            val intent = Intent(this,Carrito::class.java)
            intent.putExtra("usuario",usuario)
            intent.putExtra("misCompras",misCompras)
            startActivity(intent)
        }

        btnComprar.setOnClickListener {
            val intent = Intent(this,Tienda::class.java)
            intent.putExtra("compraProducto",producto)
            intent.putExtra("usuario",usuario)
            intent.putExtra("misCompras",misCompras)
            startActivity(intent)
        }
    }
}