package com.example.cxyang.pomelotiming.main;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class MyItemTouchHelper extends ItemTouchHelper.Callback {
    private static final String TAG = "drag";
    private OnItemTouchListener onItemTouchListener;

    public void setOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.onItemTouchListener = onItemTouchListener;
    }

    /**
     * 根据 RecyclerView 不同的布局管理器，设置不同的滑动、拖动方向
     * 该方法使用 makeMovementFlags(int dragFlags, int swipeFlags) 方法返回
     * 参数: dragFlags:拖动的方向
     *      swipeFlags:滑动的方向
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //如果是LinearLayoutManager则只能向上向下滑动，
            //此处第二个参数设置支持向右滑动
            return makeMovementFlags(ItemTouchHelper.UP   | ItemTouchHelper.DOWN , ItemTouchHelper.RIGHT );
    }

    /**
     * 当 ItemTouchHelper 拖动一个Item时该方法将会被回调，Item将从旧的位置移动到新的位置
     * 如果不拖动这个方法将从来不会调用,返回true表示已经被移动到新的位置
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition   = target.getAdapterPosition();
        onItemTouchListener.onMove(fromPosition,toPosition);
        return true;
    }

    /**
     * 当Item被滑动的时候被调用
     * 如果你不滑动这个方法将不会被调用
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //此处是侧滑删除的主要代码
        int position = viewHolder.getAdapterPosition();
        onItemTouchListener.onSwiped(position);
    }

    /**
     * 当Item被滑动、拖动的时候被调用
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //...
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 当与用户交互结束或相关动画完成之后被调用
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //...
        super.clearView(recyclerView, viewHolder);
    }

    /**
     * 移动交换数据的更新监听
     */
    public interface OnItemTouchListener {
        //拖动Item时调用
        void onMove(int fromPosition, int toPosition);
        //滑动Item时调用
        void onSwiped(int position);
    }
}