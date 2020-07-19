package com.example.user.contactlist.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.contactlist.R;
import com.example.user.contactlist.databinding.ActivityMainBinding;
import com.example.user.contactlist.viewmodel.ContactViewModel;


public class MainActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    public final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        contactViewModel = new ContactViewModel(getApplicationContext());
        binding.setViewModel(contactViewModel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS))
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
            } else {
            initRecyclerView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            initRecyclerView();
        }
    }

    private boolean hasPhoneContactsPermission(String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            return hasPermission == PackageManager.PERMISSION_GRANTED;
        } else
            return true;
    }

    // todo use binding
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.contact_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ContactAdapter adapter = new ContactAdapter(this);
        adapter.setContacts(contactViewModel.getContacts());
        recyclerView.setAdapter(adapter);
    }
}
