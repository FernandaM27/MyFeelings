package com.miranda.myfeelings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.miranda.myfeelings.utilities.CustomBarDrawable
import com.miranda.myfeelings.utilities.CustomCircleDrawable
import com.miranda.myfeelings.utilities.Emociones
import com.miranda.myfeelings.utilities.JSONFile
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy = 0.0f
    var happy = 0.0f
    var neutral = 0.0f
    var sad = 0.0f
    var verySad = 0.0f
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonFile= JSONFile()

        fetchingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0f, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0f, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0f, R.color.greenie, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0f, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0f, R.color.deepBlue, verySad))
        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener {
            guardar()
        }

        happyButton.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        veryhappyButton.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        verysadButton.setOnClickListener {
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }
    }

    fun fetchingData() {
        try {
            var json: String = jsonFile?.getData(this) ?: ""
            if (json != "") {
                this.data = true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

               for (i in lista) {
                    when (i.nombre) {
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verySad = i.total
                    }
                }
            }else{
                this.data = false
            }
        }catch (exception:JSONException){
            exception.printStackTrace()
        }
    }

    fun iconoMayoria() {
        if(happy>veryHappy && happy>neutral && happy>sad && happy>verySad){
            icon.setImageResource(R.drawable.ic_happy)
        }else if(veryHappy>happy && veryHappy>neutral && veryHappy>sad && veryHappy>verySad) {
            icon.setImageResource(R.drawable.ic_veryhappy)
        }else if(neutral>veryHappy && neutral>happy && neutral>sad && neutral>verySad){
            icon.setImageResource(R.drawable.ic_neutral)
        }else if(sad>veryHappy && sad>neutral && sad>happy &&sad> verySad){
            icon.setImageResource(R.drawable.ic_sad)
        }else if(verySad>veryHappy && verySad>neutral && verySad>sad && verySad>happy){
            icon.setImageResource(R.drawable.ic_verysad)
        }
    }

    fun actualizarGrafica() {

        val total = veryHappy+happy+neutral+verySad+sad

        var pVH: Float = (veryHappy * 100 / total)
        var pH: Float = (happy * 100 / total)
        var pN: Float = (neutral * 100 / total)
        var pS: Float = (sad * 100 / total)
        var pVS: Float = (verySad * 100 / total)

        Log.d("porcentajes", "very happy"+pVH)
        Log.d("porcentajes", "happy"+pH)
        Log.d("porcentajes", "neutral"+pN)
        Log.d("porcentajes", "sad"+pS)
        Log.d("porcentajes", "very sad"+pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue, verySad))

        val fondo= CustomCircleDrawable(this,lista)

        graphVeryHappy.background=CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background=CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral.background=CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graphSad.background=CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background=CustomBarDrawable(this,Emociones("Muy triste", pVS, R.color.deepBlue, verySad))

        graph.background = fondo

    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>
    {
        var lista=ArrayList<Emociones>()

        for(i in 0..jsonArray.length()){
            try{
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre,porcentaje,color,total)
                lista.add(emocion)
            }catch (exception:JSONException){
                exception.printStackTrace()
            }
        }

        return lista
    }

    fun guardar() {

        var jsonArray = JSONArray()
        var o : Int = 0
        for(i in lista) {
            Log.d("objetos",i.toString())
            var j: JSONObject = JSONObject()
            j.put("nombre",i.nombre)
            j.put("porcentaje",i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o,j)
            o++
        }

        jsonFile?.saveData(this,jsonArray.toString())

        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }

}