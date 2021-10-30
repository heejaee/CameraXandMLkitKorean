package com.example.cameraxandmlkitkorean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.util.List;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;
public class TestResultActivity extends AppCompatActivity {
    private TextView mTextResult;

    private TextToSpeech tts;

    private ImageButton button,fast, slow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

            button = (ImageButton) findViewById(R.id.button);
            slow = (ImageButton) findViewById(R.id.slow);
            fast = (ImageButton) findViewById(R.id.fast);

            TextView tv = findViewById(R.id.textResult2);

            Intent secondIntent = getIntent();
            String message = secondIntent.getStringExtra("key");
            tv.setText(message);
            tv.setMovementMethod(new ScrollingMovementMethod());



        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // editText에 있는 문장을 읽는다.
                tts.speak(message,TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.setPitch(1.0f);         // 음성 톤은 기본 설정
                tts.setSpeechRate(2.0f);    // 읽는 속도를 2배 빠르기로 설정

                tts.speak(message,TextToSpeech.QUEUE_FLUSH, null);
            }
        });


        slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.setPitch(1.0f);         // 음성 톤은 기본 설정
                tts.setSpeechRate(0.5f);    // 읽는 속도를 0.5빠르기로 설정

                tts.speak(message,TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        }









    }



