package com.xxd.common.basic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.xxd.common.basic.R;
import com.xxd.common.basic.utils.DensityUtil;

/**
 * @author:XiaoDan
 * @time:2022/8/15
 * @desc: 带单位的输入控件
 */
public class InputUnitView extends LinearLayout {

    private @ColorInt
    int mEtColor;
    private int mEtSize;
    private @ColorInt
    int mUnitColor;
    private int mUnitSize;
    private CharSequence mUnitText;
    private EditText mEtInput;
    private TextView mTvUnit;
    private IListener mListener;
    private int mInputType = InputType.TYPE_NUMBER_FLAG_DECIMAL;

    public InputUnitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InputUnitView);
        if (array != null) {
            mEtColor = array.getColor(R.styleable.InputUnitView_etColor, Color.parseColor("#000000"));
            mEtSize = array.getDimensionPixelSize(R.styleable.InputUnitView_etSize, DensityUtil.dip2px(context, 15));
            mUnitColor = array.getColor(R.styleable.InputUnitView_unitColor, Color.parseColor("#A0000000"));
            mUnitSize = array.getDimensionPixelSize(R.styleable.InputUnitView_unitSize, DensityUtil.dip2px(context, 15));
            mUnitText = array.getText(R.styleable.InputUnitView_unitText);
            mInputType = array.getInt(R.styleable.InputUnitView_android_inputType, InputType.TYPE_NUMBER_FLAG_DECIMAL);
            array.recycle();
        }

        addView();
    }

    private void addView() {
        setOrientation(HORIZONTAL);
        //editText
        EditText editText = new EditText(getContext());
        mEtInput = editText;
        LinearLayout.LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        editText.setLayoutParams(layoutParams);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setInputType(mInputType);
        editText.setTextColor(mEtColor);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEtSize);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setPadding(0, 0, 0, 0);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mListener != null) {
                    mListener.onInputChange(s.toString());
                }
            }
        });
        addView(editText);

        //unit text view
        TextView textView = new TextView(getContext());
        mTvUnit = textView;
        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        layoutParams.leftMargin = 20;
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(mUnitColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnitSize);
        if (!TextUtils.isEmpty(mUnitText)) {
            textView.setText(mUnitText);
        } else {
            textView.setText("");
        }
        addView(textView);
    }

    /**
     * 设置单位字符
     *
     * @param text
     */
    public void setUnitText(CharSequence text) {
        mUnitText = text;
        mTvUnit.setText(text);
    }

    /**
     * 设置子控件EditText的输入类型
     *
     * @param inputType
     */
    public void setEditTextInputType(int inputType) {
        mEtInput.setInputType(inputType);
    }

    /**
     * 设置子控件EditText的文本值
     *
     * @param text
     */
    public void setInputValue(String text) {
        if (text == null) {
            text = "";
        }
        mEtInput.setText(text);
        mEtInput.setSelection(text.length());
    }

    /**
     * 获取该控件的输入值
     *
     * @return
     */
    public String getInputValue() {
        return mEtInput.getText().toString().trim();
    }

    public EditText getEtInput() {
        return mEtInput;
    }

    public void setListener(IListener listener) {
        this.mListener = listener;
    }

    /**
     * 该控件的监听器
     */
    public interface IListener {

        /**
         * InputUnitView的子控件EditTex输入改变时的回调
         *
         * @param text
         */
        void onInputChange(String text);

    }
}
