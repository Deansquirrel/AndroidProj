package com.yuansong.xf.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yuansong.xf.BaseActivity;
import com.yuansong.xf.R;

public class UnderstanderTextActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private TextView mTextView = null;
    private EditText mEditText = null;
    private Button mButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understander_text);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("语义理解 - 文本");
        setSupportActionBar(mToolbar);

        mTextView = findViewById(R.id.textView);
        mTextView.setText("");
        mTextView.setVisibility(View.GONE);

        mEditText = findViewById(R.id.editText);
        mEditText.setText("");
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().equals("")){
                    mButton.setEnabled(false);
                }
                else{
                    mButton.setEnabled(true);
                }
            }
        });

        mButton = findViewById(R.id.btnButton);
        mButton.setText("确  定");
        mButton.setEnabled(false);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setLightWindow();
        showBackOption();
    }

    private void showTable(){
        StringBuilder sb = new StringBuilder()
                .append("大厅").append("\n")
                .append("  餐桌-1").append("\n")
                .append("  餐桌-2").append("\n")
                .append("  餐桌-3").append("\n")
                .append("  餐桌-4").append("\n")
                .append("  餐桌-5").append("\n");
        mTextView.setText(sb.toString());
        mTextView.setVisibility(View.VISIBLE);
    }

    private void showMenu(int tableNo){
        StringBuilder sb = new StringBuilder()
                .append("桌号 - ").append(tableNo).append("\n")
                .append("  1 菜品-1").append("\n")
                .append("  2 菜品-2").append("\n")
                .append("  3 菜品-3").append("\n")
                .append("  4 菜品-4").append("\n")
                .append("  5 菜品-5").append("\n");
        mTextView.setText(sb.toString());
        mTextView.setVisibility(View.VISIBLE);
    }



}
