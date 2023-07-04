package com.musicplayer.android.utils.view.equalizer

import java.io.Serializable

data class EqualizerModel(
    var isEqualizerEnabled: Boolean? = true,
    var seekbarpos: IntArray? = IntArray(5),
    var presetPos: Int? = 0,
    var reverbPreset: Short? = -1,
    var bassStrength: Short? = -1,
) : Serializable {

}
