package mx.tecnm.cdhidalgo.e401_2024

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Splash : AppCompatActivity() {

    private lateinit var logo_splash: ImageView
    private lateinit var animacion: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logo_splash = findViewById(R.id.logo_splash)
        animacion = AnimationUtils.loadAnimation(this, R.anim.anim_splash)

        logo_splash.startAnimation(animacion)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this,Login::class.java)
                val transicion = ActivityOptions.makeSceneTransitionAnimation(
                    this, Pair(logo_splash,"logo_transicion"))
                startActivity(intent,transicion.toBundle())
            },4000)
    }
}