package by.pavka.memento.habit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import by.pavka.memento.BottomNavigationListener;
import by.pavka.memento.R;
import by.pavka.memento.databinding.ActivityActivizationBinding;

public class ActivizationActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ActivizationViewModel viewModel;
    private Button endDay;
    private Button time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityActivizationBinding binding = ActivityActivizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this).get(ActivizationViewModel.class);
        if (viewModel.getEnd() == null) {
            Intent intent = getIntent();
            Habit habit = (Habit) intent.getSerializableExtra("habit");
            viewModel.setHabit(habit);
        }
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.habit);
        item.setChecked(true);

        Button buttonClean = binding.buttonClean;
        buttonClean.setOnClickListener(this);
        Button buttonCancel = binding.buttonCancel;
        buttonCancel.setOnClickListener(this);
        Button buttonOk = binding.buttonOk;
        buttonOk.setOnClickListener(this);

        endDay = binding.endDay;
        endDay.setOnClickListener(this);

        time = binding.time;
        time.setOnClickListener(this);

        if (!viewModel.isClearance()) {
            endDay.setText(viewModel.getEnd().toString());
            //todo
            time.setText(LocalTime.now().truncatedTo(ChronoUnit.MINUTES).toString());
        } else {
            endDay.setText("");
            time.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_clean:
                viewModel.setClearance(true);
                endDay.setText("");
                time.setText("");
                break;
            case R.id.button_cancel:
                viewModel.clearHabit();
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.button_ok:
                Intent intent = new Intent();
                viewModel.resetProgress(viewModel.isClearance());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.end_day:
                LocalDate now = LocalDate.now();
                new DatePickerDialog(this, this, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth()).show();
                break;
            case R.id.time:
                LocalTime localTime = LocalTime.now();
                new TimePickerDialog(this, this, localTime.getHour(), localTime.getMinute(), true).show();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LocalDate end = LocalDate.of(year, month + 1, dayOfMonth);
        viewModel.setEnd(end);
        viewModel.setClearance(false);
        endDay.setText(end.toString());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}