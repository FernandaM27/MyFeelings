package com.miranda.myfeelings.utilities

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.miranda.myfeelings.R

class CustomBarDrawable: Drawable {
    var coordenadas: RectF? = null
    var context: Context? =null
    var emocion:Emociones?=null

    constructor(context: Context?, emocion: Emociones?) : super() {
        this.context = context
        this.emocion = emocion
    }

    override fun draw(p0: Canvas) {
        val fondo: Paint = Paint();
        fondo.style = Paint.Style.FILL
        fondo.isAntiAlias = true
        fondo.color=context?.resources?.getColor(R.color.gray) ?: R.color.gray
        val ancho:Float=(p0.width-10).toFloat()
        val alto:Float = (p0.height-10).toFloat()

        coordenadas= RectF(0.0f, 0.0f, ancho, alto)

        p0.drawRect(coordenadas!!, fondo)

        if(this.emocion!=null){
            val porcentaje: Float = this.emocion!!.porcentaje*(p0.width-10)/100
            val coordenadas2=RectF(0.0f, 0.0f, porcentaje, alto)

            var seccion:Paint = Paint()
            seccion.style=Paint.Style.FILL
            seccion.isAntiAlias=true
            seccion.color= ContextCompat.getColor(this.context!!, emocion!!.color)

            p0.drawRect(coordenadas2!!, seccion)
        }
    }

    override fun setAlpha(p0: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(p0: ColorFilter?) {
    }

}