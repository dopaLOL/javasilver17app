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
    // 画面のUI要素
    private TextView textChapter, textCode, textQuestion, textExplanation;
    private RadioGroup radioGroupChoices;
    private LinearLayout checkBoxLayout; // チェックボックス用レイアウト
    private Button buttonCheck, buttonNext;

    // 問題データ関連
    private List<QuestionData> questionList;
    private int currentIndex = 0; // 現在の問題のインデックス
    private boolean isMultipleChoice = false; // 選択肢の形式（単一 or 複数）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // UI要素の取得
        textChapter = findViewById(R.id.textChapter);
        textCode = findViewById(R.id.textCode);
        textQuestion = findViewById(R.id.textQuestion);
        radioGroupChoices = findViewById(R.id.radioGroupChoices);
        checkBoxLayout = findViewById(R.id.checkBoxLayout); // チェックボックス用レイアウト
        buttonCheck = findViewById(R.id.buttonCheck);
        textExplanation = findViewById(R.id.textExplanation);
        buttonNext = findViewById(R.id.buttonNext);

        // 問題リストの取得（AppDataHolderから）
        questionList = AppDataHolder.getQuestionList();

        // 問題リストが存在し、空でない場合
        if (questionList != null && !questionList.isEmpty()) {
            // 最初の問題を表示
            showQuestion(currentIndex);
        } else {
            // 問題がない場合の処理
            textQuestion.setText("問題がありません");
            buttonCheck.setEnabled(false);
            buttonNext.setEnabled(false);
        }

        // 「回答」ボタンのクリックリスナー
        buttonCheck.setOnClickListener(v -> checkAnswer());
        // 「次へ」ボタンのクリックリスナー
        buttonNext.setOnClickListener(v -> showNextQuestion());
    }

    // 問題を表示するメソッド
    private void showQuestion(int index) {
        // 指定されたインデックスの問題を取得
        QuestionData q = questionList.get(index);

        // UI要素に問題データをセット
        textChapter.setText(q.getChapter());
        textCode.setText(q.getCode());
        textQuestion.setText(q.getQuestionText());

        // 選択肢のリストを取得
        List<String> choices = q.getChoices();
        // 正解のインデックスリストを取得
        List<Integer> correctIndexes = q.getAnswerIndex();

        // 正解が複数あるかどうかで、複数選択問題かどうかを判定
        isMultipleChoice = correctIndexes.size() > 1; // 正解が複数なら複数選択問題

        // 以前の選択肢をクリア
        radioGroupChoices.removeAllViews();
        checkBoxLayout.removeAllViews();

        // 複数選択問題の場合
        if (isMultipleChoice) {
            // ラジオボタンを非表示、チェックボックスを表示
            radioGroupChoices.setVisibility(View.GONE);
            checkBoxLayout.setVisibility(View.VISIBLE);

            // チェックボックスを動的に生成して追加
            for (int i = 0; i < choices.size(); i++) {
                CheckBox cb = new CheckBox(this);
                cb.setId(View.generateViewId()); // 一意のIDを生成
                cb.setText(choices.get(i));
                checkBoxLayout.addView(cb);
            }
        } else {
            // 単一選択問題の場合
            // チェックボックスを非表示、ラジオボタンを表示
            radioGroupChoices.setVisibility(View.VISIBLE);
            checkBoxLayout.setVisibility(View.GONE);

            // ラジオボタンを動的に生成して追加
            for (int i = 0; i < choices.size(); i++) {
                RadioButton rb = new RadioButton(this);
                rb.setId(View.generateViewId()); // 一意のIDを生成
                rb.setText(choices.get(i));
                radioGroupChoices.addView(rb);
            }
        }

        // 解説文を非表示
        textExplanation.setVisibility(View.GONE);
        // 「次へ」ボタンを非表示（解答前）
        buttonNext.setVisibility(View.GONE);
    }

    // 回答をチェックするメソッド
    private void checkAnswer() {
        // 現在の問題の正解インデックスリストを取得
        List<Integer> correctIndexes = questionList.get(currentIndex).getAnswerIndex();
        boolean isCorrect;

        // 複数選択問題の場合
        if (isMultipleChoice) {
            // 選択されたチェックボックスのインデックスを格納するSet
            Set<Integer> selectedIndexes = new HashSet<>();
            // チェックボックスレイアウト内のすべてのチェックボックスをチェック
            for (int i = 0; i < checkBoxLayout.getChildCount(); i++) {
                CheckBox cb = (CheckBox) checkBoxLayout.getChildAt(i);
                // チェックされている場合、インデックスをSetに追加
                if (cb.isChecked()) {
                    selectedIndexes.add(i);
                }
            }

            // 選択されたインデックスのSetと正解インデックスのSetが一致するかどうかで正誤を判定
            isCorrect = selectedIndexes.equals(new HashSet<>(correctIndexes));
        } else {
            // 単一選択問題の場合
            // 選択されたラジオボタンのIDを取得
            int checkedId = radioGroupChoices.getCheckedRadioButtonId();
            // 何も選択されていない場合
            if (checkedId == -1) {
                Toast.makeText(this, "選択肢を選んでください", Toast.LENGTH_SHORT).show();
                return;
            }

            // 選択されたラジオボタンのインデックスを取得
            int indexSelected = -1;
            for (int i = 0; i < radioGroupChoices.getChildCount(); i++) {
                if (radioGroupChoices.getChildAt(i).getId() == checkedId) {
                    indexSelected = i;
                    break;
                }
            }

            // 選択されたインデックスが正解インデックスリストに含まれているかどうかで正誤を判定
            isCorrect = correctIndexes.contains(indexSelected);
        }

        // 正誤に応じて解説文を設定
        if (isCorrect) {
            textExplanation.setText("正解です！\n" + questionList.get(currentIndex).getExplanation());
        } else {
            textExplanation.setText("不正解です…\n" + questionList.get(currentIndex).getExplanation());
        }
        // 解説文を表示
        textExplanation.setVisibility(View.VISIBLE);
        // 「次へ」ボタンを表示（解答後）
        buttonNext.setVisibility(View.VISIBLE);
    }

    // 次の問題を表示するメソッド
    private void showNextQuestion() {
        // まだ問題が残っている場合
        if (currentIndex < questionList.size() - 1) {
            // インデックスをインクリメント
            currentIndex++;
            // 次の問題を表示
            showQuestion(currentIndex);
        } else {
            // 最後の問題の場合
            Toast.makeText(this, "最後の問題です", Toast.LENGTH_SHORT).show();
        }
    }
}
