package com.dev.da.maratona;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * Created by Tiago Emerenciano on 11/10/2017.
 */

public class Tab1Pontuacao extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1pontuacao, container, false);
        return rootView;
    }
}
