package com.example.certiphonedemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.certiphonedemo.helper.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.certiphonedemo.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {
    //message
    private FragmentFirstBinding binding;
    private SmsSender smsSender;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smsSender = new SmsSender(this);
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }


        });
        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smsSender.onSend(binding.inputNumber.getText().toString(), binding.inputMessage.getText().toString())) {
                    Toast.makeText(getActivity().getApplicationContext(), "SMS envoyé", Toast.LENGTH_LONG);
                    binding.inputMessage.setText("");
                    binding.inputNumber.setText("");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "erreur", Toast.LENGTH_LONG);

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