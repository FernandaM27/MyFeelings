package com.miranda.myfeelings.utilities

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.miranda.myfeelings.R

class CustomCircleDrawable: Drawable {

    var coordenadas: RectF? = null
    var anguloBarrido: Float = 0.0F
    var anguloInicio: Float = 0.0F
    var grosorMetrica: Int = 0
    var grosorFondo: Int = 0
    var context: Context? = null
    var emociones = ArrayList<Emociones>()

    constructor(context: Context?, emociones: ArrayList<Emociones>) : super() {
        this.context = context
        grosorMetrica = context!!.resources.getDimensionPixelSize(R.dimen.graphWith)
        grosorFondo = context!!.resources.getDimensionPixelSize(R.dimen.graphBackGround)
        this.emociones = emociones
    }


    override fun draw(p0: Canvas) {
        val fondo: Paint = Paint()
        fondo.style = Paint.Style.STROKE
        fondo.strokeWidth = (this.grosorFondo).toFloat()
        fondo.isAntiAlias = true
        fondo.strokeCap = Paint.Cap.ROUND
        fondo.color = context?.resources?.getColor(R.color.gray) ?: R.color.gray
        val ancho: Float = (p0.width - 25).toFloat()
        val alto: Float = (p0.height - 25).toFloat()

        coordenadas = RectF(25.0f, 25.0f, ancho, alto)
        p0.drawArc(coordenadas!!, 0.0f, 360.0f, false, fondo)

        if (emociones.size != 0) {
            emociones.forEach { e ->
                val degree: Float = (e.porcentaje * 360) / 100
                this.anguloBarrido = degree

                var seccion: Paint = Paint()
                seccion.style = Paint.Style.STROKE
                seccion.isAntiAlias = true
                seccion.strokeWidth = (this.grosorMetrica).toFloat()
                seccion.strokeCap = Paint.Cap.SQUARE
                seccion.color = ContextCompat.getColor(this.context!!, e.color)

                p0.drawArc(coordenadas!!, this.anguloInicio, this.anguloBarrido, false, seccion)

                this.anguloInicio += this.anguloBarrido
            }
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
