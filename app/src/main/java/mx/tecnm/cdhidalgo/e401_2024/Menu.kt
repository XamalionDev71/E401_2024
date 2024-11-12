package mx.tecnm.cdhidalgo.e401_2024

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario

class Menu : AppCompatActivity() {
    private lateinit var nombreCliente: TextView
    private lateinit var btnTienda: Button
    private lateinit var btnSalir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val auth = FirebaseAuth.getInstance()

        nombreCliente = findViewById(R.id.txtUsuario)
        btnTienda = findViewById(R.id.btnTienda)
        btnSalir = findViewById(R.id.btnSalir_Menu)

         val _usuario = intent.getParcelableExtra<Usuario>("usuario")

        nombreCliente.text = "Cliente: ${_usuario?.nombre.toString()} " +
                "${_usuario?.apaterno.toString()} ${_usuario?.amaterno.toString()}"

        btnTienda.setOnClickListener {
            val intent = Intent(this, Tienda::class.java)
            intent.putExtra("usuario",_usuario)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            // Cerrar la sesión del usuario
            auth.signOut()

            // Redirigir a la pantalla de login
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}

