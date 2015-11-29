package com.jt.circles;

import android.content.Intent;
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
        how.setTextSize(25);
        how.setTypeface(quicksand);

        final Intent playIntent = new Intent(this, GameActivity.class);
        playIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        final Intent howIntent = new Intent(this, HowActivity.class);
        howIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);


        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                startActivity(playIntent);
            }
        });

        how.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                startActivity(howIntent);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

}
