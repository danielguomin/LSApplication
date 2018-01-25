package com.train.lingshi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by guomin on 2018/1/23.
 */

public class WheelListView extends ListView {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
    private final Object SCROLL_LOCK = new Object();
    //坐标都是相对于手机屏幕
    private int topY;//悬浮框顶部坐标
    private int middleY;//悬浮框中间坐标
    private int bottomY;//悬浮框底部坐标
    private int selectPosition;//滚动时adapter当前选中的position
    private boolean fromTouch;//当调用smoothScrollToPositionFromTop()方法时也会触发onScroll，需要屏蔽掉
    private WheelAdapter wheelAdapter;//数据适配器
    private OnSelectCallback callback;//滚动时的回调接口
    private int mMaxYOverScrollDistance;

    public interface OnSelectCallback {
        void onHandleScroll(int selectPosition);

        void onHandleIdle(WheelListView wheelListView, int selectPosition);
    }

    public WheelListView(Context context) {
        this(context, null);
    }

    public WheelListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initBounceListView();
        setClipChildren(false);
        setClipToPadding(false);
    }

    /**
     * 阻尼效果实现
     */
//    private void initBounceListView(){
//        //get the density of the screen and do some maths with it on the max overscroll distance
//        //variable so that you get similar behaviors no matter what the screen size
//        final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
//        final float density = metrics.density;
//        mMaxYOverScrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
//    }
//
//    @Override
//    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){
//        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverScrollDistance;
//        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverScrollDistance, isTouchEvent);
//    }
    public void setAdapter(WheelAdapter adapter) {
        super.setAdapter(adapter);
        wheelAdapter = adapter;
        setCallback(adapter);
    }

    private void setCallback(OnSelectCallback callback) {
        this.callback = callback;
    }

    /**
     * 初始化
     *
     * @param selectView
     * @param rootView
     */
    protected void setUp(View selectView, View rootView) {
        if (null == selectView || null == rootView) {
            return;
        }
        //让listview现实的区域刚好和悬浮框重合
        setPadding(0, selectView.getTop(), 0, rootView.getBottom() - selectView.getBottom());

        int location1[] = new int[2];
        selectView.getLocationOnScreen(location1);

        topY = location1[1];
        middleY = topY + selectView.getMeasuredHeight() / 2;
        bottomY = topY + selectView.getMeasuredHeight();

        setUpScroll();
    }

    /**
     * 设置滚动监听
     */
    private void setUpScroll() {
        setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (null == callback) {
                    return;
                }
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (!fromTouch) {
                        return;
                    }
                    fromTouch = false;
                    //adapter实现了callback接口
                    callback.onHandleIdle(WheelListView.this, selectPosition);
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    fromTouch = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null == callback) {
                    return;
                }
                if (!fromTouch) {
                    return;
                }
                synchronized (SCROLL_LOCK) {
                    handleScroll(firstVisibleItem);
                }
            }
        });
    }

    /**
     * 处理滚动
     *
     * @param firstVisibleItem
     */
    private void handleScroll(int firstVisibleItem) {
        //取出与中线距离最近的item
        int tempD = -1;
        //本次滑动计算出的position
        int tempP = -1;
        //遍历listview当前可见的item，不是所有item
        for (int i = 0; i < getChildCount(); i++) {
            //计算每个item相对于屏幕的坐标值
            View child = getChildAt(i);
            int location2[] = new int[2];
            child.getLocationOnScreen(location2);
            int childBottom = location2[1] + child.getMeasuredHeight();
            int childTop = location2[1];
            //            SLog.d("bottomY : " + bottomY + " topY : " + topY + " middleY : " + middleY + " childBottom : " + childBottom + " childTop : " + childTop);
            //在悬浮框区域外的，排除掉
            if (childBottom < topY || childTop > bottomY) {
                continue;
            }
            //找到item中线离悬浮框中线最近的item，比距离即可
            int childMiddleY = childBottom - child.getMeasuredHeight() / 2;
            int position = firstVisibleItem + i;//当前item真正的position
            int distance = Math.abs(middleY - childMiddleY);
            if (tempD == -1) {
                tempD = distance;
                tempP = position;
            } else if (tempD > distance) {
                tempD = distance;
                tempP = position;
            }
        }

        if (tempP < 0) {
            tempP = 0;
        } else if (tempP > wheelAdapter.getCount() - 1) {
            tempP = wheelAdapter.getCount() - 1;
        }
        //防止多次notify同一个position
        if (selectPosition == tempP) {
            return;
        }
        selectPosition = tempP;
        callback.onHandleScroll(selectPosition);
    }
}
