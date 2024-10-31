package mx.tecnm.cdhidalgo.e401_2024

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.e401_2024.Admin.AdminPrincipal
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario

class Login : AppCompatActivity() {

    private lateinit var correo: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var btnIngresar: Button
    private lateinit var btnNoEstoyRegistrado: MaterialButton

    private lateinit var auth: FirebaseAuth

    //inicializar Coleccion
    val coleccion = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //inicializar autenticacion
        auth = FirebaseAuth.getInstance()

        correo = findViewById(R.id.txtCorreo)
        password = findViewById(R.id.txtPass)
        btnIngresar = findViewById(R.id.btnIngresar)
        btnNoEstoyRegistrado = findViewById(R.id.btnNoEstoyRegistrado)

        btnIngresar.setOnClickListener {
            val _correo = correo.editText?.text.toString()
            val _password = password.editText?.text.toString()
            if(_correo.isNotEmpty() && _password.isNotEmpty()){
                auth.signInWithEmailAndPassword(_correo, _password)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            coleccion.collection("usuarios")
                                .document(_correo)
                                .get()
                                .addOnSuccessListener { documento ->
//                                    val _usuario = Usuario(
//                                        _correo,
//                                        documento.getString("nombre"),
//                                        documento.getString("apaterno"),
//                                        documento.getString("amaterno")
//                                    )
                                    val _usuario = documento.toObject(Usuario::class.java)!!

                                    if (_usuario != null) {
                                        if((_usuario.perfil_admin)){
                                            val intent = Intent(this@Login, AdminPrincipal::class.java)
                                            intent.putExtra("usuario",_usuario)
                                            startActivity(intent)
                                        } else{
                                            val intent = Intent(this@Login, Menu::class.java)
                                            intent.putExtra("usuario",_usuario)
                                            startActivity(intent)
                                        }
                                    } else {
                                        Toast.makeText(this@Login, "No se encontro el usuario",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else{
                            showAlert()
                        }
                    }
                }
        }

        btnNoEstoyRegistrado.setOnClickListener {
            val intent = Intent(this,Registro::class.java)
            startActivity(intent)
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialogo: AlertDialog = builder.create()
        dialogo.show()
    }
}

