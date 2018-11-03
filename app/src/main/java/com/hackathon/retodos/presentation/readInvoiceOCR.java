package com.hackathon.retodos.presentation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.hackathon.retodos.R;

import static android.content.ContentValues.TAG;


public class readInvoiceOCR extends Fragment{

    private SharedPreferences prefs;
    private Button buttonStartScan;
    private Button buttonNext;
    private TextView textViewData;
    private TableLayout tableLayoutData;
    private static final int RC_OCR_CAPTURE = 9003;
    private EditText id, idType, vin, model, plate, color, date, country, number;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.read_invoice_ocr, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind_ui();
    }

    private void bind_ui(){
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        id =getView().findViewById(R.id.id);
        idType=getView().findViewById(R.id.idType);
        vin=getView().findViewById(R.id.vin);
        model=getView().findViewById(R.id.model);
        plate=getView().findViewById(R.id.plate);
        color=getView().findViewById(R.id.color);
        date=getView().findViewById(R.id.date);
        country=getView().findViewById(R.id.country);
        number=getView().findViewById(R.id.number);


        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("layout", R.layout.read_invoice_ocr);
        editor.apply();

        textViewData=getActivity().findViewById(R.id.textViewData);
        tableLayoutData=getActivity().findViewById(R.id.tableLayoutData);

        buttonNext = getActivity().findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!id.getText().toString().equals("") && !idType.getText().toString().equals("")
                        && !vin.getText().toString().equals("") && !model.getText().toString().equals("")
                        && !plate.getText().toString().equals("") && !color.getText().toString().equals("")
                        && !date.getText().toString().equals("") && !country.getText().toString().equals("")
                        && !number.getText().toString().equals("")){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("id", id.getText().toString());
                    editor.putString("idType", idType.getText().toString());
                    editor.putString("vin", vin.getText().toString());
                    editor.putString("model", model.getText().toString());
                    editor.putString("plate", plate.getText().toString());
                    editor.putString("color", color.getText().toString());
                    editor.putString("date", date.getText().toString());
                    editor.putString("country", country.getText().toString());
                    editor.putString("number", number.getText().toString());
                    editor.apply();
                    //((MainActivity)getActivity()).displaySelectedScreen(R.layout.fragment_scan_ownership_certificate_full_data);
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill all data", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonStartScan= getActivity().findViewById(R.id.buttonStart);
        buttonStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);
                intent.putExtra("owner", true);

                startActivityForResult(intent, RC_OCR_CAPTURE);

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Log.e("read", "data:"+data.toString());

                } else {
                    //statusMessage.setText(R.string.ocr_failure);
                    Log.e(TAG, "No Text captured, intent data is null");
                }
            } else {
                Log.e("tag acquisition", "msg: "+String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
