package mx.tecnm.cdhidalgo.e401_2024

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Compra
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario

class FinalizarCompra : AppCompatActivity() {
    private lateinit var textoCompra: TextView
    private lateinit var btnRegresarTienda: Button
    private lateinit var misCompras: ArrayList<Producto>

    private lateinit var miscompra: Compra

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finalizar_compra)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Acceso a la Base de Datos de Cloud Firestore
        val db = Firebase.firestore

        val usuario = intent.getParcelableExtra<Usuario>("usuario")
        val total = intent.getStringExtra("total")
        val numeroProductos = intent.getStringExtra("numproductos")
        misCompras = intent.getParcelableArrayListExtra<Producto>("misCompras")
                as ArrayList<Producto>

        textoCompra = findViewById(R.id.tv_texto01)
        btnRegresarTienda = findViewById(R.id.btnRegresarTienda)

        textoCompra.text = "${usuario?.nombre},\n" +
                "agradecemos su preferencia!!"

        btnRegresarTienda.setOnClickListener {
            val intent = Intent(this,Tienda::class.java)

            val compras = Compra(usuario?.correo,total.toString(),
                numeroProductos.toString(),misCompras)
            db.collection("compras")
                .document() //Asigna un UID
                .set(compras)

            intent.putExtra("usuario",usuario)
            intent.putExtra("miscompras",misCompras)
            startActivity(intent)
        }

    }
}