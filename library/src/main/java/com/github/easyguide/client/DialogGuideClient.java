package com.github.easyguide.client;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.github.easyguide.EasyGuideManager;

/**
 * Created by shenxl on 2018/8/30.
 */
public class DialogGuideClient extends CommonGuideClient {
    PopupWindow mPopupWindow;
    View mDecorView;

    public DialogGuideClient(EasyGuideManager manager, Dialog dialog) {
        super(manager);
        if (dialog.getWindow() != null) {
            mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setContentView(manager.getParentView());
            this.mDecorView = dialog.getWindow().getDecorView();
        }
    }

    @Override
    public void showLayer() {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(mDecorView);
        }
        super.showLayer();
    }

    @Override
    public void dismissCurrent() {
        if (!mManager.hasNextLayer() && mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        super.dismissCurrent();
    }

    @Override
    public void dismissAll() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        super.dismissAll();
    }
}
