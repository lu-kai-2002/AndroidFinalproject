package com.example.finalproject.ui.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.PostDetailActivity;
import com.example.finalproject.R;
import com.example.finalproject.ui.dashboard.BarDao;
import com.example.finalproject.ui.dashboard.BarItem;
import com.example.finalproject.ui.login.loginDBhelper;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGES = 1001;

    private EditText editTextContent;
    private Button btnAddImage, btnPublish;
    private RecyclerView recyclerViewImages;
    private Spinner spinnerBars;

    private List<Uri> selectedImages = new ArrayList<>();
    private ImageAdapter imageAdapter;

    private PostDao postDao;
    private loginDBhelper loginHelper;
    private BarDao barDao;

    // 用来存所有 BarItem 和对应的 名字列表
    private List<BarItem> allBars;
    private List<String> barNames;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_post, container, false);

        // 1. 绑定控件
        editTextContent    = root.findViewById(R.id.editTextContent);
        btnAddImage        = root.findViewById(R.id.btnAddImage);
        btnPublish         = root.findViewById(R.id.btnPublish);
        recyclerViewImages = root.findViewById(R.id.recyclerViewImages);
        spinnerBars        = root.findViewById(R.id.spinnerBars);

        // 2. 初始化 DAO
        postDao     = new PostDao(requireContext());
        loginHelper = new loginDBhelper(requireContext());
        barDao      = new BarDao(requireContext());

        // 3. 图片列表设置
        imageAdapter = new ImageAdapter(selectedImages, getContext());
        recyclerViewImages.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imageAdapter);
        btnAddImage.setOnClickListener(v -> pickImages());

        // 4. 下拉框（Spinner）初始化
        allBars  = barDao.getAllBars();
        barNames = new ArrayList<>();
        for (BarItem b : allBars) {
            barNames.add(b.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                barNames
        );
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerBars.setAdapter(spinnerAdapter);

        // 5. 发布按钮
        btnPublish.setOnClickListener(v -> publishPost());

        return root;
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(
                Intent.createChooser(intent, "选择图片"),
                REQUEST_CODE_PICK_IMAGES
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGES
                && resultCode == Activity.RESULT_OK
                && data != null) {

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    selectedImages.add(uri);
                }
            } else if (data.getData() != null) {
                selectedImages.add(data.getData());
            }
            imageAdapter.notifyDataSetChanged();
        }
    }

    /** 发布帖子 */
    private void publishPost() {
        String content = editTextContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = getCurrentUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "用户未登录，无法发帖", Toast.LENGTH_SHORT).show();
            return;
        }

        // 从 Spinner 取选中的酒吧名（如果没有选，getSelectedItem 会返回 null）
        String barName = (String) spinnerBars.getSelectedItem();

        PostEntity post = new PostEntity();
        post.setUserId(userId);
        post.setContent(content);
        post.setBarName(barName);
        // 图片部分，这里仍默认用资源 id，或你改为保存 Uri 路径
        post.setImageResId(R.drawable.sample3);

        long postId = postDao.addPost(post);
        if (postId != -1) {
            Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
            // 清空
            editTextContent.setText("");
            selectedImages.clear();
            imageAdapter.notifyDataSetChanged();
            // 跳转详情页
            Intent intent = new Intent(getContext(), PostDetailActivity.class);
            intent.putExtra("post_id", (int) postId);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "发布失败", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCurrentUserId() {
        SharedPreferences sp = requireContext()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return sp.getInt("current_user_id", -1);
    }
}

