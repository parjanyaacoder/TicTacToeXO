package com.s090.tictactoexo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SinglePlayerMode extends AppCompatActivity {
    int playerState = 0, whoIsPlaying=0; // at present at two player local mode. 0 is x and 1 is o.
    int[] boardState = {-1, -1, -1, -1, -1, -1, -1, -1, -1}; // -1 means unplayed, else stores playerState, denoting which player tapped which cell
    int playCounter = 0, winnerwinner = 0, computerState=0; // denotes number of tapped or played grids, winnerwinner denotes somebody has won
    public int minimax(int[] boardState, int depth, int player) {
        int minScore=-999, maxScore=-999,score;
        if(winnerwinner==playerState) return -1;
        else if(winnerwinner==computerState) return 1;
        for(int i=0;i<boardState.length;i++)
        {
            if(boardState[i]==-1)
            {
                if(whoIsPlaying==computerState)
                {
                    View counter = new View(SinglePlayerMode.this);
                    View view = counter.findViewWithTag(boardState[i]);
                    drop_in(view);
                    score=minimax(boardState, depth+1, playerState);
                    maxScore=Math.max(score, maxScore);
                }
                else if(whoIsPlaying==playerState)
                {
                    View view = new View(SinglePlayerMode.this);
                    view = view.findViewWithTag(boardState[i]);
                    drop_in(view);
                    score=minimax(boardState, depth+1, computerState);
                    minScore=Math.min(score, minScore);
                }
            }
        }
        if(whoIsPlaying==computerState) return maxScore;
        else return minScore;
    }

    public void myDialog(String title, String msg) // method to call dialog box
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SinglePlayerMode.this);
        builder.setTitle(title).setMessage(msg).setCancelable(false);
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() { // attaches an onClickListener to button
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(getIntent());
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public int endStateChecker(int[] boardState) {
        //all possible winning positions below
        if (boardState[0] == boardState[1] && boardState[1] == boardState[2] && boardState[0] != -1) {
            Log.i("Info", "Player " + boardState[0] + " has won.");
            winnerwinner = boardState[0];
        } else if (boardState[0] == boardState[3] && boardState[3] == boardState[6] && boardState[0] != -1) {
            Log.i("Info", "Player " + boardState[0] + " has won.");
            winnerwinner = boardState[0];
        } else if (boardState[0] == boardState[4] && boardState[4] == boardState[8] && boardState[0] != -1) {
            Log.i("Info", "Player " + boardState[0] + " has won.");

            winnerwinner = boardState[0];
        } else if (boardState[1] == boardState[4] && boardState[4] == boardState[7] && boardState[1] != -1) {
            Log.i("Info", "Player " + boardState[1] + " has won.");

            winnerwinner = boardState[1];
        } else if (boardState[2] == boardState[5] && boardState[5] == boardState[8] && boardState[2] != -1) {
            Log.i("Info", "Player " + boardState[2] + " has won.");

            winnerwinner = boardState[2];
        } else if (boardState[2] == boardState[4] && boardState[4] == boardState[6] && boardState[2] != -1) {
            Log.i("Info", "Player " + boardState[2] + " has won.");

            winnerwinner = boardState[2];
        } else if (boardState[3] == boardState[4] && boardState[4] == boardState[5] && boardState[3] != -1) {
            Log.i("Info", "Player " + boardState[3] + " has won.");

            winnerwinner = boardState[3];
        } else if (boardState[6] == boardState[7] && boardState[7] == boardState[8] && boardState[6] != -1) {
            Log.i("Info", "Player " + boardState[6] + " has won.");

            winnerwinner = boardState[6];
        }
        if (playCounter == 9 && winnerwinner == 0) // if all grids have been tapped and nobody won, i.e. it's a draw or a tie
        {
            Log.i("Info", "Board full.");
        }
        return winnerwinner;
    }

    public void computerLogicHard() {
        int winnerstate = -1, newWinner;
        int[] copyBoardState = boardState;
        for (int i = 0; i < 9; i++) {
            if (copyBoardState[i] == -1) {
                copyBoardState[i] = playerState;
                newWinner = minimax(copyBoardState, 0, whoIsPlaying);
                if (newWinner > winnerstate) winnerstate = newWinner;
            }
        }
        boardState=copyBoardState;
    }

    public void drop_in(View view) // method invoked on tapping any grid cell
    {
        ImageView counter = (ImageView) view;
        int cellState = Integer.parseInt(counter.getTag().toString()); // getting the associated tags or basically cell number
        if (boardState[cellState] == -1) {
            counter.setTranslationY(-1000f);
            if (whoIsPlaying == 0) {
                whoIsPlaying = 1;
                counter.setImageResource(R.drawable.x);
                playCounter++; // updating number of played grids
            } else {
                whoIsPlaying = 0;
                counter.setImageResource(R.drawable.o);
                playCounter++;
            }
            counter.animate().translationYBy(1000f).rotation(360f).setDuration(300); // drop-in animation
            boardState[cellState] = whoIsPlaying; // changing grid number to record which player tapped so that nobody can change on tapping again
        }
        endStateChecker(boardState);
       if (whoIsPlaying==computerState) computerLogicHard();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_mode);
        AlertDialog.Builder builder = new AlertDialog.Builder(SinglePlayerMode.this);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        Toast.makeText(SinglePlayerMode.this, "Hard Mode Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                        Toast.makeText(SinglePlayerMode.this, "Easy Mode Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        };
        builder
                .setTitle("Select Game Mode")
                .setMessage("Select Game Difficulty level")
                .setPositiveButton("Hard", dialogClickListener)
                .setNegativeButton("Easy", dialogClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        AlertDialog.Builder initDialogBuilder = new AlertDialog.Builder(SinglePlayerMode.this);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        playerState = 1;
                        whoIsPlaying = 1;
                        computerState = 0;
                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                        playerState = 0;
                        whoIsPlaying = 0;
                        computerState = 1;
                        break;
                    }
                }
            }
        };
        initDialogBuilder
                .setTitle("Select your counter")
                .setPositiveButtonIcon(getDrawable(R.drawable.o))
                .setPositiveButton("", listener)
                .setNegativeButtonIcon(getDrawable(R.drawable.x))
                .setNegativeButton("", listener)
                .setMessage("Please select your counter.");
        AlertDialog initDialog = initDialogBuilder.create();
        initDialog.show();
        Button btnPositive = initDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = initDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 1;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
        initDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // what to do if dialog box is cancelled

            @Override
            public void onCancel(DialogInterface dialog) {
                Intent i = new Intent();
                setResult(0, i);
                finish();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent i = new Intent();
                setResult(0, i);
                finish();
            }
        });
        getSupportActionBar().setTitle("Single Player mode");
        getSupportActionBar().setDisplayShowHomeEnabled(true); // enabling back button function
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enabling back functionality
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() { // the method to be called when the back button is pressed
        onBackPressed();
        return true;
    }
}
