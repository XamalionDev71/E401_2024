package mx.tecnm.cdhidalgo.e401_2024

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.appcheck.BuildConfig
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import mx.tecnm.cdhidalgo.e401_2024.Admin.AdminMenu
import mx.tecnm.cdhidalgo.e401_2024.Admin.AdminPrincipal
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario

//Comentario

var listaProductos = ArrayList<Producto>()
var miCarrito = ArrayList<Producto>()
var misCompras = ArrayList<Producto>()

class Login : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
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

        val firebaseAppCheck = FirebaseAppCheck.getInstance()

        progressBar = findViewById(R.id.progressBar_Login)
        correo = findViewById(R.id.txtCorreo)
        password = findViewById(R.id.txtPass)
        btnIngresar = findViewById(R.id.btnIngresar)
        btnNoEstoyRegistrado = findViewById(R.id.btnNoEstoyRegistrado)

        listaProductos = ArrayList()
        misCompras = ArrayList()

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
                                    val _usuario = documento.toObject(Usuario::class.java)!!
                                    lifecycleScope.launch {
                                        cargarProductos()
                                    }
                                    if (_usuario != null) {
                                        if((_usuario.perfil_admin)){
                                            val intent = Intent(this@Login, AdminMenu::class.java)
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

    private suspend fun cargarProductos() {
        withContext(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
        }
        try {
            // Hacer la llamada a Firebase Firestore de manera asíncrona
            val documentos = withContext(Dispatchers.IO) {
                coleccion.collection("productos").get().await() // Usar await() para esperar la consulta de forma suspensiva
            }

            // Procesar los documentos
            withContext(Dispatchers.IO) {
                for (documento in documentos) {
                    listaProductos.add(documento.toObject(Producto::class.java))
                }
            }

            // Ocultar progressBar y notificar al usuario
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Login, "Colección Cargada!!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Manejo de errores
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Login, "Error al cargar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

