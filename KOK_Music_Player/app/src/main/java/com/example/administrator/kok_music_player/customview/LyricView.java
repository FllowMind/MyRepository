package com.example.administrator.kok_music_player.customview;

/**
 * Created by Administrator on 2016/5/27.
 */

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.musicutils.Lyric;

import java.util.List;

/**
 * <p>
 * 自定义View，滚动播放LRC歌词
 * </p>
 */
public class LyricView extends View {

    private int mTextSize = 40; // 字体大小
    private int mTextColor = 0xFFFFFFFF; // 字体颜色
    private int mCurrentTextColor = 0xFF0E8DC4; // 当前播放字体颜色

    // 字体透明度，用于渐变
    private int mMaxTextAlpha = 0xFF;
    private int mMinTextAlpha = 0x00;

    private int mMaxLines = 10; // 最大行数
    private int mLineSpace = 1; // 行距

    private List<Lyric> mLyrics = null;
    private int mCurrentIndex = 0; // 当前歌词对应索引

    private TextPaint mPaint;
    private int mTextHeight = 0; // 字体高度

    // View 宽高
    private int mWidth = 0, mHeight = 0;

    private ValueAnimator mLineAnimator;
    private long mLineAnimDuration = 400; // 歌词滚动动画时间长度
    private int mLineOffset = 0; // 动画当前偏移
    private int mAnimOffset = 0; // 动画总偏移

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttribute(context, attrs);
        initView();
    }

    /**
     * onSizeChanged()在onDraw()之前调用，可以在这里获取View的宽高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.lyric);
        mTextSize = ta.getDimensionPixelOffset(R.styleable.lyric_textSize, 40);
        mTextColor = ta.getColor(R.styleable.lyric_textColor, 0xFFFFFFFF);
        mCurrentTextColor = ta.getColor(R.styleable.lyric_currentTextColor, 0xFF0E8DC4);
        mMaxLines = ta.getInt(R.styleable.lyric_maxLine, 10);
        mLineSpace = ta.getDimensionPixelOffset(R.styleable.lyric_lineSpace, 15);
        mLineAnimDuration = ta.getInt(R.styleable.lyric_animDuration, 400);
        ta.recycle();
    }

    private void initView() {
        mPaint = new TextPaint();
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setAntiAlias(true);

        // 计算字体高度
        FontMetrics fm = mPaint.getFontMetrics();
        mTextHeight = (int) (fm.bottom - fm.top);

        // 滚动动画
        mLineAnimator = new ValueAnimator();
        mLineAnimator.setIntValues(0, 100);
        mLineAnimator.setDuration(mLineAnimDuration);
        mLineAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                float percent = 1 - (float) value / 100;
                mLineOffset = (int) (mAnimOffset * percent); // 更新偏移值，重绘View
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLyrics == null) {
            mPaint.setColor(mTextColor);
            String noLyrics = getContext().getString(R.string.no_lyrics);
            canvas.drawText(noLyrics, mWidth / 2, mHeight / 2, mPaint);
            return;
        }

        try {


            // 绘制当前播放歌词行
            mPaint.setColor(mCurrentTextColor);
            mPaint.setAlpha(mMaxTextAlpha);
            String currentLine = mLyrics.get(mCurrentIndex).getContent();
            float drawY = mHeight / 2;
            drawText(currentLine, canvas, drawY + mLineOffset);

            // 需要绘制已播放行数
            int alreadyPlayedCount = (int) Math.ceil((double) (mMaxLines - 1) / 2);
            // 需要绘制即将播放行数
            int soonPlayCount = (int) Math.floor((double) (mMaxLines - 1) / 2);

            // 绘制已播放歌词
            mPaint.setColor(mTextColor);
            for (int i = 1; i <= alreadyPlayedCount; i++) {
                int index = mCurrentIndex - i;
                if (index < 0) {
                    break;
                }
                int alpha = mMaxTextAlpha - (mMaxTextAlpha - mMinTextAlpha) / (alreadyPlayedCount + 1) * i;
                mPaint.setAlpha(alpha);
                String line = mLyrics.get(index).getContent();
                drawY = drawY - getNumberOfRows(line) * mTextHeight - mLineSpace;
                drawText(line, canvas, drawY + mLineOffset);
            }

            // 绘制即将播放歌词
            drawY = mHeight / 2 + getNumberOfRows(currentLine) * mTextHeight + mLineSpace;
            for (int i = 1; i <= soonPlayCount; i++) {
                int index = mCurrentIndex + i;
                if (index > mLyrics.size() - 1) {
                    break;
                }
                int alpha = mMaxTextAlpha - (mMaxTextAlpha - mMinTextAlpha) / (soonPlayCount + 1) * i;
                mPaint.setAlpha(alpha);
                String line = mLyrics.get(index).getContent();
                drawText(line, canvas, drawY + mLineOffset);
                drawY = drawY + getNumberOfRows(line) * mTextHeight + mLineSpace;
            }
        } catch (Exception e) {
            mPaint.setColor(mCurrentTextColor);
            mPaint.setAlpha(mMaxTextAlpha);
            float drawY = mHeight / 2;
            drawText("没找到歌词，请下载歌词", canvas, drawY + mLineOffset);
            e.printStackTrace();
        }
    }

    public void setLyricContent(List<Lyric> lyrics) {

        mLyrics = lyrics;
    }

    /**
     * 指定当前播放时间，刷新歌词
     *
     * @param time
     * @param animation 是否动画滚动
     */
    public void setCurrentTime(long time, boolean animation) {
        if (mLyrics == null) {
            return;
        }
        int index = getIndexByPositionTime(time);
        if (index < 0 || index > mLyrics.size() - 1) {
            return;
        }
        if (index == mCurrentIndex) {
            return;
        }
        mCurrentIndex = index;
        mLineAnimator.cancel();
        if (animation) {
            if (mCurrentIndex == 0) {
                mLineOffset = 0;
                invalidate();
            } else {
                String lastLine = mLyrics.get(mCurrentIndex - 1).getContent();
                mAnimOffset = getNumberOfRows(lastLine) * mTextHeight + mLineSpace;
                mLineAnimator.start();
            }
        } else {
            mLineOffset = 0;
            invalidate();
        }
    }

    /**
     * 当前播放时间对应的歌词索引值
     *
     * @param time
     * @return
     */
    private int getIndexByPositionTime(long time) {
        int index = 0;
        if (mLyrics != null) {
            for (int i = 0; i < mLyrics.size(); i++) {
                long t = mLyrics.get(i).getTime();
                if (t > time) {
                    break;
                }
                index = i;
            }
        }
        return index;
    }

    /**
     * 一句歌词需要显示的行数（换行次数）
     *
     * @param index
     * @return
     */
    private int getNumberOfRows(String lrc) {
        float width = mPaint.measureText(lrc);
        float maxWidth = mWidth - getPaddingLeft() - getPaddingRight();
        int num = (int) Math.ceil(width / maxWidth);
        return num;
    }

    /**
     * 绘制文本，可自动换行
     *
     * @param text
     * @param canvas
     * @param y
     */
    private void drawText(String text, Canvas canvas, float y) {
        int status = canvas.save();
        canvas.translate(mWidth / 2, y);
        int w = mWidth - getPaddingLeft() - getPaddingRight();
        StaticLayout layout = new StaticLayout(text, mPaint, w, Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        layout.draw(canvas);
        canvas.restoreToCount(status);
    }
}
