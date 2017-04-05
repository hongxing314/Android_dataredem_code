package com.billaway.lyfepoints.explanation;

import android.os.Bundle;

import com.billaway.lyfepoints.BaseActivity;
import com.billaway.lyfepoints.R;

public class ExplanationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);
    }

    @Override
    protected void onDestroy() {
        component.data().setExplanationShown(true);
        super.onDestroy();
    }
}
