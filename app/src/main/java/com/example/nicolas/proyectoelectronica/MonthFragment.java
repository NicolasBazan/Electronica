package com.example.nicolas.proyectoelectronica;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment  {
    View mView;


    public MonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_month, container, false);
        GraphView graph_month = mView.findViewById(R.id.graph_month);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(5, 8),
                new DataPoint(10, 10),
                new DataPoint(15, 3),
                new DataPoint(20, 6),
                new DataPoint(25, 1),
                new DataPoint(30, 9),



        });
        graph_month.addSeries(series);

        graph_month.getViewport().setMinX(0);
        graph_month.getViewport().setMaxX(31);
        graph_month.getViewport().setMinY(1);
        graph_month.getViewport().setMaxY(10);

        graph_month.getViewport().setYAxisBoundsManual(true);
        graph_month.getViewport().setXAxisBoundsManual(true);




        return mView;
    }

}
