package com.mypackage.riftboundlifetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {

    boolean[][] battlefieldScored = new boolean[2][2];
    Button[][] btnBattlefield = new Button[2][2];
    ImageView[][] imgBattlefield = new ImageView[2][2];
    ImageView bgP1, bgP2;
    String normalState = "normal", activeState = "active";
    Object currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideSystemBars();

        TextView[] points = {findViewById(R.id.P1Points), findViewById(R.id.P2Points)};

        // Backgrounds
        bgP1 = findViewById(R.id.backgroundP1);
        bgP1.setTag(activeState);
        bgP2 = findViewById(R.id.backgroundP2);
        bgP2.setTag(normalState);
        currentState = bgP1.getTag();

        // Battlefield buttons
        btnBattlefield[0][0] = findViewById(R.id.battlefield1P1);
        btnBattlefield[0][1] = findViewById(R.id.battlefield2P1);
        btnBattlefield[1][0] = findViewById(R.id.battlefield1P2);
        btnBattlefield[1][1] = findViewById(R.id.battlefield2P2);

        // Battlefield images
        imgBattlefield[0][0] = findViewById(R.id.imgBattlefield1P1);
        imgBattlefield[0][1] = findViewById(R.id.imgBattlefield2P1);
        imgBattlefield[1][0] = findViewById(R.id.imgBattlefield1P2);
        imgBattlefield[1][1] = findViewById(R.id.imgBattlefield2P2);

        // Start invisible
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                imgBattlefield[i][j].setVisibility(View.INVISIBLE);
                int playerIndex = i;
                int fieldIndex = j;
                btnBattlefield[i][j].setOnClickListener(v -> scoreAtBattlefield(playerIndex, fieldIndex, points[playerIndex]));
            }
        }

        // Turn change only if the clicked player is active
        for (int i = 0; i < points.length; i++) {
            int playerIndex = i;
            points[i].setOnClickListener(v -> {
                boolean isPlayer1Active = currentState.equals(activeState);
                if ((playerIndex == 0 && isPlayer1Active) || (playerIndex == 1 && !isPlayer1Active)) {
                    turnChange();
                }
            });
        }

        findViewById(R.id.btnRestart).setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog)
                    .setTitle("Restart game?")
                    .setMessage("Are you sure you want to restart the match?")
                    .setPositiveButton("Yes", (dialog, which) -> recreate())
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void scoreAtBattlefield(int player, int field, TextView points) {
        int currentPoints = Integer.parseInt(points.getText().toString());

        if (!battlefieldScored[player][field]) {
            if (currentPoints < 9) {
                points.setText(String.valueOf(currentPoints + 1));
                imgBattlefield[player][field].setVisibility(View.VISIBLE);
                battlefieldScored[player][field] = true;
            }
        } else {
            points.setText(String.valueOf(currentPoints - 1));
            imgBattlefield[player][field].setVisibility(View.INVISIBLE);
            battlefieldScored[player][field] = false;
        }
    }


    private void turnChange() {
        if (currentState.equals(activeState)) {
            resetPlayer(0);
            changeToPlayer2();
        } else {
            resetPlayer(1);
            changeToPlayer1();
        }
        currentState = bgP1.getTag();
    }

    private void resetPlayer(int playerIndex) {
        for (int i = 0; i < 2; i++) {
            imgBattlefield[playerIndex][i].setVisibility(View.INVISIBLE);
            battlefieldScored[playerIndex][i] = false;
        }
    }

    private void changeToPlayer2() {
        bgP2.setImageResource(R.drawable.layout_active);
        bgP1.setImageResource(R.drawable.layout_normal);
        toggleBattlefieldVisibility(0, View.GONE);
        toggleBattlefieldVisibility(1, View.VISIBLE);
        bgP1.setTag(normalState);
    }

    private void changeToPlayer1() {
        bgP2.setImageResource(R.drawable.layout_normal);
        bgP1.setImageResource(R.drawable.layout_active);
        toggleBattlefieldVisibility(0, View.VISIBLE);
        toggleBattlefieldVisibility(1, View.GONE);
        bgP1.setTag(activeState);
    }

    private void toggleBattlefieldVisibility(int playerIndex, int visibility) {
        for (int f = 0; f < 2; f++) {
            btnBattlefield[playerIndex][f].setVisibility(visibility);
        }
    }

    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }
}
