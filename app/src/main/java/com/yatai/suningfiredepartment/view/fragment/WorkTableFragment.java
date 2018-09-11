package com.yatai.suningfiredepartment.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.RecordEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.WorkTableAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class WorkTableFragment extends Fragment {

    WorkTableAdapter mPanelAdapter;
    ScrollablePanel scrollablePanel;
    FinalHttp mHttp;
    String gridId;
    List<String> categoryList;
    List<String> dateList;
    List<List<RecordEntity>> recordList;
    ImageView mBack;
    TextView mTitle;


    public static WorkTableFragment newInstance(String data){
        Bundle args = new Bundle();
        args.putString("gridId", data);
        WorkTableFragment fragment = new WorkTableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        gridId = data.getString("gridId");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_table,container,false);
        mPanelAdapter= new WorkTableAdapter(getContext());
        scrollablePanel= (ScrollablePanel) view.findViewById(R.id.scrollable_panel);
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        dateList = new ArrayList<>();
        categoryList = new ArrayList<>();
        recordList = new ArrayList<List<RecordEntity>>();
        getTableData(year);
        mBack=(ImageView)view.findViewById(R.id.title_image_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        mTitle=(TextView)view.findViewById(R.id.title_name);
        mTitle.setText("进度表("+year+")");
        return view;
    }

    private void getTableData(String year){
        mHttp = new FinalHttp();
        String url = getString(R.string.base_url) + "grid/"+gridId+"/schedule/"+year;
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        categoryList.clear();
                        dateList.clear();
                        recordList.clear();
                        JSONObject data = jb.getJSONObject("data");
                        JSONArray task = data.getJSONArray("task");
                        for (int i=0; i<task.length();i++){
                            categoryList.add(task.getString(i));
                        }
                        mPanelAdapter.setCategoryList(categoryList);

                        JSONArray date = data.getJSONArray("date");
                        for (int i = 0; i<date.length();i++){
                            dateList.add(date.getString(i));
                        }
                        mPanelAdapter.setDateList(dateList);
                        JSONArray record = data.getJSONArray("record");
                        List<RecordEntity> list = new ArrayList<>();
                        Gson gson = new Gson();
                        for (int i=0; i<record.length();i++){
                            RecordEntity recordEntity = gson.fromJson(record.getString(i),RecordEntity.class);
                            list.add(recordEntity);
                        }
                        recordList=splitList(list,categoryList.size());
                        Logger.i("RecordList : "+ recordList.size());
                        mPanelAdapter.setRecordList(recordList);
                        scrollablePanel.setPanelAdapter(mPanelAdapter);
                    }else{
                        ToastUtil.show(getContext(),jb.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(getContext(),strMsg);
            }
        });
    }

    public static List<List<RecordEntity>> splitList(List<RecordEntity> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }
        List<List<RecordEntity>> result = new ArrayList<List<RecordEntity>>();
        int size = list.size();
        int count = (size + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<RecordEntity> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }


}

