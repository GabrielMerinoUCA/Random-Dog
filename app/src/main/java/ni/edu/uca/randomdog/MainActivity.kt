package ni.edu.uca.randomdog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ni.edu.uca.randomdog.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicialize()
    }

    private fun inicialize() {
        binding.btnPerro.setOnClickListener {
            val client = OkHttpClient()
            val apiUrl = "https://dog.ceo/api/breeds/image/random"
            val request: Request = Request.Builder()
                .url(apiUrl)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        try {

                            val body = response.body()!!.string()
                            val jsonObject = JSONObject(body) // convertir respuesta en json pero en la app kotlin
                            /* la respuesta siempre es un objeto. de dicho objeto queremos sacar la primera instancia del argumento
                            * pasado, pero solamente el valor al cual apunta el argumento no toda la linea*/
                            val image = jsonObject.getString("message") 
                            Log.wtf("JSON IMAGE: ", image)
                            GlobalScope.launch(Dispatchers.Main) {
                                Picasso.get().load(image).into(binding.imgPerro) // poner imagen desde url en image view facilito.
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            })
        }

    }
}