package com.diploma.assistant.view.ui.main_page.admin;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfUsers;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.enumaration.MakeToastEnum;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.view.adapter.RecycleViewAdminContext;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListOfUsers extends NavigationActivity{
    private final Activity activity;
    private final Context context;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycleOwner;

    private final List<ItemsForListOfUsers> items = new ArrayList<>();
    private final String[] itemAutoCompleteBottomSheet = {TypeUserEnum.ADMIN.getTypeUserName(), TypeUserEnum.USER.getTypeUserName()};
    private AutoCompleteTextView autoCompleteTextView;

    private RecycleViewAdminContext adapter;

    public ListOfUsers(Activity activity, Context context, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycleOwner) {
        this.activity = activity;
        this.context = context;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.lifecycleOwner = lifecycleOwner;

    }

    public void showDialog(String token, RecyclerView recycleView){
        LinearLayout l = activity.findViewById(R.id.bottom_sheet);

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(l);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(115);
        bottomSheetBehavior.setHideable(false);

        activity.findViewById(R.id.fab).setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) { }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                activity.findViewById(R.id.fab).animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });

        autoCompleteTextView = l.findViewById(R.id.role_bottom_sheet);
        ArrayAdapter<String> arrayAdapterBottomSheet = new ArrayAdapter<>(context, R.layout.list_item, itemAutoCompleteBottomSheet);
        autoCompleteTextView.setAdapter(arrayAdapterBottomSheet);

        l.findViewById(R.id.button_add).setOnClickListener(v -> createUserAndAddedToViewList(l, token, recycleView));
    }

    private void createUserAndAddedToViewList(LinearLayout l, String token, RecyclerView recycleView){
        TextInputEditText tName = l.findViewById(R.id.name_bottom_sheet);
        TextInputEditText tPhone = l.findViewById(R.id.phone_bottom_sheet);

        User user = new User();
        user.setName(Objects.requireNonNull(tName.getText()).toString());
        user.setPhone(Objects.requireNonNull(tPhone.getText()).toString());
        user.setRoles(Collections.singletonList(autoCompleteTextView.getText().toString()));

        UserViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        viewModel.createUser(token,user).observe(lifecycleOwner, u -> {
            if(u != null) {
                ItemsForListOfUsers itemsForListOfUsers = new ItemsForListOfUsers.ItemsForListOfUsersBuilder("0",  tName.getText().toString())
                        .status(autoCompleteTextView.getText().toString())
                        .count("0")
                        .icon(null)
                        .build();
                items.add(itemsForListOfUsers);
                adapter = new RecycleViewAdminContext(activity, items, this, this);
                recycleView.setLayoutManager(new LinearLayoutManager(context));
                recycleView.setAdapter(adapter);
            } else Toast.makeText(context, MakeToastEnum.CAUSE.getError(), Toast.LENGTH_SHORT).show();

        });
        tName.setText(null);
        tPhone.setText(null);
    }
}