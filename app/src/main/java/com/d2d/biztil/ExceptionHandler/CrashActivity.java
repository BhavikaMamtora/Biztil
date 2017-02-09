package com.d2d.biztil.ExceptionHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.d2d.biztil.R;

/**
 * Created by Bhavika on 29-Sep-16.
 */
public class CrashActivity extends AppCompatActivity {

    TextView act_crash_textView;
    Button act_crash_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_crash);


        act_crash_textView =(TextView)findViewById(R.id.act_crash_textView);
        act_crash_textView.setText(getIntent().getStringExtra("error"));

        act_crash_btn = (Button)findViewById(R.id.act_crash_btn);
        act_crash_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(getIntent().getStringExtra("error")));
                startActivity(Intent.createChooser(sharingIntent,"Share using"));
            }
        });


    }
}