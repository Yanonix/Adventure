package com.aventure;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tile extends RelativeLayout {

    Button button;
    TextView text;
    RelativeLayout layout;

    public Tile(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Tile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.tile, this);
        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        layout = (RelativeLayout) findViewById(R.id.layout);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callOnClick();
            }
        });

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.Tile, 0, 0);

            String titleText = "";
            String detailText = "";

            try {
                titleText = a.getString(R.styleable.Tile_text);
                detailText = a.getString(R.styleable.Tile_backText);
                layout.setBackgroundColor(a.getColor(R.styleable.Tile_backgroundColor, Color.RED));
                button.setTextColor(a.getColor(R.styleable.Tile_textColor, Color.BLUE));
                button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, a.getDimension(R.styleable.Tile_textSize, 100));
            } catch (Exception e) {
                Log.e("Tile", "There was an error loading attributes.");
            } finally {
                a.recycle();
            }

            setText(titleText);
            setBackText(detailText);
        }
    }

    public void setText(String text) {
        this.button.setText(text);
    }

    public void setBackText(String backText) {
        this.text.setText(backText);
    }

    public void setTypeface(int bold) {
        button.setInputType(bold);
    }

    public void clear() {
        setText(null);
        setBackText(null);

    }
}
