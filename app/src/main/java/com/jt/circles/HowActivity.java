package com.jt.circles;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Jerry on 2015-11-28.
 */
public class HowActivity extends AppCompatActivity
{
    Typeface quicksand;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_how);
        quicksand = Typeface.createFromAsset(getAssets(), "Quicksand-Bold.ttf");

        //LinearLayout outer = (LinearLayout) findViewById(R.id.outer);



        final TextView title = (TextView) findViewById(R.id.title1);
        final TextView instructions = (TextView) findViewById(R.id.instructions);
        //Button back = (Button) findViewById(R.id.back);

        if (title != null) {
            title.setTypeface(quicksand);
            title.setTextSize(50);
        }
        if (instructions != null) {
            instructions.setTypeface(quicksand);
            instructions.setTextSize(25);
        }

        /*back.setTransformationMethod(null);
        back.setTextSize(25);
        back.setTypeface(quicksand);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                onDestroy();
            }
        });*/
    }
}
