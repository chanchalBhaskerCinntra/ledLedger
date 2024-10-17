package com.cinntra.ledure.viewpager;


import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cinntra.ledure.R;

import com.cinntra.ledure.activities.MainActivity_B2C;


import com.cinntra.ledure.customUI.CustomMarkerView;
import com.cinntra.ledure.customUI.CustomMarkerViewReceipt;
import com.cinntra.ledure.customUI.CustomMarkerViewReceivables;

import com.cinntra.ledure.customUI.RoundedBarChart;
import com.cinntra.ledure.globals.Globals;

import com.cinntra.ledure.model.GraphModel;
import com.cinntra.ledure.model.SalesGraphResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GraphPagerAdapter extends PagerAdapter {
    private static final String TAG = "GraphPagerAdapter";
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.analytics, R.drawable.activity_image, R.drawable.analytics};

    public GraphPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_graphs_on_dashboard, null);

        //saleGraphApi(view, container);
        if (position == 0)
            managebyShubh(view, container, MainActivity_B2C.Salesentries,MainActivity_B2C.SalesPreviousentries);
        if (position == 1)
            managebyShubhReceipt(view, container, MainActivity_B2C.Receiptentries,MainActivity_B2C.ReceiptPreviousentries);
        if (position == 2)
            managebyShubhReceivable(view, container, MainActivity_B2C.Receivableentries);

        return view;
    }

    private void managebyShubh(View view, ViewGroup container, List<BarEntry> entries,List<BarEntry>previousEntries) {


        List<String> xvalues = Arrays.asList("Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar");


        BarChart customer_barChart = view.findViewById(R.id.any_chart_view_dash);

        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");




        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(0f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        dataSet.setColor(context.getResources().getColor(R.color.white));
        dataSet.setDrawValues(false);


        /*dataSet.setHighLightColor(Color.WHITE); // Set the color
        dataSet.setHighLightAlpha(100);*/
        //todo new thing
        dataSet.setHighlightEnabled(true);


        //todo new code because to show data of two bars
        BarDataSet dataSet2 = new BarDataSet(previousEntries, "Values2");
        dataSet2.setColor(context.getResources().getColor(R.color.grey));
        dataSet2.setDrawValues(false);
        dataSets.add(dataSet2);
        dataSets.add(dataSet);

        //todo new thing
        dataSet2.setHighlightEnabled(true);



        BarData data = new BarData(dataSets);
        Log.e("SHUBH>>CHECK", "managebyShubh: " + dataSets.size());

        data.setBarWidth(0.3f);

        data.setValueTextColor(context.getResources().getColor(R.color.white));
        customer_barChart.setData(data);



         customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xvalues));
        customer_barChart.getXAxis().setCenterAxisLabels(true);
        customer_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        customer_barChart.getXAxis().setTextColor(context.getResources().getColor(R.color.white));
        customer_barChart.getXAxis().setLabelCount(12, false);
        customer_barChart.getXAxis().setDrawGridLines(false);
        customer_barChart.getXAxis().setGranularity(1f);




        YAxis yAxis = customer_barChart.getAxisLeft();
        yAxis.setTextColor(context.getResources().getColor(R.color.white));
        yAxis.setAxisMinimum(0f);
        yAxis.setEnabled(true);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
//                axis.setLabelCount(3, true);
                return "" + Globals.convertToLakhAndCroreFromFloat(value);
            }
        });

        customer_barChart.setTouchEnabled(true);
        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setPinchZoom(false);
        //
        customer_barChart.setScaleEnabled(false);

        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);
        // below line is to add bar
        // space to our chart.
        float barSpace = 0.1f;

        // below line is use to add group
        // spacing to our bar chart.
        float groupSpace = 0.2f;

        customer_barChart.groupBars(0, groupSpace, barSpace);



        //add animation
        customer_barChart.animateY(1000);





        CustomMarkerView markerView = new CustomMarkerView(context, R.layout.barchart_marker, MainActivity_B2C.previousSalesValueForMarker,MainActivity_B2C.SalesValueForMarker);
        customer_barChart.setMarker(markerView);



        //draw chart
        customer_barChart.invalidate();


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
    }


    private static class MyCustomValue {
        private String value;

        public MyCustomValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class MyValueFormatter extends ValueFormatter {

        private final List<MyCustomValue> customValues;

        public MyValueFormatter(List<MyCustomValue> customValues) {
            this.customValues = customValues;
        }

        @Override
        public String getFormattedValue(float value) {
            // Display the corresponding custom value
            int index = (int) value;
            if (index >= 0 && index < customValues.size()) {
                return String.valueOf(customValues.get(index).getValue());
            }
            return "";
        }
    }

    private List<MyCustomValue> getCustomValues() {
        List<MyCustomValue> customValues = new ArrayList<>();
        for (int i = 1; i <= MainActivity_B2C.SalesValueForMarker.size() - 1; i++) {
            customValues.add(new MyCustomValue(MainActivity_B2C.SalesValueForMarker.get(i)));
        }
        return customValues;
    }

//todo commented by shubh for make exact like salesTab
/*
    private void managebyShubhReceipt(View view, ViewGroup container, List<BarEntry> entries) {


        List<String> xvalues = Arrays.asList("Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar");
        //   List<String> xvalues= Arrays.asList("Jan","Feb","March","April","May","June");

        BarChart customer_barChart = view.findViewById(R.id.any_chart_view_dash);

       */
/* for (int i = 1; i <= entries.size()-1; i++) {
            temp.add(String.valueOf(entries.get(i).getY()));
        }*//*

        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");




        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(0f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        dataSet.setColor(context.getResources().getColor(R.color.yellow));
        dataSet.setDrawValues(false);
        dataSet.setHighLightColor(Color.WHITE); // Set the color
        dataSet.setHighLightAlpha(100);

        dataSets.add(dataSet);
        //todo new thing
        dataSet.setHighlightEnabled(true);


        BarDataSet dataSet2 = new BarDataSet(entries, "Values 2");
        dataSet2.setColor(context.getResources().getColor(R.color.white));
        dataSet2.setDrawValues(false);
        dataSet2.setHighLightColor(Color.WHITE); // Set the color
        dataSet2.setHighLightAlpha(100);
        dataSets.add(dataSet2);
        dataSet2.setHighLightColor(Color.WHITE);
        dataSet2.setHighLightAlpha(100);
        //todo new thing
        dataSet2.setHighlightEnabled(true);


        BarData data = new BarData(dataSets);
        Log.e("SHUBH>>CHECK", "managebyShubh: " + dataSets.size());
        data.setBarWidth(0.3f);
        data.setValueTextColor(context.getResources().getColor(R.color.white));
        //  data.setValueTextColor(Color.WHITE);
        customer_barChart.setData(data);



         customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xvalues));
        customer_barChart.getXAxis().setCenterAxisLabels(true);
        customer_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        customer_barChart.getXAxis().setTextColor(context.getResources().getColor(R.color.white));
        customer_barChart.getXAxis().setLabelCount(13, false);
        customer_barChart.getXAxis().setDrawGridLines(false);

        customer_barChart.getXAxis().setGranularity(1f);
        // xvalues.remove(0);
        //  customer_barChart.getXAxis().setGranularityEnabled(true);



        YAxis yAxis = customer_barChart.getAxisLeft();
        yAxis.setTextColor(context.getResources().getColor(R.color.white));
        yAxis.setAxisMinimum(0f);
        yAxis.setEnabled(true);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
//                axis.setLabelCount(3, true);//todo comment due to no need of 3 time differences in y axis
                return "" + Globals.convertToLakhAndCroreFromFloat(value);
            }
        });

        customer_barChart.setTouchEnabled(true);
        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setPinchZoom(false);
        customer_barChart.setScaleEnabled(false);

        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);
        float barSpace = 0.1f;

        // below line is use to add group
        // spacing to our bar chart.
        float groupSpace = 0.2f;

        customer_barChart.groupBars(0, groupSpace, barSpace);

        //add animation
        customer_barChart.animateY(1000);
        CustomMarkerViewReceipt markerView = new CustomMarkerViewReceipt(context, R.layout.barchart_marker, MainActivity_B2C.ReceiptValueForMarker);
        customer_barChart.setMarker(markerView);

        //draw chart
        customer_barChart.invalidate();


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
    }
*/


    private void managebyShubhReceipt(View view, ViewGroup container, List<BarEntry> entries,List<BarEntry>previousEntries) {


        List<String> xvalues = Arrays.asList("Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar");


        BarChart customer_barChart = view.findViewById(R.id.any_chart_view_dash);

        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");




        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(0f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        dataSet.setColor(context.getResources().getColor(R.color.white));
        dataSet.setDrawValues(false);


      /*  dataSet.setHighLightColor(Color.WHITE); // Set the color
        dataSet.setHighLightAlpha(100);*/
        //todo new thing
        dataSet.setHighlightEnabled(true);


        //todo new code because to show data of two bars
        BarDataSet dataSet2 = new BarDataSet(previousEntries, "Values2");
        dataSet2.setColor(context.getResources().getColor(R.color.grey));
        dataSet2.setDrawValues(false);
        dataSets.add(dataSet2);
        dataSets.add(dataSet);

        //todo new thing
        dataSet2.setHighlightEnabled(true);



        BarData data = new BarData(dataSets);
        Log.e("SHUBH>>CHECK", "managebyShubh: " + dataSets.size());

        data.setBarWidth(0.3f);

        data.setValueTextColor(context.getResources().getColor(R.color.white));
        customer_barChart.setData(data);



        customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xvalues));
        customer_barChart.getXAxis().setCenterAxisLabels(true);
        customer_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        customer_barChart.getXAxis().setTextColor(context.getResources().getColor(R.color.white));
        customer_barChart.getXAxis().setLabelCount(12, false);
        customer_barChart.getXAxis().setDrawGridLines(false);


        customer_barChart.getXAxis().setGranularity(1f);




        YAxis yAxis = customer_barChart.getAxisLeft();
        yAxis.setTextColor(context.getResources().getColor(R.color.white));
        yAxis.setAxisMinimum(0f);
        yAxis.setEnabled(true);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
//                axis.setLabelCount(3, true);//todo comment due to no need of 3 time differences in y axis
                return "" + Globals.convertToLakhAndCroreFromFloat(value);
            }
        });

        customer_barChart.setTouchEnabled(true);
        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setPinchZoom(false);
        //
        customer_barChart.setScaleEnabled(false);

        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);
        // below line is to add bar
        // space to our chart.
        float barSpace = 0.1f;

        // below line is use to add group
        // spacing to our bar chart.
        float groupSpace = 0.2f;

        customer_barChart.groupBars(0, groupSpace, barSpace);



        //add animation
        customer_barChart.animateY(1000);





        CustomMarkerViewReceipt markerView = new CustomMarkerViewReceipt(context, R.layout.barchart_marker,MainActivity_B2C.previousReceiptValueForMarker,MainActivity_B2C.ReceiptValueForMarker);

        customer_barChart.setMarker(markerView);



        //draw chart
        customer_barChart.invalidate();


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
    }



    private void managebyShubhReceivable(View view, ViewGroup container, List<BarEntry> entries) {


        List<String> xvalues = Arrays.asList(">90", "61-90", "0-30", "31-60");
        // List<String> xvalues= Arrays.asList("Jan","Feb","March","April","May","June");

        BarChart customer_barChart = view.findViewById(R.id.any_chart_view_dash);

       /* for (int i = 1; i <= entries.size()-1; i++) {
            temp.add(String.valueOf(entries.get(i).getY()));
        }*/
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");
        temp.add("ss");

        /*   CustomMarkerView markerView = new CustomMarkerView(context, R.layout.barchart_marker, temp);


        customer_barChart.setMarker(markerView);

        customer_barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Display your MarkerView when a value is selected
                customer_barChart.highlightValue(h);
                // markerView.content("Value: " + e.getY()); // Customize content as needed
                markerView.refreshContent(e, h);
                markerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected() {
                // Hide the MarkerView when nothing is selected
                markerView.setVisibility(View.GONE);
            }
        });*/


        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(0f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        dataSet.setColor(context.getResources().getColor(R.color.white));
        dataSet.setDrawValues(false);
        dataSet.setHighLightColor(Color.WHITE); // Set the color
        dataSet.setHighLightAlpha(100);
        dataSets.add(dataSet);


        BarData data = new BarData(dataSets);
        data.setBarWidth(0.75f);
        data.setValueTextColor(context.getResources().getColor(R.color.white));
        //  data.setValueTextColor(Color.WHITE);
        customer_barChart.setData(data);
        customer_barChart.setFitBars(false);


        // customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xvalues));
        customer_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        customer_barChart.getXAxis().setTextColor(context.getResources().getColor(R.color.white));
        customer_barChart.getXAxis().setLabelCount(13, false);
        customer_barChart.getXAxis().setDrawGridLines(false);

        customer_barChart.getXAxis().setGranularity(1f);
        customer_barChart.setPinchZoom(false);
        //todo previous type of adding xaxis
        //  customer_barChart.getXAxis().setGranularityEnabled(true);
//        customer_barChart.getXAxis().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                if (value == 0) {
//                    return ">90";
//                } else {
//                    return xvalues.get((int) value);
//                }
//
//            }
//        });

        customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(MainActivity_B2C.ReceivableentriesXaxis));


        YAxis yAxis = customer_barChart.getAxisLeft();
        yAxis.setTextColor(context.getResources().getColor(R.color.white));
        yAxis.setAxisMinimum(0f);
        yAxis.setEnabled(true);

        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
//                axis.setLabelCount(3, true);
                return "" + Globals.convertToLakhAndCroreFromFloat(value);
            }
        });

        customer_barChart.setTouchEnabled(true);
        customer_barChart.setDrawBarShadow(true);
        customer_barChart.setScaleEnabled(false);

        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);

        //add animation
        customer_barChart.animateY(2000);
        CustomMarkerViewReceivables markerView = new CustomMarkerViewReceivables(context, R.layout.barchart_marker, MainActivity_B2C.ReceivableValueForMarker);
        customer_barChart.setMarker(markerView);


        //draw chart
        customer_barChart.invalidate();


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
    }

    /*********************** Graphs APIs**************************/
    List<BarEntry> Salesentries = new ArrayList<>();
    List<BarEntry> SalesPreviousentries = new ArrayList<>();
    List<String> temp = new ArrayList<>();
    ArrayList<GraphModel> salesGraphList = new ArrayList<>();
    ArrayList<GraphModel> salesPreviousGraphList = new ArrayList<>();

    private void saleGraphApi(View view, ViewGroup container) {

        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", "2023-04-01");
        obj.put("ToDate", "2024-03-31");
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<SalesGraphResponse> call = NewApiClient.getInstance().getApiService(context).salesGraph(obj);
        call.enqueue(new Callback<SalesGraphResponse>() {
            @Override
            public void onResponse(Call<SalesGraphResponse> call, Response<SalesGraphResponse> response) {
                if (response != null) {
                    if (response.body().status == 200) {
                        if (response.body() != null)
                            salesGraphList.clear();
                        Salesentries.clear();
                        SalesPreviousentries.clear();
                        salesPreviousGraphList.clear();
                        salesGraphList.addAll(response.body().data);
                        for (int i = 0; i < salesGraphList.size(); i++) {

                            temp.add(salesGraphList.get(i).getMonthlySales());
                            Salesentries.add(new BarEntry(i, Float.parseFloat(salesGraphList.get(i).getMonthlySales())));
                            SalesPreviousentries.add(new BarEntry(i, Float.parseFloat(salesGraphList.get(i).getLastMonthlySales())));
                        }
                        // opengraph();
                        Log.e("TestBP==>", "" + Salesentries.size());
                        if (view != null && container != null)
                            managebyShubh(view, container, Salesentries,SalesPreviousentries);
                    }


                }
            }

            @Override
            public void onFailure(Call<SalesGraphResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}

