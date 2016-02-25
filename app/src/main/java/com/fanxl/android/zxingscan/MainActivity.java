package com.fanxl.android.zxingscan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fanxl.zxing.CaptureActivity;

public class MainActivity extends AppCompatActivity {

    private TextView main_scan_result;
    private StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sb = new StringBuffer();
        main_scan_result = (TextView)findViewById(R.id.main_scan_result);
    }

    public void scan(View view){
        Intent scan = new Intent(this, CaptureActivity.class);
        startActivityForResult(scan, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == CaptureActivity.SCAN_RESULT_CODE){
            Bundle extras = data.getExtras();
            if(extras!=null){
                String scanResult = extras.getString("result");
                sb.append(scanResult).append("\n");
                Toast.makeText(this, scanResult, Toast.LENGTH_SHORT).show();
                main_scan_result.setText(sb.toString());
            }
        }
    }
}
