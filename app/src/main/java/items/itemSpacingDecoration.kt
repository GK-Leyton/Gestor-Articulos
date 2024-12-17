package items

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // posición del ítem

        // Espaciado superior e inferior
        outRect.top = spacing
        outRect.bottom = spacing

        // Espaciado izquierdo y derecho
        if (position % 2 == 0) { // Columna izquierda
            outRect.left = spacing / 2 // Espacio a la izquierda
            outRect.right = spacing / 2 // Espacio a la derecha
        } else { // Columna derecha
            outRect.left = spacing / 2 // Espacio a la izquierda
            outRect.right = spacing / 2 // Espacio a la derecha
        }
    }
}
