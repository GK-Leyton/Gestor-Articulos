package popups

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.segundoparcial.R
import kotlin.random.Random

fun setPopupBackGroundHelper(view: View, context: Context) {
    var backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_1_gold)

    val randoma = Random.nextInt(1, 11)

    when (randoma) {
        1 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_1_gold)
        2 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_1_red)
        3 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_1_turquesa)
        4 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_1_pink)
        5 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_1_purple)
        6 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_2_gold)
        7 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_2_red)
        8 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_2_turquesa)
        9 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_2_pink)
        10 -> backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.vectorial_draw_2_purple)
    }

    view.background = backgroundDrawable
}
