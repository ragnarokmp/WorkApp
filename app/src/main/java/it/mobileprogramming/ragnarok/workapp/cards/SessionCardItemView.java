package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;

import it.mobileprogramming.ragnarok.workapp.R;

public class SessionCardItemView extends CardItemView<SessionCard> {
    private final static int DIVIDER_MARGIN_DP = 16;

    // Default constructors
    public SessionCardItemView(Context context) {
        super(context);
    }

    public SessionCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SessionCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(SessionCard card) {
        super.build(card);

        setTitle(card.getTitle());
        setDescription(card.getDescription());
        setDivider(card.isDividerVisible(), card.isFullWidthDivider());
        setButtons(card.getRightButtonText(), card.getRightButtonTextColor(), card.getOnRightButtonPressedListener(), card.getLeftButtonText(), card.getLeftButtonTextColor(), card.getOnLeftButtonPressedListener(), card);
    }

    private void setButtons(String rightButtonText, int rightButtonTextColor, final OnButtonPressListener onRightButtonPressedListener, String leftButtonText, int leftButtonTextColor, final OnButtonPressListener onLeftButtonPressedListener, final Card card) {
        final TextView rightTextView = (TextView) findViewById(R.id.right_text_button);
        final TextView leftTextView = (TextView) findViewById(R.id.left_text_button);

        if (rightButtonTextColor != -1) {
            rightTextView.setTextColor(rightButtonTextColor);
        }

        if (rightButtonText != null) {
            rightTextView.setText(rightButtonText.toUpperCase());
            rightTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRightButtonPressedListener != null) {
                        onRightButtonPressedListener.onButtonPressedListener(rightTextView, card);
                    }
                }
            });
        }

        if (rightButtonTextColor != -1) {
            leftTextView.setTextColor(leftButtonTextColor);
        }

        if (leftButtonText != null) {
            leftTextView.setText(leftButtonText.toUpperCase());
            leftTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onLeftButtonPressedListener != null) {
                        onLeftButtonPressedListener.onButtonPressedListener(leftTextView, card);
                    }
                }
            });
        }
    }

    private void setDivider(Boolean dividerVisible, Boolean fullWidthDivider) {
        View divider = findViewById(R.id.card_divider);
        divider.setVisibility(dividerVisible ? VISIBLE : INVISIBLE);

        // After setting the visibility, we prepare the divider params according to the preferences
        if (dividerVisible) {
            // If the divider has to be from side to side, the margin will be 0
            if (fullWidthDivider) {
                ((ViewGroup.MarginLayoutParams) divider.getLayoutParams()).setMargins(0, 0, 0, 0);
            } else {
                int dividerMarginPx = (int) dpToPx(DIVIDER_MARGIN_DP);
                // Set the margin
                ((ViewGroup.MarginLayoutParams) divider.getLayoutParams()).setMargins(dividerMarginPx, 0, dividerMarginPx, 0);
            }
        }
    }

    private void setTitle(String title){
        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        titleTextView.setText(title);
    }

    private void setDescription(String description){
        TextView descriptionTextView = (TextView) findViewById(R.id.description_text_view);
        descriptionTextView.setText(description);
    }

    private float dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private float spToPx(int sp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(sp * (displayMetrics.scaledDensity / DisplayMetrics.DENSITY_DEFAULT));
    }
}
