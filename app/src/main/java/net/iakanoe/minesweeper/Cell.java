package net.iakanoe.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

class Cell {
    int i, j;
	double w, x, y;
    int neighborCount = 0;
    boolean bee = false;
    boolean revealed = false;
    Context context;
    RelativeLayout linkedLayout;
    MainActivity mainContext;

    Cell(Context context, MainActivity mainContext, RelativeLayout parentLayout, int i, int j, double w){
        this.context = context;
        this.mainContext = mainContext;
        this.i = i;
        this.j = j;
        this.x = i * w;
        this.y = j * w;
        this.w = w;

        linkedLayout = new RelativeLayout(context);
        linkedLayout.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (this.w + 0.5d), (int) (this.w + 0.5d));
        layoutParams.leftMargin = (int) (this.x + 0.5d);
        layoutParams.topMargin = (int) (this.y + 0.5d);
        linkedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked();
            }
        });
        parentLayout.addView(linkedLayout, layoutParams);
	    
        setBackgroundColor(Color.WHITE);
    }

    void setBackgroundColor(int color){
	    GradientDrawable shape = new GradientDrawable();
	    shape.setShape(GradientDrawable.RECTANGLE);
	    shape.setColor(color);
	    shape.setStroke(2, Color.BLACK);
    	
    	
	    /*ShapeDrawable rectShapeDrawable = new ShapeDrawable(new RectShape());
	    Paint paint = rectShapeDrawable.getPaint();
	    paint.setColor(color); //Color.BLACK
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
	    rectShapeDrawable.setStroke(5, Color.BLACK);*/
	    linkedLayout.setBackground(shape);
    }
    
    void clicked(){
        if(mainContext.firstClick){
        	mainContext.placeBees(i, j);
        }
    	reveal();
    }

    boolean hasBee(){
        return bee;
    }

    void setBee(boolean b){
        bee = b;
    }

    void reveal(){
    	if(!revealed){
    		mainContext.leftCells--;
	    }
        revealed = true;
        if(bee){
            setBackgroundColor(Color.RED);
            mainContext.gameOver();
            return;
        } else {
            setBackgroundColor(Color.GRAY);
            if(neighborCount > 0){
                TextView textView = new TextView(context);
                textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                textView.setText(String.valueOf(neighborCount));
                textView.setTextColor(Color.BLACK);
                linkedLayout.addView(textView);
            } else if(neighborCount == 0){
                floodFill();
            }
        }
	    if(mainContext.leftCells == MainActivity.totalBees){
        	mainContext.ganaste();
	    }
    }
    
    void reveal(boolean x){
    	if(!revealed){
		    revealed = true;
		    if(bee){
			    setBackgroundColor(Color.RED);
		    } else {
			    setBackgroundColor(Color.GRAY);
			    if(neighborCount > 0){
				    TextView textView = new TextView(context);
				    textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
				    textView.setText(String.valueOf(neighborCount));
				    textView.setTextColor(Color.BLACK);
				    linkedLayout.addView(textView);
			    }
		    }
	    }
    }

    void floodFill(){
        for (int xoff = -1; xoff <= 1; xoff++) {
            for (int yoff = -1; yoff <= 1; yoff++) {
                int ix = i + xoff;
                int jx = j + yoff;
                if (ix > -1 && ix < mainContext.cols && jx > -1 && jx < mainContext.rows) {
                    Cell neighbor = mainContext.grid.get(ix).get(jx);
                    if (!neighbor.bee && !neighbor.revealed) {
                        neighbor.reveal();
                    }
                }
            }
        }
    }

    void countBees(){
        if(bee){
            neighborCount = -1;
            return;
        }
        int total = 0;
        for(int xoff = -1; xoff <= 1; xoff++){
            for(int yoff = -1; yoff <= 1; yoff++){
                int ix = i + xoff;
                int jx = j + yoff;
                if (ix > -1 && ix < mainContext.cols && jx > -1 && jx < mainContext.rows) {
                    if(mainContext.grid.get(ix).get(jx).hasBee()) total++;
                }
            }
        }
        neighborCount = total;
    }

}
