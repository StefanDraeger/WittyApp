package wittyapp.draegerit.de.wittyapp.examples;

import android.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import wittyapp.draegerit.de.wittyapp.MainActivity;
import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.util.EAction;
import wittyapp.draegerit.de.wittyapp.util.IActionView;

public class MatrixActivity extends Fragment implements OnFragmentInteractionListener, IActionView,View.OnClickListener {

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

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_matrix, container, false);
        matrixBrightnessTextView = view.findViewById(R.id.matrixBrightnessTextView);

        ImageView leftArrowImageView = view.findViewById(R.id.leftArrowImageView);
        leftArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightness = brightness == 1 ? 1 : brightness - 1;
                matrixBrightnessTextView.setText(String.valueOf(brightness));
                fireBrightnessAction();
            }
        });

        ImageView rightArrowImageView = view.findViewById(R.id.rightArrowImageView);
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
            ImageView imageView = view.findViewById(imageViewIds[i]);
            imageView.setOnClickListener(this);
            imageView.setTag(R.string.imageResId, R.drawable.circle_gray);
            imageView.setTag(R.string.currentRowId, row);
            imageView.setTag(R.string.currentColId, col);
        }
        return view;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void save() {

    }

    @Override
    public void clearData() {
        for (int i = 0; i < imageViewIds.length; i++) {
            ImageView imageView = view.findViewById(imageViewIds[i]);
            imageView.setImageDrawable(getContext().getDrawable(R.drawable.circle_gray));
            imageView.setTag(R.string.imageResId, R.drawable.circle_gray);
        }

        brightness = 1;
        clear = 1;
        matrixBrightnessTextView.setText(String.valueOf(brightness));
        fireClearAction();
    }

    private void fireClearAction() {
        MainActivity mainActivity = (MainActivity) view.getContext();
        mainActivity.fireAction(EAction.MATRIX);
    }

    private void fireBrightnessAction() {
        MainActivity mainActivity = (MainActivity) view.getContext();
        mainActivity.fireAction(EAction.MATRIX);
    }


    @Override
    public void update() {

    }

    @Override
    public int getUpdateInterval() {
        return 0;
    }

    @Override
    public void setUpdateInterval(int intervall) {

    }

    @Override
    public void addChartEntry(String entryValue) {

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
        imageView.setImageDrawable(getContext().getDrawable(newDrawableId));
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
