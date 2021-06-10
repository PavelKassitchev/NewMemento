package by.pavka.memento;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.Calendar;

import by.pavka.memento.databinding.ActivityWeightBinding;
import by.pavka.memento.user.User;
import by.pavka.memento.util.Validator;

public class WeightActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private MementoApplication application;
    private EditText weight;
    private EditText height;
    private TextView bmi;
    private Button calculate;
    private Button forward;
    private SeekBar seekBar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWeightBinding binding = ActivityWeightBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
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
        setInterface();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int w = Integer.parseInt(weight.getText().toString());
            user.setWeight(w);
            int h = Integer.parseInt(height.getText().toString());
            user.setHeight(h);
            application.saveBMIData(w, h);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.calculate:
                //Just to try gson
                SharedPreferences.Editor editor = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE).edit();
                GsonBuilder builder = new GsonBuilder();
                builder.enableComplexMapKeySerialization();
                builder.setDateFormat("MM-dd-yyyy");
                Gson gson = builder.create();
                String date = gson.toJson(Calendar.getInstance());
                Log.d("MYSTERY", "Date = " + date);
                //TODO
                break;
            case R.id.forward:
                if (validateHeight() && validateWeight()) {
                    Intent intent = new Intent(this, QuestionnaireActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Validator.showSnackbar(R.string.bmi_valid, v);
                }
                break;
            default:
        }
    }

    private void setInterface() {
        int w = user.getWeight();
        int h = user.getHeight();
        if (w != 0 && h != 0) {
            weight.setText(String.valueOf(w));
            height.setText(String.valueOf(h));
        }
    }

    private boolean validateWeight() {
        if (weight.getText().toString().isEmpty()) {
            return false;
        }
        int w = Integer.parseInt(weight.getText().toString());
        if (w < 2) {
          return false;
        }
        return true;
    }

    private boolean validateHeight() {
        if (height.getText().toString().isEmpty()) {
            return false;
        }
        int h = Integer.parseInt(height.getText().toString());
        if (h < 30) {
            return false;
        }
        return true;
    }
}