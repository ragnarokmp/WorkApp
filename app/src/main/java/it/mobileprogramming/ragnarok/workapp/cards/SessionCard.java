package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.events.BusProvider;

import it.mobileprogramming.ragnarok.workapp.R;

public class SessionCard extends SimpleCard {

    protected String leftButtonText = getResources().getString(R.string.session_left_button);
    protected String rightButtonText = getResources().getString(R.string.session_right_button);
    protected int rightButtonTextColor = getResources().getColor(R.color.accent);
    protected int leftButtonTextColor = getResources().getColor(R.color.secondary_text);
    protected OnButtonPressListener onLeftButtonPressedListener;
    protected OnButtonPressListener onRightButtonPressedListener;
    protected boolean dividerVisible = true;
    protected boolean fullWidthDivider = true;

    public SessionCard(Context context) {
        super(context);
    }

    @Override
    public int getLayout(){
        return R.layout.session_card_layout;
    }

    public String getLeftButtonText() {
        return leftButtonText;
    }

    public void setLeftButtonText(int leftButtonTextId) {
        setLeftButtonText(getString(leftButtonTextId));
    }

    public void setLeftButtonText(String leftButtonText) {
        this.leftButtonText = leftButtonText;
        BusProvider.dataSetChanged();
    }

    public String getRightButtonText() {
        return rightButtonText;
    }

    public void setRightButtonText(int rightButtonTextId) {
        setRightButtonText(getString(rightButtonTextId));
    }

    public void setRightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
        BusProvider.dataSetChanged();
    }

    public int getRightButtonTextColor() {
        return rightButtonTextColor;
    }

    public void setRightButtonTextColor(int color) {
        this.rightButtonTextColor = color;
        BusProvider.dataSetChanged();
    }

    public void setRightButtonTextColorRes(int colorId) {
        setRightButtonTextColor(getResources().getColor(colorId));
    }

    public int getLeftButtonTextColor() {
        return leftButtonTextColor;
    }

    public void setLeftButtonTextColor(int color) {
        this.leftButtonTextColor = color;
        BusProvider.dataSetChanged();
    }

    public void setLeftButtonTextColorRes(int colorId) {
        setLeftButtonTextColor(getResources().getColor(colorId));
    }


    public boolean isDividerVisible() {
        return dividerVisible;
    }

    public boolean isFullWidthDivider() {
        return fullWidthDivider;
    }

    public void setFullWidthDivider(boolean fullWidthDivider) {
        this.fullWidthDivider = fullWidthDivider;
        BusProvider.dataSetChanged();
    }

    public void setDividerVisible(boolean visible) {
        this.dividerVisible = visible;
        BusProvider.dataSetChanged();
    }

    public OnButtonPressListener getOnLeftButtonPressedListener() {
        return onLeftButtonPressedListener;
    }

    public void setOnLeftButtonPressedListener(OnButtonPressListener onLeftButtonPressedListener) {
        this.onLeftButtonPressedListener = onLeftButtonPressedListener;
    }

    public OnButtonPressListener getOnRightButtonPressedListener() {
        return onRightButtonPressedListener;
    }

    public void setOnRightButtonPressedListener(OnButtonPressListener onRightButtonPressedListener) {
        this.onRightButtonPressedListener = onRightButtonPressedListener;
    }
}