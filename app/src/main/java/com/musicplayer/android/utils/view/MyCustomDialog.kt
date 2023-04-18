package com.musicplayer.android.utils.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.musicplayer.android.R
import com.musicplayer.android.databinding.MyCustomDialogBinding
import com.musicplayer.android.utils.Extension.Companion.gone
import com.musicplayer.android.utils.Extension.Companion.visible

class MyCustomDialog(
    val mContext: Context,
    val title: String? = null,
    val subtitle: String? = null,
    val positiveButtonText: String? ="Ok",
    val negativeButtonText: String? = "cancel",
    val dialogStile:Int?= STYLE_1,
    private val isCancelable: Boolean? = false

) : Dialog(mContext) {
    private var onDialogListener: OnDialogListener? = null
    companion object{
        const val STYLE_1:Int=1
        const val STYLE_2:Int=2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MyCustomDialogBinding.inflate(LayoutInflater.from(mContext))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window!!.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        setCancelable(isCancelable!!)
        val closeBt=binding.closeBt
        val titleTv=binding.title
        val subtitleTv=binding.subtitle
        val posBtn=binding.positiveBtn
        val negBtn=binding.negativeBtn
        title?.run {
            titleTv.text=this
        }?:titleTv.gone()

        subtitle?.run {
            subtitleTv.text=this
        }?:subtitleTv.gone()
        posBtn.text=positiveButtonText
        negBtn.text=negativeButtonText

        if (dialogStile== STYLE_2){
            closeBt.visible()
            titleTv.textAlignment= TextView.TEXT_ALIGNMENT_TEXT_START
            subtitleTv.textAlignment= TextView.TEXT_ALIGNMENT_TEXT_START
            negBtn.background=mContext.getDrawable(R.drawable.bg_round_btn)
        }else{

            closeBt.gone()
            titleTv.textAlignment= TextView.TEXT_ALIGNMENT_CENTER
            subtitleTv.textAlignment= TextView.TEXT_ALIGNMENT_CENTER
            negBtn.background= null
        }

        posBtn.setOnClickListener {
            onDialogListener!!.onPositiveButton()
        }

        negBtn.setOnClickListener {
            onDialogListener!!.onNegativeButton()
        }

    }


    public fun setOnDialogListener(onDialogListener: OnDialogListener) {
        this.onDialogListener = onDialogListener
    }

    public interface OnDialogListener {
        public fun onNegativeButton()
        public fun onPositiveButton()
    }
}