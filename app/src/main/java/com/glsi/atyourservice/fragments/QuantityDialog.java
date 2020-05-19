package com.glsi.atyourservice.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.glsi.atyourservice.R;
import com.glsi.atyourservice.models.Product;

public class QuantityDialog extends DialogFragment {
    private NumberPicker mQuantityPicker;
    private TextView mOkBtn, mCancelBtn;
    private OnChooseQuantityListener mOnChooseQuantityListener;
    private Product product;
    private int quantity = 1;
    private int itemPosition;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_quantity_fragment, container, false);
        mQuantityPicker = view.findViewById(R.id.quantityPicker);
        mOkBtn = view.findViewById(R.id.btnOk);
        mCancelBtn = view.findViewById(R.id.btnCancel);
        mQuantityPicker.setWrapSelectorWheel(true);
        mQuantityPicker.setMaxValue(10);
        mQuantityPicker.setMinValue(1);
        mQuantityPicker.setValue(quantity);

        mQuantityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                quantity = newVal;
            }
        });

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnChooseQuantityListener.passData(product, quantity, itemPosition);
                getDialog().dismiss();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, 900);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnChooseQuantityListener = (OnChooseQuantityListener) getActivity();
        }catch (ClassCastException e){
            Log.e("Quantity Dialog", "onAttach: ClassCastException: " + e.getMessage() );
        }
    }

    public interface OnChooseQuantityListener {
        void passData(Product product, int quantity, int itemPosition);
    }
}
