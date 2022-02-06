package sg.edu.rp.c346.id20007998.elderlyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Registration extends AppCompatActivity {
    EditText edName,edContact,edPassword,edGNum;
    RadioGroup rgGender,rgSymptom;
    Button btnRegister;
    RadioButton rbF,rbM,rgsL,rgsM,rgsH;
    DatePicker datePicker;

    AsyncHttpClient asyncHttpClient;
    RequestParams requestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edName=findViewById(R.id.edName);
        edContact=findViewById(R.id.edContact);
        edPassword=findViewById(R.id.edPassword);
        edGNum=findViewById(R.id.edGuardianNum);
        rgGender=findViewById(R.id.rgGender);
        rbF=findViewById(R.id.rgGenderF);
        rbM=findViewById(R.id.rgGenderM);
        rgSymptom=findViewById(R.id.rgSymptom);
        rgsL=findViewById(R.id.btnGrp1);
        rgsM=findViewById(R.id.btnGrp2);
        rgsH=findViewById(R.id.btnGrp3);
        btnRegister=findViewById(R.id.btnRegister);
        datePicker=findViewById(R.id.datePicker);

        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numCheck=edGNum.getText().toString();
                String GET_GUARDIANNUM_URL = "https://c200team1.myapplicationdev.com/get_GuardianNum.php?contactNum="+numCheck;

                asyncHttpClient.get(GET_GUARDIANNUM_URL, requestParams, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject raw_response) {
                        super.onSuccess(statusCode, headers, raw_response);


                        try {
                            JSONArray response = raw_response.getJSONArray("record");
                            for(int i=0; i < response.length(); i++) {
                                JSONObject obj = (JSONObject)response.get(i);
                                String GuardianNum = obj.getString("ContactNum");

                                if (!GuardianNum.isEmpty()){
                                    String GuardianID = obj.getString("guardianId");
                                    String name=edName.getText().toString();
                                    String contact=edContact.getText().toString();
                                    String password=edPassword.getText().toString();
                                    String symptom=" ";
                                    String gender=" ";

                                    if (rgGender.getCheckedRadioButtonId() ==rbF.getId()){
                                        gender="f";
                                    }else if (rgGender.getCheckedRadioButtonId() ==rbM.getId()){
                                        gender="m";
                                    }

                                    if (rgSymptom.getCheckedRadioButtonId() ==rgsL.getId()){
                                        symptom="L";
                                    }else if (rgSymptom.getCheckedRadioButtonId() ==rgsM.getId()){
                                        symptom="M";
                                    }else if (rgSymptom.getCheckedRadioButtonId() ==rgsH.getId()) {
                                        symptom = "H";
                                    }
                                    int dateD=datePicker.getDayOfMonth();
                                    int dateM=datePicker.getMonth();
                                    int dateY=datePicker.getYear();

                                    String date=dateY+"-"+dateM+"-"+dateD;

                                    String INSERT_URL = "https://c200team1.myapplicationdev.com/Elderly_insert.php?name="+name+"&password="+password+"&birthday="+date+"&phone="+contact+"&sympthomLvl="+symptom+"&guardianId="+GuardianID;

                                    asyncHttpClient.get(INSERT_URL, requestParams, new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject raw_response) {
                                            super.onSuccess(statusCode, headers, raw_response);


                                            try {
                                                JSONArray response2 = raw_response.getJSONArray("record");
                                                for(int i=0; i < response2.length(); i++) {
                                                    JSONObject obj = (JSONObject)response2.get(i);
                                                }



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });


                                }else{
                                    Toast.makeText(getApplicationContext(),"You did not fill up a valid guardian number!",Toast.LENGTH_SHORT).show();

                                }



                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });




                if(edName.getText().toString().trim()!="" && edContact.getText().toString().trim()!="" && rgGender.getCheckedRadioButtonId() != -1
                && rgSymptom.getCheckedRadioButtonId() != -1 && edPassword.getText().toString().trim()!="" && edGNum.getText().toString().trim()!=""){
                    Toast.makeText(getApplicationContext(),"Successfully Registered! Now direct to Login page",Toast.LENGTH_SHORT).show();
                    Intent goLogin=new Intent(Registration.this, MainActivity.class);
                    startActivity(goLogin);
                }else{
                    Toast.makeText(getApplicationContext(),"You have yet to fill up all information",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}