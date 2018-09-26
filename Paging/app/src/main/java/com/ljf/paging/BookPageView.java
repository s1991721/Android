package com.ljf.paging;

import android.annotation.Nullable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;
import android.widget.Toast;

/**
 * Created by mr.lin on 2018/9/19.
 * 书页
 */

public class BookPageView extends View {

    private MyPoint a, f, g, e, h, c, j, b, k, d, i;//各点坐标

    private Paint paintPoint;//画点笔
    private Paint paintAreaA;//区域A画笔
    private Paint paintAreaC;//区域C画笔
    private Paint paintAreaB;//区域B画笔
    private Paint paintText;//文字画笔
    private Paint paintContentC;//C区域内容画笔

    private Path pathA;//区域A路径
    private Path pathC;//区域C路径
    private Path pathB;//区域B路径

    //页面宽高
    private int width;
    private int height;

    //翻页其实位置
    private static final int FROM_TOP = 0;
    private static final int FROM_BOTTOM = 1;
    private static final int FROM_LEFT = 2;
    private static final int FROM_RIGHT = 3;

    //滑动用
    private Scroller mScroller;

    public BookPageView(Context context) {
        this(context, null);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        init();
    }

    //初始化变量
    private void init() {

        width = 600;
        height = 1000;

        mScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());

        initPoint();
        initPaint();
        initPath();

    }

    //初始化点坐标
    private void initPoint() {
        a = new MyPoint();
        f = new MyPoint();

        g = new MyPoint();
        e = new MyPoint();
        h = new MyPoint();
        c = new MyPoint();
        j = new MyPoint();
        b = new MyPoint();
        k = new MyPoint();
        d = new MyPoint();
        i = new MyPoint();

        calculatePoint();
    }

    //根据定点求动点坐标
    private void calculatePoint() {
        //g点为af的中点
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        //e点 做辅助线gm通过相似垂直三角形egm和gmf求得em，e点坐标为：(gx-em, height)
        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        //h点 同e点
        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        //c点 做n点为ag中点，有三角形cjf和ehf
        c.x = e.x - (f.x - e.x) / 2;
        c.y = f.y;

        //j点 同c点
        j.x = f.x;
        j.y = h.y - (f.y - h.y) / 2;

        b = getCrosspoint(a, e, c, j);
        k = getCrosspoint(a, h, c, j);

        //d点 ed连接交cb于p点，d为pe的中点
        d.x = (c.x + 2 * e.x + b.x) / 4;
        d.y = (2 * e.y + c.y + b.y) / 4;

        //i点 同d点
        i.x = (j.x + 2 * h.x + k.x) / 4;
        i.y = (2 * h.y + j.y + k.y) / 4;

    }

    //获取两条直线的交点
    private MyPoint getCrosspoint(MyPoint line1StartP, MyPoint line1EndP, MyPoint line2StartP, MyPoint line2EndP) {
        float x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = line1StartP.x;
        y1 = line1StartP.y;
        x2 = line1EndP.x;
        y2 = line1EndP.y;
        x3 = line2StartP.x;
        y3 = line2StartP.y;
        x4 = line2EndP.x;
        y4 = line2EndP.y;

        float pointX = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

        return new MyPoint(pointX, pointY);
    }

    //初始化画笔
    private void initPaint() {
        paintPoint = new Paint();
        paintPoint.setColor(Color.RED);
        paintPoint.setTextSize(25);

        paintAreaA = new Paint();
        paintAreaA.setColor(Color.GREEN);
        paintAreaA.setAntiAlias(true);

        paintAreaC = new Paint();
        paintAreaC.setColor(Color.YELLOW);
        paintAreaC.setAntiAlias(true);
        paintAreaC.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        paintAreaB = new Paint();
        paintAreaB.setColor(Color.BLUE);
        paintAreaB.setAntiAlias(true);
//        paintAreaB.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setSubpixelText(true);
        paintText.setTextSize(30);

        paintContentC = new Paint();
        paintContentC.setColor(Color.YELLOW);
        paintContentC.setAntiAlias(true);

    }

    //初始化路径
    private void initPath() {
        pathA = new Path();
        pathC = new Path();
        pathB = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(bitmap);

        if (a.x == -1 && a.y == -1) {//未翻页状态
            drawPathAContent(bitmapCanvas, getPathAreaA(), paintAreaA);
//            bitmapCanvas.drawPath(getPathAreaA(), paintAreaA);
        } else {
            if (from == FROM_TOP) {//顶部翻页
                drawPathAContent(bitmapCanvas, getPathAreaATop(), paintAreaA);
                bitmapCanvas.drawPath(getPathAreaC(), paintAreaC);
                drawPathCContent(bitmapCanvas, getPathAreaATop(), getPathAreaC(), paintContentC);
                drawPathBContent(bitmapCanvas, getPathAreaATop(), getPathAreaB(), paintAreaB);
            } else {//底部翻页
                drawPathAContent(bitmapCanvas, getPathAreaABottom(), paintAreaA);
                bitmapCanvas.drawPath(getPathAreaC(), paintAreaC);
                drawPathCContent(bitmapCanvas, getPathAreaABottom(), getPathAreaC(), paintContentC);
                drawPathBContent(bitmapCanvas, getPathAreaABottom(), getPathAreaB(), paintAreaB);
            }
//            bitmapCanvas.drawPath(getPathAreaC(), paintAreaC);
//            bitmapCanvas.drawPath(getPathAreaB(), paintAreaB);
        }

        canvas.drawBitmap(bitmap, 0, 0, null);

//        因为使用了图像混合模式,所以不能够直接画path
//        canvas.drawPath(getPathAreaA(), paintAreaA);//画区域A
//        canvas.drawPath(getPathAreaC(), paintAreaC);//画区域C

//        canvas.drawText("a", a.x, a.y, paintPoint);
//        canvas.drawText("f", f.x, f.y, paintPoint);
//        canvas.drawText("g", g.x, g.y, paintPoint);
//        canvas.drawText("e", e.x, e.y, paintPoint);
//        canvas.drawText("h", h.x, h.y, paintPoint);
//        canvas.drawText("c", c.x, c.y, paintPoint);
//        canvas.drawText("j", j.x, j.y, paintPoint);
//        canvas.drawText("b", b.x, b.y, paintPoint);
//        canvas.drawText("k", k.x, k.y, paintPoint);
//        canvas.drawText("d", d.x, d.y, paintPoint);
//        canvas.drawText("i", i.x, i.y, paintPoint);

    }

    //绘制A区内容
    private void drawPathAContent(Canvas canvas, Path path, Paint paint) {
        Bitmap contentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);
        contentCanvas.drawPath(path, paint);
        contentCanvas.drawText("AAAAAAAAAA", width - 260, height - 100, paintText);

        canvas.save();
        canvas.clipPath(path, Region.Op.INTERSECT);
        canvas.drawBitmap(contentBitmap, 0, 0, null);
        canvas.restore();
    }

    //绘制B区内容
    private void drawPathBContent(Canvas canvas, Path pathA, Path pathB, Paint paint) {
        Bitmap contentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);
        contentCanvas.drawPath(pathB, paint);
        contentCanvas.drawText("BBBBBBBBBB", width - 260, height - 100, paintText);

        canvas.save();
        canvas.clipPath(pathA);
        canvas.clipPath(getPathAreaC(), Region.Op.UNION);
        canvas.clipPath(getPathAreaB(), Region.Op.REVERSE_DIFFERENCE);
        canvas.drawBitmap(contentBitmap, 0, 0, null);
        drawPathBShadow(canvas);
        canvas.restore();

    }

    //绘制投在B区域的阴影
    private void drawPathBShadow(Canvas canvas) {
        int deepColor = 0xff111111;
        int lightColor = 0x00111111;
        int[] gradientColors = new int[]{deepColor, lightColor};

        int deepOffset = 0;
        int lightOffset = 0;

        float aTof = (float) Math.hypot((a.x - f.x), (a.y - f.y));
        float viewDiagonalLength = (float) Math.hypot(width, height);

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + top);

        GradientDrawable gradientDrawable;

        if (from == FROM_TOP) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            left = (int) (c.x - deepOffset);
            right = (int) (c.x + aTof / 4 + lightOffset);
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            left = (int) (c.x - aTof / 4 - lightOffset);
            right = (int) (c.x + deepOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        float rotateDegrees = (float) Math.toDegrees(Math.atan2(e.x - f.x, h.y - f.y));
        canvas.rotate(rotateDegrees, c.x, c.y);
        gradientDrawable.draw(canvas);
    }

    //绘制C区内容
    private void drawPathCContent(Canvas canvas, Path pathA, Path pathC, Paint paint) {
        Bitmap contentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);
        contentCanvas.drawPath(getPathAreaB(), paint);
        contentCanvas.drawText("CCCCCCCCCC", width - 260, height - 100, paintText);

        canvas.save();
        canvas.clipPath(pathA);
        canvas.clipPath(pathC, Region.Op.UNION);
        canvas.clipPath(pathC, Region.Op.INTERSECT);

        float eh = (float) Math.hypot(f.x - e.x, h.y - f.y);
        float sin = (f.x - e.x) / eh;
        float cos = (h.y - f.y) / eh;

        float[] matrix = {0, 0, 0, 0, 0, 0, 0, 0, 1f};
        matrix[0] = -(1 - 2 * sin * sin);
        matrix[1] = 2 * sin * cos;
        matrix[3] = 2 * sin * cos;
        matrix[4] = 1 - 2 * sin * sin;

        Matrix mMatrix = new Matrix();
        mMatrix.reset();
        mMatrix.setValues(matrix);
        mMatrix.preTranslate(-e.x, -e.y);
        mMatrix.postTranslate(e.x, e.y);

        canvas.drawBitmap(contentBitmap, mMatrix, null);
        canvas.restore();
    }

    //区域A的路径 右上角翻页
    private Path getPathAreaATop() {
        pathA.reset();
        pathA.lineTo(c.x, c.y);
        pathA.quadTo(e.x, e.y, b.x, b.y);
        pathA.lineTo(a.x, a.y);
        pathA.lineTo(k.x, k.y);
        pathA.quadTo(h.x, h.y, j.x, j.y);
        pathA.lineTo(width, height);
        pathA.lineTo(0, height);
        pathA.close();
        return pathA;
    }

    //区域A的路径 右下角翻页
    private Path getPathAreaABottom() {
        pathA.reset();//原点左上角
        pathA.lineTo(0, height);//直线到左下角
        pathA.lineTo(c.x, c.y);//直线到c点
        pathA.quadTo(e.x, e.y, b.x, b.y);//贝塞尔曲线画弧，e为控点，b为终点
        pathA.lineTo(a.x, a.y);//弧线到直线，下半边画完
        pathA.lineTo(k.x, k.y);//上半边直线
        pathA.quadTo(h.x, h.y, j.x, j.y);//直线到弧线
        pathA.lineTo(width, 0);//弧线到右上角
        pathA.close();//封闭路径
        return pathA;
    }

    //完整区域A的路径
    private Path getPathAreaA() {
        pathA.reset();
        pathA.lineTo(0, height);
        pathA.lineTo(width, height);
        pathA.lineTo(width, 0);
        pathA.close();
        return pathA;
    }

    //区域A的路径
    private Path getPathAreaC() {
        pathC.reset();//原点左上角
        pathC.moveTo(i.x, i.y);//i点为起点
        pathC.lineTo(d.x, d.y);//直线到d点
        pathC.lineTo(b.x, b.y);//直线到b点
        pathC.lineTo(a.x, a.y);//直线到a点
        pathC.lineTo(k.x, k.y);//直线到k点
        pathC.close();//封闭路径
        return pathC;
    }

    //区域B的路径
    private Path getPathAreaB() {
        pathB.reset();
        pathB.lineTo(0, height);//左下角
        pathB.lineTo(width, height);//右下角
        pathB.lineTo(width, 0);//右上角
        pathB.close();
        return pathB;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = measureSize(width, widthMeasureSpec);
        int h = measureSize(height, heightMeasureSpec);
        setMeasuredDimension(w, h);
        width = w;
        height = h;

        f.x = width;
        f.y = height;
        a.x = a.y = -1;
        calculatePoint();
    }

    //根据MeasureSpec返回具体的宽高
    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        }

        return result;
    }

    //记录翻页方向
    private int from;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (x <= width / 3) {//左侧翻页
                    from = FROM_LEFT;
                } else if (x > width / 3 && y <= height / 3) {//上侧翻页
                    from = FROM_TOP;
                } else if (x > width / 3 && y >= height * 2 / 3) {//下侧翻页
                    from = FROM_BOTTOM;
                } else if (x >= width * 2 / 3 && y >= height / 3 && y <= height * 2 / 3) {//右侧翻页
                    from = FROM_RIGHT;
                } else {
                    from = -1;
                    Toast.makeText(getContext(), "center", Toast.LENGTH_LONG).show();
                    return false;
                }

                setTouchPoint(x, y, from);
                break;
            case MotionEvent.ACTION_MOVE:
                setTouchPoint(event.getX(), event.getY(), from);
                break;
            case MotionEvent.ACTION_UP:
                backToNormal();
                break;
        }

        return super.onTouchEvent(event);
    }

    //设置触摸点
    private void setTouchPoint(float x, float y, int from) {
        a.x = x;
        a.y = y;
        if (from == FROM_TOP) {
            f.x = width;
            f.y = 0;
            calculatePoint();
            if (c.x < 0) {
                calculateAPoint();
            }
        } else if (from == FROM_BOTTOM) {
            f.x = width;
            f.y = height;
            calculatePoint();
            if (c.x < 0) {
                calculateAPoint();
            }
        } else if (from == FROM_LEFT || from == FROM_RIGHT) {
            f.x = width;
            f.y = height;
            a.y = height - 1;

        }
        calculatePoint();
        postInvalidate();
    }

    //计算C点X坐标，防止左边线超出屏幕
    private float calculateCPoint(MyPoint a, MyPoint f) {
        MyPoint g, e;
        g = new MyPoint();
        e = new MyPoint();
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;
        return e.x - (f.x - e.x) / 2;
    }

    //计算A点X坐标，防止左边线超出屏幕
    private void calculateAPoint() {
        float w0 = width - c.x;
        float w1 = Math.abs(f.x - a.x);
        float w2 = width * w1 / w0;
        a.x = Math.abs(f.x - w2);

        float h1 = Math.abs(f.y - a.y);
        float h2 = w2 * h1 / w1;
        a.y = Math.abs(f.y - h2);
    }

    //返回正常状态
    private void backToNormal() {
        int dx;
        int dy;
        if (from == FROM_TOP) {
            dx = (int) (width - 1 - a.x);
            dy = (int) (1 - a.y);
        } else {
            dx = (int) (width - 1 - a.x);
            dy = (int) (height - 1 - a.y);
        }
        mScroller.startScroll((int) a.x, (int) a.y, dx, dy, 400);
    }

    //mScroller回调
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();

            setTouchPoint(x, y, from);

            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y) {
                a.x = a.y = -1;
                postInvalidate();
            }
        }
    }

    //anroid.graphics.Point的x、y为整型
    public class MyPoint {
        float x, y;

        MyPoint() {
        }

        MyPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}