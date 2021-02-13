package kr.co.calmme

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Window
import kotlinx.android.synthetic.main.activity_progress.*
import java.lang.Thread.sleep

class ProgressDialog(context: Context) {
    private val dialog = Dialog(context)

    fun progress() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_progress)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        val frameAnimation: AnimationDrawable =
            dialog.loading_icon.background as AnimationDrawable
        frameAnimation.start()
    }

    fun finish() {
        dialog.dismiss()
    }
}