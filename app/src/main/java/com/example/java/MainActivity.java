package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean dataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 画面全体（activity_main.xmlのルートビュー）をタップで次画面へ遷移
        View root = findViewById(R.id.main_root);
        root.setOnClickListener(v -> {
            if (!dataLoaded) {
                Toast.makeText(MainActivity.this, "読み込み中です…", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
            startActivity(intent);

        });

        loadQuestionsFromContentful();
    }

    private void loadQuestionsFromContentful() {
        new Thread(() -> {
            try {
                // Contentfulクライアントの取得
                CDAClient client = ContentfulManager.getClient();
                // Content ModelのIDが "javaSilverQ" のエントリ一覧を取得
                CDAArray result = client.fetch(CDAEntry.class)
                        .where("content_type", "javaSilverQ")
                        .all();


                List<QuestionData> questionList = new ArrayList<>();
                for (CDAEntry entry : result.entries().values()) {
                    String qid = (String) entry.getField("qid");
                    String chapter = (String) entry.getField("chapter");
                    String code = (String) entry.getField("code");
                    String questionText = (String) entry.getField("questionText");
                    List<String> choices = (List<String>) entry.getField("choices");
                    List<Integer> answerIndex = (List<Integer>) entry.getField("answer");
                    String explanation = (String) entry.getField("explanation");

                    QuestionData q = new QuestionData(qid, chapter, code, questionText, choices, answerIndex, explanation);
                    questionList.add(q);
                }


                // 取得した問題リストをグローバルに保持
                AppDataHolder.setQuestionList(questionList);

                runOnUiThread(() -> {
                    dataLoaded = true;
                    Toast.makeText(MainActivity.this, "データ読み込み完了", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "読み込みエラー: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
