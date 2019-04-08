package com.github.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.GuideLayerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/8/27.
 */
public class RecyclerViewActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylerview);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            data.add("recyclerview's item: " + i);
        }
        recyclerView.setAdapter(new Adapter(data));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    int targetPosition = 30;
                    if (firstVisibleItemPosition <= targetPosition
                            && targetPosition < lastVisibleItemPosition) {

                        /*   EasyGuide starts  */
                        new EasyGuideManager().
                                addLayer(
                                        GuideLayerImpl.Companion.coverActivity(RecyclerViewActivity.this).
                                                addHighlightTarget(mLayoutManager.findViewByPosition(targetPosition))
                                ).show();
                        /*   EasyGuide ends  */
                    }
                }
            }
        });

        recyclerView.post(new Runnable() {
            @Override
            public void run() {

                /*   EasyGuide starts  */
                new EasyGuideManager().
                        addLayer(
                                GuideLayerImpl.Companion.coverActivity(RecyclerViewActivity.this).
                                        addHighlightTarget(mLayoutManager.findViewByPosition(0))
                        ).show();
                /*   EasyGuide ends  */
            }
        });
    }

    public static class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public Adapter(@Nullable List<String> data) {
            super(R.layout.item_rcv, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.item_tv, item);
        }
    }

}