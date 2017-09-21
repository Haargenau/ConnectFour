package com.example.martin.connectfour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import static java.lang.Long.bitCount;

public class PlayActivity extends AppCompatActivity implements android.view.View.OnClickListener  {

    Spielfeld f = new Spielfeld();
    boolean finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent play = getIntent();
        Bundle b = play.getExtras();
        int[] array = b.getIntArray("extra");

        if(array[0] == 2){
            computermove(array[1]);
        }

        Button btn0 = (Button) findViewById(R.id.btn0);
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        Button btn4 = (Button) findViewById(R.id.btn4);
        Button btn5 = (Button) findViewById(R.id.btn5);
        Button btn6 = (Button) findViewById(R.id.btn6);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
    }

    public void onClick(View v){
        Intent play = getIntent();
        Bundle b = play.getExtras();
        int[] array = b.getIntArray("extra");

        if(!this.finish) {
            boolean nextTurn = playermove(v.getId());
            if (nextTurn) {
                check();
                computermove(array[1]);
                check();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(this.finish){
            if(action == MotionEvent.ACTION_DOWN) {
                return true;
            }
            else if(action == MotionEvent.ACTION_UP) {
                finish();
            }
        }
        return super.onTouchEvent(event);
    }

    private void check (){
        if(f.sieg(1)){
            Toast.makeText(PlayActivity.this, "Congratulation Player 1", Toast.LENGTH_SHORT).show();
            this.finish = true;
            findFour(1);
        }
        else if(f.sieg(2)){
            Toast.makeText(PlayActivity.this, "Congratulation Computer", Toast.LENGTH_SHORT).show();
            this.finish = true;
            findFour(2);
        }
        else if(f.full()){
            Toast.makeText(PlayActivity.this, "Playfield full", Toast.LENGTH_SHORT).show();
            this.finish = true;
        }
    }

    private boolean playermove(int btnID){
        Spielfeld foo = null;
        switch (btnID) { 
            case R.id.btn0:
                foo = this.f.zug(1, 0);
                break;
            case R.id.btn1:
                foo = this.f.zug(1, 1);
                break;
            case R.id.btn2:
                foo = this.f.zug(1, 2);
                break;
            case R.id.btn3:
                foo = this.f.zug(1, 3);
                break;
            case R.id.btn4:
                foo = this.f.zug(1, 4);
                break;
            case R.id.btn5:
                foo = this.f.zug(1, 5);
                break;
            case R.id.btn6:
                foo = this.f.zug(1, 6);
                break;
        }
        if(foo == null){
            return false;
        }
        spielstand(this.f);
        return true;
    }

    private void computermove(int tiefe) {
        int inp;
        inp = f.bester(2, tiefe);
        f.zug(2, inp);
        spielstand(this.f);
    }

    public void spielstand(Spielfeld sp){
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                int field = j+8*i;
                if(((sp.getlong(1) >> field) & 1) == 1){
                    int resID = getResources().getIdentifier("v"+(10*i+j), "id", getPackageName());
                    ImageView im = (ImageView) findViewById(resID);
                    im.setImageResource(R.drawable.red);
                }
                else if(((sp.getlong(2) >> field) & 1) == 1){
                    int resID = getResources().getIdentifier("v"+(10*i+j), "id", getPackageName());
                    ImageView im = (ImageView) findViewById(resID);
                    im.setImageResource(R.drawable.blue);
                }
            }
        }
    }

    public void findFour(int spieler) {
        // TODO: Implementieren Sie hier die Angabe
        long player = (spieler==1) ? this.f.getlong(1) : this.f.getlong(2);

        long row = (player >> 0) & (player >> 1) & (player >> 2) & (player >> 3);
        long column = (player >> 0) & (player >> 8) & (player >> 16) & (player >> 24);
        long diagonalR = (player >> 0) & (player >> 9) & (player >> 18) & (player>> 27);
        long diagonalL = (player >> 0) & (player >> 7) & (player >> 14) & (player>> 21);

        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                int field = j+8*i;
                if(((row >> field) & 1) == 1){
                    for(int k=0; k<4; k++){
                        int resID = getResources().getIdentifier("v"+(10*i+j+k), "id", getPackageName());
                        ImageView im = (ImageView) findViewById(resID);
                        if(spieler==1) {
                            im.setImageResource(R.drawable.yellow1);
                        }
                        else{
                            im.setImageResource(R.drawable.yellow2);
                        }
                    }
                }
                else if(((column >> field) & 1) == 1){
                    for(int k=0; k<4; k++){
                        int resID = getResources().getIdentifier("v"+(10*(i+k)+j), "id", getPackageName());
                        ImageView im = (ImageView) findViewById(resID);
                        if(spieler==1) {
                            im.setImageResource(R.drawable.yellow1);
                        }
                        else{
                            im.setImageResource(R.drawable.yellow2);
                        }
                    }
                }
                else if(((diagonalR >> field) & 1) == 1){
                    for(int k=0; k<4; k++){
                        int resID = getResources().getIdentifier("v"+(10*(i+k)+j+k), "id", getPackageName());
                        ImageView im = (ImageView) findViewById(resID);
                        if(spieler==1) {
                            im.setImageResource(R.drawable.yellow1);
                        }
                        else{
                            im.setImageResource(R.drawable.yellow2);
                        }
                    }
                }
                else if(((diagonalL >> field) & 1) == 1){
                    for(int k=0; k<4; k++){
                        int resID = getResources().getIdentifier("v"+(10*(i+k)+j-k), "id", getPackageName());
                        ImageView im = (ImageView) findViewById(resID);
                        if(spieler==1) {
                            im.setImageResource(R.drawable.yellow1);
                        }
                        else{
                            im.setImageResource(R.drawable.yellow2);
                        }
                    }
                }
            }
        }
    }
}
