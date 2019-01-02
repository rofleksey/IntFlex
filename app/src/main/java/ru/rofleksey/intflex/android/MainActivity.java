package ru.rofleksey.intflex.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import ru.rofleksey.intflex.R;
import ru.rofleksey.intflex.misc.Util;
import ru.rofleksey.intflex.parser.TokenType;
import ru.rofleksey.intflex.runtime.IntFlex;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    TextView codeView;
    FlowLayout codeButtons;
    Button runButton, delButton;

    Typeface hackRegular;
    ArrayList<TokenRecord> tokensArr = new ArrayList<>();

    Map<TokenType, Integer> tokenColorMap = new EnumMap<>(TokenType.class);
    int defaultTokenColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hackRegular = Typeface.createFromAsset(getAssets(), "fonts/hack-regular.ttf");
        codeView = findViewById(R.id.code);
        codeView.setTypeface(hackRegular);
        codeButtons = findViewById(R.id.code_buttons);
        runButton = findViewById(R.id.buttonRun);
        delButton = findViewById(R.id.buttonDel);

        delButton.setOnClickListener(new CodeButtonListener() {
            @Override
            void operate() {
                tokensArr.remove(tokensArr.size() - 1);
            }
        });

        runButton.setOnClickListener((v) -> {
            String realInput = getCurString(false);
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(ResultActivity.INPUT_CODE, realInput);
            startActivity(intent);
        });

        Resources resources = getResources();

        defaultTokenColor = resources.getColor(R.color.colorWhite);
        tokenColorMap.put(TokenType.AT, resources.getColor(R.color.codePurple));
        tokenColorMap.put(TokenType.ASSIGN_FLEX, resources.getColor(R.color.codePurple));
        tokenColorMap.put(TokenType.IDENTIFIER, resources.getColor(R.color.codeYellow));
        tokenColorMap.put(TokenType.NUMBER, resources.getColor(R.color.codeOrange));
        tokenColorMap.put(TokenType.DOTS, resources.getColor(R.color.codeGreen));
        tokenColorMap.put(TokenType.POTENTIAL, resources.getColor(R.color.codeAqua));
        tokenColorMap.put(TokenType.COMMA, resources.getColor(R.color.codeAqua));

        recreateButtons();
    }

    Button createButton(TokenType type) {
        Button b = new Button(this);
        //b.setLayoutParams(new FlowLayout.LayoutParams(20, ViewGroup.LayoutParams.WRAP_CONTENT));
        b.setTextSize(10);
        b.setTypeface(hackRegular);
        b.setText(type.toString());
        addButtonListener(b, type);
        return b;
    }

    String getCurString(boolean includeCarret) {
        StringBuilder builder = new StringBuilder();
        if (!tokensArr.isEmpty()) {
            TokenRecord last;
            builder.append(last = tokensArr.get(0));
            last.setFrom(0);
            int cur = last.length();
            for (int i = 1; i < tokensArr.size(); i++) {
                if (last.type != TokenType.LINE_BREAK) {
                    builder.append(" ");
                    cur++;
                }
                builder.append(last = tokensArr.get(i));
                last.setFrom(cur);
                cur += last.length();
            }
        }
        if (includeCarret) {
            builder.append(needSpaceBeforeCarret() ? " " : "").append("_");
        }
        return builder.toString();
    }

    SpannableString getCurInput() {
        String baseString = getCurString(true);
        SpannableString result = new SpannableString(baseString);
        for (TokenRecord r : tokensArr) {
            r.apply(result);
        }
        return result;
    }

    boolean needSpaceBeforeCarret() {
        return !tokensArr.isEmpty() && tokensArr.get(tokensArr.size() - 1).type != TokenType.LINE_BREAK;
    }

    void recreateButtons() {
        TokenType[] types = IntFlex.getNext(getCurString(true));
        codeButtons.removeAllViews();
        if (types == null) {
            types = new TokenType[]{TokenType.POTENTIAL};
        }
        for (TokenType t : types) {
            if (t != TokenType.EOF) {
                Button b = createButton(t);
                codeButtons.addView(b);
                ViewGroup.LayoutParams params = b.getLayoutParams();
                params.width = Util.dpToPixels(50, this);
                b.setLayoutParams(params);
            }
        }
        delButton.setEnabled(!tokensArr.isEmpty());
        runButton.setEnabled(Arrays.asList(types).contains(TokenType.EOF));
    }

    void openWindow(int what, WindowCallback checker) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(what, null);
        PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        Button buttonOK = view.findViewById(R.id.buttonOK);
        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        EditText editText = view.findViewById(R.id.editText);
        buttonOK.setTypeface(hackRegular);
        buttonCancel.setTypeface(hackRegular);
        editText.setTypeface(hackRegular);
        InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (inputMgr != null) {
                    inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    inputMgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        checker.initEditText(editText);
        buttonCancel.setOnClickListener((v) -> {
            window.dismiss();
        });
        buttonOK.setOnClickListener((v) -> {
            String text = editText.getText().toString();
            String error = checker.check(text);
            if (error == null) {
                checker.onDone(text);
                window.dismiss();
            } else {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
        window.setOnDismissListener(() -> {
            if (inputMgr != null) {
                inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                inputMgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        LinearLayout popupBackground = view.findViewById(R.id.popupBackground);
        popupBackground.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        window.setAnimationStyle(R.style.alpha_change_anim);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
        editText.requestFocus();
    }

    void addButtonListener(Button b, TokenType t) {
        if (t == TokenType.IDENTIFIER) {
            b.setOnClickListener(new PopupButtonListener(R.layout.popup_enter_variable, t) {
                @Override
                String checkImpl(String text) {
                    if (!text.matches("[a-zA-Z][a-zA-Z0-9]*")) {
                        return "Input is not a valid variable";
                    }
                    return null;
                }
            });
        } else if (t == TokenType.NUMBER) {
            b.setOnClickListener(new PopupButtonListener(R.layout.popup_enter_variable, t) {
                @Override
                String checkImpl(String text) {
                    if (!text.matches("\\d+(\\.\\d+)?")) {
                        return "Input is not a valid number";
                    }
                    return null;
                }

                @Override
                public void init(EditText editText) {
                    editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            });
        } else if (t == TokenType.LINE_BREAK) {
            b.setOnClickListener(new CodeButtonListener() {
                @Override
                void operate() {
                    tokensArr.add(new TokenRecord("\n", t));
                }
            });
        } else {
            b.setOnClickListener(new CodeButtonListener() {
                @Override
                void operate() {
                    tokensArr.add(new TokenRecord(t.toString(), t));
                }
            });
        }
    }

    interface WindowCallback {
        String check(String text);

        void onDone(String text);

        void initEditText(EditText editText);
    }

    class TokenRecord {
        private final TokenType type;
        private final String text;
        boolean hasBackgroundColor;
        private int from;
        private int color, backgroundColor;

        TokenRecord(String text, TokenType type) {
            this.text = text;
            this.type = type;
            this.color = tokenColorMap.containsKey(type) ? tokenColorMap.get(type) : defaultTokenColor;
        }

        void setFrom(int from) {
            this.from = from;
        }

        void apply(SpannableString s) {
            s.setSpan(new ForegroundColorSpan(color), from, from + text.length(), 0);
        }

        int length() {
            return text.length();
        }

        @Override
        public String toString() {
            return text;
        }
    }

    abstract class CodeButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            operate();
            SpannableString in = getCurInput();
            codeView.setText(in, TextView.BufferType.SPANNABLE);
            recreateButtons();
        }

        abstract void operate();
    }

    abstract class PopupButtonListener implements View.OnClickListener {
        final int what;
        final TokenType type;

        PopupButtonListener(int what, TokenType type) {
            this.what = what;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            openWindow(what, new WindowCallback() {
                @Override
                public String check(String text) {
                    return checkImpl(text);
                }

                @Override
                public void onDone(String text) {
                    tokensArr.add(new TokenRecord(text, type));
                    SpannableString in = getCurInput();
                    codeView.setText(in, TextView.BufferType.SPANNABLE);
                    recreateButtons();
                }

                @Override
                public void initEditText(EditText editText) {
                    init(editText);
                }
            });
        }

        abstract String checkImpl(String text);

        public void init(EditText editText) {
        }

        ;
    }
}
