package net.iakanoe.minesweeper;

import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.Random;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    int cols, rows;
    double width, height;
    static final double w = 120;
    static final int totalBees = 10;
    int leftCells;
    public boolean firstClick;
    public Vector<Vector<Cell>> grid;

    int random(int val){
        Random r = new Random();
        return r.nextInt(val);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;
        cols = (int) Math.floor(width / w);
        rows = (int) Math.floor(height / w);
	    leftCells = rows * cols;
        reset();
    }

    void placeBees(int firstI, int firstJ){
    	int startI = (firstI > 0 ? firstI - 1 : 0);
	    int endI = (firstI < cols ? firstI + 1 : cols);
	    int startJ = (firstJ > 0 ? firstJ - 1 : 0);
	    int endJ = (firstJ < rows ? firstJ + 1 : rows);
	    for(int n = 0; n < totalBees; n++){
		    boolean rr = false;
		    while(!rr){
			    int i = random(cols);
			    int j = random(rows);
			    if(!grid.get(i).get(j).hasBee() && !(i >= startI && i <= endI && j >= startJ && j <= endJ)){
				    grid.get(i).get(j).setBee(true);
				    rr = true;
			    }
		    }
	    }
	    for(int i = 0; i < cols; i++) for(int j = 0; j < rows; j++) grid.get(i).get(j).countBees();
	    firstClick = false;
    }
    
    void reset(){
    	firstClick = true;
	    ((RelativeLayout) findViewById(R.id.mainLayout)).removeAllViewsInLayout();
	    grid = new Vector<Vector<Cell>>();
	    for(int i = 0; i < cols; i++){
		    grid.add(new Vector<Cell>(rows));
		    for(int j = 0; j < rows; j++){
			    grid.get(i).add(new Cell(this, this, (RelativeLayout) findViewById(R.id.mainLayout), i, j, w));
		    }
	    }
    }
    
    void ganaste(){
	    new AlertDialog
		    .Builder(this)
		    .setTitle("FELICITACIONES!")
		    .setMessage("Ganaste!")
		    .setNeutralButton("Reiniciar", new DialogInterface.OnClickListener(){
			    @Override
			    public void onClick(DialogInterface dialogInterface, int i){
				    reset();
			    }
		    })
		    .setCancelable(false)
		    .create()
		    .show();
    }
    
    void gameOver(){
	    for(int i = 0; i < cols; i++) for(int j = 0; j < rows; j++) grid.get(i).get(j).reveal(false);
	    new AlertDialog
		    .Builder(this)
		    .setTitle("GAME OVER!")
		    .setMessage("Perdiste!")
		    .setNeutralButton("Reiniciar", new DialogInterface.OnClickListener(){
			    @Override
			    public void onClick(DialogInterface dialogInterface, int i){
					reset();
			    }
		    })
		    .create()
		    .show();
    }
}
