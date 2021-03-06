package by.pavka.memento.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import by.pavka.memento.MementoActivity;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;
import by.pavka.memento.databinding.ActivityIntroductionBinding;
import by.pavka.memento.user.User;
import by.pavka.memento.util.CalendarConverter;
import by.pavka.memento.util.Displayer;
import by.pavka.memento.util.MementoValidator;

public class IntroductionActivity extends MementoActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;

    private MementoApplication application;
    private EditText name;
    private EditText year;
    private Spinner month;
    private RadioGroup genderChoice;
    private RadioButton male;
    private RadioButton female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityIntroductionBinding binding = ActivityIntroductionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        name = binding.name;
        year = binding.year;
        month = binding.month;
        genderChoice = binding.genderChoice;
        male = binding.male;
        female = binding.female;
        Button buttonWeight = binding.buttonWeight;
        buttonWeight.setOnClickListener(this);
        application = (MementoApplication)getApplication();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setInterface();
    }

    @Override
    public void onClick(View v) {
        if (MementoValidator.validateDate(year, month) && MementoValidator.validateGender(genderChoice)) {
            Intent intent = new Intent(this, WeightActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (!MementoValidator.validateDate(year, month)){
            Displayer.showSnackbar(R.string.limit_age, v);
        } else {
            Displayer.showSnackbar(R.string.limit_gender, v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            User user = application.getUser();
            String userName = name.getText().toString();
            user.setName(userName);
            String birthYear = year.getText().toString();
            int birthMonth = month.getSelectedItemPosition();
            String dateOfBirth = birthMonth + "/" + birthYear;
            user.setBirthDate(CalendarConverter.intoCalendar(dateOfBirth));
            int buttonId = genderChoice.getCheckedRadioButtonId();
            int gender;
            switch (buttonId) {
                case R.id.male:
                    gender = 1;
                    break;
                case R.id.female:
                    gender = -1;
                    break;
                default:
                    gender = 0;
                    break;
            }
            user.setGender(gender);
            application.savePersonData(userName, dateOfBirth, gender);
            user.updateTracker(false);
            application.saveHabits();
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    private void setInterface() {
        User user = application.getUser();
        String username = user.getName();
        Calendar birthDate = user.getBirthDate();
        int gender = user.getGender();
        name.setText(username);
        if (birthDate != null) {
            int birthYear = birthDate.get(Calendar.YEAR);
            year.setText(String.valueOf(birthYear));
            int birthMonth = birthDate.get(Calendar.MONTH);
            month.setSelection(birthMonth);
        }
        if (gender == -1) {
            female.setChecked(true);
        }
        if (gender == 1) {
            male.setChecked(true);
        }
    }

}