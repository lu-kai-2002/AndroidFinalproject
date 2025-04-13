package com.example.finalproject.ui.post;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import android.widget.Toast;
import android.app.Activity;
import androidx.annotation.Nullable;

import com.example.finalproject.R;

public class PostFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGES = 1001;

    private EditText editTextContent;
    private Button btnAddImage, btnPublish;
    private RecyclerView recyclerViewImages;

    private List<Uri> selectedImages = new ArrayList<>();
    private ImageAdapter imageAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_post, container, false);

        editTextContent = root.findViewById(R.id.editTextContent);
        btnAddImage = root.findViewById(R.id.btnAddImage);
        btnPublish = root.findViewById(R.id.btnPublish);
        recyclerViewImages = root.findViewById(R.id.recyclerViewImages);

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

    private void publishPost() {
        String content = editTextContent.getText().toString().trim();
        Toast.makeText(getContext(), "内容：" + content + "，图片数：" + selectedImages.size(), Toast.LENGTH_SHORT).show();
    }
}

