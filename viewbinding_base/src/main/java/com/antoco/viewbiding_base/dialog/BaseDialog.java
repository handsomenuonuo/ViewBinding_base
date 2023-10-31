package com.antoco.viewbiding_base.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentManager;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**********************************
 * @Name: BaseDialog
 * @Copyright： CreYond
 * @CreateDate： 2022/1/11 15:54
 * @author: HuangFeng
 * @Version： 1.0
 * @Describe:
 *
 **********************************/
public abstract class BaseDialog extends BDialogFragment implements LifecycleProvider<FragmentEvent> {
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    public String t = "BaseDialog";
    protected View baseView;

    private Map<Integer,GetViewListener> getViewMap = new LinkedHashMap<>();

    public BaseDialog addGetViewCallBack(@IdRes int id,@NonNull GetViewListener getViewListener){
        getViewMap.put(id,getViewListener);
        return this;
    }


    public BehaviorSubject<FragmentEvent> getLifecycleSubject() {
        return lifecycleSubject;
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    @CallSuper
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
        setBackgroundAlpha(getBackgroundAlpha());
        int w = 0;
        int h = 0;
        if(getWidth()!=0){
            w = getWidth();
        }else if(getWidthPercent()!=0){
            w = getWidthByPercent(getWidthPercent());
        }else {
            w =  ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if(getHeight()!=0){
            h = getHeight();
        }else {
            h =  ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        setWidthHeight(w,h);
        init();
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    @CallSuper
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setGravity(getGravity());
        getDialog().setCanceledOnTouchOutside(outsideCancelable());
        getDialog().setCancelable(cancelable());
        if(getAnim() != 0){
            getDialog().getWindow().setWindowAnimations(getAnim());
        }
        baseView =  inflater.inflate(getLayout(), container, false);
        initView();
        for(Integer id : getViewMap.keySet()){
            View v = baseView.findViewById(id);
            if(v!=null){
                getViewMap.get(id).onGetView(v);
            }
        }
        return baseView;
    }

    /***
     * 设置屏宽百分比 和 高度自适应
     * @param percent
     */
    public void setWidthPercent(@FloatRange(from = 0.0, to = 1.0) float percent){
        int w = getWidthByPercent(percent);
        int h = ViewGroup.LayoutParams.WRAP_CONTENT;
        setWidthHeight(w,h);
    }

    /***
     * 获取 占屏宽百分比的宽度
     * @param percent
     * @return
     */
    public int getWidthByPercent(@FloatRange(from = 0.0, to = 1.0) float percent){
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        return (int) (dm.widthPixels*percent);
    }

    /***
     * 设置弹框具体宽高
     * @param width
     * @param height
     */
    public void setWidthHeight(int width,int height){
        if(width <= 0)width = ViewGroup.LayoutParams.WRAP_CONTENT;
        if(height <= 0)height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width,height);
    }

    public void setBackgroundAlpha(@FloatRange(from = 0.0, to = 1.0) float backgroundAlpha){
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount = backgroundAlpha;
        lp.flags = getDialog().getWindow().getAttributes().flags | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getDialog().getWindow().setAttributes(lp);
    }

    /***
     * 展示弹框，如果layout不传，会在屏幕中间，弹出默认弹框
     */
    public void show(FragmentManager fm){
        show(fm,t);
    }

    protected abstract @FloatRange(from = 0.0, to = 1.0) float getBackgroundAlpha();
    protected abstract @FloatRange(from = 0.0, to = 1.0) float getWidthPercent();
    protected abstract int getWidth();//优先
    protected abstract int getHeight();
    protected abstract  @DialogGravity int getGravity();
    protected abstract  @StyleRes int getAnim();
    protected abstract  boolean cancelable();
    protected abstract  boolean outsideCancelable();
    protected abstract void init();
    protected abstract int getLayout();
    protected abstract void initView();


    public interface GetViewListener<T extends View>{
        void onGetView(T view);
    }

}
