package com.github.easyguide.client;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.github.easyguide.EasyGuideManager;

/**
 * Created by shenxl on 2018/8/30.
 */
public class DialogGuideDecorator implements IGuideAction {
    private EasyGuideManager mManager;
    private IGuideAction mGuideComponent;
    private PopupWindow mPopupWindow;
    private View mDecorView;

    public DialogGuideDecorator(EasyGuideManager manager, IGuideAction guideComponent, Dialog dialog) {
        this.mManager = manager;
        this.mGuideComponent = guideComponent;
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
        mGuideComponent.showLayer();
    }

    @Override
    public void dismissCurrent() {
        if (mManager.getCurrentLayer().nextLayer() == null && mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mGuideComponent.dismissCurrent();
    }

    @Override
    public void dismissAll() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mGuideComponent.dismissAll();
    }
}
