package test.research.sjsu.beaconbroadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    AdvertiseData mAdvertiseData;
    AdvertiseData.Builder mAdvertiseDataBuilder;
    ParcelUuid mServiceDataUUID;
    AdvertiseSettings mAdvertiseSettings;
    AdvertiseSettings.Builder mAdvertiseSettingBuilder = new AdvertiseSettings.Builder();
    Button BroadcastButton;
    Button StopBroadcastButton;
    ParcelUuid mServiceUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BLESetUpAdvertiser();
        PrepareSettings();
        PrepareData("LOMO");
        BroadcastButton = (Button) findViewById(R.id.BroadcastButton);
        BroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAdvertise();
            }
        });
        StopBroadcastButton = (Button) findViewById(R.id.StopBraodcastButton);
        StopBroadcastButton.setVisibility(View.INVISIBLE);
        StopBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAdvertise();
            }
        });

    }

    private AdvertiseCallback mCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);

        }
    };
    public void PrepareSettings(){
        mAdvertiseSettingBuilder.setAdvertiseMode(1);
        mAdvertiseSettingBuilder.setTimeout(0);
        mAdvertiseSettingBuilder.setTxPowerLevel(2);
        mAdvertiseSettingBuilder.setConnectable(true);
        mAdvertiseSettings = mAdvertiseSettingBuilder.build();
    }
    public void BLESetUpAdvertiser(){
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
    }
    public void PrepareData(String str){
        mAdvertiseDataBuilder = new AdvertiseData.Builder();
        mServiceDataUUID = ParcelUuid.fromString("00009208-0000-1000-8000-00805F9B34FB");
        mAdvertiseDataBuilder.addServiceData(mServiceDataUUID,str.getBytes());
        mServiceUUID = ParcelUuid.fromString("00001830-0000-1000-8000-00805F9B34FB");
        mAdvertiseDataBuilder.setIncludeDeviceName(true);
        mAdvertiseDataBuilder.setIncludeTxPowerLevel(true);
        mAdvertiseDataBuilder.addServiceUuid(mServiceUUID);
        mAdvertiseData= mAdvertiseDataBuilder.build();
    }
    public void startAdvertise(){
        mBluetoothLeAdvertiser.startAdvertising(mAdvertiseSettings,mAdvertiseData,mCallback);
        BroadcastButton.setVisibility(View.INVISIBLE);
        StopBroadcastButton.setVisibility(View.VISIBLE);
    }
    public void stopAdvertise(){
        mBluetoothLeAdvertiser.stopAdvertising(mCallback);
        BroadcastButton.setVisibility(View.VISIBLE);
        StopBroadcastButton.setVisibility(View.INVISIBLE);
    }
}
