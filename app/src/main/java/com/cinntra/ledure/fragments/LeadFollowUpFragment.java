package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.MessageAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.FragmentRefresher;
import com.cinntra.ledure.model.ChatModel;
import com.cinntra.ledure.model.ChatResponse;
import com.cinntra.ledure.model.FollowUpData;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadFollowUpFragment extends Fragment implements View.OnClickListener, FragmentRefresher {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.send)
    ImageView send;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.createmsz)
    RelativeLayout createmsz;
    String EmpId;
    ArrayList<ChatModel> messagelist = new ArrayList<>();
    LeadValue opportunityItem;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            opportunityItem =(LeadValue) b.getParcelable(Globals.Lead_Data);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.leadfollowupfragment, container, false);
        ButterKnife.bind(this,v);
        head_title.setText(getContext().getResources().getString(R.string.follow_up));

       /* send.setVisibility(View.VISIBLE);
        send.setImageResource(R.drawable.ic_baseline_add_circle_24);*/
        createmsz.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams params = createmsz.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = 120;
        createmsz.setLayoutParams(params);

        EmpId =  Prefs.getString(Globals.EmployeeID, "");
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if(Globals.checkInternet(getContext()))
        {

            callApi(opportunityItem.getId());
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(sendmessage_text.getText().toString().trim().length()>0){
                   *//* LeadChatModel chatModel = new LeadChatModel();
                    chatModel.setMessage(sendmessage_text.getText().toString().trim());
                    chatModel.setEmpId(Prefs.getString(Globals.EmployeeID,""));
                    chatModel.setEmpName(Prefs.getString(Globals.USER_NAME,""));
                    chatModel.setOppId(opportunityItem.getId().toString());
                    chatModel.setUpdateDate(Globals.getTodaysDate());
                    chatModel.setUpdateTime(Globals.getTCurrentTime());*//*
                    ChatModel chatModel = new ChatModel();
                    chatModel.setMessage(sendmessage_text.getText().toString().trim());
                    chatModel.setEmpId(Prefs.getString(Globals.EmployeeID,""));
                    chatModel.setEmpName(Prefs.getString(Globals.USER_NAME,""));
                    chatModel.setOppId(opportunityItem.getId().toString());
                    chatModel.setUpdateDate(new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date()));
                    chatModel.setUpdateTime(new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date()));
                    chatModel.setSourceType("Lead");
                    callcreateApi(chatModel);
                    Globals.hideKeybaord(sendmessage_text,getContext());
                    sendmessage_text.setText("");

                }*/

                showBottomSheet(opportunityItem.getId(),opportunityItem.getCompanyName());
            }
        });
        createmsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showBottomSheet(opportunityItem.getId(),opportunityItem.getCompanyName());
            }
        });
        back_press.setOnClickListener(this);


        return v;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_press:

                getActivity().onBackPressed();
                break;

        }
    }

    private void callApi(Integer sequentialNo) {
        FollowUpData followUpData = new FollowUpData();
        followUpData.setEmp(Integer.parseInt(Prefs.getString(Globals.EmployeeID,"")));
        followUpData.setSourceType("Lead");
        followUpData.setSourceID(sequentialNo.toString());
        Call<ChatResponse> call = NewApiClient.getInstance().getApiService().getAllLeadChat(followUpData);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {

                if(response.body()!=null){
                    messagelist.clear();
                    messagelist.addAll(response.body().getData());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    MessageAdapter messageAdapter = new MessageAdapter(getContext(),messagelist);
                   layoutManager.setStackFromEnd(true);
                   // recyclerView.smoothScrollToPosition(recyclerView.getBottom());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(messageAdapter);

                    messageAdapter.notifyDataSetChanged();
                    if(messageAdapter.getItemCount()>0){
                        no_datafound.setVisibility(View.GONE);
                    }else{
                        no_datafound.setVisibility(View.VISIBLE);
                    }

                }
            }
            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {

            }
        });
//        callContactApi(opportunityItem.getCardCode());

    }

    private void callcreateApi(ChatModel chatModel) {

        Call<ChatResponse> call = NewApiClient.getInstance().getApiService().createChat(chatModel);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {

                if(response.body()!=null){
                    callApi(opportunityItem.getId());
                }
            }
            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {

            }
        });
//        callContactApi(opportunityItem.getCardCode());

    }



    private void showBottomSheet(int id,String name)
    {

        ReminderSelectionSheet bottomSheetFragment = new ReminderSelectionSheet(id,name, com.cinntra.ledure.fragments.LeadFollowUpFragment.this);
        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());

    }

    @Override
    public void onRefresh() {
        callApi(opportunityItem.getId());
    }
}
