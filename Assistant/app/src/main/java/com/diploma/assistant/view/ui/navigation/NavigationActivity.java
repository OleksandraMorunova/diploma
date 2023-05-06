package com.diploma.assistant.view.ui.navigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.NavActivityBinding;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfUsers;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.adapter.RecycleViewAdminContext;
import com.diploma.assistant.view_model.FilesViewModel;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.gms.common.util.IOUtils;
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

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavActivityBinding binding;
    RecycleViewAdminContext adapter;

    private final List<ItemsForListOfUsers> items = new ArrayList<>();
    private RecyclerView recycleView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = NavActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.navAppBar.toolbar);

        AuthenticatorService accounts = new AuthenticatorService(getApplicationContext());
        String token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");

        String name = new String(Base64.getDecoder().decode(getIntent().getStringExtra("name")), StandardCharsets.UTF_8);
        String phone = new String(Base64.getDecoder().decode(getIntent().getStringExtra("phone")), StandardCharsets.UTF_8);
        List<String> roles = new ArrayList<>(Arrays.asList(getIntent().getStringExtra("role").split(" ")));
        String icon = getIntent().getStringExtra("icon");
        TextView nameContainer = findViewById(R.id.name_icon_setting);
        TextView emailContainer = findViewById(R.id.email_icon_setting);
        ShapeableImageView imageContainer = findViewById(R.id.image_icon_setting);
        nameContainer.setText(name);
        emailContainer.setText(phone);

        if(icon != null){
                FilesViewModel viewModel = new ViewModelProvider(this).get(FilesViewModel.class);
                viewModel.getFiles(token, icon).observe(this, f -> {
                    byte[] bytes = Base64.getDecoder().decode(f.getFile());
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageContainer.setImageBitmap(bitmap);
                });
        } else imageContainer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_user, getTheme()));

        for(int i = 0; i < roles.size(); i++) {
            if(roles.get(i).equals(TypeUserEnum.ADMIN.getTypeUserName()) && token != null) {
                recycleView = findViewById(R.id.recycle_view);
                SwipeRefreshLayout swipe = findViewById(R.id.swipe_layout);

                callToDataBaseForListOfUsers(token);
                swipe.setOnRefreshListener(() -> {
                    callToDataBaseForListOfUsers(token);
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

    private void callToDataBaseForListOfUsers(String token){
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUsers(token).observe(this, users -> {
            if(users != null){
                List<User> userList = users.getUserList();
                List<Integer> integerList = users.getIntegerList();
                items.clear();
                for (int i = 0; i < userList.size(); i++) {
                    String name = new String(Base64.getDecoder().decode(userList.get(i).getName()), StandardCharsets.UTF_8);
                    ItemsForListOfUsers itemsForListOfUsers =
                            new ItemsForListOfUsers.ItemsForListOfUsersBuilder(userList.get(i).getId(), name)
                                    .status(userList.get(i).getStatus())
                                    .count(integerList.get(i).toString())
                                    .icon(bitmap)
                                    .build();
                    items.add(itemsForListOfUsers);
                }
                adapter = new RecycleViewAdminContext(this, items);
                recycleView.setLayoutManager(new LinearLayoutManager(this));
                recycleView.setAdapter(adapter);
            }
        });
    }
}