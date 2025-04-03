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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private static final int DETAIL_REQUEST_CODE = 1001;
    private List<BarItem> barItems;
    private LeaderboardAdapter adapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST_CODE && resultCode == RESULT_OK) {
            BarItem updatedBar = data.getParcelableExtra("UPDATED_BAR");
            updateListData(updatedBar);
        }
    }

    private void updateListData(BarItem updatedBar) {
        for (int i = 0; i < barItems.size(); i++) {
            if (barItems.get(i).getName().equals(updatedBar.getName())) {
                barItems.set(i, updatedBar);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    // 修改原来的跳转代码
    private void startDetailActivity(BarItem item) {
        Intent intent = new Intent(getActivity(), BarDetailActivity.class);
        intent.putExtra("BAR_ITEM", item);
        startActivityForResult(intent, DETAIL_REQUEST_CODE); // 使用startActivityForResult
    }
    // 初始化测试数据

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);



        // 按评分降序排序
       // Collections.sort(barItems, (o1, o2) -> Double.compare(o2.getRating(), o1.getRating()));
        initData();
        // 设置RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LeaderboardAdapter adapter = new LeaderboardAdapter(barItems, item -> {
            Intent intent = new Intent(getActivity(), BarDetailActivity.class);
            intent.putExtra("BAR_ITEM", item);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
    private void initData() {

        barItems = Arrays.asList(
                new BarItem("月光酒吧", R.drawable.bar1, "朝阳区酒仙桥路1号", 4.5),
                new BarItem("星空酒廊", R.drawable.bar2, "海淀区中关村大街5号", 4.0),
                new BarItem("蓝调之夜", R.drawable.bar3, "东城区王府井大街8号", 3.3),
                new BarItem("爵士俱乐部", R.drawable.bar4, "西城区金融街3号", 3.2),
                new BarItem("老地方酒吧", R.drawable.bar5, "丰台区方庄路12号", 3.1)
        );
        Collections.sort(barItems, (o1, o2) -> Double.compare(o2.getRating(), o1.getRating()));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}