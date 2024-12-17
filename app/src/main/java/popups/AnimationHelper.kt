package popups

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object AnimationHelper {
    fun animatePopupView(view: View, duration: Long, onAnimationEnd: () -> Unit) {
        // Animaciones de entrada (escala y opacidad)
        view.scaleX = 0.8f // Escalar a 80%
        view.scaleY = 0.8f // Escalar a 80%
        view.alpha = 0f // Hacerlo invisible al principio

        val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f)
        val alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)

        // Combinación de animaciones de entrada
        val enterAnimatorSet = AnimatorSet()
        enterAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
        enterAnimatorSet.duration = duration
        enterAnimatorSet.start()

        // Animaciones de salida (escala y opacidad)
        enterAnimatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                // Llamar a la función de finalización de animación
                onAnimationEnd()
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}

            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })
    }

    fun dismissPopupView(view: View, duration: Long, onDismiss: () -> Unit) {
        // Animaciones de salida (escala y opacidad)
        val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f)
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f)
        val alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)

        // Combinación de animaciones de salida
        val exitAnimatorSet = AnimatorSet()
        exitAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
        exitAnimatorSet.duration = duration

        exitAnimatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                onDismiss()
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}

            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        exitAnimatorSet.start() // Iniciar la animación de salida
    }
}
