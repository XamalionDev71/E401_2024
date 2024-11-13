package mx.tecnm.cdhidalgo.e401_2024.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario
import mx.tecnm.cdhidalgo.e401_2024.R
import java.text.NumberFormat

class AdminDetalleProducto : AppCompatActivity() {
    private lateinit var _foto: ImageView
    private lateinit var _nombre: TextView
    private lateinit var _descripcion: TextView
    private lateinit var _precio: TextView
    private lateinit var btnRegresar: ImageButton

    //Comentario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_detalle_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _foto = findViewById(R.id.foto_DetalleProd)
        _nombre = findViewById(R.id.nombre_DetalleProd)
        _descripcion = findViewById(R.id.descripcion_DetalleProd)
        _precio = findViewById(R.id.precio_DetalleProd)
        btnRegresar = findViewById(R.id.btnRegresar_DetalleProd)

        val usuario = intent.getParcelableExtra<Usuario>("usuario")
        val producto = intent.getParcelableExtra<Producto>("producto")
        val indice = intent.getIntExtra("indice",0)


        Glide.with(this)
            .load(producto?.imagen)
            .override(500,500)
            .into(_foto)
        _nombre.text = "${producto?.nombre}"
        _descripcion.text = "${producto?.descripcion}"
        val numberFormat = NumberFormat.getCurrencyInstance()
        val cantidadFormateada = numberFormat.format(producto?.precio)
        _precio.text = "$cantidadFormateada"

        btnRegresar.setOnClickListener {
            val intent = Intent(this, AdminPrincipal::class.java)
            intent.putExtra("usuario",usuario)
            startActivity(intent)
        }
    }
}