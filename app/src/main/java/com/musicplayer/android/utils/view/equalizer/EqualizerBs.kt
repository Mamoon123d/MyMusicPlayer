package com.musicplayer.android.utils.view.equalizer

import android.app.Activity
import android.content.Context
import android.media.audiofx.Equalizer
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.musicplayer.android.R
import com.musicplayer.android.databinding.EchoLayoutBinding


class EqualizerBs(context: Context, val audioSesionId: Int) {
    var mEqualizer: Equalizer? = null
    /*var equalizerSwitch: SwitchCompat
    val bassBoost: BassBoost
    val presetReverb: PresetReverb
    val seekBarFinal = arrayOfNulls<SeekBar>(6)
    var numberOfFrequencyBands: Short
    var points: FloatArray*/
    init {
        val bs = BottomSheetDialog(context)
        val binding = EchoLayoutBinding.inflate(LayoutInflater.from(context))
        bs.setContentView(binding.root)
        Settings.isEditing = true

         //--------------set chips---------------------------
          val modes= arrayOf("Custom","Normal","Classical","Dance","Flat","Folk","Hiphop","Jazz","Pop","Rock")

        for (it in modes){
            val chip=(context as Activity).layoutInflater.inflate(R.layout.eq_chip_lay,null,false)as Chip
            chip.text=it
            binding.eqModeCg.addView(chip)
            chip.setOnClickListener {
                if (chip.isChecked){
                 //
                }
            }
        }
        //---------------------------------------------------
        /*if (Settings.equalizerModel == null) {
            Settings.equalizerModel = EqualizerModel()
            Settings.equalizerModel.reverbPreset = PresetReverb.PRESET_NONE
            Settings.equalizerModel.bassStrength = (1000 / 19).toShort()
        }
        mEqualizer = Equalizer(0, audioSesionId)
        bassBoost = BassBoost(0, audioSesionId)
        bassBoost.setEnabled(Settings.isEqualizerEnabled)

        val bassBoostSettingTemp: BassBoost.Settings = bassBoost.getProperties()
        val bassBoostSetting = BassBoost.Settings(bassBoostSettingTemp.toString())
        bassBoostSetting.strength = Settings.equalizerModel.bassStrength!!
        bassBoost.setProperties(bassBoostSetting)

        presetReverb = PresetReverb(0, audioSesionId)
        presetReverb.preset = Settings.equalizerModel.reverbPreset!!
        presetReverb.enabled = Settings.isEqualizerEnabled
        if (Settings.presetPos == 0) {
            for (bandIdx in 0 until mEqualizer!!.numberOfBands) {
                mEqualizer!!.setBandLevel(bandIdx.toShort(), Settings.seekbarpos[bandIdx].toShort())
            }
        } else {
            mEqualizer!!.usePreset(Settings.presetPos.toShort())
        }

       *//* equalizerSwitch = binding.eqSwitch
        equalizerSwitch.isChecked = Settings.isEqualizerEnabled
        equalizerSwitch.setOnCheckedChangeListener(object : OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                mEqualizer!!.setEnabled(isChecked);
                bassBoost.setEnabled(isChecked);
                presetReverb.setEnabled(isChecked);
                Settings.isEqualizerEnabled = isChecked;
                Settings.equalizerModel.isEqualizerEnabled = isChecked
            }

        })

        var layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.weight = 1F
        numberOfFrequencyBands = 5
        //R.layout.eq_chip_lay

        points = floatArrayOf(numberOfFrequencyBands.toFloat())
        for (i in 1..numberOfFrequencyBands) {
            val seekBarRowLayout = LinearLayout(context)
            seekBarRowLayout.orientation = LinearLayout.VERTICAL
            var seekBar = VerticalSeekBar(context)
            when (i) {
                1 -> seekBar = binding.sb230hz
                2 -> seekBar = binding.sb910hz
                3 -> seekBar = binding.sb3600hz
                4 -> seekBar = binding.sb14000hz
                5 -> seekBar = binding.sb14000hz
            }
            seekBarFinal[i] = seekBar
            seekBar.progressDrawable.colorFilter =
                PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)
            seekBar.thumb.colorFilter =
                PorterDuffColorFilter(context.getColor(com.musicplayer.android.R.color.text_1), PorterDuff.Mode.SRC_IN)
            seekBar.id = i
            // seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
            if (Settings.isEqualizerReloaded) {
                //points[i] = Settings.seekbarpos[i] - lowerEqualizerBandLevel;
                //  dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                //seekBar.setProgress(Settings.seekbarpos[i] - lowerEqualizerBandLevel);
            } else {
                //points[i] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                //dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                //seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
                //Settings.seekbarpos[i]       = mEqualizer.getBandLevel(equalizerBandIndex);
                Settings.isEqualizerReloaded = true;
            }
            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                  //  points[seekBar!!.id] = mEqualizer.getBandLevel(equalizerBandIndex)
                    Toast.makeText(context, ""+progress, Toast.LENGTH_SHORT).show()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Settings.presetPos = 0;
                   // Settings.equalizerModel.setPresetPos(0);

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            })*//*
        }*/

        binding.backBt.setOnClickListener {
            bs.dismiss()
        }
        bs.show()
    }

    fun equalizeSound() {
        val equalizerPresetNames: ArrayList<String> = ArrayList()
       /* val equalizerPresetSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
            ctx,
            R.layout.spinner_item,
            equalizerPresetNames
        )
        equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)*/
        equalizerPresetNames.add("Custom")

        for (i in 0 until mEqualizer!!.getNumberOfPresets()) {
            equalizerPresetNames.add(mEqualizer!!.getPresetName(i.toShort()))

        }
        //presetSpinner.setAdapter(equalizerPresetSpinnerAdapter);
        //presetSpinner.setDropDownWidth((Settings.screen_width * 3) / 4);
        if (Settings.isEqualizerReloaded && Settings.presetPos != 0) {
//            correctPosition = false;
          //  presetSpinner.setSelection(Settings.presetPos);
        }
    }


}