package com.example.finalproject.ui.post;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.PostDetailActivity;
import com.example.finalproject.R;
import com.example.finalproject.ui.login.loginDBhelper;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGES = 1001;

    private EditText editTextContent;
    private Button btnAddImage, btnPublish;
    private RecyclerView recyclerViewImages;

    private List<Uri> selectedImages = new ArrayList<>();
    private ImageAdapter imageAdapter;

    private PostDao postDao;
    private loginDBhelper loginHelper;

    private PostDbHelper postDbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_post, container, false);

        editTextContent = root.findViewById(R.id.editTextContent);
        btnAddImage = root.findViewById(R.id.btnAddImage);
        btnPublish = root.findViewById(R.id.btnPublish);
        recyclerViewImages = root.findViewById(R.id.recyclerViewImages);

        // 初始化 DAO 和登录助手
        postDbHelper=new PostDbHelper(requireContext());
        postDao = new PostDao(requireContext());
        loginHelper = new loginDBhelper(requireContext());

        // 设置图片列表
        imageAdapter = new ImageAdapter(selectedImages, getContext());
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imageAdapter);

        btnAddImage.setOnClickListener(v -> pickImages());
        btnPublish.setOnClickListener(v -> publishPost());

        return root;
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_PICK_IMAGES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImages.add(imageUri);
                }
            } else if (data.getData() != null) {
                selectedImages.add(data.getData());
            }
            imageAdapter.notifyDataSetChanged();
        }
    }

    /** 发布帖子：将数据写入数据库 */
    private void publishPost() {
        String content = editTextContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = getCurrentUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "用户未登录，无法发帖", Toast.LENGTH_SHORT).show();
            return;
        }

        // 这里只使用一张图片，真实项目可扩展为保存路径或上传 URL
        int imageResId = R.drawable.sample3;
        String barName = null;

        PostEntity post = new PostEntity();
        post.setUserId(userId);
        post.setContent(content);
        post.setImageResId(imageResId);
        post.setBarName(barName);

        long postId = postDao.addPost(post);

        if (postId != -1) {
            Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();

            // 清空输入框
            editTextContent.setText("");
            selectedImages.clear();
            imageAdapter.notifyDataSetChanged();

            // 跳转详情页查看新发的帖子
            Intent intent = new Intent(getContext(), PostDetailActivity.class);
            intent.putExtra("post_id", (int) postId);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "发布失败", Toast.LENGTH_SHORT).show();
        }
    }

    /** 示例方法：从 SharedPreferences 获取登录用户 ID（暂用固定值） */
    private int getCurrentUserId() {
        // 真实实现中，你应从已登录用户信息中提取 ID，这里暂写死
        return 1;
    }
}
