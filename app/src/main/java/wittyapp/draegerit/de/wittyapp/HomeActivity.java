package wittyapp.draegerit.de.wittyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView homeVersionTextView = findViewById(R.id.homeVersionTextView);
        homeVersionTextView.setText(getString(R.string.version_text,BuildConfig.VERSION_NAME,  BuildConfig.VERSION_CODE));
    }
}
