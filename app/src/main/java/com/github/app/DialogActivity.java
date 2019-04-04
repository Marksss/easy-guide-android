package com.github.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.github.easyguide.layer.IGuideLayer;
import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.GuideLayerImpl;

/**
 * Created by shenxl on 2018/8/27.
 */
public class DialogActivity extends AppCompatActivity {
    private EasyGuideManager mEasyGuideManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        findViewById(R.id.btn_show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view1 = LayoutInflater.from(DialogActivity.this).inflate(R.layout.dialog,null);
                final AlertDialog dialog = new AlertDialog.Builder(DialogActivity.this)
                        .create();
                dialog.setTitle("This is dialog");
                dialog.setView(view1);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        /*   EasyGuide starts  */
                        IGuideLayer basicLayer = GuideLayerImpl.Companion.coverDialog(dialog).addHighlightTarget(view1.findViewById(R.id.dialog_top));
                        mEasyGuideManager = EasyGuideManager.Companion.create().addLayer(basicLayer);
                        mEasyGuideManager.show();
                        /*   EasyGuide ends  */

                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // This is strongly recommended in case of memory leak
                        if (mEasyGuideManager != null){
                            mEasyGuideManager.dismiss();
                        }
                    }
                });
                dialog.show();

                view1.findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                view1.findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
