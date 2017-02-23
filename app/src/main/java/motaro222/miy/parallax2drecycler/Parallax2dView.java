package motaro222.miy.parallax2drecycler;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Parallax2dView
 * Must be associated with {@link RecyclerView}
 */
public class Parallax2dView extends ViewGroup {
    private static final float DEFAULT_TRANSITION_INTENSITY = 0.1f;
    private static final float DEFAULT_TRANSITION_RATIO = 1f / 40;
    private static final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;

    // Transition size in pixel = (View position / screen-height) * Max-transition-space * TRANSITION-INTENSITY
    private float mTransitionIntensity = DEFAULT_TRANSITION_INTENSITY;
    // Max transition space = TRANSITION-RATIO * View-height
    private float mTransitionRatio = DEFAULT_TRANSITION_RATIO;

    // Max size of transition (in pixel)
    private int mMaxTransitionSpace = 100;

    // Catch scroll event of RecyclerView
    private OnScrollListener mOnScrollListener = new OnScrollListener();
    // RecyclerView
    private RecyclerView mRecyclerView = null;

    public Parallax2dView(Context context) {
        this(context, null);
    }

    public Parallax2dView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Parallax2dView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Parallax2dView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Read custom attributes
     *
     * @param context Context of the current view
     * @param attrs   Input attributes
     */
    private void init(Context context, AttributeSet attrs) {

        // Layout res id from xml
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Parallax2dView,
                0, 0);

        try {
            mTransitionIntensity = a.getFloat(R.styleable.Parallax2dView_transition_intensity, DEFAULT_TRANSITION_INTENSITY);
            mTransitionRatio = a.getFloat(R.styleable.Parallax2dView_transition_ratio, DEFAULT_TRANSITION_RATIO);
            if (mTransitionIntensity > 1) {
                mTransitionIntensity = 1;
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Find RecyclerView to associated with
        ViewParent viewParent = getParent();
        while (viewParent != null) {
            if (viewParent instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) viewParent;
                mRecyclerView.addOnScrollListener(mOnScrollListener);
                break;
            }
            viewParent = viewParent.getParent();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(mOnScrollListener);
            mRecyclerView = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        relayoutChild();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();

        mMaxTransitionSpace = (int) (h * mTransitionRatio);

        int sizeExtensionWidth = childCount > 1 ? mMaxTransitionSpace / (childCount - 1) : mMaxTransitionSpace;

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != View.GONE) {
                measureChild(view, MeasureSpec.makeMeasureSpec(w + (mMaxTransitionSpace - sizeExtensionWidth * i) * 2, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(h + (mMaxTransitionSpace - sizeExtensionWidth * i) * 2, MeasureSpec.EXACTLY));
            }
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Re-layout all children on each scroll
     */
    public void relayoutChild() {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int topPosition = getTop();

        int maxTransitionY = (int) (((SCREEN_HEIGHT - h) / 2f - topPosition) / SCREEN_HEIGHT * mMaxTransitionSpace * mTransitionIntensity);

        int childCount = getChildCount();
        int eachSpace = childCount > 1 ? mMaxTransitionSpace / (childCount - 1) : mMaxTransitionSpace;
        int transitionSpace = childCount > 1 ? maxTransitionY / (childCount - 1) : maxTransitionY;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int transitionY = maxTransitionY - transitionSpace * i;
                child.layout(-(mMaxTransitionSpace - eachSpace * i) + paddingLeft,
                        -transitionY - (mMaxTransitionSpace - eachSpace * i) + paddingTop,
                        w + (mMaxTransitionSpace - eachSpace * i) - paddingRight,
                        h - transitionY + (mMaxTransitionSpace - eachSpace * i) - paddingBottom);
            }
        }
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            relayoutChild();
        }
    }
}
