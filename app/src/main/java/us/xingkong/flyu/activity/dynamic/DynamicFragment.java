package us.xingkong.flyu.activity.dynamic;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.detail.DetailActivity;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BaseFragment;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.model.EventModel;
import us.xingkong.flyu.util.S;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:11
 * @描述:
 * @更新日志:
 */
public class DynamicFragment extends BaseFragment<DynamicContract.Presenter>
        implements DynamicContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static DynamicFragment newInstance() {
        return new DynamicFragment();
    }

    @Override
    protected DynamicContract.Presenter newPresenter() {
        return new DynamicPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View root) {
        EventBus.getDefault().register(this);

        LinearLayoutManager manager =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        swipeRefresh.setColorSchemeResources(R.color.zhihu);

        mPresenter.loadDynamic();
    }

    @Override
    protected void initData() {

    }

    @Subscribe
    public void onMainActivityFinished(EventModel<String> eventModel) {
        if (!eventModel.getPublisher().equals("MainActivity")) {
            return;
        }
        mPresenter.loadDynamic();
    }

    @Override
    protected void initListener() {
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public String getUsername() {
        //return ContainerActivity.getUserModel().getUsername();
        return ContainerActivity.getBmobUserModel().getUsername();
    }

    @Override
    public void setDynamic(String size) {
        EventBus.getDefault().post(new EventModel<>("DynamicFragment", size));
    }

    @Override
    public void setRefresh(boolean refresh) {
        swipeRefresh.setRefreshing(refresh);
    }

    @Override
    public void setAdapter(DynamicAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setEnable(boolean enable) {
        //swipeRefresh.setEnabled(enable);
        recyclerView.setEnabled(enable);
    }

    @Override
    public void toOtherActivity(DownloadModel.Message message) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra("Message", message);
        startActivity(intent);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMessage(String message) {
        S.shortSnackbar(mActivity.findViewById(R.id.root), message);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadDynamic();
    }
}
