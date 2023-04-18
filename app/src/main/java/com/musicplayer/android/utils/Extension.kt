package com.musicplayer.android.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

class Extension {
    companion object {
        //view visibility
        fun View.gone() = kotlin.run { this.visibility = View.GONE }
        fun View.visible() = kotlin.run { this.visibility = View.VISIBLE }
        fun View.invisible() = kotlin.run { this.visibility = View.INVISIBLE }

        infix fun View.visibleIf(condition: Boolean) =
            kotlin.run { visibility = if (condition) View.VISIBLE else View.GONE }

        infix fun View.goneIf(condition: Boolean) =
            kotlin.run { visibility = if (condition) View.GONE else View.VISIBLE }

        infix fun View.invisibleIf(condition: Boolean) =
            kotlin.run { visibility = if (condition) View.INVISIBLE else View.VISIBLE }

        //Snackbar with view
        fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
            Snackbar.make(this, message, duration).show()
        }

        fun View.snackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
            Snackbar.make(this, message, duration).show()
        }

    }
}