package popups

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import android.widget.TextView


class TextViewTiembleManager {
    private var animator: ValueAnimator? = null

    fun iniciarTiemble(textView: TextView, intensidad: Float = 3f) {
        // Detener cualquier animación existente primero
        detenerTiemble(textView)

        animator = ValueAnimator.ofFloat(1f, intensidad, -intensidad, -1f).apply {
            duration = 200
            repeatCount = ValueAnimator.INFINITE // Ya lo tenías, pero lo remarco
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                textView.translationX = value
            }
        }
        animator?.start()
    }

    fun detenerTiemble(textView: TextView) {
        animator?.cancel()
        animator?.removeAllUpdateListeners()
        animator = null

        // Usar post para asegurar que se ejecute después de los frames de la interfaz
        textView.post {
            textView.translationX = 0f
        }
    }
}

