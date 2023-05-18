package com.diploma.assistant.view.ui.main_page.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.NavActivityBinding;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfUsers;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.adapter.RecycleViewAdminContext;
import com.diploma.assistant.view.ui.main_page.UpdateUsersDataActivity;
import com.diploma.assistant.view_model.FilesViewModel;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private NavActivityBinding binding;
    private RecycleViewAdminContext adapter;

    private final List<ItemsForListOfUsers> items = new ArrayList<>();
    private RecyclerView recycleView;
    private Bitmap bitmap;
    private byte[] bytes;

    private static String name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NavActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.navAppBar.toolbar);

        String token = getToken();
        name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        List<String> roles = new ArrayList<>(Arrays.asList(getIntent().getStringExtra("role").split(" ")));
        String icon = getIntent().getStringExtra("icon");
        TextView nameContainer = findViewById(R.id.name_icon_setting);
        TextView emailContainer = findViewById(R.id.email_icon_setting);
        ShapeableImageView imageContainer = findViewById(R.id.image_icon_setting);
        ImageView settings = findViewById(R.id.setting);
        nameContainer.setText(name);
        emailContainer.setText(phone);

        settings.setOnClickListener(v -> {
            this.startActivity(new Intent(this, UpdateUsersDataActivity.class)
                    .putExtra("name", name)
                    .putExtra("phone_number", phone)
                    .putExtra("icon", bytes)
            );
        });

        if(icon != null && token != null){
                FilesViewModel viewModel = new ViewModelProvider(this).get(FilesViewModel.class);
                viewModel.getFiles(token, icon).observe(this, f -> {
                   if (f != null){
                       bytes = Base64.getDecoder().decode(f.getFile());
                       bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                       imageContainer.setImageBitmap(bitmap);
                   } else Toast.makeText(this, ErrorEnum.CONNECTION_TO_INTERNET.getName(), Toast.LENGTH_SHORT).show();
                });
        } else imageContainer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_user, getTheme()));

        for(int i = 0; i < roles.size(); i++) {
            if(roles.get(i).equals(TypeUserEnum.ADMIN.getTypeUserName()) && token != null) {
                recycleView = findViewById(R.id.recycle_view);
                SwipeRefreshLayout swipe = findViewById(R.id.swipe_layout);

                callToDataBaseForListOfUsers(token);
                swipe.setOnRefreshListener(() -> {
                    callToDataBaseForListOfUsers(token);
                    getListOfComments(token);
                    swipe.setRefreshing(false);
                });

                ListOfUsers list = new ListOfUsers(this, this, this, this);
                list.showDialog(token, recycleView);
                getListOfComments(token);
            }
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(binding.navDrawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setIconified(false);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public void onBackPressed(){
       Log.i("OnBackPressed", "Navigation activity back pressed");
    }

    public void getListOfComments(String token){
        ListOfComments listOfComments = new ListOfComments(this, this, this, this);
        listOfComments.getCommentsFromDataBase(token);
    }

    private String getToken(){
        AuthenticatorService accounts = new AuthenticatorService(this);
        return accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
    }

    private void callToDataBaseForListOfUsers(String token){
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUsers(token).observe(this, users -> {
            items.clear();
            if(users != null){
                int i = 0;
                while (users.getUserList().size() > i){
                       if(!users.getUserList().get(i).getRoles().contains(TypeUserEnum.ADMIN.getTypeUserName())){
                           String name = users.getUserList().get(i).getName();
                           ItemsForListOfUsers itemsForListOfUsers =
                                   new ItemsForListOfUsers.ItemsForListOfUsersBuilder(users.getUserList().get(i).getId(), name)
                                           .status(users.getUserList().get(i).getStatus())
                                           .count(users.getIntegerList().get(i).toString())
                                           .icon(users.getUserList().get(i).getIcon())
                                           .build();
                           items.add(itemsForListOfUsers);
                       }
                       i++;
                }
                adapter = new RecycleViewAdminContext(this, items, this, this);
                recycleView.setLayoutManager(new LinearLayoutManager(this));
                recycleView.setAdapter(adapter);
            } else Toast.makeText(this, ErrorEnum.CONNECTION_TO_INTERNET.getName(), Toast.LENGTH_SHORT).show();
        });
    }
}