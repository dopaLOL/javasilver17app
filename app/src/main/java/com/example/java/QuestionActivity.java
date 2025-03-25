package com.example.java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionActivity extends AppCompatActivity {
    private TextView textChapter, textCode, textQuestion, textExplanation;
    private RadioGroup radioGroupChoices;
    private LinearLayout checkBoxLayout; // チェックボックス用レイアウト
    private Button buttonCheck, buttonNext;
    private List<QuestionData> questionList;
    private int currentIndex = 0; // 現在の問題のインデックス
    private boolean isMultipleChoice = false; // 選択肢の形式（単一 or 複数）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        textChapter = findViewById(R.id.textChapter);
        textCode = findViewById(R.id.textCode);
        textQuestion = findViewById(R.id.textQuestion);
        radioGroupChoices = findViewById(R.id.radioGroupChoices);
        checkBoxLayout = findViewById(R.id.checkBoxLayout); // チェックボックス用レイアウト
        buttonCheck = findViewById(R.id.buttonCheck);
        textExplanation = findViewById(R.id.textExplanation);
        buttonNext = findViewById(R.id.buttonNext);

        questionList = AppDataHolder.getQuestionList();
        if (questionList != null && !questionList.isEmpty()) {
            showQuestion(currentIndex);
        } else {
            textQuestion.setText("問題がありません");
            buttonCheck.setEnabled(false);
            buttonNext.setEnabled(false);
        }

        buttonCheck.setOnClickListener(v -> checkAnswer());
        buttonNext.setOnClickListener(v -> showNextQuestion());
    }

    private void showQuestion(int index) {
        QuestionData q = questionList.get(index);
        textChapter.setText(q.getChapter());
        textCode.setText(q.getCode());
        textQuestion.setText(q.getQuestionText());

        List<String> choices = q.getChoices();
        List<Integer> correctIndexes = q.getAnswerIndex();

        isMultipleChoice = correctIndexes.size() > 1; // 正解が複数なら複数選択問題

        // 以前の選択肢をクリア
        radioGroupChoices.removeAllViews();
        checkBoxLayout.removeAllViews();

        if (isMultipleChoice) {
            radioGroupChoices.setVisibility(View.GONE);
            checkBoxLayout.setVisibility(View.VISIBLE);

            for (int i = 0; i < choices.size(); i++) {
                CheckBox cb = new CheckBox(this);
                cb.setId(View.generateViewId());
                cb.setText(choices.get(i));
                checkBoxLayout.addView(cb);
            }
        } else {
            radioGroupChoices.setVisibility(View.VISIBLE);
            checkBoxLayout.setVisibility(View.GONE);

            for (int i = 0; i < choices.size(); i++) {
                RadioButton rb = new RadioButton(this);
                rb.setId(View.generateViewId());
                rb.setText(choices.get(i));
                radioGroupChoices.addView(rb);
            }
        }

        textExplanation.setVisibility(View.GONE);
        buttonNext.setVisibility(View.GONE); // 解答前は「次へ」ボタンを非表示
    }

    private void checkAnswer() {
        List<Integer> correctIndexes = questionList.get(currentIndex).getAnswerIndex();
        boolean isCorrect;

        if (isMultipleChoice) {
            Set<Integer> selectedIndexes = new HashSet<>();
            for (int i = 0; i < checkBoxLayout.getChildCount(); i++) {
                CheckBox cb = (CheckBox) checkBoxLayout.getChildAt(i);
                if (cb.isChecked()) {
                    selectedIndexes.add(i);
                }
            }

            isCorrect = selectedIndexes.equals(new HashSet<>(correctIndexes));
        } else {
            int checkedId = radioGroupChoices.getCheckedRadioButtonId();
            if (checkedId == -1) {
                Toast.makeText(this, "選択肢を選んでください", Toast.LENGTH_SHORT).show();
                return;
            }

            int indexSelected = -1;
            for (int i = 0; i < radioGroupChoices.getChildCount(); i++) {
                if (radioGroupChoices.getChildAt(i).getId() == checkedId) {
                    indexSelected = i;
                    break;
                }
            }

            isCorrect = correctIndexes.contains(indexSelected);
        }

        if (isCorrect) {
            textExplanation.setText("正解です！\n" + questionList.get(currentIndex).getExplanation());
        } else {
            textExplanation.setText("不正解です…\n" + questionList.get(currentIndex).getExplanation());
        }
        textExplanation.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE); // 解答後に「次へ」ボタンを表示
    }

    private void showNextQuestion() {
        if (currentIndex < questionList.size() - 1) {
            currentIndex++;
            showQuestion(currentIndex);
        } else {
            Toast.makeText(this, "最後の問題です", Toast.LENGTH_SHORT).show();
        }
    }
}
