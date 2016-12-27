package br.ufc.quimica.keyboard;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import br.ufc.quimica.keyboard.parser.Evaluator;

public class MainActivity extends AppCompatActivity {

    private Keyboard keyBoard;
    private KeyboardView kbView;
    private EditText editText;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyBoard = new Keyboard(this, R.xml.keyboard);
        kbView = (KeyboardView)findViewById(R.id.keyboardview);

        kbView.setKeyboard(keyBoard);
        kbView.setOnKeyboardActionListener(mKbListener);

        okButton = (Button)findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("VLAD", String.valueOf(new Evaluator().eval("H20NCl4.2H2Cl")));
            }
        });

        editText = (EditText)findViewById(R.id.editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    showCustomKeyboard(view);
                else
                    hideCustomKeyboard();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onBackPressed() {
        if( isCustomKeyboardVisible() )
            hideCustomKeyboard();
        else this.finish();

    }

    public void hideCustomKeyboard() {
        kbView.setVisibility(View.GONE);
        kbView.setEnabled(false);
    }

    public void showCustomKeyboard( View v ) {
        kbView.setVisibility(View.VISIBLE);
        kbView.setEnabled(true);
        if( v!=null ) ((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public boolean isCustomKeyboardVisible() {
        return kbView.getVisibility() == View.VISIBLE;
    }

    private KeyboardView.OnKeyboardActionListener mKbListener = new KeyboardView.OnKeyboardActionListener() {

        public final static int CodeDelete   = -5; // Keyboard.KEYCODE_DELETE
        public final static int CodeCancel   = -3; // Keyboard.KEYCODE_CANCEL
        public final static int CodePrev     = 55000;
        public final static int CodeAllLeft  = 55001;
        public final static int CodeLeft     = 55002;
        public final static int CodeRight    = 55003;
        public final static int CodeAllRight = 55004;
        public final static int CodeNext     = 55005;
        public final static int CodeClear    = 55006;
        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }


        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            View focusCurrent = MainActivity.this.getWindow().getCurrentFocus();
            if( focusCurrent==null || focusCurrent.getClass()!=AppCompatEditText.class ) return;
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            // Handle key
            if( primaryCode==CodeCancel ) {
                hideCustomKeyboard();
            } else if( primaryCode==CodeDelete ) {
                if( editable!=null && start>0 ) editable.delete(start - 1, start);
            } else if( primaryCode==CodeClear ) {
                if( editable!=null ) editable.clear();
            } else if( primaryCode==CodeLeft ) {
                if( start>0 ) editText.setSelection(start - 1);
            } else if( primaryCode==CodeRight ) {
                if (start < editText.length()) editText.setSelection(start + 1);
            } else if( primaryCode==CodeAllLeft ) {
                editText.setSelection(0);
            } else if( primaryCode==CodeAllRight ) {
                editText.setSelection(editText.length());
            } else if( primaryCode==CodePrev ) {
                View focusNew= editText.focusSearch(View.FOCUS_LEFT);
                if( focusNew!=null ) focusNew.requestFocus();
            } else if( primaryCode==CodeNext ) {
                View focusNew= editText.focusSearch(View.FOCUS_RIGHT);
                if( focusNew!=null ) focusNew.requestFocus();
            } else {// Insert character
                editable.insert(start, CodeConverter.convert(primaryCode));
            }
        }

        @Override
        public void onText(CharSequence charSequence) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }

    };
}
