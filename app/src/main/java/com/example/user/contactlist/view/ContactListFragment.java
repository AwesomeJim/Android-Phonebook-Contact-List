package com.example.user.contactlist.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.user.contactlist.R;
import com.example.user.contactlist.databinding.FragmentContactListBinding;
import com.example.user.contactlist.viewmodel.ContactViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment implements View.OnClickListener {

    private ContactViewModel contactViewModel;

    private FragmentContactListBinding binding;
    private boolean isSearch = false;
    ContactAdapter adapter;

    public ContactListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactListFragment.
     */
    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactViewModel = new ContactViewModel(getContext().getApplicationContext());
        binding = FragmentContactListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initRecyclerView();
        binding.searchView.setOnClickListener(this);
        binding.etSearch.setOnClickListener(this);
        return view;
    }


    private void initRecyclerView() {
        binding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(binding.contactRecyclerView.getContext()));
        binding.contactRecyclerView.addItemDecoration(new DividerItemDecoration(binding.contactRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new ContactAdapter(binding.contactRecyclerView.getContext());
        adapter.setContacts(contactViewModel.getContacts());
        binding.contactRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, obj, position) -> {
            Toast.makeText(getContext(), "Contact Selected\n"+obj.getName()+obj.getPhoneNumber(), Toast.LENGTH_LONG).show();
        });
    }


    /**
     * Handle  clicks  on the registered views
     *
     * @param view pressed view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.et_search) {
            isSearch = true;
            binding.searchLayout.setVisibility(View.INVISIBLE);
            binding.searchView.setVisibility(View.VISIBLE);
            setUpSearch();
        }
    }

    /**
     * Close Search
     */
    private void closeSearch() {
        if (isSearch) {
            isSearch = false;
            binding.searchLayout.setVisibility(View.VISIBLE);
            binding.searchView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Set up search.
     */
    private void setUpSearch() {
        binding.searchView.setIconified(false);
        binding.searchView.setQueryHint("Enter name");
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });
        binding.searchView.setOnCloseListener(() -> {
            closeSearch();
            return true;
        });
    }
}
