package mx.tecnm.cdhidalgo.e401_2024

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.cdhidalgo.e401_2024.Adaptadores.AdaptadorCarrito
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario
import java.text.NumberFormat

class Carrito : AppCompatActivity() {
    private lateinit var recyclerViewCarrito: RecyclerView
    private lateinit var adaptadorCarrito: AdaptadorCarrito

    private lateinit var nombreCliente: TextView
    private lateinit var totalCompra: TextView

    private lateinit var listaCompras: ArrayList<Producto>

    private lateinit var btnRegresar: Button
    private lateinit var btnFinalizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carrito)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRegresar = findViewById(R.id.btnRegresar)
        btnFinalizar = findViewById(R.id.btnFinalizar)

        recyclerViewCarrito = findViewById(R.id.rv_carrito)
        recyclerViewCarrito.setHasFixedSize(true)

        nombreCliente = findViewById(R.id.tvNombreCliente)
        totalCompra = findViewById(R.id.txtTotal)

        val usuario = intent.getParcelableExtra<Usuario>("usuario")
        val misCompras = intent.getParcelableArrayListExtra<Producto>("misCompras")

        val numeroProductos = misCompras?.size

        if (misCompras != null) {
            listaCompras = misCompras
        }

        var totalPagar = 0.0
        if (misCompras != null) {
            for (i in misCompras.indices) {
                totalPagar += misCompras[i].precio
            }
        }

        val numberFormat = NumberFormat.getCurrencyInstance()
        val cantidadFormateada = numberFormat.format(totalPagar)
        totalCompra.text = "TOTAL $cantidadFormateada"

        nombreCliente.text = "Cliente: ${usuario?.nombre} ${usuario?.apaterno} ${usuario?.amaterno}"

        //Recyclerview Vertical
        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)

        adaptadorCarrito = AdaptadorCarrito(listaCompras)
        recyclerViewCarrito.adapter = adaptadorCarrito

        btnRegresar.setOnClickListener {
            finish()
        }

        btnFinalizar.setOnClickListener {
            if(totalPagar>0){
                val intent = Intent(this,FinalizarCompra::class.java)
                intent.putExtra("usuario",usuario)
                intent.putExtra("misCompras",misCompras)
                intent.putExtra("total",totalPagar.toString())
                intent.putExtra("numproductos",numeroProductos.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this,"Debe agregar productos al carrito de compras!!",
                    Toast.LENGTH_LONG).show()
            }

        }
    }
}