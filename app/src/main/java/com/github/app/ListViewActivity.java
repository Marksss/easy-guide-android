package com.github.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.CommonGuideLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/11/20.
 */
public class ListViewActivity extends AppCompatActivity {
    private ListView listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            dataList.add("listview's item - " + i);
        }
        ListAdapter adapter = new ArrayAdapter<String>(ListViewActivity.this,
                android.R.layout.simple_list_item_1, dataList);
        listview = findViewById(R.id.listview);
        listview.setAdapter(adapter);

        listview.post(new Runnable() {
            @Override
            public void run() {
                /*   EasyGuide starts  */
                new EasyGuideManager(ListViewActivity.this).
                        addLayer(
                                new CommonGuideLayer(ListViewActivity.this).
                                        addHighlightTarget(getViewByPosition(3, listview))
                        ).show();
                /*   EasyGuide ends  */
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                View item = getViewByPosition(37, listview);
                if (scrollState == SCROLL_STATE_IDLE && item != null){
                    /*   EasyGuide starts  */
                    new EasyGuideManager(ListViewActivity.this).
                            addLayer(
                                    new CommonGuideLayer(ListViewActivity.this).
                                            addHighlightTarget(item)
                            ).show();
                    /*   EasyGuide ends  */
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return null;
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
