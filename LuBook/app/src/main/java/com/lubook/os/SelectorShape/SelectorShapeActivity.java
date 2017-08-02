package com.lubook.os.SelectorShape;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lubook.os.R;

public class SelectorShapeActivity extends Activity implements View.OnClickListener {

    private Button selector_shape_btn1, selector_shape_btn1_combine, selector_shape_btn2_short_dashline, selector_shape_btn3_oval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_shape);
        selector_shape_btn1 = (Button) findViewById(R.id.selector_shape_btn1);
        selector_shape_btn1.setOnClickListener(this);
        selector_shape_btn1_combine = (Button) findViewById(R.id.selector_shape_btn1_combine);
        selector_shape_btn1_combine.setOnClickListener(this);
        selector_shape_btn2_short_dashline = (Button) findViewById(R.id.selector_shape_btn2_short_dashline);
        selector_shape_btn2_short_dashline.setOnClickListener(this);
        selector_shape_btn3_oval = (Button) findViewById(R.id.selector_shape_btn3_oval);
        selector_shape_btn3_oval.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selector_shape_btn1:
                break;
            case R.id.selector_shape_btn1_combine:
                break;
            case R.id.selector_shape_btn2_short_dashline:
                break;
            case R.id.selector_shape_btn3_oval:
                break;
            default:
                break;
        }
    }
}
