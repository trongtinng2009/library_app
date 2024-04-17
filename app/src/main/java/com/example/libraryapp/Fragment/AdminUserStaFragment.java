package com.example.libraryapp.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.libraryapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminUserStaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminUserStaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnuser,btnlib;
    private BarChart barChart;
    private ArrayList<BarEntry> entries;
    private FirebaseFirestore db;
    private int max;

    public AdminUserStaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminUserStaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminUserStaFragment newInstance(String param1, String param2) {
        AdminUserStaFragment fragment = new AdminUserStaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_user_sta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        entries = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        initView(view);
        setClick();
    }
    private void initView(View view)
    {
        barChart = view.findViewById(R.id.fragadminsta_chart);
        btnlib = view.findViewById(R.id.fragadminsta_libcard);
        btnuser = view.findViewById(R.id.fragadminsta_user);
    }
    private void setClick()
    {
        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barChart.setVisibility(View.VISIBLE);
                setBarChart("User","add_date");
            }
        });
        btnlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barChart.setVisibility(View.VISIBLE);
                setBarChart("Libcard","acc_date");
            }
        });
    }
    private void setBarChart(String collection,String field)
    {
        entries.clear();
        barChart.getAxisRight().setDrawLabels(false);
        db.collection(collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                max = task.getResult().size();
            }
        });

        for(int i = 1;i<=12;i++)
        {
            addEntry(collection,field,i);
        }
    }
    private void addEntry(String collection,String field,int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Timestamp ts2 = new Timestamp(calendar.getTime());
        calendar.set(calendar.get(Calendar.YEAR),month,1);
        Timestamp ts1 = new Timestamp(calendar.getTime());
        db.collection(collection).
                whereGreaterThanOrEqualTo(field,ts1)
                .whereLessThanOrEqualTo(field,ts2)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            entries.add(new BarEntry(month,task.getResult().size()));
                            YAxis yAxis = barChart.getAxisLeft();
                            yAxis.setAxisMaximum(0f);
                            yAxis.setAxisMaximum(max);
                            yAxis.setAxisLineWidth(2f);
                            yAxis.setAxisLineColor(Color.BLACK);
                            yAxis.setLabelCount(5);
                            yAxis.setTextSize(16f);

                            BarDataSet barDataSet = new BarDataSet(entries,"Tháng");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextSize(16f);

                            BarData barData = new BarData(barDataSet);
                            barChart.setData(barData);
                            barChart.setFitBars(true);
                            barChart.getDescription().setEnabled(false);
                            barChart.invalidate();
                            ArrayList<String> months = new ArrayList<>(Arrays.asList("1","2"
                                    ,"3","4","5","6"
                                    ,"7","8","9","10"
                                    ,"11","12"));
                            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                            barChart.getXAxis().setTextSize(16f);
                            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChart.getXAxis().setGranularity(1f);
                            barChart.getXAxis().setGranularityEnabled(true);
                        }
                    }
                });
    }
}