package mx.tecnm.cdhidalgo.e401_2024.Admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Producto
import mx.tecnm.cdhidalgo.e401_2024.Data_Class.Usuario
import mx.tecnm.cdhidalgo.e401_2024.R
import mx.tecnm.cdhidalgo.e401_2024.listaProductos

class AdminAgregarProducto : AppCompatActivity() {
    private lateinit var nombreCorto: TextInputLayout
    private lateinit var nombre: TextInputLayout
    private lateinit var descripcion: TextInputLayout
    private lateinit var precio: TextInputLayout
    private lateinit var categoria: TextInputLayout
    private lateinit var nombreFoto: TextView
    private lateinit var foto: ImageView
    private lateinit var btnAgregarFoto: ImageButton
    private lateinit var btnRegresar: Button
    private lateinit var btnAgregarProducto: Button

    private lateinit var producto: Producto

    private var storageReference = FirebaseStorage.getInstance().reference
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_agregar_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nombreCorto = findViewById(R.id.nombreCorto_AgregarProd)
        nombre = findViewById(R.id.nombreCompleto_AgregarProd)
        descripcion = findViewById(R.id.descripcion_AgregarProd)
        precio = findViewById(R.id.precio_AgregarProd)
        categoria = findViewById(R.id.categoria_AgregarProd)
        nombreFoto = findViewById(R.id.nombreFoto_AgregarProd)
        foto = findViewById(R.id.foto_AgregarProd)
        btnAgregarFoto = findViewById(R.id.btnAgregarFoto_AgregarProd)
        btnRegresar = findViewById(R.id.btnRegresar_AgregarProd)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto_AgregarProd)

        val usuario = intent.getParcelableExtra<Usuario>("usuario")

        val baseDeDatos = Firebase.firestore

        //Dropdown Categoria
        val _categoria = resources.getStringArray(R.array.categoria_producto)
        val adaptadorCategoria = ArrayAdapter(this, R.layout.lista_desplegable,_categoria)
        (categoria.editText as? AutoCompleteTextView)?.setAdapter(adaptadorCategoria)

        btnAgregarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        btnAgregarProducto.setOnClickListener {
            //Aseguramos que exista una imagen seleccionada
            if (selectedImageUri != null) {
                //Guardamos la imagen en Firebase Storage
                val storageRef =
                    storageReference.child("productos/${selectedImageUri?.lastPathSegment}")
                storageRef.putFile(selectedImageUri!!)
                    .addOnSuccessListener {
                        //busca el url de la imagen después de ser subida
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            Toast.makeText(this, "$downloadUrl", Toast.LENGTH_SHORT).show()
                            producto = Producto(
                                downloadUrl,
                                nombreCorto.editText?.text.toString().trim(),
                                nombre.editText?.text.toString().trim(),
                                precio.editText?.text.toString().toDouble(),
                                descripcion.editText?.text.toString().trim(),
                                categoria.editText?.text.toString().trim(),)
                            baseDeDatos.collection("productos")
                                .document()
                                .set(producto)
                                .addOnSuccessListener {
                                    listaProductos.add(producto)
                                    Toast.makeText(this,
                                        "Se ha agregado el producto a la colección de Productos",
                                        Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, AdminPrincipal::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this,
                                        "Error al guardar la información", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
            }
        }

        btnRegresar.setOnClickListener {
            val intent = Intent(this, AdminPrincipal::class.java)
            intent.putExtra("usuario",usuario)
            startActivity(intent)
            finish()
        }
    }
}