package com.xujun.contralayout.UI.bottomsheet;

import android.os.AsyncTask;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.xujun.contralayout.R;
import com.xujun.contralayout.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BaiduMapSample extends AppCompatActivity implements GridAdapter.OnRecyclerViewItemClickListener {

    private static RecyclerView recyclerview;
    private CoordinatorLayout coordinatorLayout;
    private GridAdapter mAdapter;//recyclerView适配器
    private List<Meizi> meizis;
    private StaggeredGridLayoutManager mLayoutManager;
    private int lastVisibleItem ;//recyclerview最后显示的Item,用于判断recyclerview自动加载下一页
    private int page=1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout design_bottom_sheet,design_bottom_sheet_bar;
    private BottomSheetBehavior behavior;
    private ImageView bottom_sheet_iv;
    private TextView bottom_sheet_tv;

    public static  final String TAG="xujun";

    /**
     * 标识初始化时是否修改了底栏高度
     */
    private boolean isSetBottomSheetHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_demo);

        StatusBarUtil.setStatusBarColor(BaiduMapSample.this,R.color.colorPrimaryDark);//设置状态栏颜色

        initView();
        setListener();

        new GetData().execute("http://gank.io/api/data/福利/10/1");//初始化数据

    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.bottom_sheet_demo_coordinatorLayout);

        recyclerview=(RecyclerView)findViewById(R.id.bottom_sheet_demo_recycler);
        mLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);

        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.bottom_sheet_demo_swipe_refresh) ;
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));//调整下拉控件位置

        design_bottom_sheet_bar=(RelativeLayout) findViewById(R.id.design_bottom_sheet_bar);

        design_bottom_sheet=(RelativeLayout) findViewById(R.id.design_bottom_sheet);
        bottom_sheet_iv=(ImageView) findViewById(R.id.bottom_sheet_iv);
        bottom_sheet_tv=(TextView) findViewById(R.id.bottom_sheet_tv);

        behavior = BottomSheetBehavior.from(design_bottom_sheet);

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

        //修改SetBottomSheet的高度 留出顶部工具栏的位置
        if(!isSetBottomSheetHeight){
            CoordinatorLayout.LayoutParams linearParams =(CoordinatorLayout.LayoutParams) design_bottom_sheet.getLayoutParams();
            linearParams.height=coordinatorLayout.getHeight()-design_bottom_sheet_bar.getHeight();
            design_bottom_sheet.setLayoutParams(linearParams);
            isSetBottomSheetHeight=true;
        }

    }

    private void setListener(){

        //底栏状态改变的监听
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState!=BottomSheetBehavior.STATE_COLLAPSED&&bottom_sheet_tv.getVisibility()==View.VISIBLE){
                    bottom_sheet_tv.setVisibility(View.GONE);
                    bottom_sheet_iv.setVisibility(View.VISIBLE);
                    mAdapter.setOnItemClickListener(null);//底栏展开状态下屏蔽RecyclerView item的点击
                }else if(newState==BottomSheetBehavior.STATE_COLLAPSED&&bottom_sheet_tv.getVisibility()==View.GONE){
                    bottom_sheet_tv.setVisibility(View.VISIBLE);
                    bottom_sheet_iv.setVisibility(View.GONE);
                    mAdapter.setOnItemClickListener(BaiduMapSample.this);//底栏折叠状态下恢复RecyclerView item的点击
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                if(bottomSheet.getTop()<2*design_bottom_sheet_bar.getHeight()){
                    //设置底栏完全展开时，出现的顶部工具栏的动画
                    design_bottom_sheet_bar.setVisibility(View.VISIBLE);
                    design_bottom_sheet_bar.setAlpha(slideOffset);
                    design_bottom_sheet_bar.setTranslationY(bottomSheet.getTop()-2*design_bottom_sheet_bar.getHeight());
                }
                else{
                    design_bottom_sheet_bar.setVisibility(View.INVISIBLE);
                }
            }
        });



        design_bottom_sheet_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: =" );

                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//点击顶部工具栏 将底栏变为折叠状态
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                new GetData().execute("http://gank.io/api/data/福利/10/1");
            }
        });

        //recyclerView滑动监听
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(behavior.getState()!=BottomSheetBehavior.STATE_COLLAPSED){
                    //recyclerview滚动时  如果BottomSheetBehavior不是折叠状态就置为折叠
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                //用来判断recyclerview自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +2>=mLayoutManager.getItemCount()) {
                    new GetData().execute("http://gank.io/api/data/福利/10/"+(++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //用来判断recyclerview自动加载
                int[] positions= mLayoutManager.findLastVisibleItemPositions(null);
                lastVisibleItem = Math.max(positions[0],positions[1]);
            }
        });
    }

    @Override
    public void onItemClick(View view) {
        //recyclerview item点击事件处理

        int position=recyclerview.getChildAdapterPosition(view);



        Picasso.with(BaiduMapSample.this).load(meizis.get(position).getUrl()).into(bottom_sheet_iv);
    }

    @Override
    public void onItemLongClick(View view) {

    }

    //获取图片列表数据
    private class GetData extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {

            return MyOkhttp.get(params[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!TextUtils.isEmpty(result)){

                JSONObject jsonObject;
                Gson gson=new Gson();
                String jsonData=null;

                try {
                    jsonObject = new JSONObject(result);
                    jsonData = jsonObject.getString("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(meizis==null||meizis.size()==0){
                    meizis= gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {}.getType());
                    Meizi pages=new Meizi();
                    pages.setPage(page);
                    meizis.add(pages);

                    Picasso.with(BaiduMapSample.this).load(meizis.get(0).getUrl()).into(bottom_sheet_iv);
                }else{
                    List<Meizi> more= gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {}.getType());
                    meizis.addAll(more);
                    Meizi pages=new Meizi();
                    pages.setPage(page);
                    meizis.add(pages);
                }

                if(mAdapter==null){
                    recyclerview.setAdapter(mAdapter = new GridAdapter(BaiduMapSample.this,meizis));
                    mAdapter.setOnItemClickListener(BaiduMapSample.this);

                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
