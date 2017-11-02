package com.etiennelawlor.discreteslider.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etiennelawlor.discreteslider.R;
import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.etiennelawlor.discreteslider.library.utilities.DisplayUtility;

import java.util.Objects;

/**
 * Created by etiennelawlor on 7/1/16.
 */

public class MainFragment extends Fragment {

    // region Views
    DiscreteSlider discreteSlider;
    RelativeLayout tickMarkLabelsRelativeLayout;
    // endregion
    private String TAG_SELECTED;
    private String TAG_NOT_SELECTED;

    // region Constructors
    public MainFragment() {
    }
    // endregion

    // region Factory Methods
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public static MainFragment newInstance(Bundle extras) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(extras);
        return fragment;
    }
    // endregion

    // region Lifecycle Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        discreteSlider = (DiscreteSlider) rootView.findViewById(R.id.discrete_slider);
        tickMarkLabelsRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.tick_mark_labels_rl);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Dynamically update attributes
//        discreteSlider.setTickMarkCount(10);
//        discreteSlider.setTickMarkRadius(16);
//        discreteSlider.setHorizontalBarThickness(18);
//        discreteSlider.setBackdropFillColor(getResources().getColor(R.color.purple_500));
//        discreteSlider.setBackdropStrokeColor(getResources().getColor(R.color.orange_300));
//        discreteSlider.setBackdropStrokeWidth(6);
//        discreteSlider.setThumb(getResources().getDrawable(android.R.drawable.ic_notification_clear_all));
//        discreteSlider.setProgressDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));

        // Detect when slider position changes
        discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
                // position selected
            }

            @Override
            public void onDragPositionChanged(int position) {
                Log.d("WTF", "Drag position changed: " + position);
                int childCount = tickMarkLabelsRelativeLayout.getChildCount();
                for(int i= 0; i<childCount; i++){
                    TextView tv = (TextView) tickMarkLabelsRelativeLayout.getChildAt(i);
                    showSelected(tv, i == position, true);
                }
            }
        });

        tickMarkLabelsRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tickMarkLabelsRelativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                addTickMarkTextLabels();
            }
        });
    }

    // endregion

    // region Helper Methods

    private void addTickMarkTextLabels(){
        int width = tickMarkLabelsRelativeLayout.getMeasuredWidth();
        int tickMarkCount = discreteSlider.getTickMarkCount();

        float leftMargin = discreteSlider.getBackdropLeftMargin();
        float rightMargin = discreteSlider.getBackdropRightMargin();
        float interval = (width - (leftMargin + rightMargin)) / (tickMarkCount - 1);

        int labelWidth = (int) Math.min(Math.min(interval, leftMargin*2), rightMargin*2);
        int labelPadding = DisplayUtility.dp2px(2);
        labelWidth -= 2*labelPadding;

        String[] tickMarkLabels = {"$", "$$", "$$$", "$$$$", "$$$$$"};
        for(int i=0; i<tickMarkCount; i++) {
            TextView tv = new TextView(getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    labelWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(layoutParams);
            tv.setText(tickMarkLabels[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setX(leftMargin + (i * interval) - labelWidth/2);
            showSelected(tv, i == discreteSlider.getPosition(), false);
            tickMarkLabelsRelativeLayout.addView(tv);
        }
    }
    // endregion

    private void showSelected(TextView textView, boolean selected, boolean animate) {
        if (textView.getTag() != null) {
            boolean oldSelected = textView.getTag().equals(TAG_SELECTED);
            if (oldSelected == selected) {
                return;
            }
        }
        if (selected) {
            textView.setTag(TAG_SELECTED);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (animate) {
                textView.animate().y(DisplayUtility.dp2px(8)).setInterpolator(new DecelerateInterpolator());
            } else {
                textView.setY(DisplayUtility.dp2px(8));
            }
        } else {
            textView.setTag(TAG_NOT_SELECTED);
            textView.setTextColor(getResources().getColor(R.color.grey_400));
            if (animate) {
                textView.animate().y(DisplayUtility.dp2px(22)).setInterpolator(new DecelerateInterpolator());
            } else {
                textView.setY(DisplayUtility.dp2px(22));
            }
        }
    }
}