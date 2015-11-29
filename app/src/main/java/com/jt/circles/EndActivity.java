package com.jt.circles;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Jerry on 2015-11-28.
 */
public class EndActivity extends AppCompatActivity{

    Typeface quicksand;
    @Override
    public void onCreate(Bundle SavedInstanceState)
    {
        super.onCreate(SavedInstanceState);
        Intent called = getIntent();
        String output = called.getStringExtra("score");
        quicksand = Typeface.createFromAsset(getAssets(), "Quicksand-Bold.ttf");

        setContentView(R.layout.activity_end);

        TextView gameOver = (TextView) findViewById(R.id.game_over);
        TextView score = (TextView) findViewById(R.id.score);
        Button again = (Button) findViewById(R.id.play_again);

        gameOver.setTextSize(50);
        gameOver.setTypeface(quicksand);
        score.setTextSize(25);
        score.setTypeface(quicksand);
        score.setText("score: " + output);
        again.setTransformationMethod(null);
        again.setTextSize(35);
        again.setTypeface(quicksand);


        final Intent playIntent = new Intent(this, GameActivity.class);
        playIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        again.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(playIntent);
            }
        });

    }
}
