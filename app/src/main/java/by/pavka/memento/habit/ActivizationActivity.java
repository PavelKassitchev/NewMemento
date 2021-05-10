package by.pavka.memento.habit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.Calendar;

import by.pavka.memento.BottomNavigationListener;
import by.pavka.memento.R;
import by.pavka.memento.databinding.ActivityActivizationBinding;

public class ActivizationActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private ActivizationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityActivizationBinding binding = ActivityActivizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this).get(ActivizationViewModel.class);
        Intent intent = getIntent();
        if (intent != null) {
            Habit habit = (Habit)intent.getSerializableExtra("habit");
            viewModel.setHabit(habit);
        }
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.action_dial);
        item.setChecked(true);

        Button buttonClean = binding.buttonClean;
        buttonClean.setOnClickListener(this);
        Button buttonCancel = binding.buttonCancel;
        buttonCancel.setOnClickListener(this);
        Button buttonOk = binding.buttonOk;
        buttonOk.setOnClickListener(this);

        Button endDay = binding.endDay;
        endDay.setOnClickListener(this);
        endDay.setText(viewModel.getEnd().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                viewModel.setHabit(null);
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.button_ok:
                Intent intent = new Intent();
                viewModel.resetProgress();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.end_day:
                LocalDate now = LocalDate.now();
                new DatePickerDialog(this, this, now.getYear(), now.getMonthValue(), now.getDayOfMonth()).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}