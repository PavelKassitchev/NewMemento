package by.pavka.memento.profile;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import by.pavka.memento.MementoActivity;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;
import by.pavka.memento.databinding.ActivityQuestionnaireBinding;
import by.pavka.memento.user.User;

public class QuestionnaireActivity extends MementoActivity implements View.OnClickListener {

    private ActivityQuestionnaireBinding binding;
    private QuestionnaireViewModel viewModel;
    private TextView question;
    private Button buttonNext;
    private Button buttonPrev;
    private Button buttonSkip;
    private RadioButton yes;
    private RadioButton no;
    private RadioButton unknown;
    private RadioGroup radioGroup;
    private MementoApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionnaireBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        viewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);
        question = binding.question;
        question.setText(viewModel.getText());
        buttonNext = binding.buttonNext;
        buttonNext.setOnClickListener(this);
        buttonPrev = binding.buttonPrev;
        buttonPrev.setOnClickListener(this);
        buttonSkip = binding.buttonSkip;
        buttonSkip.setOnClickListener(this);
        radioGroup = binding.radioGroup;
        yes = binding.yes;
        no = binding.no;
        unknown = binding.unknown;
        application = (MementoApplication) getApplication();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setReply();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonNext)) {
            obtainReply();
            if (viewModel.getPage() == viewModel.getLength() - 1) {
                saveAnswers();
                finish();
            } else {
                viewModel.next();
                setReply();
                question.setText(viewModel.getText());
            }
        } else if (v.equals(buttonPrev)) {
            if (viewModel.getPage() == 0) {
                setResult(RESULT_CANCELED);
                finish();
            } else {
                viewModel.prev();
                setReply();
                question.setText(viewModel.getText());
            }

        } else {
            obtainReply();
            saveAnswers();
            finish();
        }
    }

    private void setReply() {
        int reply = viewModel.getReply();
        switch (reply) {
            case -1:
                no.setChecked(true);
                break;
            case 1:
                yes.setChecked(true);
                break;
            default:
                unknown.setChecked(true);
                break;
        }
        if (viewModel.getPage() == viewModel.getLength() - 1) {
            buttonNext.setText(getResources().getString(R.string.save));
            buttonSkip.setEnabled(false);
        } else {
            buttonNext.setText(getResources().getString(R.string.next));
            buttonSkip.setEnabled(true);
        }
    }

    private void saveAnswers() {
        User user = application.getUser();
        int[] answers = viewModel.getAnswers();
        user.setAnswers(answers);
        application.saveAnswers(answers);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }

    private void obtainReply() {
        int buttonId = radioGroup.getCheckedRadioButtonId();
        switch (buttonId) {
            case R.id.yes:
                viewModel.setReply(1);
                break;
            case R.id.no:
                viewModel.setReply(-1);
                break;
            default:
                viewModel.setReply(0);
        }
    }
}