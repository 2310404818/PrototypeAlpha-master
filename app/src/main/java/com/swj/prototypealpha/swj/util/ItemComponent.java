package com.swj.prototypealpha.swj.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swj.prototypealpha.R;


public class ItemComponent extends ConstraintLayout
{
    private ImageView leftImage;

    private ImageView rightArrow;

    private TextView title;

    private TextView content;

    private ConstraintLayout rootView;

    private OnItemClickListener listener;


    public ItemComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.item_view,this,true);
        addView(view);
        title = findViewById(R.id.item_title);
        content = findViewById(R.id.item_content);
        leftImage = findViewById(R.id.item_left_image);
        rightArrow = findViewById(R.id.item_right_arrow);
        rootView = findViewById(R.id.root_item);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Project_Info_Item);
        rightArrow.setVisibility(View.VISIBLE);

        rootView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //listener.itemClick(rightDesc.getText().toString());
            }
        });
        rightArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        attributes.recycle();
    }
    public void setLeftImage(int value)
    {
        Drawable drawable = getResources().getDrawable(value);
        leftImage.setBackground(drawable);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setContent(String content) {
        this.content.setText(content);
    }

    public void setRightArrow(ImageView rightArrow) {
        this.rightArrow = rightArrow;
    }

    public void setShowrightArror(boolean value)
    {
        rightArrow.setVisibility(value ? View.VISIBLE:View.INVISIBLE);
    }

    public void setItemOnclickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public void setArrowOnClickListener(OnClickListener listener)
    {
        rightArrow.setOnClickListener( listener);
    }
}
