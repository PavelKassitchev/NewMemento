package by.pavka.memento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import by.pavka.memento.databinding.ActivityMeasureBinding;
import by.pavka.memento.habit.ActivizationViewModel;
import by.pavka.memento.util.CalendarConverter;
import by.pavka.memento.util.Displayer;

public class MeasureActivity extends MementoActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Button measureDate;
    private EditText measureResult;
    private Button buttonMeasure;
    private Button buttonHistory;
    private MeasureViewModel viewModel;
    private MementoApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMeasureBinding binding = ActivityMeasureBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setBackOverridden();
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
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
        buttonHistory = binding.buttonHistory;
        buttonHistory.setOnClickListener(this);
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.weights);
        item.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.measure_date:
                Calendar today = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.button_measure:
                if (!measureResult.getText().toString().isEmpty()) {
                    double weight = 0;
                    try {
                        weight = Double.parseDouble(measureResult.getText().toString());
                    } catch (NumberFormatException e) {
                        Displayer.showSnackbar(R.string.weight_valid, v);
                        return;
                    }
                    if (weight > 2) {
                        viewModel.setWeight(Double.parseDouble(measureResult.getText().toString()));
                        viewModel.updateChronicler();
                    } else {
                        Displayer.showSnackbar(R.string.weight_valid, v);
                        return;
                    }
                } else {
                    viewModel.removeRecord(viewModel.getMeasureDate());
                }
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.button_history:
                startActivity(new Intent(this, HistoryActivity.class));
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