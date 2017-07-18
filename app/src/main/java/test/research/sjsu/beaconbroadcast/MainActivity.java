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
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    AdvertiseData mAdvertiseData;
    AdvertiseData.Builder mAdvertiseDataBuilder;
    byte[] Data = new byte[10];
    ParcelUuid mServiceDataUUID;
    AdvertiseSettings mAdvertiseSettings;
    AdvertiseSettings.Builder mAdvertiseSettingBuilder = new AdvertiseSettings.Builder();
    Button BroadcastButton;
    Button StopBroadcastButton;
    Switch mSwitch;
    Boolean switchState;
    ParcelUuid mServiceUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        Data = hexStringToByteArray("12345678901234567890");
        mServiceDataUUID = ParcelUuid.fromString("00009208-0000-1000-8000-00805F9B34FB");
        // mAdvertiseDataBuilder.addServiceData(mServiceDataUUID,Data);
        mAdvertiseSettingBuilder.setAdvertiseMode(1);
        mAdvertiseSettingBuilder.setTimeout(0);
        mAdvertiseSettingBuilder.setTxPowerLevel(2);
        mAdvertiseSettingBuilder.setConnectable(true);
        mAdvertiseSettings = mAdvertiseSettingBuilder.build();
        mSwitch = (Switch) findViewById(R.id.Switch);
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

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    private AdvertiseCallback mCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);

        }
    };
    public void startAdvertise(){
        switchState = mSwitch.isChecked();
        if(switchState.booleanValue()){
            mAdvertiseDataBuilder = new AdvertiseData.Builder();
            mAdvertiseDataBuilder.addServiceData(mServiceDataUUID,Data);
            mServiceUUID = ParcelUuid.fromString("00001830-0000-1000-8000-00805F9B34FB");
            mAdvertiseDataBuilder.setIncludeDeviceName(true);
            mAdvertiseDataBuilder.setIncludeTxPowerLevel(true);
            mAdvertiseDataBuilder.addServiceUuid(mServiceUUID);
            mAdvertiseData = mAdvertiseDataBuilder.build();
        }
        else{
            mAdvertiseDataBuilder = new AdvertiseData.Builder();
            mServiceUUID = ParcelUuid.fromString("00001829-0000-1000-8000-00805F9B34FB");
            mAdvertiseDataBuilder.setIncludeDeviceName(true);
            mAdvertiseDataBuilder.setIncludeTxPowerLevel(true);
            mAdvertiseDataBuilder.addServiceUuid(mServiceUUID);
            mAdvertiseData = mAdvertiseDataBuilder.build();
        }
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
