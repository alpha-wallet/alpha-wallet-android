package io.stormbird.wallet.ui.widget.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;
import io.stormbird.wallet.R;

/**
 * Created by James on 20/07/2019.
 * Stormbird in Sydney
 */
public class TextHolder extends BinderViewHolder<String>
{
    public static final int VIEW_TYPE = 1041;

    private TextView text;

    public TextHolder(int resId, ViewGroup parent)
    {
        super(resId, parent);
        text = findViewById(R.id.text);
    }

    @Override
    public void bind(@Nullable String data, @NonNull Bundle addition)
    {
        text.setText(data);
    }
}