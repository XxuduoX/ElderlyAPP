package sg.edu.rp.c346.id20007998.elderlyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    TextView goRegistration;
    EditText edUsername,edPassword;

    AsyncHttpClient asyncHttpClient;
    RequestParams requestParams;


    String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin=findViewById(R.id.btnLogin);
        goRegistration=findViewById(R.id.Registration);
        edPassword=findViewById(R.id.TVpassword);
        edUsername=findViewById(R.id.TVusername);

        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String entered_username=edUsername.getText().toString();
                String entered_password=edPassword.getText().toString();
                String LOGIN_URL = "https://c200team1.myapplicationdev.com/get_elderlyLoginInfo.php?username="+entered_username;
                asyncHttpClient.get(LOGIN_URL, requestParams, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject raw_response) {
                        super.onSuccess(statusCode, headers, raw_response);


                        try {
                            JSONArray response = raw_response.getJSONArray("record");
                            for(int i=0; i < response.length(); i++) {
                                JSONObject obj = (JSONObject)response.get(i);
                                String username = obj.getString("phone");
                                String password=obj.getString("password");
                                String user_id=obj.getString("id");


                                if (username.equals(entered_username)&&password.equals(entered_password)){
                                    Intent openMainMenu = new Intent(MainActivity.this, MainMenu.class);
                                    openMainMenu.putExtra("USER_ID", user_id);
                                    startActivity(openMainMenu);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Incorrect username or password"+username+password, Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });






            }
        });
        goRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Registration = new Intent(MainActivity.this, Registration.class);
                startActivity(Registration);

            }
        });

    }

}