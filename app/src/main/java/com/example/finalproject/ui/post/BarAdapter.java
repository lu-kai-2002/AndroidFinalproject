package com.example.finalproject.ui.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.dashboard.BarItem;

import java.util.List;

public class BarAdapter extends RecyclerView.Adapter<BarAdapter.ViewHolder> {

    private final List<BarItem> bars;
    // -1 表示没有选中
    private int selectedPosition = -1;

    public BarAdapter(List<BarItem> bars) {
        this.bars = bars;
    }

    /** 获取当前选中的酒吧名称 */
    public String getSelectedBarName() {
        if (selectedPosition != -1) {
            return bars.get(selectedPosition).getName();
        }
        return null;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bar_checkbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BarItem bar = bars.get(position);
        holder.cbBar.setText(bar.getName());

        // 先解绑旧的 listener（防止复用时错乱）
        holder.cbBar.setOnCheckedChangeListener(null);
        // 根据 selectedPosition 绑定选中状态
        holder.cbBar.setChecked(position == selectedPosition);

        // 点击时重新获取 adapterPosition
        holder.cbBar.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos == RecyclerView.NO_POSITION) return;

            // 如果点同一项，则取消选中；否则选中新项
            int previous = selectedPosition;
            if (adapterPos == selectedPosition) {
                selectedPosition = -1;
                notifyItemChanged(adapterPos);
            } else {
                selectedPosition = adapterPos;
                notifyItemChanged(adapterPos);
                if (previous != -1) {
                    notifyItemChanged(previous);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbBar;
        ViewHolder(View itemView) {
            super(itemView);
            cbBar = itemView.findViewById(R.id.cbBar);
        }
    }
}

