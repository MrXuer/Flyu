package us.xingkong.flyu.activity.register;

import android.support.annotation.NonNull;
import android.view.View;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.BasePresenterImpl;
import us.xingkong.flyu.model.BmobUserModel;
import us.xingkong.flyu.util.L;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 21:15
 * @描述:
 * @更新日志:
 */
public class RegisterPresenter extends BasePresenterImpl<RegisterContract.View>
        implements RegisterContract.Presenter {

    private RegisterModel registerModel;
    private UserModelDao dao;

    RegisterPresenter(@NonNull RegisterContract.View view) {
        super(view);
        registerModel = new RegisterModel();
        //dao = App.getInstance().getDaoSession().getUserModelDao();
    }

    @Override
    public void register() {
        mView.setEnable(false);
        mView.setVisibility(View.VISIBLE);

        if (!mView.getPassword().equals(mView.getRepassword())) {
            mView.setEnable(true);
            mView.setVisibility(View.INVISIBLE);
            mView.showMessage("两次密码不一致");
            return;
        }

        registerModel.bmobRegister(mView.getUserName(), mView.getPassword()
                , mView.getEmail()).signUp(new SaveListener<BmobUserModel>() {
            @Override
            public void done(BmobUserModel bmobUserModel, BmobException e) {
                if (e == null) {
                    L.i("RegisterPresenter", mView.getUserName() + "signUp successfully");
                    mView.showToast("注册成功");
                    mView.toOtherActivity(bmobUserModel);
                } else {
                    L.e("RegisterPresenter", e.getMessage());
                    L.e("ErrorCode", e.getErrorCode() + "");
                    showBmobMessage(e.getErrorCode());
                }
                mView.setEnable(true);
                mView.setVisibility(View.INVISIBLE);
            }
        });

        /*registerModel.register(mView.getUserName(), mView.getEmail(),
                mView.getPassword()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                if (s.equals(Constants.SUCCESS)) {
                    UserModel userModel = new UserModel();
                    userModel.setUsername(mView.getUserName());
                    userModel.setEmail(mView.getEmail());
                    userModel.setPassword(mView.getPassword());
                    userModel.setIsLogged(false);
                    dao.insert(userModel);
                    mView.showToast("注册成功");
                    mView.toOtherActivity(userModel);
                } else {
                    showMessage(s);
                }
                mView.setEnable(true);
                mView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    mView.showMessage("网络连接错误");
                } else if (e instanceof SocketTimeoutException) {
                    mView.showMessage("网络连接超时");
                }
                mView.setEnable(true);
                mView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {

            }
        });*/
    }

    @Override
    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
    }

    private void showMessage(String message) {
        switch (message) {
            case Constants.NETWORK_IS_UNAVAILABLE:
                mView.showMessage("网络连接错误");
                break;
            case Constants.USER_EXISTS:
                mView.showMessage("用户已被注册");
                break;
            case Constants.USERNAME_IS_TOO_LONG:
                mView.showMessage("用户名过长");
                break;
            case Constants.PASSWORD_IS_ILLEAGAL:
                mView.showMessage("密码长度不在6-16位");
                break;
            case Constants.EMAIL_IS_ILLEAGAL:
                mView.showMessage("邮件格式有误");
                break;
            case Constants.PASSWORD_ISNT_THE_SAME:
                mView.showMessage("两次密码不一致");
                break;
            case Constants.USERNAME_OR_PASSWORD_IS_EMPTY:
                mView.showMessage("不能为空");
                break;
        }
    }

    private void showBmobMessage(int errorCode) {
        switch (errorCode) {
            case 202:
                mView.showMessage("这个人已经被注册了");
                break;
            case 203:
                mView.showMessage("这个邮箱已经被注册了");
                break;
            case 301:
                mView.showMessage("请好好填写你的邮箱");
                break;
            case 304:
                mView.showMessage("请好好填写你的用户名和密码");
                break;
        }
    }
}
