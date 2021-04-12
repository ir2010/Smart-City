package com.ir.smartcity.user;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ir.smartcity.R;
import com.ir.smartcity.job.Job;
import com.ir.smartcity.job.JobAdapter;

import java.util.List;

public class CallsFragment extends Fragment {

    private RecyclerView historylistview;
    private List<Job> jobList;
    private JobAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        historylistview= view.findViewById(R.id.hirer_lists);
//        historylistview.setHasFixedSize(true);
//        historylistview.setLayoutManager(new LinearLayoutManager(getContext()));-->
        return inflater.inflate(R.layout.fragment_calls, container, false);
    }

}
