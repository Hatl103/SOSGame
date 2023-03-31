package com.example.sosapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView p1score, p2score, gameStatus, pTurn;
    private final Button[] cells = new Button[25];
    private int Count;
    boolean CurrentPlayer;//where true is player 1 and false is player 2
    int[] cellstate = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[][] possibleWins = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}, {6, 7, 8}, {7, 8, 9}, {8, 9, 10}, {11, 12, 13}, {12, 13, 14}, {13, 14, 15}, {16, 17, 18}, {17, 18, 19}, {18, 19, 20}, {21, 22, 23}, {22, 23, 24}, {23, 24, 25}
            , {1, 6, 11}, {6, 11, 16}, {11, 16, 21}, {2, 7, 12}, {7, 12, 17}, {12, 17, 22}, {3, 8, 13}, {8, 13, 18}, {13, 18, 23}, {4, 9, 14}, {9, 14, 19}, {14, 19, 24}, {5, 10, 15}, {10, 15, 20}, {15, 20, 25}
            , {1, 7, 13}, {7, 13, 19}, {13, 19, 25}, {2, 8, 14}, {8, 14, 20}, {3, 9, 15}, {6, 12, 18}, {12, 18, 24}, {11, 17, 23}
            , {3, 7, 11}, {4, 8, 23}, {8, 12, 16}, {5, 9, 13}, {9, 13, 17}, {13, 17, 21}, {10, 14, 18}, {14, 18, 22}, {15, 19, 23}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing variables
        CurrentPlayer = true;//current player is player 1
        Count = 0;//to keep in check if all the buttons have been selected
        p1score = findViewById(R.id.score1);//displays the score obtained by the player 1
        p2score = findViewById(R.id.score2);//displays the score obtained by the player 2
        gameStatus = findViewById(R.id.status);//informs user about the game status
        pTurn = findViewById(R.id.playersTurns);//informs users about whose turn to play
        //initialising all the buttons present
        for (int i = 0; i < cells.length; i++) {
            int j = i + 1;
            String btnId = "btn_" + j;
            int Id = getResources().getIdentifier(btnId, "id", getPackageName());
            cells[i] = findViewById(Id);
            cells[i].setOnClickListener(this);
        }

    }

    //method used by all buttons on the grid such that when clicked and a letter is chosen they can't be clicked again
    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        int oldScore1 = Integer.parseInt(p1score.getText().toString());
        int oldScore2 = Integer.parseInt(p2score.getText().toString());
        String btnId = view.getResources().getResourceEntryName((view.getId()));
        int btnNumber = Integer.parseInt(btnId.substring(4));
        if (CurrentPlayer) {
            button.setTextColor(Color.parseColor("black"));
            cellstate[btnNumber - 1] = 1;
        } else {
            button.setTextColor(Color.parseColor("white"));
            cellstate[btnNumber - 1] = 2;
        }
        pick(button, oldScore1, oldScore2, btnId, btnNumber);
    }

    //pop-up dialog to let current player choose a letter
    private void pick(Button button, int oldScore1, int oldScore2, String btnId, int btnNumber) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Choose a letter")
                .setPositiveButton(getString(R.string.O), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        button.setText(getString(R.string.O));
                        calculate(button, oldScore1, oldScore2, btnId, btnNumber);
                    }
                })
                .setNegativeButton(getString(R.string.S), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        button.setText(getString(R.string.S));
                        calculate(button, oldScore1, oldScore2, btnId, btnNumber);
                    }
                })
                .show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void home(View View) {
        Intent intent = new Intent(this, SOS.class);
        startActivity(intent);
        reset();
    }

    private void setState() {
        int p1 = Integer.parseInt((p1score.getText().toString()));//getting player 1 score
        int p2 = Integer.parseInt(((p2score)).getText().toString()); //getting player 2 score
        if (p1 == p2) {
            gameStatus.setText(getString(R.string.gameStatus));
        } else {
            if (p1 > p2) {
                gameStatus.setText(R.string.player1Winning);
            } else {
                gameStatus.setText(R.string.player2Winning);
            }
        }
    }

    private void calculate(View view, int oldScore1, int oldScore2, String btnId, int btnNumber) {

        Button button = (findViewById(getResources().getIdentifier(btnId, "id", getPackageName())));
        if (button.getText().toString().equals(getString(R.string.O)) || button.getText().toString().equals(getString(R.string.S))) {
            view.setClickable(false);
            Count += 1;
            determine(CurrentPlayer, btnNumber);
            setState();
            int updatedScore1 = Integer.parseInt(p1score.getText().toString());
            int updatedScore2 = Integer.parseInt(p2score.getText().toString());
            if (Count < 25) {
                if (oldScore1 == updatedScore1 && oldScore2 == updatedScore2) {
                    if (CurrentPlayer) {
                        Toast.makeText(this, getString(R.string.player2turn), Toast.LENGTH_SHORT).show();
                        pTurn.setText(getString(R.string.player2turn));
                    } else {
                        Toast.makeText(this, getString(R.string.turns), Toast.LENGTH_SHORT).show();
                        pTurn.setText(getString(R.string.turns));
                    }
                    CurrentPlayer = !CurrentPlayer;
                } else {
                    if (updatedScore1 > oldScore1) {
                        Toast.makeText(this, getString(R.string.anotherPlayer1), Toast.LENGTH_SHORT).show();
                    } else {
                        if (updatedScore2 > oldScore2) {
                            Toast.makeText(this, getString(R.string.anotherPlayer2), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                if (updatedScore1 == updatedScore2) {
                    Toast.makeText(this, getString(R.string.draw), Toast.LENGTH_LONG).show();
                } else {
                    if (updatedScore1 > updatedScore2) {
                        Toast.makeText(this, getString(R.string.player1won), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, getString(R.string.player2won), Toast.LENGTH_LONG).show();
                    }
                }
                reset();
            }
        }
    }

    private void determine(boolean CurrentPlayer, int btnNum) {
        int score = 0;
        String btnId = "btn_" + btnNum;
        Button button = (findViewById(getResources().getIdentifier(btnId, "id", getPackageName())));
        String b = button.getText().toString();
        if (button.getText().toString().equals(getString(R.string.O))) {
            for (int[] possibleWin : possibleWins) {
                String Id1 = "btn_" + possibleWin[0];
                Button btn1 = (findViewById(getResources().getIdentifier(Id1, "id", getPackageName())));
                String Id2 = "btn_" + possibleWin[2];
                Button btn2 = (findViewById(getResources().getIdentifier(Id2, "id", getPackageName())));
                if (btn1.getText().toString().equals(getString(R.string.S)) && btn2.getText().toString().equals(getString(R.string.S))
                        && (btnNum == possibleWin[1])) {
                    score += 1;
                    update(button,btn1,btn2);
                }
            }
        } else {
            if (button.getText().toString().equals(getString(R.string.S))) {
                for (int[] possibleWin : possibleWins) {
                    if (btnNum != possibleWin[1]) {
                        if (btnNum == possibleWin[0]) {
                            String Id1 = "btn_" + possibleWin[1];
                            Button btn1 = (findViewById(getResources().getIdentifier(Id1, "id", getPackageName())));
                            String Id2 = "btn_" + possibleWin[2];
                            Button btn2 = (findViewById(getResources().getIdentifier(Id2, "id", getPackageName())));
                            if (btn1.getText().toString().equals(getString(R.string.O)) && btn2.getText().toString().equals(getString(R.string.S))) {
                                score += 1;
                                update(button,btn1,btn2);
                            }
                        } else {
                            if (btnNum == possibleWin[2]) {
                                String Id0 = "btn_" + possibleWin[0];
                                Button btn0 = (findViewById(getResources().getIdentifier(Id0, "id", getPackageName())));
                                String Id1 = "btn_" + possibleWin[1];
                                Button btn1 = (findViewById(getResources().getIdentifier(Id1, "id", getPackageName())));
                                if (btn0.getText().toString().equals(getString(R.string.S)) && btn1.getText().toString().equals(getString(R.string.O))) {
                                    score += 1;
                                    update(button,btn0,btn1);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (CurrentPlayer) {
            int p1 = Integer.parseInt(p1score.getText().toString());
            p1 += score;
            String s = Integer.toString(p1);
            p1score.setText(s);
        } else {
            int p2 = Integer.parseInt(p2score.getText().toString());
            p2 += score;
            String s = Integer.toString(p2);
            p2score.setText(s);
        }
    }
    public void update(Button button,Button btn0,Button btn1)
    {
        if (CurrentPlayer) {
            button.setTextColor(Color.parseColor("blue"));
            btn0.setTextColor(Color.parseColor("blue"));
            btn1.setTextColor(Color.parseColor("blue"));
        } else {
            button.setTextColor(Color.parseColor("red"));
            btn1.setTextColor(Color.parseColor("red"));
            btn0.setTextColor(Color.parseColor("red"));
        }
    }

    public void reset() {
        Count = 0;
        CurrentPlayer = true;
        p1score.setText(getString(R.string.player1Score));
        p2score.setText(getString(R.string.player2Score));
        for (int i = 0; i < cells.length; i++) {
            cellstate[i] = 0;
            cells[i].setText("");
            cells[i].setClickable(true);
        }
    }

    public void rst(View v) {
        reset();
    }
}