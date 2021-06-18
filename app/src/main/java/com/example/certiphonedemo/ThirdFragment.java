package com.example.certiphonedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.certiphonedemo.helper.*;
import com.example.certiphonedemo.databinding.FragmentThirdBinding;

public class ThirdFragment extends Fragment {
    private FragmentThirdBinding binding;
    private AddContact addContact;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addContact = new AddContact((MainActivity) getActivity());
        binding.buttonThrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addContact.add(binding.editTextTextPersonName.getText().toString(), binding.editTextPhone.getText().toString())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Contact ajouté", Toast.LENGTH_LONG);
                    binding.editTextPhone.setText("");
                    binding.editTextTextPersonName.setText("");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Erreur", Toast.LENGTH_LONG);

                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
