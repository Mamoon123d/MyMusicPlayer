package com.musicplayer.android.music

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.TextUtils
import android.util.Log
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.MplayerActivityBinding
import com.musicplayer.android.model.MainMusicData
import java.security.MessageDigest


class MPlayerActivity : BaseActivity<MplayerActivityBinding>() {
    companion object {
        lateinit var audioList: ArrayList<MainMusicData>
    }

    override fun setLayoutId(): Int {
        return R.layout.mplayer_activity
    }

    override fun initM() {
        setTb()
        initializeLayout()
        createPlayer()
    }

    private fun setTb() {
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initializeLayout() {
        audioList = ArrayList()
        audioList = MainActivity.audioList
    }

    private fun createPlayer() {
        val pos = intent.getIntExtra("pos", -1)
        val data = audioList[pos]
        val uriAlbum = getAlbumArtUri(data.albumId.trim().toLong())
        // val bm = getAlbumArt(data.albumId)
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    mActivity.contentResolver,
                    uriAlbum!!
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(mActivity.contentResolver, uriAlbum)
        }
        val softwareBitmap: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
        binding.mainImg.apply {

            if (softwareBitmap != null) {
                Glide.with(mActivity).load(softwareBitmap)
                    //.transform(BlurTransformation(mActivity))
                    .override(2, 2)
                    .into(this)
                //val cls=MyColor(softwareBitmap)
                Palette.from(softwareBitmap).generate(){ palette ->
                    val mutedDark = palette!!.darkMutedSwatch!!.rgb
                    binding.mainLay.setBackgroundColor(mutedDark)
                      showMsg(mutedDark.toString())

                }
            }
        }
        binding.tvSongName.apply {
            text = data.title
            ellipsize = TextUtils.TruncateAt.MARQUEE
            setSingleLine()
            isSelected = true
        }

        binding.tvArtistAndAlbum.text = data.artist
        binding.albumCover.apply {
            // setImageBitmap(bitmap)
            Glide.with(mActivity).asBitmap().load(uriAlbum).into(binding.albumCover)

        }
    }

    /*@SuppressLint("Range")
    private fun getAlbumArt(albumId: String): Bitmap? {
        Log.d("album", "getAlbumArt: " + albumId)
        val musicResolve = contentResolver
        val smusicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Albums._ID + "=?"
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM_ART,
        )
        val music: Cursor? = musicResolve.query(
            smusicUri, projection //should use where clause(_ID==albumid)
            , selection, arrayOf(albumId), null
        )

        var bm: Bitmap? = null

        //val albumIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)) ?: "0"
        if (music != null) {
            try {
                val artAlbum =
                    music.getString(music.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)) ?: "Unknown"
                //val thisArt: String = music.getString(x)
                bm = BitmapFactory.decodeFile(artAlbum)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        // val image: ImageView = findViewById<View>(R.id.image) as ImageView
        // image.setImageBitmap(bm)
        return bm!!
    }*/


    fun getAlbumArtUri(albumId: Long): Uri? {
        return ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
        )
    }


    fun fastblur(sentBitmap: Bitmap, scale: Float, radius: Int): Bitmap? {
        var sentBitmap = sentBitmap
        val width = StrictMath.round(sentBitmap.width * scale)
        val height = StrictMath.round(sentBitmap.height * scale)
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false)
        val bitmap = sentBitmap.copy(sentBitmap.config, true)
        if (radius < 1) {
            return null
        }
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        Log.e("pix", w.toString() + " " + h + " " + pix.size)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1
        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(w, h))
        var divsum = div + 1 shr 1
        divsum *= divsum
        var dv = IntArray(256 * divsum)
        i = 0
        while (i < 256 * divsum) {
            dv[i] = i / divsum
            i++
        }
        yi = 0
        yw = yi
        val stack = Array(div) {
            IntArray(
                3
            )
        }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int
        y = 0
        while (y < h) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                i++
            }
            stackpointer = radius
            x = 0
            while (x < w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x
                sir = stack[i + radius]
                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]
                rbs = r1 - Math.abs(i)
                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                if (i < hm) {
                    yp += w
                }
                i++
            }
            yi = x
            stackpointer = radius
            y = 0
            while (y < h) {

                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] =
                    -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vmin[y]
                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi += w
                y++
            }
            x++
        }
        Log.e("pix", w.toString() + " " + h + " " + pix.size)
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }

    class BlurTransformation(val context: Context?) : BitmapTransformation() {
        private val rs: RenderScript

        init {
            rs = RenderScript.create(context)
        }

        override fun updateDiskCacheKey(messageDigest: MessageDigest) {

        }

        override fun transform(
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
        ): Bitmap {
            val blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true)

            // Allocate memory for Renderscript to work with
            val input: Allocation = Allocation.createFromBitmap(
                rs,
                blurredBitmap,
                Allocation.MipmapControl.MIPMAP_FULL,
                Allocation.USAGE_SHARED
            )
            val output: Allocation = Allocation.createTyped(rs, input.getType())

            // Load up an instance of the specific script that we want to use.
            val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            script.setInput(input)

            // Set the blur radius
            script.setRadius(10f)

            // Start the ScriptIntrinisicBlur
            script.forEach(output)

            // Copy the output to the blurred bitmap
            output.copyTo(blurredBitmap)
            toTransform.recycle()
            return blurredBitmap
        }

        val id: String
            get() = "blur"
    }
}

