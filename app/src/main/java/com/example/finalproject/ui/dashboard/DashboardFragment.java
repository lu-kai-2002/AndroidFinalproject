package com.example.finalproject.ui.dashboard;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.Locale;

public class DashboardFragment extends Fragment {
    private static final int DETAIL_REQUEST_CODE = 1001;
    private LeaderboardAdapter adapter;
    private List<BarItem> barItems = new ArrayList<>();
    private BarDao barDao;
    private Spinner sortSpinner;
    private int currentSortPosition = 0; // 0:评分排序, 1:热度排序

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // 初始化数据库
        barDao = new BarDao(requireContext());

        // 初始化下拉排序选择器
        sortSpinner = view.findViewById(R.id.sort_spinner);
        setupSortSpinner();

        // 初始化RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderboardAdapter(barItems, this::onBarItemClick);
        recyclerView.setAdapter(adapter);

        // 加载数据
        loadData();

        return view;
    }

    private void setupSortSpinner() {
        // 创建排序选项
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                R.layout.item_spinner_selected
        );
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        sortSpinner.setAdapter(adapter);

        // 设置下拉选择监听
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSortPosition = position;
                sortData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadData() {
        barItems = barDao.getAllBars();
        sortData();
    }

    private void sortData() {
        if (currentSortPosition == 0) {
            // 按评分排序（降序）
            Collections.sort(barItems, (o1, o2) ->
                    Double.compare(o2.getAverageRating(), o1.getAverageRating()));
        } else {
            // 按热度排序（降序）
            Collections.sort(barItems, (o1, o2) ->
                    Integer.compare(o2.getRatingCount(), o1.getRatingCount()));
        }
        adapter.updateData(barItems);
    }

    private void onBarItemClick(BarItem item, int position) {
        Intent intent = new Intent(getActivity(), BarDetailActivity.class);
        intent.putExtra("BAR_ITEM", item);
        intent.putExtra("ITEM_POSITION", position);
        startActivityForResult(intent, DETAIL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            BarItem updatedBar = data.getParcelableExtra("UPDATED_BAR");
            int position = data.getIntExtra("ITEM_POSITION", -1);

            if (position != -1 && updatedBar != null) {
                // 更新数据并保持当前排序方式
                barItems.set(position, updatedBar);
                sortData();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sortPosition", currentSortPosition);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            currentSortPosition = savedInstanceState.getInt("sortPosition", 0);
            sortSpinner.setSelection(currentSortPosition);
        }
    }
}