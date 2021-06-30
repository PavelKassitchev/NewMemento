package by.pavka.memento;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import by.pavka.memento.habit.Habit;
import by.pavka.memento.user.User;

public class MainActivity extends MementoActivity implements View.OnClickListener, DialogInterface.OnClickListener {

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
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        header = binding.header;
        forecast = binding.forecast;
        buttonUpdate = binding.buttonUpdate;
        buttonUpdate.setOnClickListener(this);
        buttonClear = binding.buttonClear;
        buttonClear.setOnClickListener(this);
        application = (MementoApplication) getApplication();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        successHabit(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreCalculator preCalculator = new PreCalculatorImpl();
        LifeSpanCalculator calculator = new LifeSpanCalculatorImpl();
        User user = application.getUser();
        Calendar birthDate = user.getBirthDate();
        Calendar end = null;
        Log.d("PROF", "Now " + birthDate + " " + user.isHabitCustomized());
        if (birthDate != null) {
            int gender = user.getGender();
            Questionnaire questionnaire = application.getQuestionnaire();
            int[] answers = user.getAnswers();
            double weight = user.getWeight();
            double height = user.getHeight();
            end = calculator.tuneLifeDaySpan(gender, birthDate, weight, height, null, preCalculator, questionnaire, answers);
            setButtons(true);
        } else if (user.isHabitCustomized() && !user.allHabitsClear()) {
            setButtons(true);
            buttonUpdate.setText(getResources().getString(R.string.start));
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
                if (application.getUser().getBirthDate() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.clearance)).setMessage(getString(R.string.confidence))
                            .setNegativeButton(getString(R.string.cancel), this)
                            .setNeutralButton(getString(R.string.quest_only), this)
                            .setPositiveButton(getString(R.string.clear_all), this);
                    builder.create().show();
                } else {
                    if (application.getUser().isHabitCustomized()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(getString(R.string.clearance)).setMessage(getString(R.string.confidence_short))
                                .setNegativeButton(getString(R.string.cancel), this)
                                .setPositiveButton(getString(R.string.clear_all), this);
                        builder.create().show();
                    } else {
                        clearUser();
                        setHeader(null);
                        setForecast(null);
                        setButtons(false);
                    }
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        successHabit(intent);
    }

    private void clearUser() {
        application.setUser(null);
        application.clear();
    }

    private void setHeader(User user) {
        String rawText = getResources().getString(R.string.header_main);
        String userName = (user == null || user.getName().equals("")) ? getResources().getString(R.string.username) : user.getName();
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
        if (status) {
            buttonUpdate.setText(getResources().getString(R.string.update));
        } else {
            buttonUpdate.setText(getResources().getString(R.string.start));
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                clearUserQuestionnaire();
                application.clearQuestionnaireData();
                application.saveHabits();
                setHeader(null);
                setForecast(null);
                setButtons(false);
                break;
            case DialogInterface.BUTTON_POSITIVE:
                clearUser();
                setHeader(null);
                setForecast(null);
                setButtons(false);
        }
    }

    private void clearUserQuestionnaire() {
        application.getUser().cleanQuestionnaire();
    }

    private void successHabit(Intent intent) {
        if (intent != null && intent.getSerializableExtra("habit") != null) {
            Habit habit = (Habit) intent.getSerializableExtra("habit");
            User user = application.getUser();

            user.setHabitCustomized(true);
            application.customizeHabits(true);
            int[] answers = user.getAnswers();
            answers[habit.getQuestion()] = -habit.getBetter();
            user.setAnswers(answers);
            application.saveAnswers(answers);
            user.getTracker().updateWithAnswers(answers, false);
            application.saveHabits();

            Calendar birthDate = user.getBirthDate();
            Calendar end = null;
            if (birthDate != null) {
                PreCalculator preCalculator = new PreCalculatorImpl();
                LifeSpanCalculator calculator = new LifeSpanCalculatorImpl();
                end = calculator.tuneLifeDaySpan(user.getGender(), birthDate, user.getWeight(), user.getHeight(),
                        null, preCalculator, application.getQuestionnaire(), answers);
                setButtons(true);
            } else {
                setButtons(false);
            }
            setForecast(end);
            setHeader(user);
        }
    }
}