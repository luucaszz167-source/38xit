package com.thirtyeightxhx.panel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.view.View

/**
 * Desenha um alvo fictício para testar a camada de sobreposição.
 * Não lê dados do Free Fire nem localiza jogadores reais.
 */
class EspDemoView(context: Context) : View(context) {
    private val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(40, 220, 120)
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(40, 220, 120)
        textSize = 34f
        style = Paint.Style.FILL
        isFakeBoldText = true
    }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(40, 220, 120)
        strokeWidth = 3f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width * 0.68f
        val centerY = height * 0.43f
        val halfWidth = 48f
        val boxHeight = 150f

        canvas.drawRect(
            centerX - halfWidth,
            centerY - boxHeight / 2,
            centerX + halfWidth,
            centerY + boxHeight / 2,
            boxPaint
        )
        canvas.drawText("JOGADOR DE TESTE", centerX - 145f, centerY - boxHeight / 2 - 18f, textPaint)
        canvas.drawLine(width / 2f, height.toFloat(), centerX, centerY + boxHeight / 2, linePaint)
    }
}
