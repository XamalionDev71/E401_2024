package mx.tecnm.cdhidalgo.e401_2024

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.cdhidalgo.e401_2024.Adaptadores.AdaptadorArtesania
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario

class Tienda : AppCompatActivity() {
    private lateinit var nombreCliente: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var recyclerViewArtesanias: RecyclerView
    private lateinit var recyclerViewPerfumes: RecyclerView
    private lateinit var recyclerViewBolsos: RecyclerView
    private lateinit var recyclerViewPulseras: RecyclerView

    private lateinit var listaArtesanias: ArrayList<Producto>
    private lateinit var listaPerfumes: ArrayList<Producto>
    private lateinit var listaBolsos: ArrayList<Producto>
    private lateinit var listaPulseras: ArrayList<Producto>

    private lateinit var adaptadorArtesanias: AdaptadorArtesania
//    private lateinit var adaptadorPerfumes: AdaptadorPerfumes
//    private lateinit var adaptadorBolsos: AdaptadorBolsos
//    private lateinit var adaptadorPulseras: AdaptadorPulseras

    private lateinit var btnCarrito: Button
    private lateinit var btnSalir: Button
    private lateinit var _usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tienda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerViewArtesanias = findViewById(R.id.rv_artesanias)
        recyclerViewPerfumes = findViewById(R.id.rv_perfumes)
        recyclerViewBolsos = findViewById(R.id.rv_bolsos)
        recyclerViewPulseras = findViewById(R.id.rv_pulseras)
        btnCarrito = findViewById(R.id.btnCarrito)
        btnSalir = findViewById(R.id.btnSalir)
        nombreCliente = findViewById(R.id.tvNombreCliente)
        progressBar = findViewById(R.id.progressBar_Tienda)

        _usuario = intent.getParcelableExtra("usuario")!!
        val compraProducto = intent.getParcelableExtra<Producto>("compraProducto")
        val listaCompras = intent.getParcelableArrayListExtra<Producto>("misCompras")

        nombreCliente.text = "Cliente: ${_usuario?.nombre} " +
                "${_usuario?.apaterno} ${_usuario?.amaterno}"

        listaArtesanias = ArrayList()
        listaPerfumes = ArrayList()
        listaBolsos = ArrayList()
        listaPulseras = ArrayList()

        //misCompras = ArrayList()

        if(listaCompras != null){
            misCompras = listaCompras
        }

        if(compraProducto != null){
            misCompras.add(compraProducto)
        }

        //Artesanias
        listaArtesanias = listaProductos.filter { it.categoria == "Artesanía" } as ArrayList<Producto>
        recyclerViewArtesanias.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        adaptadorArtesanias = AdaptadorArtesania(listaArtesanias)
        recyclerViewArtesanias.adapter = adaptadorArtesanias

        adaptadorArtesanias.setOnItemClickListener(object : AdaptadorArtesania.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@Tienda, DetalleProducto::class.java)
                intent.putExtra("usuario",_usuario)
                intent.putExtra("producto", listaArtesanias[position])
                intent.putExtra("indice",position)
                startActivity(intent)
            }
        })

//        //Perfumes
//        listaPerfumes = listaProductos.filter { it.categoria == "Perfume" } as ArrayList<Producto>
//        recyclerViewPerfumes.layoutManager = LinearLayoutManager(this,
//            LinearLayoutManager.HORIZONTAL,false)
//        adaptadorPerfumes = AdaptadorPerfumes(listaPerfumes)
//        recyclerViewPerfumes.adapter = adaptadorPerfumes
//
//        adaptadorPerfumes.setOnItemClickListener(object : AdaptadorPerfumes.onItemClickListener{
//            override fun onItemClick(position: Int) {
//                val intent = Intent(this@Tienda, DetalleProducto::class.java)
//                intent.putExtra("usuario",_usuario)
//                intent.putExtra("producto", listaPerfumes[position])
//                intent.putExtra("indice",position)
//                startActivity(intent)
//            }
//        })
//
//        //Bolsos
//        listaBolsos = listaProductos.filter { it.categoria == "Bolso" } as ArrayList<Producto>
//        recyclerViewBolsos.layoutManager = LinearLayoutManager(this,
//            LinearLayoutManager.HORIZONTAL,false)
//        adaptadorBolsos = AdaptadorBolsos(listaBolsos)
//        recyclerViewBolsos.adapter = adaptadorBolsos
//
//        adaptadorBolsos.setOnItemClickListener(object : AdaptadorBolsos.onItemClickListener{
//            override fun onItemClick(position: Int) {
//                val intent = Intent(this@Tienda, DetalleProducto::class.java)
//                intent.putExtra("usuario",_usuario)
//                intent.putExtra("producto", listaBolsos[position])
//                intent.putExtra("indice",position)
//                startActivity(intent)
//            }
//        })
//
//        //Pulseras
//        listaPulseras = listaProductos.filter { it.categoria == "Pulsera" } as ArrayList<Producto>
//        recyclerViewPulseras.layoutManager = LinearLayoutManager(this,
//            LinearLayoutManager.HORIZONTAL,false)
//        adaptadorPulseras = AdaptadorPulseras(listaPulseras)
//        recyclerViewPulseras.adapter = adaptadorPulseras
//
//        adaptadorPulseras.setOnItemClickListener(object : AdaptadorPulseras.onItemClickListener{
//            override fun onItemClick(position: Int) {
//                val intent = Intent(this@Tienda, DetalleProducto::class.java)
//                intent.putExtra("usuario",_usuario)
//                intent.putExtra("producto", listaPulseras[position])
//                intent.putExtra("indice",position)
//                startActivity(intent)
//            }
//        })

        btnCarrito.setOnClickListener {
            val intent = Intent(this,Carrito::class.java)
            intent.putExtra("usuario",_usuario)
            intent.putExtra("misCompras",misCompras)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
    }
}