package com.jt.circles;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Jerry on 2015-11-28.
 */
public class MenuActivity extends AppCompatActivity{

    Typeface quicksand;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        quicksand = Typeface.createFromAsset(getAssets(), "Quicksand-Bold.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView title = (TextView) findViewById(R.id.title);
        Button play = (Button) findViewById(R.id.play);
        Button how = (Button) findViewById(R.id.how);

        title.setTypeface(quicksand);
        title.setTextSize(100);
        play.setTransformationMethod(null);
        play.setTextSize(50);
        play.setTypeface(quicksand);
        how.setTransformationMethod(null);
        how.setTextSize(20);
        how.setTypeface(quicksand);

        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

            }
        });

        how.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

            }
        });
    }

}
