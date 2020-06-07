package com.alphawallet.app.ui.widget.holder;

import android.os.Bundle;
<<<<<<< HEAD
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
=======
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
>>>>>>> e3074436a... Attempt to upgrade to AndroidX
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphawallet.app.R;
import com.alphawallet.app.util.LocaleUtils;

import java.util.Date;

public class TransactionDateHolder extends BinderViewHolder<Date> {

    public static final int VIEW_TYPE = 1004;
    private final TextView title;

    public TransactionDateHolder(int resId, ViewGroup parent) {
        super(resId, parent);

        title = findViewById(R.id.title);
    }

    @Override
    public void bind(@Nullable Date data, @NonNull Bundle addition) {
        if (data == null) {
            title.setText(null);
        } else {
            title.setText(getDate(data));
        }
    }

    private String getDate(Date date)
    {
        if (DateUtils.isToday(date.getTime()))
        {
            return getString(R.string.today);
        }
        else
        {
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, LocaleUtils.getDeviceLocale(getContext()));
            return dateFormat.format(date);
        }
    }
}
