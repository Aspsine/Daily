package com.aspsine.zhihu.daily.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Theme;
import com.aspsine.zhihu.daily.entity.Themes;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.ui.adapter.NavigationDrawerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment {
    public static final String TAG = NavigationFragment.class.getSimpleName();
    NavigationDrawerAdapter mAdapter;
    List<Theme> mThemes;

    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThemes = new ArrayList<Theme>();
        mAdapter = new NavigationDrawerAdapter(mThemes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    private void refresh() {
        new Thread(new GetThemeTask()).start();
    }

    private final class GetThemeTask implements Runnable {

        @Override
        public void run() {
            try {
                String jsonStr = Http.get(Constants.Url.ZHIHU_DAILY_THEME);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                Themes themes = gson.fromJson(jsonStr, Themes.class);
                handler.obtainMessage(0, themes).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mThemes.clear();
                    mThemes.addAll(((Themes) msg.obj).getOthers());
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

}
