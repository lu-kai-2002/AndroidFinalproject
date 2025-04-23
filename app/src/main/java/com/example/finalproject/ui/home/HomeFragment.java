package com.example.finalproject.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.FragmentHomeBinding;
import com.example.finalproject.ui.login.loginDBhelper;
import com.example.finalproject.ui.post.PostDao;
import com.example.finalproject.ui.post.PostEntity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PostAdapter adapter;
    private List<Post> postList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewFeed;
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

//        postList = new ArrayList<>();
//        postList.add(new Post("Rubby", "Wow!", R.drawable.sample1));
//        postList.add(new Post("Rechal", "So chill~", R.drawable.sample2));
//        postList.add(new Post("Rainie", "It's a special place.", R.drawable.sample3));

        // 从数据库加载帖子数据
        postList = loadPostsFromDb(getContext());

        adapter = new PostAdapter(postList, getContext());
        recyclerView.setAdapter(adapter);

        return root;
    }

    /**
     * 从本地数据库加载帖子数据并封装为 Post（首页展示模型）
     */
    private List<Post> loadPostsFromDb(Context context) {
        PostDao postDao = new PostDao(context);
        loginDBhelper userHelper = new loginDBhelper(context);

        List<PostEntity> entityList = postDao.getAllPosts();
        List<Post> result = new ArrayList<>();

        for (PostEntity entity : entityList) {
            String username = userHelper.getUsernameById(entity.getUserId());
            result.add(new Post(username != null ? username : "匿名用户",
                    entity.getContent(),
                    entity.getImageResId(),
                    entity.getId())); // 新增 postId
        }

        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

