package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.BottomSheetAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.ChangeTeam;
import com.cinntra.ledure.interfaces.FragmentRefresher;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomSheetFragment extends BottomSheetDialogFragment implements BottomSheetAdapter.ItemListener {

    @BindView(R.id.database_listing)
    RecyclerView database_listing;
    @BindView(R.id.cross)
    ImageView cross;
    FragmentRefresher fragmentRefresher;
    ChangeTeam changeTeam;


    public BottomSheetFragment(Dashboard dashboard) {
        this.changeTeam = dashboard;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alert_databse_listing, container, false);

        ButterKnife.bind(this,v);
        fragmentRefresher =(FragmentRefresher)getActivity();


        database_listing.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        database_listing.setAdapter(new BottomSheetAdapter(getActivity(),Dashboard.teamList_Hearchi,this));

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }


    @Override
    public void onClickAt(int position) {
        Prefs.putString(Globals.SalesEmployeeCode,Dashboard.teamList_Hearchi.get(position).getSalesEmployeeCode());
        Prefs.putString(Globals.SalesEmployeeName,Dashboard.teamList_Hearchi.get(position).getSalesEmployeeName());

        Globals.TeamEmployeeID = Dashboard.teamList_Hearchi.get(position).getId().toString();
        Prefs.putString(Globals.Role,Dashboard.teamList_Hearchi.get(position).getRole());
        changeTeam.changeTeam(Dashboard.teamList_Hearchi.get(position).getSalesEmployeeName());
        fragmentRefresher.onRefresh();
        dismiss();
    }
}