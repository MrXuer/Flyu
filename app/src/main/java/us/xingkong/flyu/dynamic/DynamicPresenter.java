package us.xingkong.flyu.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.OnRequestListener;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/9 1:15
 * @描述:
 * @更新日志:
 */
public class DynamicPresenter implements DynamicContract.Presenter,
        OnRequestListener<DownloadModel> {

    private DynamicContract.View mView;
    private DynamicModel model;
    private DynamicAdapter mAdapter;

    DynamicPresenter(DynamicContract.View view) {
        mView = view;
        mView.setPresenter(this);
        model = new DynamicModel();
        model.setOnRequestListener(this);
    }

    @Override
    public void load() {
        mView.setEnable(false);
        mView.setRefresh(true);
        model.load(mView.getUsername());
    }

    @Override
    public void display() {
        mView.setAdapter(mAdapter);
        mView.setEnable(true);
        mView.setRefresh(false);
        mAdapter.setItemClickListener(new DynamicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DownloadModel.Message message) {
                mView.toOtherActivity(message);
            }

            @Override
            public void onReloadClick() {
                load();
            }
        });
    }

    @Override
    public void start() {
        if (!mView.isActive()) {
            return;
        }
        load();
    }

    @Override
    public void destroy() {

    }

    private DynamicAdapter setAdapter(DownloadModel downloadModel) {
        List<DownloadModel.Message> list = downloadModel.getMessage();
        Collections.reverse(list);
        DynamicAdapter adapter = new DynamicAdapter(mView.getContext(), list);
        adapter.setName(mView.getUsername());
        adapter.notifyDataSetChanged();
        mView.setDynamic(String.valueOf(list.size()));
        return adapter;
    }

    @Override
    public void success(DownloadModel downloadModel) {
        mAdapter = setAdapter(downloadModel);
        display();
    }

    @Override
    public void failure(String errorMsg) {
        if (errorMsg.equals("Gson Error")) {
            List<DownloadModel.Message> list = new ArrayList<>();
            mAdapter = new DynamicAdapter(mView.getContext(), list);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new DynamicAdapter(mView.getContext(), null);
            mAdapter.notifyDataSetChanged();
        }
        display();
        mView.showMessage(Constants.NETWORK_IS_UNAVAILABLE);
    }
}