package by.pavka.memento;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import by.pavka.memento.databinding.ActivityWeightBinding;
import by.pavka.memento.util.Displayer;
import by.pavka.memento.util.MementoValidator;

public class WeightActivity extends MementoActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private EditText weight;
    private EditText height;
    private TextView bmi;
    private Button calculate;
    private Button forward;
    private SeekBar seekBar;
    private double bodyMassIndex;
    private WeightViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWeightBinding binding = ActivityWeightBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        viewModel = new ViewModelProvider(this).get(WeightViewModel.class);
        weight = binding.weight;
        height = binding.height;
        bmi = binding.bmi;
        calculate = binding.calculate;
        calculate.setOnClickListener(this);
        forward = binding.forward;
        forward.setOnClickListener(this);
        seekBar = binding.seekBar;
        seekBar.setEnabled(false);
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setInterface();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            double w = Double.parseDouble(weight.getText().toString());
            double h = Double.parseDouble(height.getText().toString());
            viewModel.resetBMIData(w, h);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.calculate:
                if (MementoValidator.validateWeight(weight) && MementoValidator.validateHeight(height)) {
                    viewModel.recalculateBMI(Double.parseDouble(weight.getText().toString()), Double.parseDouble(height.getText().toString()));
                    setInterface();
                } else {
                    Displayer.showSnackbar(R.string.bmi_valid, v);
                }
                break;
            case R.id.forward:
                if (MementoValidator.validateWeight(weight) && MementoValidator.validateHeight(height)) {
                    viewModel.recalculateBMI(Double.parseDouble(weight.getText().toString()), Double.parseDouble(height.getText().toString()));
                    setInterface();
                    Intent intent = new Intent(this, QuestionnaireActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Displayer.showSnackbar(R.string.bmi_valid, v);
                }
                break;
            default:
        }
    }

    private void setScale() {
        bodyMassIndex = viewModel.getBmi();
        if (bodyMassIndex != 0) {
            bmi.setText(String.format(getResources().getString(R.string.bmi), bodyMassIndex));
        } else {
            bmi.setText("");
        }
        seekBar.setProgress((int) (bodyMassIndex - 14));
    }

    private void setInterface() {
        double w = viewModel.getWeight();
        double h = viewModel.getHeight();
        if (w != 0 && h != 0) {
            weight.setText(String.valueOf(w));
            height.setText(String.valueOf(h));
        }
        setScale();
    }
}