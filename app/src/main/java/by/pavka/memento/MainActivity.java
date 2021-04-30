package by.pavka.memento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import by.pavka.memento.calculator.LifeSpanCalculator;
import by.pavka.memento.calculator.PreCalculator;
import by.pavka.memento.calculator.Questionnaire;
import by.pavka.memento.calculator.impl.LifeSpanCalculatorImpl;
import by.pavka.memento.calculator.impl.PreCalculatorImpl;
import by.pavka.memento.databinding.ActivityMainBinding;
import by.pavka.memento.user.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1;
    private TextView header;
    private TextView forecast;
    private Button buttonUpdate;
    private Button buttonClear;
    private MementoApplication application;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        header = binding.header;
        forecast = binding.forecast;
        buttonUpdate = binding.buttonUpdate;
        buttonUpdate.setOnClickListener(this);
        buttonClear = binding.buttonClear;
        buttonClear.setOnClickListener(this);
        application = (MementoApplication) getApplication();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreCalculator preCalculator = new PreCalculatorImpl();
        LifeSpanCalculator calculator = new LifeSpanCalculatorImpl();
        User user = application.getUser();
        Calendar birthDate = user.getBirthDate();
        Calendar end = null;
        if (birthDate != null) {
            int gender = user.getGender();
            Questionnaire questionnaire = application.getQuestionnaire();
            int[] answers = user.getAnswers();
            int weight = user.getWeight();
            int height = user.getHeight();
            end = calculator.tuneLifeDaySpan(gender, birthDate, weight, height,null, preCalculator, questionnaire, answers);
            setButtons(true);
        } else {
            setButtons(false);
        }
        setForecast(end);
        setHeader(user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_update:
                Intent intent = new Intent(this, IntroductionActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.button_clear:
                clearUser();
                setHeader(null);
                setForecast(null);
                setButtons(false);
                break;
        }
    }

    private void clearUser() {
        application.setUser(null);
        application.clear();
    }

    private void setHeader(User user) {
        String rawText = getResources().getString(R.string.header_main);
        String userName = (user == null || user.getName().equals(""))? getResources().getString(R.string.username) : user.getName();
        String head = String.format(rawText, userName);
        header.setText(head);
    }

    private void setForecast(Calendar calendar) {
        if (calendar == null) {
            forecast.setText(getResources().getString(R.string.general));
        } else {
            String rawText = getResources().getString(R.string.forecast);
            Calendar cal = ((MementoApplication) getApplication()).getUser().getBirthDate();
            String date;
            if (cal != null) {
                int month = calendar.get(Calendar.MONTH);
                date = getResources().getStringArray(R.array.month)[month] + " " + calendar.get(Calendar.YEAR);
            } else {
                date = "somewhere";
            }
            String forecaster = String.format(rawText, date);
            forecast.setText(forecaster);
        }
    }

    private void setButtons(boolean status) {
        buttonClear.setEnabled(status);
        if(status) {
            buttonUpdate.setText(getResources().getString(R.string.update));
        } else {
            buttonUpdate.setText(getResources().getString(R.string.start));
        }
    }
}