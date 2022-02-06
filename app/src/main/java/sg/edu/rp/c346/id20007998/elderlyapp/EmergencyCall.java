package sg.edu.rp.c346.id20007998.elderlyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class EmergencyCall extends AppCompatActivity {
    Button emergencyCall;
    TextView testShowCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    AsyncHttpClient asyncHttpClient;
    RequestParams requestParams;


    String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
        emergencyCall=findViewById(R.id.btnCall);
        testShowCurrentLocation=findViewById(R.id.testLocationShow);

        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();

        //Initialize fusedLocationProviderClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        String userID=getIntent().getStringExtra("USER_ID");

        String CONTACT_URL = "https://c200team1.myapplicationdev.com/get_contact.php?elderlyId="+userID;

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asyncHttpClient.get(CONTACT_URL, requestParams, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject raw_response) {
                        super.onSuccess(statusCode, headers, raw_response);
                        try {
                            JSONArray response = raw_response.getJSONArray("record");
                            for(int i=0; i < response.length(); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                String emergencyNumber = obj.getString("number");
                                Toast.makeText(getApplicationContext(), "contact number is:" + emergencyNumber , Toast.LENGTH_SHORT).show();
                                //call emergency contact
                                if(ActivityCompat.checkSelfPermission(EmergencyCall.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {

                                    Intent call=new Intent(Intent.ACTION_CALL);
                                    call.setData(Uri.parse("tel:"+emergencyNumber));
                                    startActivity(call);
                                }else{
                                    ActivityCompat.requestPermissions(EmergencyCall.this,new String[]{Manifest.permission.CALL_PHONE},1);

                                }

                                //check permission to get current location
                                if(ActivityCompat.checkSelfPermission(EmergencyCall.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                                    mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Location> task) {
                                            //Initialize Location
                                            Location location=task.getResult();
                                            if(location!=null) {
                                                try {
                                                    //initialize geoCoder
                                                    Geocoder geocoder = new Geocoder(EmergencyCall.this, Locale.getDefault());
                                                    //Initialize address list
                                                    List<Address> addresses = geocoder.getFromLocation(
                                                            location.getLatitude(), location.getLongitude(), 1
                                                    );
                                                    //set address on textview
                                                    testShowCurrentLocation.setText(Html.fromHtml("Latitude: " + addresses.get(0).getLatitude())
                                                            + "Longitude: " + addresses.get(0).getLongitude() + "Country: " + addresses.get(0).getCountryName() +"Address: " + addresses.get(0).getAddressLine(0));

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            //check permission to send message
                                            if(ActivityCompat.checkSelfPermission(EmergencyCall.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                                                //Initialize sms manager
                                                String address=testShowCurrentLocation.getText().toString();
                                                SmsManager sendMessage=SmsManager.getDefault();
                                                sendMessage.sendTextMessage(emergencyNumber,null,address,null,null);


                                            }else{
                                                ActivityCompat.requestPermissions(EmergencyCall.this,new String[]{Manifest.permission.SEND_SMS},3);

                                            }
                                        }

                                    });



                                }else {
                                    ActivityCompat.requestPermissions(EmergencyCall.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });




            }


        });
    }
}