package com.dev.rhyan.furiastreetwear.ui.components

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class OnlyBottomRightRoundedShape(
    private val radius: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadius = with(density) { radius.toPx() }

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - cornerRadius)
            arcTo(
                rect = Rect(
                    offset = Offset(size.width - cornerRadius * 2, size.height - cornerRadius * 2),
                    size = Size(cornerRadius * 2, cornerRadius * 2)
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(0f, size.height)
            close()
        }

        return Outline.Generic(path)
    }
}