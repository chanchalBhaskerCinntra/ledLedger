package com.cinntra.ledure.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeadBottomsheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{


    private static final String TAG = "TAg";
    @BindView(R.id.whatsapp_view)
    TextView whatsapp_view;
    @BindView(R.id.callView)
    TextView callView;
    @BindView(R.id.messageView)
    TextView messageView;
    @BindView(R.id.emailView)
    TextView emailView;
    @BindView(R.id.btnCancelDialog)
    TextView btnCancelDialog;
    Context context;

    LeadValue leadValue;
    public LeadBottomsheetFragment(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.bottom_sheet,container,false);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            leadValue = b.getParcelable(Globals.LeadDetails);
        }

        ButterKnife.bind(this,v);
        emailView.setOnClickListener(this);
        whatsapp_view.setOnClickListener(this);
        messageView.setOnClickListener(this);
        callView.setOnClickListener(this);
        btnCancelDialog.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.whatsapp_view:

                openWhatsapp(leadValue.getPhoneNumber());

                break;
            case R.id.callView:
                if(!leadValue.getPhoneNumber().isEmpty()){


                    Dexter.withActivity(getActivity())
                            .withPermission(Manifest.permission.CALL_PHONE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    // permission is granted, open the camera
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:" + leadValue.getPhoneNumber()));
                                    context.startActivity(Intent.createChooser(callIntent, "Choose App"));
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                    // check for permanent denial of permission
                                    if (response.isPermanentlyDenied()) {
                                        // navigate user to app settings
                                        Toast.makeText(context,"Enable permission from app Setting",Toast.LENGTH_LONG).show();

                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                }else {
                    Toast.makeText(context,"Mobile number not found",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.messageView:
                if(!leadValue.getPhoneNumber().isEmpty()) {
                    /*Uri smsUri = Uri.parse("tel:" + leadValue.getPhoneNumber());
                    Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                    intent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(intent);*/
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", leadValue.getPhoneNumber(), null)));

                }else{
                    Toast.makeText(getContext(),"No Mobile Number Found", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.emailView:
               if(!leadValue.getEmail().isEmpty()){
                   Intent intent = new Intent(Intent.ACTION_SENDTO);
                   intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                   intent.putExtra(Intent.EXTRA_EMAIL,  new String[]{leadValue.getEmail()});
                   intent.putExtra(Intent.EXTRA_SUBJECT, "");
                   /*if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                       startActivity(intent);
                   }*/
                   startActivity(intent);
               }else{
                   Toast.makeText(getContext(),"No Email Address Found", Toast.LENGTH_SHORT).show();
               }

                break;
            case R.id.btnCancelDialog:
                dismiss();
                break;
        }
    }

    private void openWhatsapp(String phoneNumber) {
        try {
          //  phoneNumber = phoneNumber.replace(" ", "").replace("+", "");

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", "91"+phoneNumber+"@s.whatsapp.net");
            context.startActivity(sendIntent);

        } catch(Exception e) {
//            Log.e(TAG, "ERROR_OPEN_MESSANGER"+e.toString());
        }
    }
}
