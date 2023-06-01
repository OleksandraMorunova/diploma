package com.diploma.assistant.view.ui.main_page.admin;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

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
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.view.adapter.RecycleViewCommentsContext;
import com.diploma.assistant.view_model.TasksViewModel;
import com.diploma.assistant.view_model.UserViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GeListOfCommentsAllUsersWithoutAdmins {
    private final Activity activity;
    private final Context context;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycleOwner;
    private RecyclerView navRecyclerView;

    private final Map<String, String> stringList = new HashMap<>();
    private final Map<String, String> strFirebaseToken = new HashMap<>();
    private final List<List<CommentsDto>> dtoList = new ArrayList<>();
    private final List<ArticleWithId> listOfArticle = new ArrayList<>();
    private final List<ItemsForListOfComments> list = new ArrayList<>();

    public GeListOfCommentsAllUsersWithoutAdmins(Activity activity, Context context, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycleOwner) {
        this.activity = activity;
        this.context = context;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.lifecycleOwner = lifecycleOwner;
    }

    public void getCommentsFromDataBase(String token){
        navRecyclerView = activity.findViewById(R.id.nav_drawer_recycler_view);
        TasksViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(TasksViewModel.class);
        List<String> taskId = new ArrayList<>();
        viewModel.getTaskDtoMutableLiveData(token).observe(lifecycleOwner, listOfTasks -> {
          if(listOfTasks != null){
              listOfArticle.clear();
              list.clear();
              stringList.clear();
              dtoList.clear();
              for(int t = 0; t < listOfTasks.size(); t++){
                  if(listOfTasks.get(t).getComments() != null){
                      for(int tt = 0; tt < listOfTasks.get(t).getComments().size(); tt++){
                          taskId.add(listOfTasks.get(t).getId());
                          listOfArticle.add(new ArticleWithId(listOfTasks.get(t).getTitle(), listOfTasks.get(t).getId()));
                      }
                      dtoList.add(listOfTasks.get(t).getComments());
                  }
              }
              addCommentsToView(taskId, token);
          } else Toast.makeText(activity.getApplicationContext(), ErrorEnum.CONNECTION_TO_INTERNET.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    private void addCommentsToView(List<String> idTitle, String token){
        List<CommentsDto> newDtoList = dtoList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<ItemsForListOfComments> finalItems = sortedList(newDtoList, idTitle);
        AtomicInteger y = new AtomicInteger();
        UserViewModel userViewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        userViewModel.getUsers(token).observe(lifecycleOwner, users -> {
            if(users != null){
                List<User> userList = users.getUserList();
                getUsersMap(userList);
                while (y.get() < finalItems.size()){
                   secondSorted(finalItems, y);
                }
                navRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                navRecyclerView.setAdapter(new RecycleViewCommentsContext(activity, list, viewModelStoreOwner, lifecycleOwner));
            } else Toast.makeText(activity.getApplicationContext(), ErrorEnum.CONNECTION_TO_INTERNET.getName(), Toast.LENGTH_SHORT).show();
    });
    }

    private List<ItemsForListOfComments> sortedList(List<CommentsDto> newDtoList, List<String> idTitle){
        List<ItemsForListOfComments> items = new ArrayList<>();
        for(int u = 0; u < newDtoList.size(); u++){
            items.add(new ItemsForListOfComments.ItemsBuilder(newDtoList.get(u).getUser_comment_id(), newDtoList.get(u).getComment())
                    .idComment(newDtoList.get(u).getId())
                    .idTask(idTitle.get(u))
                    .commentAddedData(newDtoList.get(u).getComment_added_data())
                    .reviewed(newDtoList.get(u).getReviewed())
                    .article(listOfArticle.get(u).getTitle())
                    .articleId(listOfArticle.get(u).getTaskId())
                    .build()
            );
        }
        if(items != null) {
            return items.stream().sorted(Comparator.comparing(o -> LocalDateTime.parse(o.getComment_added_data()))).collect(Collectors.toList());
        }
        return null;
    }

    private void getUsersMap(List<User> userList){
        for (int j = 0; j < userList.size(); j++) {
            stringList.put(userList.get(j).getId(), userList.get(j).getName());
            strFirebaseToken.put(userList.get(j).getId(), userList.get(j).getUserTokenFirebase());
        }
    }

    private void secondSorted(List<ItemsForListOfComments> finalItems, AtomicInteger y){
        String userName = stringList.get(finalItems.get(y.get()).getUserName());
        String token = strFirebaseToken.get(finalItems.get(y.get()).getUserName());
        finalItems.get(y.get()).setUserName(userName);
        finalItems.get(y.get()).setFirebaseToken(token);
        list.add(new ItemsForListOfComments
                .ItemsBuilder(finalItems.get(y.get()).getUserName(), finalItems.get(y.get()).getComment())
                .idComment(finalItems.get(y.get()).getIdCommentsByExistList())
                .idTask(finalItems.get(y.get()).getIdTask())
                .commentAddedData(finalItems.get(y.get()).getComment_added_data())
                .reviewed(finalItems.get(y.get()).getReviewed())
                .article(finalItems.get(y.get()).getArticleId())
                .articleId(finalItems.get(y.get()).getArticle())
                .firebaseToken(finalItems.get(y.get()).getFirebaseToken())
                .build()
        );
        y.getAndIncrement();
    }
}