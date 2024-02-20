package com.cinntra.ledure.fragments;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.MainActivity_B2C;
import com.cinntra.ledure.customUI.RoundedBarChart;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.CounterResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.Team;
import com.cinntra.ledure.model.Top5CustomerResponse;
import com.cinntra.ledure.model.Top5Item;
import com.cinntra.ledure.model.Top5ItemResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;


public class Graph_ extends Fragment implements View.OnClickListener {
    @BindView(R.id.customer_barChart)
    BarChart customer_barChart;
    @BindView(R.id.selling_barChart)
    BarChart selling_barChart;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.relativeCalView)
    RelativeLayout relativeCalView;
    @BindView(R.id.relativeInfoView)
    RelativeLayout relativeInfoView;
    @BindView(R.id.filterView)
    RelativeLayout filterView;

    @BindView(R.id.search)
    RelativeLayout search;

    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;


    @BindView(R.id.cus_1)
    TextView cus_1;
    @BindView(R.id.cus_2)
    TextView cus_2;
    @BindView(R.id.cus_3)
    TextView cus_3;
    @BindView(R.id.cus_4)
    TextView cus_4;
    @BindView(R.id.cus_5)
    TextView cus_5;
    @BindView(R.id.seller_1)
    TextView seller_1;
    @BindView(R.id.seller_2)
    TextView seller_2;
    @BindView(R.id.seller_3)
    TextView seller_3;
    @BindView(R.id.seller_4)
    TextView seller_4;
    @BindView(R.id.seller_5)
    TextView seller_5;

    @BindView(R.id.actual_progress_bar)
    ProgressBar actual_progress_bar;
    @BindView(R.id.actual_progress_value)
    TextView actual_progress_value;
    @BindView(R.id.initial_progress_bar)
    ProgressBar initial_progress_bar;
    @BindView(R.id.initial_progress_value)
    TextView initial_progress_value;
    @BindView(R.id.current_progress_bar)
    ProgressBar current_progress_bar;
    @BindView(R.id.current_progress_value)
    TextView current_progress_value;

    @BindView(R.id.inventory_pieChart)
    PieChart pieChart;

    @BindView(R.id.actual_projection_value)
    TextView actual_projection_value;
    @BindView(R.id.initial_projection_value)
    TextView initial_projection_value;
    @BindView(R.id.current_projection_value)
    TextView current_projection_value;

    @BindView(R.id.month_1)
    TextView month_1;
    @BindView(R.id.month_3)
    TextView month_3;
    @BindView(R.id.month_6)
    TextView month_6;
    @BindView(R.id.year)
    TextView year;


    @BindView(R.id.head_title)
    TextView head_title;


    Context context;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Access the Context here
        context = getContext();
    }

    public Graph_() {


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Objects.requireNonNull(((MainActivity_B2C) requireActivity()).getSupportActionBar()).hide();
        getActivity().findViewById(R.id.app_bar).setVisibility(View.GONE);
    }

    // TODO: Rename and change types and number of parameters
    public static Graph_ newInstance(String param1, String param2) {
        Graph_ fragment = new Graph_();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.graph, container, false);
        ButterKnife.bind(this, v);
        Globals.CURRENT_CLASS = getClass().getName();
        setSelectedBackground(month_1, month_3, month_6, year);
        head_title.setText("Reports");
        hideToolbarMenu();

        setDefaults();

        setupPieChart();
        addData();
        setProjectionData("1");
        loadTop5Customer();

        return v;
    }

    public void hideToolbarMenu() {
        //  getSupportActionBar().hide();


        relativeCalView.setVisibility(View.GONE);
        relativeInfoView.setVisibility(View.GONE);
        filterView.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        new_quatos.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
    }

    private void setDefaults() {

        month_1.setOnClickListener(this);
        month_3.setOnClickListener(this);
        month_6.setOnClickListener(this);
        year.setOnClickListener(this);


    }


    /************* Inventory PieChart ****************/
    public static final int[] COLORS = {
            rgb("#FFA63D"), rgb("#4A79E4"), rgb("#95B5FF"), rgb("#3498db")
    };

    float time[] = {37, 14, 100 - (37 + 14)};
    String activity[] = {"Slow Moving", "Fast Moving", "Non Moving"};

    // CircularProgressIndicator circularProgress;
    private void setupPieChart() {

        //pupulating list of PieEntires
        List<PieEntry> pieEntires = new ArrayList<>();
        for (int i = 0; i < time.length; i++) {
            pieEntires.add(new PieEntry(time[i], activity[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntires, "");
        dataSet.setColors(COLORS);
        PieData data = new PieData(dataSet);
        //Get the chart
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.setCenterText("50% \n ");
        pieChart.setDrawEntryLabels(false);
        pieChart.setContentDescription("");
        //pieChart.setDrawMarkers(true);
        //pieChart.setMaxHighlightDistance(34);
        pieChart.setEntryLabelTextSize(12);
        //pieChart.setHoleRadius(75);

        //legend attributes
        Legend legend = pieChart.getLegend();
        //legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12);
        legend.setFormSize(20);


    }


    private float[] yData = {37, 14, 100 - (37 + 14)};
    private String[] xData = {"Slow Moving", "Fast Moving", "Non Moving"};

    private void addData() {
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new PieEntry(yData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1,
                "Market Share");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);


        // instantiate pie data object now
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        // update pie chart
        pieChart.invalidate();
    }


    /***************** To 5 Item data *******************************/

    public static ArrayList<Top5Item> topItem_5 = new ArrayList<>();

    private void loadTop5Item() {
        HashMap<String, String> hd = new HashMap<>();
        hd.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<Top5ItemResponse> call = NewApiClient.getInstance().getApiService().getTop5Items(hd);
        call.enqueue(new Callback<Top5ItemResponse>() {
            @Override
            public void onResponse(Call<Top5ItemResponse> call, Response<Top5ItemResponse> response) {
                if (response != null) {
                    if (response.body().getValue().size() > 0) {
                        topItem_5.clear();
                        topItem_5.addAll(response.body().getValue());
                        ItemChart();

                    } else {
                        Toast.makeText(getContext(), "No data Found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Top5ItemResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void ItemChart() {
        float setItemData[];

        String sellItems[];

        sellItems = new String[topItem_5.size()];
        setItemData = new float[topItem_5.size()];
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < topItem_5.size(); i++) {
            sellItems[i] = topItem_5.get(i).getItemCode();

            setItemData[i] = Float.valueOf(topItem_5.get(i).getTotal());
            entries.add(new BarEntry((float) i, setItemData[i]));
            if (i == 0)
                seller_1.setText(sellItems[0]);
            else if (i == 1)
                seller_2.setText(sellItems[1]);
            else if (i == 2)
                seller_3.setText(sellItems[2]);
            else if (i == 3)
                seller_4.setText(sellItems[3]);
            else if (i == 4)
                seller_5.setText(sellItems[4]);

        }
        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(selling_barChart, selling_barChart.getAnimator(), selling_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(20f);
        selling_barChart.setRenderer(roundedBarChartRenderer);

        selling_barChart.setDrawBarShadow(false);
        selling_barChart.setDrawValueAboveBar(false);
        selling_barChart.getDescription().setEnabled(false);
        selling_barChart.setDrawGridBackground(false);


        selling_barChart.getAxisRight().setEnabled(false);
        Legend legend = selling_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(entries, " ");
        barDataSet.setColor(context.getResources().getColor(R.color.colorPrimary));
        barDataSet.setDrawValues(false);
        dataSets.add(barDataSet);


        BarData data = new BarData(dataSets);
        data.setBarWidth(0.25f);
        data.setValueTextColor(context.getResources().getColor(R.color.colorPrimary));
        selling_barChart.setData(data);
        selling_barChart.setFitBars(false);
        selling_barChart.invalidate();


        XAxis xaxis = selling_barChart.getXAxis();
        xaxis.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(0.5f);
        xaxis.setGranularityEnabled(true);
        xaxis.setDrawLabels(false);    // make changes bottom txt
        xaxis.setDrawAxisLine(false);

        selling_barChart.setTouchEnabled(false);
        selling_barChart.setDrawBarShadow(false);


        //hide grid lines
        selling_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        selling_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        selling_barChart.getDescription().setEnabled(false);

        //add animation
        selling_barChart.animateY(3000);


        //draw chart
        selling_barChart.invalidate();

    }


    /**
     * Top  5 customer
     **/

    public static ArrayList<Team> topSeller_5 = new ArrayList<>();

    private void loadTop5Customer() {
        HashMap<String, String> hd = new HashMap<>();
        hd.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<Top5CustomerResponse> call = NewApiClient.getInstance().getApiService().getTop5Customer(hd);
        call.enqueue(new Callback<Top5CustomerResponse>() {
            @Override
            public void onResponse(Call<Top5CustomerResponse> call, Response<Top5CustomerResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getValue().size() > 0) {
                        topSeller_5.clear();
                        topSeller_5.addAll(response.body().getValue());
                        setCustomergraph();

                    } else {
                        Toast.makeText(getContext(), "No data Found", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Top5CustomerResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        loadTop5Item();
    }

    private void setCustomergraph() {
        String customers[];
        float setData[];
        customers = new String[topSeller_5.size()];
        setData = new float[topSeller_5.size()];
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < topSeller_5.size(); i++) {
            customers[i] = topSeller_5.get(i).getEmpName();

            setData[i] = Float.valueOf(topSeller_5.get(i).getId());
            entries.add(new BarEntry((float) i, setData[i]));

            if (i == 0)
                cus_1.setText(customers[0]);
            else if (i == 1)
                cus_2.setText(customers[1]);
            else if (i == 2)
                cus_3.setText(customers[2]);
            else if (i == 3)
                cus_4.setText(customers[3]);
            else if (i == 4)
                cus_5.setText(customers[4]);
        }

        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(20f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(entries, " ");
        barDataSet.setColor(context.getResources().getColor(R.color.colorPrimary));
        barDataSet.setDrawValues(false);
        dataSets.add(barDataSet);


        BarData data = new BarData(dataSets);
        data.setBarWidth(0.25f);
        data.setValueTextColor(Color.BLACK);
        customer_barChart.setData(data);
        customer_barChart.setFitBars(false);
        customer_barChart.invalidate();


        XAxis xaxis = customer_barChart.getXAxis();
        xaxis.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(0.5f);
        xaxis.setGranularityEnabled(true);
        xaxis.setDrawLabels(false);    // make changes bottom txt
        xaxis.setDrawAxisLine(false);

        customer_barChart.setTouchEnabled(false);
        customer_barChart.setDrawBarShadow(false);


        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);

        //add animation
        customer_barChart.animateY(3000);


        //draw chart
        customer_barChart.invalidate();


    }


    /**
     * Three rounded projection data
     **/


    String initialProj;
    String actualColl;
    String currenProj;

    private void setProjectionData(String month) {


        SalesEmployeeItem salesEmployeeItem = new SalesEmployeeItem();
        salesEmployeeItem.setSalesEmployeeCode(Prefs.getString(Globals.SalesEmployeeCode, ""));
        salesEmployeeItem.setMonth(month);
        Call<CounterResponse> call = NewApiClient.getInstance().getApiService().projectiondata(salesEmployeeItem);
        call.enqueue(new Callback<CounterResponse>() {
            @Override
            public void onResponse(Call<CounterResponse> call, Response<CounterResponse> response) {
                if (response != null) {
                    initialProj = response.body().getValue().get(0).getAmount();
                    currenProj = response.body().getValue().get(0).getSale();
                    initial_projection_value.setText(initialProj);
                    current_projection_value.setText(currenProj);
                    setProjectionGraph(initialProj, currenProj);

                }
            }

            @Override
            public void onFailure(Call<CounterResponse> call, Throwable t) {

            }
        });


    }


    private void setProjectionGraph(String initialProj, String currenProj) {
        Calendar cal = Calendar.getInstance();
        int monthMaxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        int currentDays = Integer.parseInt(dateFormat.format(cal.getTime()));
        if (currentDays > 1)
            currentDays = currentDays - 1;

        actualColl = String.valueOf(Float.parseFloat(currenProj) / currentDays * monthMaxDays);

        actual_projection_value.setText(actualColl);


        float actPer = Float.parseFloat(actualColl) / Float.parseFloat(initialProj);
        actPer = actPer * 100;
        float initPer = Float.parseFloat(initialProj) / Float.parseFloat(String.valueOf(currentDays)) * 100;


        actual_progress_bar.setProgress((int) actPer);
        actual_progress_value.setText("" + (int) actPer + "%");

        initial_progress_bar.setProgress((int) initPer);
        initial_progress_value.setText("" + (int) initPer + "%");

        if (Float.parseFloat(currenProj) > Float.parseFloat(initialProj)) {
            float per = Float.parseFloat(currenProj) - Float.parseFloat(initialProj);
            per = per / Float.parseFloat(initialProj);
            per = per * 100;
            current_progress_bar.setProgress((int) per);
            current_progress_value.setText("" + (int) per + "%");
            current_progress_bar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

        } else {
            float per = Float.parseFloat(initialProj) - Float.parseFloat(currenProj);
            per = per / Float.parseFloat(initialProj);
            per = per * 100;
            current_progress_bar.setProgress((int) per);
            current_progress_value.setText("" + (int) per + "%");
            current_progress_bar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.month_1:

                setSelectedBackground(month_1, month_3, month_6, year);
                setProjectionData("1");
                break;
            case R.id.month_3:

                setSelectedBackground(month_3, month_1, month_6, year);
                setProjectionData("3");
                break;
            case R.id.month_6:

                setSelectedBackground(month_6, month_3, month_1, year);
                setProjectionData("6");
                break;
            case R.id.year:

                setSelectedBackground(year, month_3, month_6, month_1);
                setProjectionData("12");
                break;
        }
    }

    private void setSelectedBackground(TextView month_1, TextView month_3, TextView month_6, TextView year)
           {
        month_1.setBackground(getResources().getDrawable(R.drawable.text_selected_rounded_back));
        month_1.setTextColor(getResources().getColor(R.color.white));
        month_3.setBackground(getResources().getDrawable(R.drawable.text_rounded_back));
        month_3.setTextColor(getResources().getColor(R.color.black));
        month_6.setBackground(getResources().getDrawable(R.drawable.text_rounded_back));
        month_6.setTextColor(getResources().getColor(R.color.black));
        year.setBackground(getResources().getDrawable(R.drawable.text_rounded_back));
        year.setTextColor(getResources().getColor(R.color.black));
          }
}