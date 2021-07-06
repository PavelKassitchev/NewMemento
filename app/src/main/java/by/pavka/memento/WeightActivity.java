package by.pavka.memento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import by.pavka.memento.databinding.ActivityWeightBinding;
import by.pavka.memento.user.User;
import by.pavka.memento.util.Displayer;

public class WeightActivity extends MementoActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private MementoApplication application;
    private EditText weight;
    private EditText height;
    private TextView bmi;
    private Button calculate;
    private Button forward;
    private SeekBar seekBar;
    private User user;
    private double bodyMassIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWeightBinding binding = ActivityWeightBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        weight = binding.weight;
        height = binding.height;
        bmi = binding.bmi;
        calculate = binding.calculate;
        calculate.setOnClickListener(this);
        forward = binding.forward;
        forward.setOnClickListener(this);
        seekBar = binding.seekBar;
        seekBar.setEnabled(false);
        application = (MementoApplication) getApplication();
        user = application.getUser();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        if (savedInstanceState != null) {
            bodyMassIndex = savedInstanceState.getDouble("BMI");
            Log.d("BMI", "bmi = " + bodyMassIndex);
        }
        setInterface();
        Log.d("BMI", "after onCreate weight = " + weight.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BMI", "after onStart weight = " + weight.getText().toString());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("BMI", bodyMassIndex);
        Log.d("BMI", "writing in bundle bmi = " + bodyMassIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            double w = Double.parseDouble(weight.getText().toString());
            user.setWeight(w);
            double h = Double.parseDouble(height.getText().toString());
            user.setHeight(h);
            application.saveBMIData(w, h);
            user.getChronicler().addRecord(Calendar.getInstance(), w);
            application.saveChronicler();
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.calculate:
                bodyMassIndex = calculateBMI();
                if (bodyMassIndex != 0) {
                    bmi.setText(String.format(getResources().getString(R.string.bmi), bodyMassIndex));
                } else {
                    bmi.setText("");
                }
                seekBar.setProgress((int) (bodyMassIndex - 14));
                break;
            case R.id.forward:
                if (validateHeight() && validateWeight()) {
                    Intent intent = new Intent(this, QuestionnaireActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Displayer.showSnackbar(R.string.bmi_valid, v);
                }
                break;
            default:
        }
    }

    private double calculateBMI() {
        if (!weight.getText().toString().isEmpty() && !height.getText().toString().isEmpty()) {
            try {
                double wt = Double.parseDouble(weight.getText().toString());
                Log.d("BMI", "while calculating bmi weight = " + wt);
                double ht = Double.parseDouble(height.getText().toString());
                if (wt == 0 || ht == 0) {
                    return 0;
                }
                return wt * 10000 / ht / ht;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private void setScale() {
        if (bodyMassIndex == 0) {
            bodyMassIndex = calculateBMI();
        }
        if (bodyMassIndex != 0) {
            bmi.setText(String.format(getResources().getString(R.string.bmi), bodyMassIndex));
        } else {
            bmi.setText("");
        }
        seekBar.setProgress((int) (bodyMassIndex - 14));
    }

    private void setInterface() {
        double w = user.getWeight();
        double h = user.getHeight();
        Log.d("BMI", "weight: " + w + " height: " + h + " condition = " + (w != 0 && h != 0));
        if (w != 0 && h != 0) {
            Log.d("BMI", "w = " + w);
            weight.setText(String.valueOf(w));
            Log.d("BMI", "weight view set on " + weight.getText().toString());
            height.setText(String.valueOf(h));
        }
        setScale();
    }

    private boolean validateWeight() {
        if (weight.getText().toString().isEmpty()) {
            return false;
        }
        double w = 0;
        try {
            w = Double.parseDouble(weight.getText().toString());
        } catch (NumberFormatException e) {
            return false;
        }
        if (w < 2) {
            return false;
        }
        return true;
    }

    private boolean validateHeight() {
        if (height.getText().toString().isEmpty()) {
            return false;
        }
        double h = 0;
        try {
            h = Double.parseDouble(height.getText().toString());
        } catch (NumberFormatException e) {
            return false;
        }
        if (h < 30) {
            return false;
        }
        return true;
    }
}