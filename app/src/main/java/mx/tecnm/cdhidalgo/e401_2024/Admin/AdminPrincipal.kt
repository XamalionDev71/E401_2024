package mx.tecnm.cdhidalgo.e401_2024.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.e401_2024.Adaptadores.AdaptadorListadoProd
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario
import mx.tecnm.cdhidalgo.e401_2024.Login
import mx.tecnm.cdhidalgo.e401_2024.R
import mx.tecnm.cdhidalgo.e401_2024.listaProductos

class AdminPrincipal : AppCompatActivity() {
    private lateinit var rvProductos: RecyclerView
    private lateinit var btnAgregarProd: ImageButton
    private lateinit var btnSalir: Button

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rvProductos = findViewById(R.id.rvProductos_Principal)
        btnAgregarProd = findViewById(R.id.btnAgregarProd_Principal)
        btnSalir = findViewById(R.id.btnSalir_Principal)

        val usuario = intent.getParcelableExtra<Usuario>("usuario")

        val adaptador = AdaptadorListadoProd(listaProductos)
        rvProductos.layoutManager = LinearLayoutManager(this)
        rvProductos.adapter = adaptador

        adaptador.setOnItemClickListener(object : AdaptadorListadoProd.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@AdminPrincipal,
                    AdminDetalleProducto::class.java)
                intent.putExtra("usuario",usuario)
                intent.putExtra("producto", listaProductos[position])
                intent.putExtra("indice",position)
                startActivity(intent)
            }
        })

        btnAgregarProd.setOnClickListener {
            val intent = Intent(this, AdminAgregarProducto::class.java)
            intent.putExtra("usuario",usuario)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, AdminMenu::class.java)
            startActivity(intent)
            finish()
        }
    }
}