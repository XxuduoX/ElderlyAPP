package sg.edu.rp.c346.id20007998.elderlyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;

public class FallAlert extends AppCompatActivity implements SensorEventListener {

    private  static final String TAG="FallAlert";

    private SensorManager sensorManager;
    Sensor accelerometer;
    TextView displayX,displayY,displayZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_alert);

        displayX=findViewById(R.id.tvX);
        displayY=findViewById(R.id.tvY);
        displayZ=findViewById(R.id.tvZ);

        Log.d(TAG,"onCreate: Initialising Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(FallAlert.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG,"onCreate: Registered accelerometer listener");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG,"onSensorChange:X:"+event.values[0]+ "onSensorChange:Y:"+event.values[1]
                +"onSensorChange:Z:"+event.values[2]);
        displayX.setText("Value X: "+event.values[0]);
        displayY.setText("Value Y: "+event.values[1]);
        displayZ.setText("Value Z: "+event.values[2]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
}