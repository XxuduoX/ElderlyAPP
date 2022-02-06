package sg.edu.rp.c346.id20007998.elderlyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {
    ImageButton GoEmergency,GoMap,GoFallAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        GoEmergency=findViewById(R.id.btnEmergencyCall);
        GoMap=findViewById(R.id.btnMapGuide);
        GoFallAlert=findViewById(R.id.btnTestOnlyFallAlert);

        String userid = getIntent().getStringExtra("USER_ID");

        GoEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openEmergencyMode = new Intent(MainMenu.this, EmergencyCall.class);
                openEmergencyMode.putExtra("USER_ID", userid);
                startActivity(openEmergencyMode);
            }
        });

        GoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMapMode = new Intent(MainMenu.this, MapGuide.class);
                startActivity(openMapMode);
            }
        });

        GoFallAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openFallAlert = new Intent(MainMenu.this, FallAlert.class);
                startActivity(openFallAlert);
            }
        });
    }

}