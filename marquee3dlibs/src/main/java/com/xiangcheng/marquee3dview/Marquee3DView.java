package com.xiangcheng.marquee3dview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangcheng on 18/1/19.
 * 一款3d旋转式的marqueeview
 */

public class Marquee3DView extends View {

    private int currentItem;
    private int nextItem;

    private int width, height;

    private int direction;

    //从下到上进行旋转滚动
    private static final int D2U = 1;
    //从上到下进行旋转滚动
    private static final int U2D = 2;

    //负责围绕那个轴进行旋转
    private Camera camera;
    //负责控制旋转点
    private Matrix matrix;

    private ValueAnimator rotateAnimator;

    private float changeRotate;

    private float translateY;
    private Paint textPaint;
    private Bitmap textBitmap;
    //当前画笔，主要是用来改变alpha值的
    private Paint currentPaint;

    private Paint nextPaint;

    //这个是看到别人加了高亮的显示的，所以自己也弄一个玩玩
    private int highLightPosition = 1;
    private Paint linePaint;
    private Paint highLightPaint;

    private boolean isRunning;

    private Region textRegion;

    private int backColor;

    private int rotateDuration = 1000;

    private int showDuration = 1000;

    private int highLightColor;

    private ShowItemRunable showItemRunable;

    public void setMarqueeLabels(List<String> marqueeLabels) {
        this.marqueeLabels = marqueeLabels;
        if (highLightPosition >= this.marqueeLabels.size()) {
            highLightPosition = this.marqueeLabels.size() - 1;
        }
        rotateAnimator.start();
    }

    private List<String> marqueeLabels = new ArrayList<>();

    public Marquee3DView(Context context) {
        this(context, null);
    }

    public Marquee3DView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Marquee3DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initArgus(context, attrs);
        initialize();

    }

    private void initArgus(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Marquee3DView);
        backColor = typedArray.getColor(R.styleable.Marquee3DView_back_color, Color.parseColor("#cccccc"));
        direction = typedArray.getInt(R.styleable.Marquee3DView_direction, D2U);
        highLightPosition = typedArray.getInt(R.styleable.Marquee3DView_highlight_position, highLightPosition);
        highLightColor = typedArray.getColor(R.styleable.Marquee3DView_highlight_color, Color.parseColor("#FF1493"));
        rotateDuration = typedArray.getInt(R.styleable.Marquee3DView_rotate_duration, rotateDuration);
        showDuration = typedArray.getInt(R.styleable.Marquee3DView_show_duration, showDuration);
    }

    private void initialize() {
        camera = new Camera();
        matrix = new Matrix();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(sp2px(15));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);

        currentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentPaint.setTextSize(sp2px(15));
        currentPaint.setColor(Color.BLACK);
        currentPaint.setTextAlign(Paint.Align.CENTER);

        nextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nextPaint.setTextSize(sp2px(15));
        nextPaint.setColor(Color.BLACK);
        nextPaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(highLightColor);
        linePaint.setStrokeWidth(dp2px(1));
        linePaint.setStyle(Paint.Style.FILL);

        highLightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highLightPaint.setTextSize(sp2px(15));
        highLightPaint.setColor(highLightColor);
        highLightPaint.setTextAlign(Paint.Align.CENTER);

        textRegion = new Region();
    }

    private void initAnimation() {
        showItemRunable = new ShowItemRunable();
        rotateAnimator = ValueAnimator.ofFloat(0, 90);
        rotateAnimator.setDuration(rotateDuration);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                isRunning = true;
                changeRotate = (float) animation.getAnimatedValue();
                caculateCurrentPaint(changeRotate);
                caculateNextPaint(changeRotate);
                //从0到height的一个过程
                translateY = height * animation.getAnimatedFraction();
                invalidate();
            }
        });
        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRunning = false;
                postDelayed(showItemRunable, 1000);
            }
        });
    }

    private class ShowItemRunable implements Runnable {

        @Override
        public void run() {
            currentItem++;
            if (currentItem >= marqueeLabels.size()) {
                currentItem = 0;
            }
            rotateAnimator.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (marqueeLabels == null || marqueeLabels.size() <= 0) {
            return;
        }
        drawCurrentItem(canvas);
        drawNextItem(canvas);
    }

    private void drawCurrentItem(Canvas canvas) {
        canvas.save();
        camera.save();
        if (direction == D2U) {
            //当前的item是往里面转动的，因此角度是增大的
            camera.rotateX(changeRotate);//0到90度的过程
        } else {
            camera.rotateX(-changeRotate);
        }
        camera.getMatrix(matrix);
        camera.restore();
        if (direction == D2U) {
            matrix.preTranslate(-width / 2, -height);
            matrix.postTranslate(width / 2, height - translateY);//最后跑到0的位置了
        } else {
            matrix.preTranslate(-width / 2, 0);
            matrix.postTranslate(width / 2, translateY);
        }
        textBitmap = createChild(currentItem, false);
        canvas.drawBitmap(textBitmap, matrix, null);
        canvas.restore();
    }

    private void drawNextItem(Canvas canvas) {
        caculateNextItem();
        canvas.save();
        camera.save();
        if (direction == D2U) {
            //从-90度到0度的过程
            camera.rotateX(-90 + changeRotate);
        } else {
            //从上到下是90度到0度的过程
            camera.rotateX(90 - changeRotate);
        }
        camera.getMatrix(matrix);
        camera.restore();

        if (direction == D2U) {
            matrix.preTranslate(-width / 2, 0);
            matrix.postTranslate(width / 2, height + (-translateY));
        } else {
            matrix.preTranslate(-width / 2, -height);
            matrix.postTranslate(width / 2, translateY);
        }
        textBitmap = createChild(nextItem, true);
        canvas.drawBitmap(textBitmap, matrix, null);
        canvas.restore();
    }

    private Bitmap createChild(int position, boolean isNext) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(backColor);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float allHeight = fontMetrics.descent - fontMetrics.ascent;
        float textWidth = textPaint.measureText(marqueeLabels.get(position));
        Rect rect = new Rect();
        rect.left = (int) (width * 1.0 / 2 - textWidth / 2);
        rect.right = (int) (width * 1.0 / 2 + textWidth / 2);
        rect.top = (int) (height / 2 - allHeight / 2);
        rect.bottom = (int) (height / 2 + allHeight / 2);
        textRegion.set(rect);
        if (isNext) {
            if (highLightPosition == position) {
                caculateHighLightPaint(changeRotate, true);
                canvas.drawText(marqueeLabels.get(position), width / 2, height / 2 - allHeight / 2 - fontMetrics.ascent, highLightPaint);
                canvas.drawLine((float) (width * 1.0 / 2 - textWidth / 2), (float) (height * 1.0 / 2 + allHeight / 2),
                        (float) (width * 1.0 / 2 + textWidth / 2), (float) (height * 1.0 / 2 + allHeight / 2), linePaint);
            } else {
                canvas.drawText(marqueeLabels.get(position), width / 2, height / 2 - allHeight / 2 - fontMetrics.ascent, nextPaint);
            }
        } else {
            if (highLightPosition == position) {
                caculateHighLightPaint(changeRotate, false);
                canvas.drawText(marqueeLabels.get(position), width / 2, height / 2 - allHeight / 2 - fontMetrics.ascent, highLightPaint);
                canvas.drawLine((float) (width * 1.0 / 2 - textWidth / 2), (float) (height * 1.0 / 2 + allHeight / 2),
                        (float) (width * 1.0 / 2 + textWidth / 2), (float) (height * 1.0 / 2 + allHeight / 2), linePaint);
            } else {
                canvas.drawText(marqueeLabels.get(position), width / 2, height / 2 - allHeight / 2 - fontMetrics.ascent, currentPaint);
            }
        }
        return bitmap;
    }

    private void caculateHighLightPaint(float rotate, boolean isNext) {
        if (isNext) {
            float percent = rotate / 90;
            int alpha = (int) (percent * 255);
            highLightPaint.setAlpha(alpha);
            linePaint.setAlpha(alpha);
        } else {
            float percent = rotate / 90;
            int alpha = (int) (255 - percent * 255);
            highLightPaint.setAlpha(alpha);
            linePaint.setAlpha(alpha);
        }
    }

    private void caculateNextItem() {
        nextItem = currentItem + 1;
        if (nextItem >= marqueeLabels.size()) {
            nextItem = 0;
        }
    }

    private void caculateCurrentPaint(float rotateAngle) {
        float percent = rotateAngle / 90;
        int alpha = (int) (255 - percent * 255);
        currentPaint.setAlpha(alpha);
    }

    private void caculateNextPaint(float rotateAngle) {
        float percent = rotateAngle / 90;
        int alpha = (int) (percent * 255);
        nextPaint.setAlpha(alpha);
    }

    private int sp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, getResources().getDisplayMetrics());
    }

    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("TAG", "onSizeChanged");
        width = w;
        height = h;
        initAnimation();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isRunning) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (textRegion.contains((int) event.getX(), (int) event.getY())) {
                if (onWhereItemClick != null) {
                    onWhereItemClick.onItemClick(nextItem);
                }
            }
        }
        return true;
    }

    private OnWhereItemClick onWhereItemClick;

    public void setOnWhereItemClick(OnWhereItemClick onWhereItemClick) {
        this.onWhereItemClick = onWhereItemClick;
    }

    public interface OnWhereItemClick {
        public void onItemClick(int position);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (rotateAnimator != null && rotateAnimator.isRunning()) {
            rotateAnimator.end();
        }
        if (showItemRunable != null) {
            removeCallbacks(showItemRunable);
        }
    }
}
