package wittyapp.draegerit.de.wittyapp.examples;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Arrays;

import wittyapp.draegerit.de.wittyapp.MainActivity;
import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EAction;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;

public class MatrixView extends AbstractView implements View.OnClickListener {

    private static int[] imageViewIds = new int[]{R.id.imgR1_1, R.id.imgR1_2, R.id.imgR1_3, R.id.imgR1_4, R.id.imgR1_5, R.id.imgR1_6, R.id.imgR1_7, R.id.imgR1_8,
            R.id.imgR2_1, R.id.imgR2_2, R.id.imgR2_3, R.id.imgR2_4, R.id.imgR2_5, R.id.imgR2_6, R.id.imgR2_7, R.id.imgR2_8,
            R.id.imgR3_1, R.id.imgR3_2, R.id.imgR3_3, R.id.imgR3_4, R.id.imgR3_5, R.id.imgR3_6, R.id.imgR3_7, R.id.imgR3_8,
            R.id.imgR4_1, R.id.imgR4_2, R.id.imgR4_3, R.id.imgR4_4, R.id.imgR4_5, R.id.imgR4_6, R.id.imgR4_7, R.id.imgR4_8,
            R.id.imgR5_1, R.id.imgR5_2, R.id.imgR5_3, R.id.imgR5_4, R.id.imgR5_5, R.id.imgR5_6, R.id.imgR5_7, R.id.imgR5_8,
            R.id.imgR6_1, R.id.imgR6_2, R.id.imgR6_3, R.id.imgR6_4, R.id.imgR6_5, R.id.imgR6_6, R.id.imgR6_7, R.id.imgR6_8,
            R.id.imgR7_1, R.id.imgR7_2, R.id.imgR7_3, R.id.imgR7_4, R.id.imgR7_5, R.id.imgR7_6, R.id.imgR7_7, R.id.imgR7_8,
            R.id.imgR8_1, R.id.imgR8_2, R.id.imgR8_3, R.id.imgR8_4, R.id.imgR8_5, R.id.imgR8_6, R.id.imgR8_7, R.id.imgR8_8};

    private TextView matrixBrightnessTextView;
    private int brightness = 1;

    private int currentRow;
    private int currentCol;

    private int state;

    private int clear = 0;

    public MatrixView(Context ctx, ViewSwitcher viewSwitcher) {
        super(ctx, viewSwitcher, EActiveView.MATRIX);
    }

    @Override
    public void clearData() {
        for (int i = 0; i < imageViewIds.length; i++) {
            ImageView imageView = getView().findViewById(imageViewIds[i]);
            imageView.setImageDrawable(getCtx().getDrawable(R.drawable.circle_gray));
            imageView.setTag(R.string.imageResId, R.drawable.circle_gray);
        }

        brightness = 1;
        clear = 1;
        matrixBrightnessTextView.setText(String.valueOf(brightness));
        fireClearAction();
    }

    private void fireClearAction() {
        MainActivity mainActivity = (MainActivity) getView().getContext();
        mainActivity.fireAction(EAction.MATRIX);
    }

    private void fireBrightnessAction() {
        MainActivity mainActivity = (MainActivity) getView().getContext();
        mainActivity.fireAction(EAction.MATRIX);
    }

    @Override
    public void initialize() {
        matrixBrightnessTextView = getView().findViewById(R.id.matrixBrightnessTextView);

        ImageView leftArrowImageView = getView().findViewById(R.id.leftArrowImageView);
        leftArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightness = brightness == 1 ? 1 : brightness - 1;
                matrixBrightnessTextView.setText(String.valueOf(brightness));
                fireBrightnessAction();
            }
        });

        ImageView rightArrowImageView = getView().findViewById(R.id.rightArrowImageView);
        rightArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightness = brightness == 8 ? 8 : brightness + 1;
                matrixBrightnessTextView.setText(String.valueOf(brightness));
                fireBrightnessAction();
            }
        });


        int row = 0;
        int col = -1;
        for (int i = 0; i < imageViewIds.length; i++) {
            col++;
            if (col > 7) {
                row++;
                col = 0;
            }
            ImageView imageView = getView().findViewById(imageViewIds[i]);
            imageView.setOnClickListener(this);
            imageView.setTag(R.string.imageResId, R.drawable.circle_gray);
            imageView.setTag(R.string.currentRowId, row);
            imageView.setTag(R.string.currentColId, col);
        }

    }

    @Override
    public void update() {

    }

    @Override
    public void onClick(View v) {
        currentRow = (int) v.getTag(R.string.currentRowId);
        currentCol = (int) v.getTag(R.string.currentColId);
        toggleImage(v);
        MainActivity mainActivity = (MainActivity) getView().getContext();
        mainActivity.fireAction(EAction.MATRIX);

    }

    private void toggleImage(View v) {
        ImageView imageView = (ImageView) v;
        int currentDrawableId = -1;
        if (imageView.getTag(R.string.imageResId) != null) {
            currentDrawableId = (int) imageView.getTag(R.string.imageResId);
        }
        int newDrawableId = -1;
        switch (currentDrawableId) {
            case -1:
            case R.drawable.circle_gray:
                state = 1;
                newDrawableId = R.drawable.circle_red;
                break;
            case R.drawable.circle_red:
                state = 0;
                newDrawableId = R.drawable.circle_gray;
                break;
        }
        imageView.setImageDrawable(getCtx().getDrawable(newDrawableId));
        imageView.setTag(R.string.imageResId, newDrawableId);
    }


    public int getBrightness() {
        return brightness;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getCurrentCol() {
        return currentCol;
    }

    public void setCurrentCol(int currentCol) {
        this.currentCol = currentCol;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getClear() {
        return clear;
    }

    public void setClear(int clear) {
        this.clear = clear;
    }
}
