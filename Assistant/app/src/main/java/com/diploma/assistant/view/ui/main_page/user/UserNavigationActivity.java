package com.diploma.assistant.view.ui.main_page.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.NavUserActivityBinding;
import com.diploma.assistant.databinding.TasksListActivityFinallyBinding;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.chat.user.ChatUserFragment;
import com.diploma.assistant.view.ui.main_page.UpdateUsersDataActivity;
import com.diploma.assistant.view.ui.tasks.user.TasksListFragment;
import com.diploma.assistant.view_model.FilesViewModel;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Base64;

public class UserNavigationActivity extends AppCompatActivity {
    private NavUserActivityBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private Bitmap bitmap;
    private byte[] bytes;

    private String id, name, phone, icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NavUserActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.navAppBarFragment.toolbarFragment);

        AuthenticatorService accounts = new AuthenticatorService(this);
        id = accounts.getStringFromSharedPreferences("id_user", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");

        ImageView settings = findViewById(R.id.setting);
        settings.setOnClickListener(v -> {
            this.startActivity(new Intent(this, UpdateUsersDataActivity.class)
                    .putExtra("name", name)
                    .putExtra("phone_number", phone)
                    .putExtra("icon", bytes)
            );
        });

        replaceToTasks();
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.user_task_item){
               replaceToTasks();
            } else if(item.getItemId() == R.id.user_chat_item){
               replaceFragment(new ChatUserFragment());
            }
            return true;
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(binding.navDrawerLayoutFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_user_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewFragment, navController);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void replaceToTasks(){
        TasksListFragment fragment = new TasksListFragment();
        Bundle args = new Bundle();
        args.putByteArray("icon", bytes);
        args.putString("name", name);
        fragment.setArguments(args);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String token = getToken();
        getListOfComments(token);
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

                return false;
            }
        });
        searchView.setIconified(false);
        return true;
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
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.checkUserId(id).observe(this, userEntity -> {
            if(userEntity != null){
                TextView nameContainer = findViewById(R.id.name_icon_setting);
                TextView emailContainer = findViewById(R.id.email_icon_setting);
                ShapeableImageView imageContainer = findViewById(R.id.image_icon_setting);

                name = userEntity.getName();
                phone = userEntity.getPhone();
                icon = userEntity.getIcon();

                nameContainer.setText(name);
                emailContainer.setText(phone);


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

            }
        });

        GetListOfCommentsForCertainUser list = new GetListOfCommentsForCertainUser(this, this, this, this);
        list.getListOfComments();
    }

    private String getToken(){
        AuthenticatorService accounts = new AuthenticatorService(this);
        return accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
    }
}