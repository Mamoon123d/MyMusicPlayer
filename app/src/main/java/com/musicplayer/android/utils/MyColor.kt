package com.musicplayer.android.utils

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

class MyColor {
    companion object {
       final val RGB: String = "rgb"
       final val VIBRANT: String = "vibrant"
       final val VIBRANT_DARK: String = "vibrantDark"
       final val VIBRANT_LIGHT: String = "vibrantLight"
       final val MUTED: String = "muted"
       final val MUTED_DARK: String = "mutedDark"
       final val MUTED_LIGHT: String = "mutedLight"

        fun Bitmap.generateColor(): Map<String, Int> {
            var colorMap = mapOf<String, Int>()
            Palette.from(this).generate { palette ->
                val rgb = palette!!.mutedSwatch!!.rgb
                val vibrant = palette.vibrantSwatch!!.rgb
                val vibrantDark = palette.darkVibrantSwatch!!.rgb
                val vibrantLight = palette.lightVibrantSwatch!!.rgb
                val muted = palette.mutedSwatch!!.rgb
                val mutedDark = palette.darkMutedSwatch!!.rgb
                val mutedLight = palette.lightMutedSwatch!!.rgb
                colorMap= hashMapOf(
                    RGB to rgb,
                    VIBRANT to vibrant,
                    VIBRANT_DARK to vibrantDark,
                    VIBRANT_LIGHT to vibrantLight,
                    MUTED to muted,
                    MUTED_DARK to mutedDark,
                    MUTED_LIGHT to mutedLight,
                )
              //  colorMap

            }

            return colorMap
        }

    }
}