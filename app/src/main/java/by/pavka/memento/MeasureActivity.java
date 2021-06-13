package by.pavka.memento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import by.pavka.memento.databinding.ActivityMeasureBinding;
import by.pavka.memento.habit.ActivizationViewModel;
import by.pavka.memento.util.CalendarConverter;

public class MeasureActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Button measureDate;
    private EditText measureResult;
    private Button buttonMeasure;
    private MeasureViewModel viewModel;
    private MementoApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMeasureBinding binding = ActivityMeasureBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this).get(MeasureViewModel.class);
        application = (MementoApplication) getApplication();
        measureDate = binding.measureDate;
        measureDate.setOnClickListener(this);
        if (viewModel.getMeasureDate() == null) {
            viewModel.setMeasureDate(Calendar.getInstance());
        }
        measureDate.setText(CalendarConverter.showDate(viewModel.getMeasureDate()));
        measureResult = binding.measureResult;
        if (viewModel.getWeight() != 0) {
            measureResult.setText(String.valueOf(viewModel.getWeight()));
        }
        buttonMeasure = binding.buttonMeasure;
        buttonMeasure.setOnClickListener(this);
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.weights);
        item.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.measure_date:
                Calendar today = Calendar.getInstance();
                new DatePickerDialog(this, this, today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.button_measure:
                // TODO
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar measure = Calendar.getInstance();
        measure.set(year, month, dayOfMonth);
        viewModel.setMeasureDate(measure);
        measureDate.setText(CalendarConverter.showDate(viewModel.getMeasureDate()));
    }
}