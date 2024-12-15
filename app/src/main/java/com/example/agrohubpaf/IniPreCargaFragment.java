package com.example.agrohubpaf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agrohubpaf.databinding.FragmentIniPreCargaBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class IniPreCargaFragment extends Fragment {

    private FragmentIniPreCargaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIniPreCargaBinding.inflate(inflater, container, false);

        binding.buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(IniPreCargaFragment.this)
                        .navigate(R.id.action_FirstFragment_to_iniLoginFragment);
            }
        });
        return binding.getRoot();
    }

}