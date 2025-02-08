package com.goutom64.tic_tac_toe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {


    AdView adView;

    int step = 0;
    int count = 0;
    boolean gameOver = false;
    Button[] buttons = new Button[9];
    Button btnReset;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adView = findViewById(R.id.adView);
        MobileAds.initialize(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);









        init();
    }

    private void init() {
        mainLayout = findViewById(R.id.main);
        mainLayout.setBackgroundColor(Color.parseColor("#2575fc")); // background

        int[] buttonIds = {
                R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9};

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = findViewById(buttonIds[i]);
            buttons[i].setBackgroundColor(Color.parseColor("#4b1c85")); // Button color
            buttons[i].setTextColor(Color.WHITE);
            buttons[i].setTextSize(25);
        }


        btnReset = findViewById(R.id.btnReset);
        btnReset.setBackgroundColor(Color.parseColor("#E60808")); // Red background
        btnReset.setText("Reset Game");

        btnReset.setOnClickListener(view -> resetGame(view));
    }

    public void check(View view) {
        if (gameOver) return;

        Button btnCurrent = (Button) view;
        if (btnCurrent.getText().toString().equals("")) {
            count++;
            btnCurrent.setText(step == 0 ? "X" : "O");
            step = 1 - step;
            btnReset.setText("Reset Game");

            if (count > 4) {
                int[][] winPatterns = {
                        {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                        {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                        {0, 4, 8}, {2, 4, 6}             // Diagonals
                };

                for (int[] pattern : winPatterns) {
                    if (checkWinner(pattern[0], pattern[1], pattern[2])) {
                        highlightWinner(pattern[0], pattern[1], pattern[2]);
                        showWinnerDialog(buttons[pattern[0]].getText().toString());
                        return;
                    }
                }

                if (count == 9) {
                    showWinnerDialog("Draw");
                }
            }
        }
    }

    private boolean checkWinner(int a, int b, int c) {
        return buttons[a].getText().toString().equals(buttons[b].getText().toString()) &&
                buttons[b].getText().toString().equals(buttons[c].getText().toString()) &&
                !buttons[a].getText().toString().equals("");
    }

    private void highlightWinner(int a, int b, int c) {
        buttons[a].setBackgroundColor(Color.parseColor("#00ADB5")); // Highlight color
        buttons[b].setBackgroundColor(Color.parseColor("#00ADB5"));
        buttons[c].setBackgroundColor(Color.parseColor("#00ADB5"));
        gameOver = true;
    }

    private void showWinnerDialog(String winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage(winner.equals("Draw") ? "It's a Draw!" : "Winner is " + winner);
        builder.setPositiveButton("OK", (dialog, which) ->{
            Log.d("TicTacToe", "OK button clicked");
            btnReset.setText("New Game");
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void resetGame(View view) {
        for (Button button : buttons) {
            button.setText("");
            button.setBackgroundColor(Color.parseColor("#4b1c85"));

        }
        count = 0;
        step = 0;
        gameOver = false;
    }
}
