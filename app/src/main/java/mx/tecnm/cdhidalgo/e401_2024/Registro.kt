package mx.tecnm.cdhidalgo.e401_2024

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario

class Registro : AppCompatActivity() {

    private lateinit var nombre: TextInputLayout
    private lateinit var paterno: TextInputLayout
    private lateinit var materno: TextInputLayout
    private lateinit var correo: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var cbCliente: CheckBox
    private lateinit var cbAdmin: CheckBox

    private var perfilCliente: Boolean = false
    private var perfilAdmin: Boolean = false

    private lateinit var btnRegistrarme: Button
    private lateinit var btnEstoyRegistrado: MaterialButton

    private lateinit var usuario: Usuario

    private lateinit var auth: FirebaseAuth

    //inicializar Coleccion de usuarios
    private var coleccionUsuarios = Firebase.firestore.collection("usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        nombre = findViewById(R.id.txtNombre)
        paterno = findViewById(R.id.txtPaterno)
        materno = findViewById(R.id.txtMaterno)
        correo = findViewById(R.id.txtCorreo)
        password = findViewById(R.id.txtPassword)

        cbCliente = findViewById(R.id.cbCliente_registro)
        cbAdmin = findViewById(R.id.cbAdmin_registro)

        btnRegistrarme = findViewById(R.id.btnRegistrarme)
        btnEstoyRegistrado = findViewById(R.id.btnEstoyRegistrado)

        btnRegistrarme.setOnClickListener {
            usuario = Usuario(
                correo.editText?.text.toString(),
                nombre.editText?.text.toString(),
                paterno.editText?.text.toString(),
                materno.editText?.text.toString(),
                perfilCliente,
                perfilAdmin
            )
            confirmarUsuario(usuario,password.editText?.text.toString())
        }

        btnEstoyRegistrado.setOnClickListener {
            finish()
        }
    }

    private fun confirmarUsuario(user: Usuario, pass:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Datos del Usuario")
        builder.setMessage("""
            Nombre: ${user.nombre}
            Paterno: ${user.apaterno}
            Materno: ${user.amaterno}
            Correo: ${user.correo}
            Password: ${pass}
            Perfil Cliente: ${user.perfil_cliente}
            Perfil Administrador: ${user.perfil_admin}
        """.trimIndent())
        builder.setPositiveButton("Confirmar", { dialog, which ->
            auth.createUserWithEmailAndPassword(user.correo.toString(),pass)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "autenticación exitosa!!",
                            Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "usuario: ${user.correo}",
                            Toast.LENGTH_SHORT).show()
                        //Crear una coleccion de Usuarios en Firebase
                        val intent = Intent(this,Login::class.java).apply {
                            coleccionUsuarios
                                .document(user.correo.toString())
                                .set(user)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "No se puede crear autenticación",
                               Toast.LENGTH_SHORT).show()
                    }
                }
        })
        //boton cancelar y confirmar
        builder.setNegativeButton("Cancelar",null)
        val dialogo: AlertDialog = builder.create()
        dialogo.show()
    }

    fun onCheckBoxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.cbCliente_registro -> {
                    perfilCliente = checked
                    perfilAdmin = false
                    cbAdmin.isChecked = false
                }
                R.id.cbAdmin_registro -> {
                    perfilAdmin = checked
                    perfilCliente = false
                    cbCliente.isChecked = false
                }
            }
        }
    }
}

