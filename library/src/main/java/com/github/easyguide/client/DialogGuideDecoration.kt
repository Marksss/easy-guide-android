package com.github.easyguide.client

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow

/**
 * Created by shenxl on 2018/8/30.
 */
class DialogGuideDecoration(dialog: Dialog, private var mClient: IGuideClient = CommonGuideClient()) : IGuideClient by mClient {
    private lateinit var mPopupWindow: PopupWindow
    private var mDecorView: View = dialog.window?.decorView ?: throw IllegalArgumentException("Window in dialog is null!")

    override fun show() {
        mPopupWindow = PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mPopupWindow.contentView = parentView
        mPopupWindow.showAsDropDown(mDecorView)
        mClient.show()
    }

    override fun dismissCurrent() {
        if (!layerChain.hasNextLayer()) {
            mPopupWindow.dismiss()
        }
        mClient.dismissCurrent()
    }

    override fun dismissAll() {
        mPopupWindow.dismiss()
        mClient.dismissAll()
    }
}
