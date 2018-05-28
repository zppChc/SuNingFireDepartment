package com.yatai.suningfiredepartment.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yatai.suningfiredepartment.R;

public class CensusFragment extends Fragment {
    public static CensusFragment newInstance(String data){
        Bundle args = new Bundle();
        args.putString("key",data);
        CensusFragment fragment = new CensusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_census,container,false);
        return view;
    }
}
