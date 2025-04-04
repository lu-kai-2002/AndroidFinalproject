package com.example.finalproject.ui.dashboard;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.BarDetailActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {
    private static final int DETAIL_REQUEST_CODE = 1001;
    private LeaderboardAdapter adapter;
    private List<BarItem> barItems = new ArrayList<>();
    private BarDao barDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        barDao = new BarDao(requireContext());
        // 初始化 RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 加载数据
        loadData();

        // 初始化适配器
        adapter = new LeaderboardAdapter(barItems, (item, position) -> {
            // 带位置信息的点击事件
            Intent intent = new Intent(getActivity(), BarDetailActivity.class);
            intent.putExtra("BAR_ITEM", item);
            intent.putExtra("ITEM_POSITION", position);
            startActivityForResult(intent, DETAIL_REQUEST_CODE);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadData() {
        // 从数据库获取最新数据
        barItems = barDao.getAllBars();
        Collections.sort(barItems, (o1, o2) -> Double.compare(o2.getAverageRating(), o1.getAverageRating()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 获取更新后的数据
            BarItem updatedBar = data.getParcelableExtra("UPDATED_BAR");
            int position = data.getIntExtra("ITEM_POSITION", -1);

            if (position != -1) {
                // 局部更新
                barItems.set(position, updatedBar);
                Collections.sort(barItems, (o1, o2) -> Double.compare(o2.getRating(), o1.getRating()));
                adapter.updateData(barItems); // 触发重新排序和刷新
            }
        }
    }
}