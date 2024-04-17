package com.example.libraryapp.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.libraryapp.Adapter.BookAdapter;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Category;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianStatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianStatisticFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BarChart barChart;
    private RecyclerView rcv;
    private RadioGroup rdgr;
    private boolean isBtn1Click;
    private int btn1,max;
    private Button btnbookview,btnbookbr,btnbookbrmonth,btnbookim,btnbookcate;
    private FirebaseFirestore db;
    private BookAdapter bookAdapter;
    private ArrayList<BookOffline> bookOfflines;
    private TextView txttotal;
    private ArrayList<BarEntry> entries;

    public LibrarianStatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianStatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianStatisticFragment newInstance(String param1, String param2) {
        LibrarianStatisticFragment fragment = new LibrarianStatisticFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_statistic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        isBtn1Click = true;
        initView(view);
        setButton1click();
        setButton2click();
        checkRadioBtn();
    }
    private void initView(View view)
    {
        barChart = view.findViewById(R.id.fraglibsta_chart);
        rcv = view.findViewById(R.id.fraglibsta_rcv);
        btnbookbr = view.findViewById(R.id.fraglibsta_bookbrbtn);
        btnbookcate = view.findViewById(R.id.fraglibsta_bookcatebtn);
        btnbookbrmonth = view.findViewById(R.id.fraglibsta_bookbrmonthbtn);
        btnbookim = view.findViewById(R.id.fraglibsta_bookimbtn);
        txttotal = view.findViewById(R.id.fraglibsta_txttotal);
        btnbookview = view.findViewById(R.id.fraglibsta_bookviewbtn);
        rdgr = view.findViewById(R.id.fraglibsta_radiolevel);
        rcv.setNestedScrollingEnabled(false);
        bookOfflines = new ArrayList<>();
        entries = new ArrayList<>();
        bookAdapter = new BookAdapter(bookOfflines,getContext(),R.layout.book_item2,1);
    }
    private void setButton2click()
    {
        btnbookim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtn1Click = false;
                checkButton1click();
                setBarChart("BookOffline","add_date");
            }
        });
        btnbookbrmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtn1Click = false;
                checkButton1click();
                setBarChart("BookInBorrow","borrow_date");
            }
        });
        btnbookcate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtn1Click = false;
                checkButton1click();
                setCharCate();
            }
        });
    }
    private void setCharCate()
    {
        entries.clear();
        barChart.getAxisRight().setDrawLabels(false);
        db.collection("Category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    max = task.getResult().size();
                    ArrayList<String> catename = new ArrayList<>();
                    int i = 0;
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        Category category = queryDocumentSnapshot.toObject(Category.class);
                        entries.add(new BarEntry(i,category.getView()));
                        catename.add(category.getName());
                        i++;
                    }
                    YAxis yAxis = barChart.getAxisLeft();
                    yAxis.setAxisLineWidth(2f);
                    yAxis.setAxisLineColor(Color.BLACK);
                    yAxis.setLabelCount(5);
                    yAxis.setTextSize(16f);

                    BarDataSet barDataSet = new BarDataSet(entries,"Thể loại");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextSize(16f);

                    BarData barData = new BarData(barDataSet);
                    barChart.setData(barData);
                    barChart.setFitBars(true);
                    barChart.getDescription().setEnabled(false);
                    barChart.invalidate();
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(catename));
                    barChart.getXAxis().setTextSize(16f);
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.getXAxis().setGranularity(1f);
                    barChart.getXAxis().setGranularityEnabled(true);
                }
            }
        });

    }
    private void setBarChart(String collection, String field)
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
    private void setButton1click()
    {
        btnbookview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtn1Click = true;
                checkButton1click();
                setRcv("view_point", Query.Direction.DESCENDING);
                if(btn1 != 1) {
                    btn1 = 1;
                    rdgr.check(R.id.fraglibsta_rdmost);
                }
            }
        });
        btnbookbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtn1Click = true;
                checkButton1click();
                setRcv("borrow_point", Query.Direction.DESCENDING);
                if (btn1 != 2) {
                    btn1 = 2;
                    rdgr.check(R.id.fraglibsta_rdmost);
                }
            }
        });
    }
    private void checkButton1click()
    {
        if(isBtn1Click)
        {
            rdgr.setVisibility(View.VISIBLE);
            txttotal.setVisibility(View.VISIBLE);
            rcv.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
        }
        else
        {
            txttotal.setVisibility(View.GONE);
            rdgr.setVisibility(View.GONE);
            rcv.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
        }
    }
    private void checkRadioBtn()
    {
        rdgr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.fraglibsta_rdmost)
                {
                    if(btn1 == 1)
                        setRcv("view_point", Query.Direction.DESCENDING);
                    else
                        setRcv("borrow_point", Query.Direction.DESCENDING);
                }
                else if(checkedId == R.id.fraglibsta_rdless)
                {
                    if(btn1 == 1)
                        setRcv("view_point", Query.Direction.ASCENDING);
                    else
                        setRcv("borrow_point", Query.Direction.ASCENDING);
                }
            }
        });
    }
    private void setRcv(String field, Query.Direction most)
    {
        db.collection("BookOffline").whereEqualTo("bookstate",BookOffline.BookState.ACCEPTED.value)
                .limit(10)
                .orderBy(field,most).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           bookOfflines.clear();
                           txttotal.setText("Tổng: "+ task.getResult().size());
                           for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                           {
                               BookOffline bookOffline = queryDocumentSnapshot.toObject(BookOffline.class);
                               bookOfflines.add(bookOffline);
                           }
                           rcv.setAdapter(bookAdapter);
                           rcv.setLayoutManager(new GridLayoutManager(getContext(),2));
                       }
                    }
                });
    }
}