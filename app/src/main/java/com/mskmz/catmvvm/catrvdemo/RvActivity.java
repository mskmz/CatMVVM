package com.mskmz.catmvvm.catrvdemo;

import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mskmz.catmvvm.core.v.CatActivity;
import com.mskmz.catmvvm.databinding.ActivityRvBinding;


public class RvActivity extends CatActivity<ActivityRvBinding> {
    ObservableList<RvItemModel> list;
    RvItemModel a = new RvItemModel("7");

    @Override
    protected void init() {
        super.init();
        list = new ObservableArrayList<>();
        getDb().rvShow.setAdapter(new RvAdapter(list));
        getDb().rvShow.setLayoutManager(new LinearLayoutManager(this));

        list.add(new RvItemModel("1"));
        list.add(new RvItemModel("2"));
        list.add(new RvItemModel("3"));
        list.add(new RvItemModel("4"));
        list.add(new RvItemModel("5"));
        list.add(new RvItemModel("6"));
        list.add(a);
        list.add(new RvItemModel("8"));
        getDb().btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(5);
                a.str = "9";
                a.notifyChange();
            }
        });
    }
}
