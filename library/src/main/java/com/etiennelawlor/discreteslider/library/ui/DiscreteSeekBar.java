package com.etiennelawlor.discreteslider.library.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;

/**
 * Created by etiennelawlor on 7/4/16.
 */

public class DiscreteSeekBar extends AppCompatSeekBar {

    // region Constants
    private static String PROGRESS_PROPERTY = "progress";
    private static int MULTIPLIER = 100;
    // endregion

    // region Member Variables
    private int tickMarkCount = 0;
    private float stepSize = 0.0f;
    // This counter detects if the user clicked the SeekBar or dragged the SeekBar
    // If this counter exceeds 1 then the user dragged the SeekBar otherwise
    // the user clicked the SeekBar
    private int fromUserCount = 0;
    private OnDiscreteSeekBarChangeListener onDiscreteSeekBarChangeListener;
    // endregion

    // region Interfaces
    public interface OnDiscreteSeekBarChangeListener {
        void onPositionChanged(int position);
        void onDragPositionChanged(int position);
    }
    // endregion

    // region Constructors
    public DiscreteSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public DiscreteSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiscreteSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    // endregion

    // region Helper Methods
    private void init(Context context, AttributeSet attrs){
        setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int prevPosition = 0;
            private int prevProgress = 0;
            private int prevDragPosition = 0;

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                prevPosition = getPosition();
                prevDragPosition = prevPosition;
                prevProgress = seekBar.getProgress();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    fromUserCount+=1;

                    if (onDiscreteSeekBarChangeListener != null) {
                        int newProgress = getStepProgress(progress);
                        int newPosition = newProgress/MULTIPLIER;
                        if (prevDragPosition != newPosition) {
                            prevDragPosition = newPosition;
                            onDiscreteSeekBarChangeListener.onDragPositionChanged(newPosition);
                        }
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                int oldProgress = seekBar.getProgress();
                int newProgress = getStepProgress(oldProgress);

                if(fromUserCount>1){ // SeekBar Dragged
                    ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, PROGRESS_PROPERTY, oldProgress, newProgress);
                    animation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                } else { // SeekBar Clicked
                    ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, PROGRESS_PROPERTY, prevProgress, newProgress);
                    animation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                }

                fromUserCount = 0;
                int newPosition = newProgress/MULTIPLIER;
                if (prevPosition != newPosition) {
                    prevPosition = newPosition;
                    if (onDiscreteSeekBarChangeListener != null) {
                        onDiscreteSeekBarChangeListener.onPositionChanged(newPosition);
                    }
                }
            }
        });
    }

    public void setTickMarkCount(int tickMarkCount) {
        this.tickMarkCount = tickMarkCount < 2 ? 2 : tickMarkCount;
        setMax((this.tickMarkCount-1) * MULTIPLIER);
        this.stepSize = getMax()/(this.tickMarkCount-1);
    }

    public void setOnDiscreteSeekBarChangeListener(OnDiscreteSeekBarChangeListener onDiscreteSeekBarChangeListener){
        this.onDiscreteSeekBarChangeListener = onDiscreteSeekBarChangeListener;
    }

    public void setPosition(int position){
        setProgress(position*(int)stepSize);
    }

    public int getPosition() {
        return getStepProgress(getProgress()) / MULTIPLIER;
    }
    // endregion

    private int getStepProgress(int progress) {
        if((progress % stepSize) >= stepSize/2F){
            return (int)(((progress/(int)stepSize)+1)*stepSize);
        } else {
            return (int)(((progress/(int)stepSize))*stepSize);
        }
    }


}
