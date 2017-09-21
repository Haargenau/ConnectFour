package com.example.martin.connectfour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Menue extends AppCompatActivity implements android.view.View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menue);

        Button btn;
        btn = (Button) this.findViewById(R.id.play);
        btn.setOnClickListener(this);

        RadioButton rd1;
        RadioButton rd2;
        rd1 = (RadioButton) this.findViewById(R.id.playerFirst);
        rd2 = (RadioButton) this.findViewById(R.id.computerFirst);
        rd1.setOnClickListener(this);
        rd2.setOnClickListener(this);
    }

    public void onClick(View v){
        int id = v.getId();

        if(id == R.id.play) {
            if(((RadioButton) findViewById(R.id.pvp)).isChecked()){
                Intent startPlayerActivity = new Intent(this, PlayerActivity.class);
                startActivity(startPlayerActivity);
            }
            else if(((RadioButton) findViewById(R.id.pvc)).isChecked()) {
                Intent startPlayActivity = new Intent(this, PlayActivity.class);
                int[] extraArray = new int[2];

                if (((RadioButton) findViewById(R.id.playerFirst)).isChecked()) {
                    extraArray[0] = 1;
                } else {
                    extraArray[0] = 2;
                }

                Spinner spn = (Spinner) findViewById(R.id.difficulty);
                String diff = String.valueOf(spn.getSelectedItem());
                switch (diff) {
                    case "easy as pi":
                        extraArray[1] = 2;
                        break;
                    case "easy":
                        extraArray[1] = 4;
                        break;
                    case "moderate":
                        extraArray[1] = 5;
                        break;
                    case "hard":
                        extraArray[1] = 6;
                        break;
                    case "nightmare":
                        extraArray[1] = 8;
                        break;
                }

                startPlayActivity.putExtra("extra", extraArray);
                startActivity(startPlayActivity);
            }
        }
    }
}
