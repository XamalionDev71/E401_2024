package mx.tecnm.cdhidalgo.e401_2024.Admin

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
import mx.tecnm.cdhidalgo.e401_2024.Login
import mx.tecnm.cdhidalgo.e401_2024.R
import mx.tecnm.cdhidalgo.e401_2024.Tienda

class AdminMenu : AppCompatActivity() {
    private lateinit var nombreCliente: TextView
    private lateinit var btnProductos: Button
    private lateinit var btnUsuarios: Button
    private lateinit var btnSalir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val auth = FirebaseAuth.getInstance()

        nombreCliente = findViewById(R.id.txtUsuario_AdminMenu)
        btnProductos = findViewById(R.id.btnProductos_AdminMenu)
        btnSalir = findViewById(R.id.btnSalir_AdminMenu)

        val _usuario = intent.getParcelableExtra<Usuario>("usuario")

        nombreCliente.text = "Usuario: ${_usuario?.nombre.toString()} " +
                "${_usuario?.apaterno.toString()} ${_usuario?.amaterno.toString()}"

        btnProductos.setOnClickListener {
            val intent = Intent(this, AdminPrincipal::class.java)
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