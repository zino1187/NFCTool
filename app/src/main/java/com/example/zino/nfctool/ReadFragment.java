package com.example.zino.nfctool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
 * Created by zino on 2016-06-28.
 */
public class ReadFragment extends Fragment implements View.OnClickListener{
    Button read_start;
    TextView txt_data;
    ProgressBar bar;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.read_layout, container, false);

        read_start = (Button)view.findViewById(R.id.read_start);
        txt_data=(TextView)view.findViewById(R.id.txt_data);
        bar=(ProgressBar)view.findViewById(R.id.bar);

        //버튼과 리스너와의 연결
        read_start.setOnClickListener(this);

        return view;
    }

    public void onClick(View view) {
        //프로그래스바 보이기!!
        bar.setVisibility(View.VISIBLE);

    }
}









