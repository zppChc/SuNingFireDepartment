package com.yatai.suningfiredepartment.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.WorkCalendar;
import com.yatai.suningfiredepartment.view.activity.SubWorkCalendarActivity;
import com.yatai.suningfiredepartment.view.activity.SubWorkScheduleActivity;

public class BottomDialogView extends Dialog {

    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private View view;
    private Context context;
    private Button mCancle;
    private Button mCanendar;
    private Button mSchedule;
    private String gridId;
    private int categoryId;

    //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    public BottomDialogView(Context context,boolean isCancelable,boolean isBackCancelable,String gridId, int categoryId) {
        super(context, R.style.MyDialog);

        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.item_work_dialog,null);
        this.iscancelable = isCancelable;
        this.isBackCancelable = isBackCancelable;
        this.gridId =gridId;
        this.categoryId = categoryId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);//这行一定要写在前面
        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCancelable);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        mCanendar=(Button)view.findViewById(R.id.btn_open_calendar);
        mCanendar.setOnClickListener(listener);
        mSchedule=(Button)view.findViewById(R.id.btn_open_schedule);
        mSchedule.setOnClickListener(listener);
        mCancle = (Button)view.findViewById(R.id.btn_cancel);
        mCancle.setOnClickListener(listener);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_cancel:
                    dismiss();
                    break;
                case R.id.btn_open_calendar:
                    Intent intentCalendar = new Intent(context, SubWorkCalendarActivity.class);
                    intentCalendar.putExtra("gridId",gridId);
                    intentCalendar.putExtra("categoryId",categoryId);
                    context.startActivity(intentCalendar);
                    dismiss();
                    break;
                case R.id.btn_open_schedule:
                    Intent intentSchedule = new Intent(context, SubWorkScheduleActivity.class);
                    intentSchedule.putExtra("gridId",gridId);
                    context.startActivity(intentSchedule);
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };

}
