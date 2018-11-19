package com.github.easyguide.client

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

/**
 * Created by shenxl on 2018/8/30.
 */
internal class DialogGuideClient(dialog: Dialog, private var mClient: IGuideClient = CommonGuideClient()) : IGuideClient by mClient{
    private lateinit var popupWindow: PopupWindow
    private var decorView: View = dialog.window?.decorView ?: throw IllegalArgumentException("Window in dialog is null!")

    override fun show() {
        popupWindow = PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            contentView = parentView
            showAsDropDown(decorView)
        }
        mClient.show()
    }

    override fun goNext() {
        if (currentLayer.next == null) {
            popupWindow.dismiss()
        }
        mClient.goNext()
    }

    override fun dismiss() {
        popupWindow.dismiss()
        mClient.dismiss()
    }
}
