package by.pavka.memento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import by.pavka.memento.databinding.ActivityMeasureBinding;

public class MeasureActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText measureDate;
    private EditText measureResult;
    private Button buttonMeasure;
    private MementoApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMeasureBinding binding = ActivityMeasureBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        application = (MementoApplication) getApplication();
        measureDate = binding.measureDate;
        measureResult = binding.measureResult;
        buttonMeasure = binding.buttonMeasure;
        buttonMeasure.setOnClickListener(this);
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.weights);
        item.setChecked(true);
    }

    @Override
    public void onClick(View v) {

    }
}