package ru.rofleksey.intflex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.rofleksey.intflex.runtime.IntFlex;
import ru.rofleksey.intflex.runtime.Result;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

        button.setOnClickListener((v) -> {
            IntFlex.execute(editText.getText().toString(), new IntFlex.IntFlexCallback() {


                @Override
                public void onDone(Result result) {
                    handler.post(() -> {
                        textView.setText("OK: " + result.toString());
                    });

                }

                @Override
                public void onPercentage(float percent) {

                }

                @Override
                public void onError(Exception e) {
                    handler.post(() -> {
                        textView.setText("ERROR: " + e.getMessage());
                    });
                }
            });
        });
    }
}
