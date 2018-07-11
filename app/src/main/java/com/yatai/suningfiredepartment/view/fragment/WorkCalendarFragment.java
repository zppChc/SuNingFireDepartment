package com.yatai.suningfiredepartment.view.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.app.MyApplication;
import com.yatai.suningfiredepartment.entity.CategoryEntity;
import com.yatai.suningfiredepartment.entity.WorkCalendar;
import com.yatai.suningfiredepartment.entity.WorkItemEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.activity.WorkDetailActivity;
import com.yatai.suningfiredepartment.view.activity.WorkDetailFinishActivity;
import com.yatai.suningfiredepartment.view.adapter.WorkCalendarAdapter;
import com.yatai.suningfiredepartment.view.adapter.WorkCategoryAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WorkCalendarFragment extends Fragment {
    @BindView(R.id.title_image_back)
    ImageView mBack;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.work_category_recycler_view)
    RecyclerView workCategoryRecyclerView;
    @BindView(R.id.calendar_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.prev_month)
    TextView prevMonthTv;
    @BindView(R.id.next_month)
    TextView nextMonthTv;
    @BindView(R.id.month_display)
    TextView yearMonthTv;

    Unbinder unbinder;


    private String gridId;
    private FinalHttp mHttp;
    private int currentMonth;
    private int currentYear;
    private String categoryId;

    private List<CategoryEntity> categoryList;
    private List<WorkCalendar> workCalendarList;

    private ProgressDialog mProgressDialog;
    private GridLayoutManager mLayoutManager;
    private LinearLayoutManager categoryLLM;
    private WorkCalendarAdapter mCalendarAdapter;
    private WorkCategoryAdapter mCategoryAdapter;

    private int year;
    private int month;
    private int day;

    private int refreshFlag = 0;

    public static WorkCalendarFragment newInstance(String gridId, int categoryId) {
        Bundle args = new Bundle();
        args.putString("gridId", gridId);
        args.putInt("categoryId",categoryId);
        WorkCalendarFragment fragment = new WorkCalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        gridId = data.getString("gridId");
        categoryId =String.valueOf(data.getInt("categoryId",0));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_calendar, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);

        return view;
    }

    private void initView(View view){
        categoryList = new ArrayList<>();
        workCalendarList = new ArrayList<>();
        mHttp = new FinalHttp();

        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        currentMonth = getMonth()+1;
        currentYear = getYear();
        yearMonthTv.setText(""+currentYear+"年"+currentMonth+"月");

        yearMonthTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showDatePicker();
            }
        });

        prevMonthTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentMonth==1){
                    currentYear--;
                    currentMonth = 12;
                    getWorkList(categoryId,String.valueOf(currentYear),String.valueOf(currentMonth));
                }else{
                    currentMonth--;
                    getWorkList(categoryId,String.valueOf(currentYear),String.valueOf(currentMonth));
                }
                yearMonthTv.setText(currentYear+"年"+currentMonth+"月");
            }
        });

        nextMonthTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentMonth == 12){
                    currentMonth=1;
                    currentYear++;
                    getWorkList(categoryId,String.valueOf(currentYear),String.valueOf(currentMonth));
                }else{
                    currentMonth++;
                    getWorkList(categoryId,String.valueOf(currentYear),String.valueOf(currentMonth));
                }
                yearMonthTv.setText(currentYear+"年"+currentMonth+"月");
            }
        });

        mProgressDialog = new ProgressDialog(getContext(), ProgressDialog.THEME_HOLO_DARK);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mTitleName.setText("工作日历");

        mCategoryAdapter = new WorkCategoryAdapter(getContext());
        mCategoryAdapter.setClickListener(new WorkCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtil.show(getContext(), "Position: "+ String.valueOf(position));
                mProgressDialog.show();
                refreshFlag = position;
                mCategoryAdapter.setDefSelect(position);
                categoryId=categoryList.get(position).getId();
                getWorkList(categoryId,String.valueOf(currentYear),String.valueOf(currentMonth));
            }
        });

        categoryLLM = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        workCategoryRecyclerView.setLayoutManager(categoryLLM);
        workCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setCategoryEntityList(categoryList);


        mLayoutManager = new GridLayoutManager(getContext(),7);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCalendarAdapter = new WorkCalendarAdapter(getContext());
        mCalendarAdapter.setListener(new WorkCalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (workCalendarList.get(position).isHasTask()){
                    Gson gson = new Gson();
                    String workItemDetail = gson.toJson(workCalendarList.get(position).getTask());
                    if (workCalendarList.get(position).getTask().getStatus() == 0){
                        if (gridId.equals(PreferenceUtils.getPerfString(getContext(),"gridId",""))) {
                            Intent intent = new Intent(getActivity(), WorkDetailActivity.class);
                            intent.putExtra("workItem", workItemDetail);
                            startActivity(intent);
                        }
                    }else{
                        Intent intent = new Intent(getActivity(), WorkDetailFinishActivity.class);
                        intent.putExtra("workItem", workCalendarList.get(position).getTask().getId());
                        startActivity(intent);
                    }
                }
            }
        });

        mRecyclerView.setAdapter(mCalendarAdapter);
        mCalendarAdapter.setList(workCalendarList);

        getCategoryData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mProgressDialog.show();
        getWorkList(categoryId,String.valueOf(currentYear),String.valueOf(currentMonth));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getCategoryData() {
        String url = getString(R.string.base_url) + "taskCategory";
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                categoryList.clear();
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONArray("data");
                        Gson gson = new Gson();
                        if (data.length() > 0) {
                            CategoryEntity temp = new CategoryEntity();
                            temp.setId("99999");
                            temp.setName("全部");
                            categoryList.add(temp);
                            for (int i = 0; i < data.length(); i++) {
                                CategoryEntity categoryEntity = gson.fromJson(data.getJSONObject(i).toString(), CategoryEntity.class);
                                categoryList.add(categoryEntity);
                            }
                            mCategoryAdapter.setCategoryEntityList(categoryList);
                            if (categoryId.equals("0")){
                                refreshFlag = 0;
                                mCategoryAdapter.setDefSelect(refreshFlag);
                            }else{
                                for (int i = 0;i<categoryList.size();i++){
                                    if (categoryList.get(i).getId().equals(String.valueOf(categoryId))){
                                        refreshFlag = i;
                                        mCategoryAdapter.setDefSelect(refreshFlag);
                                    }
                                }
                            }

                            for(int j = 0; j<categoryList.size(); j++) {
                                if (categoryList.get(j).getId().equals(String.valueOf(categoryId))) {
                                    categoryLLM.scrollToPositionWithOffset(j, 0);
                                }
                            }
                        } else {
                            mCategoryAdapter.setCategoryEntityList(categoryList);
                        }
                    } else {
                        ToastUtil.show(getContext(), jb.getString("message"));
                        mCategoryAdapter.setCategoryEntityList(categoryList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(getContext(), strMsg);
            }
        });
    }

    private void getWorkList(String categoryId,String year,String month){
        String url = getString(R.string.base_url) + "grid/" + gridId + "/task/" + categoryId+"/calendar/"+ year+"/"+month;
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                workCalendarList.clear();
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONObject("data").getJSONArray("calendar");
                        if (data.length() > 0) {
                            Gson gson = new Gson();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject workCalendarOb = data.getJSONObject(i);
                                String taskOb = workCalendarOb.getString("task");
                                WorkCalendar workCalendar = new WorkCalendar();
                                workCalendar.setDay(workCalendarOb.getInt("day"));
                                workCalendar.setWeek(workCalendarOb.getInt("week"));
                                if (taskOb != null && !taskOb.equals("null")){
                                    WorkItemEntity workItemEntity = gson.fromJson(taskOb,WorkItemEntity.class);
                                    workCalendar.setTask(workItemEntity);
                                    workCalendar.setHasTask(true);
                                }else{
                                    workCalendar.setHasTask(false);
                                }
                                workCalendarList.add(workCalendar);
                            }
                            mCalendarAdapter.setList(workCalendarList);
                        } else {
                            mCalendarAdapter.setList(workCalendarList);
                        }
                    } else {
                        ToastUtil.show(getContext(), jb.getString("message"));
                        mCalendarAdapter.setList(workCalendarList);
                    }
                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(getContext(), strMsg);
                mProgressDialog.dismiss();
            }
        });

    }


    private int getMonth(){
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.MONTH);
    }
    private int getYear(){
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.YEAR);
    }

    private void showDatePicker() {
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(getContext(), Datelistener, year, month, day);
        dialog.show();

        //只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup)((ViewGroup)dp.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            //如果想隐藏掉年，将getChildAt(2)改为getChildAt(0)
        }
    }

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener Datelistener=new DatePickerDialog.OnDateSetListener(){
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {

            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year=myyear;
            month=monthOfYear;
            day=dayOfMonth;
            //更新日期
            updateDate();

        }
        //当DatePickerDialog关闭时，更新日期显示
        private void updateDate(){
            //在TextView上显示日期
            currentMonth = month+1;
            currentYear = year;
            yearMonthTv.setText(currentYear+"年"+currentMonth+"月");
            getWorkList(categoryId,String.valueOf(currentYear),String.valueOf(currentMonth));
        }
    };
}
