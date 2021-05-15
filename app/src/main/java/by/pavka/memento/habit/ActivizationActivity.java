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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener {
    private ActivizationViewModel viewModel;
    private Button endDay;
    private Button time;
    private CheckBox mo, tue, wed, thu, fri, sat, snd, all;

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

        mo = binding.mo;
        mo.setOnCheckedChangeListener(this);
        tue = binding.tue;
        tue.setOnCheckedChangeListener(this);
        wed = binding.wed;
        wed.setOnCheckedChangeListener(this);
        thu = binding.thu;
        thu.setOnCheckedChangeListener(this);
        fri = binding.fri;
        fri.setOnCheckedChangeListener(this);
        sat = binding.sat;
        sat.setOnCheckedChangeListener(this);
        snd = binding.snd;
        snd.setOnCheckedChangeListener(this);
        all = binding.all;
        all.setOnCheckedChangeListener(this);

        if (!viewModel.isClearance()) {
            endDay.setText(viewModel.getEnd().toString());
            setWeek();
            time.setText(viewModel.getTime().toString());
        } else {
            endDay.setText("");
            time.setText("");
            checkWeek(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_clean:
                viewModel.setClearance(true);
                endDay.setText("");
                time.setText("");
                checkWeek(false);
                break;
            case R.id.button_cancel:
                viewModel.clearHabit();
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.button_ok:
                Intent intent = new Intent();
                intent.putExtra("habit", viewModel.getHabit());
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
        LocalTime localTime = LocalTime.of(hourOfDay, minute);
        viewModel.setTime(localTime);
        viewModel.setClearance(false);
        time.setText(viewModel.getTime().truncatedTo(ChronoUnit.MINUTES).toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.all:
                if (isChecked) {
                    checkWeek(true);
                    viewModel.setWeek(new boolean[] {true, true, true, true, true, true, true, true});
                } else {
                    viewModel.setDay(7, false);
                }
                break;
            case R.id.mo:
                if (!isChecked) {
                    all.setChecked(false);
                    viewModel.setDay(7, false);
                }
                viewModel.setDay(0, isChecked);
                break;
            case R.id.tue:
                if (!isChecked) {
                    all.setChecked(false);
                    viewModel.setDay(7, false);
                }
                viewModel.setDay(1, isChecked);
                break;
            case R.id.wed:
                if (!isChecked) {
                    all.setChecked(false);
                    viewModel.setDay(7, false);
                }
                viewModel.setDay(2, isChecked);
                break;
            case R.id.thu:
                if (!isChecked) {
                    all.setChecked(false);
                    viewModel.setDay(7, false);
                }
                viewModel.setDay(3, isChecked);
                break;
            case R.id.fri:
                if (!isChecked) {
                    all.setChecked(false);
                    viewModel.setDay(7, false);
                }
                viewModel.setDay(4, isChecked);
                break;
            case R.id.sat:
                if (!isChecked) {
                    all.setChecked(false);
                    viewModel.setDay(7, false);
                }
                viewModel.setDay(5, isChecked);
                break;
            case R.id.snd:
                if (!isChecked) {
                    all.setChecked(false);
                    viewModel.setDay(7, false);
                }
                viewModel.setDay(6, isChecked);
                break;
            default:
                break;
        }
    }

    private void checkWeek(boolean check) {
        mo.setChecked(check);
        tue.setChecked(check);
        wed.setChecked(check);
        thu.setChecked(check);
        fri.setChecked(check);
        sat.setChecked(check);
        snd.setChecked(check);
        all.setChecked(check);
    }

    private void setWeek() {
        mo.setChecked(viewModel.getDay(0));
        System.out.println("MONDAY = " + viewModel.getDay(0));
        tue.setChecked(viewModel.getDay(1));
        wed.setChecked(viewModel.getDay(2));
        thu.setChecked(viewModel.getDay(3));
        fri.setChecked(viewModel.getDay(4));
        sat.setChecked(viewModel.getDay(5));
        snd.setChecked(viewModel.getDay(6));
        all.setChecked(viewModel.getDay(7));
    }
}