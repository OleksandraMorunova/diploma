package com.diploma.assistant.view.ui.navigation;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfComments;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.resource_service.ArticleWithId;
import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.view.adapter.RecycleViewCommentsContext;
import com.diploma.assistant.view_model.TasksViewModel;
import com.diploma.assistant.view_model.UserViewModel;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ListOfComments {
    private final Activity activity;
    private final Context context;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycleOwner;

    private final Map<String, String> stringList = new HashMap<>();
    private final List<List<CommentsDto>> dtoList = new ArrayList<>();
    private final List<ArticleWithId> listOfArticle = new ArrayList<>();
    private final List<ItemsForListOfComments> list = new ArrayList<>();

    public ListOfComments(Activity activity, Context context, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycleOwner) {
        this.activity = activity;
        this.context = context;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.lifecycleOwner = lifecycleOwner;
    }

    public void getCommentsFromDataBase(String token){
        RecyclerView navRecyclerView = activity.findViewById(R.id.nav_drawer_recycler_view);
        TasksViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(TasksViewModel.class);
        viewModel.getTaskDtoMutableLiveData(token).observe(lifecycleOwner, listOfTasks -> {
            list.clear();
            for(int t = 0; t < listOfTasks.size(); t++){
                if(listOfTasks.get(t).getComments() != null){
                    for(int tt = 0; tt < listOfTasks.get(t).getComments().size(); tt++){
                        listOfArticle.add(new ArticleWithId(listOfTasks.get(t).getTitle(), listOfTasks.get(t).getId()));
                    }
                    dtoList.add(listOfTasks.get(t).getComments());
                }
            }
            addCommentsToView(token, navRecyclerView);
        });
    }

    private void addCommentsToView(String token, RecyclerView navRecyclerView){
        List<CommentsDto> newDtoList = dtoList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<ItemsForListOfComments> items = new ArrayList<>();
        for(int u = 0; u < newDtoList.size(); u++){
            items.add(new ItemsForListOfComments.ItemsBuilder(newDtoList.get(u).getUser_comment_id(), newDtoList.get(u).getComment())
                    .idComment(newDtoList.get(u).getId())
                    .commentAddedData(newDtoList.get(u).getComment_added_data())
                    .reviewed(newDtoList.get(u).getReviewed())
                    .article(listOfArticle.get(u).getTitle())
                    .articleId(listOfArticle.get(u).getTaskId())
                    .build()
            );
        }
        items = items.stream().sorted(Comparator.comparing(o -> LocalDateTime.parse(o.getComment_added_data()))).collect(Collectors.toList());

        AtomicInteger y = new AtomicInteger();
        UserViewModel userViewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        List<ItemsForListOfComments> finalItems = items;

        userViewModel.getUsers(token).observe(lifecycleOwner, users -> {
            if(users != null){
                List<User> userList = users.getUserList();
                getUsersMap(userList);
                String userName;
                while (y.get() < finalItems.size()){
                    if(stringList.get(finalItems.get(y.get()).getUserName())!= null) {
                        userName = new String(Base64.getDecoder().decode(stringList.get(finalItems.get(y.get()).getUserName())), StandardCharsets.UTF_8);
                        finalItems.get(y.get()).setUserName(userName);
                        list.add(new ItemsForListOfComments
                                .ItemsBuilder(finalItems.get(y.get()).getUserName(), finalItems.get(y.get()).getComment())
                                .idComment(finalItems.get(y.get()).getIdCommentsByExistList())
                                .commentAddedData(finalItems.get(y.get()).getComment_added_data())
                                .reviewed(finalItems.get(y.get()).getReviewed())
                                .article(finalItems.get(y.get()).getArticleId())
                                .articleId(finalItems.get(y.get()).getArticle())
                                .build()
                        );
                    } else {
                        listOfArticle.remove(y.get());
                        finalItems.remove(y.get());
                        y.getAndDecrement();
                    }
                    y.getAndIncrement();
                }
                navRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                navRecyclerView.setAdapter(new RecycleViewCommentsContext(context, list, viewModelStoreOwner, lifecycleOwner));
            }
        });
    }

    private void getUsersMap(List<User> userList){
        for (int j = 0; j < userList.size(); j++) {
            stringList.put(userList.get(j).getId(), userList.get(j).getName());
        }
    }
}