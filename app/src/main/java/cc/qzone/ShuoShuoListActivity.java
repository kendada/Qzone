package cc.qzone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.List;

import cc.qzone.adapter.QzoneShuoShuoAdapter;
import cc.qzone.model.Girl;
import cc.qzone.util.JsonUtil;
import cc.qzone.util.api.ApiUtil;

public class ShuoShuoListActivity extends AppCompatActivity {

    private ListView list_view = null;

    private QzoneShuoShuoAdapter adapter;
    private List<Girl> girlList = null;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuo_shuo_list);

        list_view = (ListView)findViewById(R.id.list_view);

        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    ImageLoader.getInstance().resume(); //滑动停止，开始加载图片
                } else {
                    ImageLoader.getInstance().pause();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount - 1) {
                    loadMore();
                }
            }
        });

        initData();


        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
    }

    public void initData(){
        ApiUtil.girlsData(page, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                girlList = JsonUtil.girlList(s);
                if (girlList != null && girlList.size() > 0) {
                    adapter = new QzoneShuoShuoAdapter(ShuoShuoListActivity.this, girlList);
                    list_view.setAdapter(adapter);
                    page++;
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void loadMore(){
        ApiUtil.girlsData(page, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                List<Girl> list = JsonUtil.girlList(s);
                girlList.addAll(list);
                adapter.notifyDataSetChanged();
                page++;
            }

            @Override
            public void onFinish() {
                Toast.makeText(ShuoShuoListActivity.this, "第"+page+"页", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
