package com.antoco.viewbiding_base.dialog;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DimenRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentManager;


import com.antoco.viewbiding_base.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**********************************
 * @Name: BaseDialog
 * @Copyright： CreYond
 * @CreateDate： 2021/12/15 11:27
 * @author: HuangFeng
 * @Version： 1.0
 * @Describe:
 *
 **********************************/
public class QuickDialog extends BaseDialog {

    private String t = "dialog";
    private int layout = 0;
    private int anim = 0;
    private float perSent = 1.0f;
    private int gravity = Gravity.CENTER;
    private boolean cancelOutside = true;
    private boolean cancelable = true;
    private Map<Integer, View.OnClickListener> clickViewMap = new HashMap<>();
    private Map<Integer, GetViewListener<? extends View>> getViewMap = new HashMap<>();
    private HashMap<Integer, String> setTextMap = new HashMap<>();
    private float backgroundAlpha = 0.4f;
    private int  cancelId = 0;
    private int  mHeight = 0;
    private int  mWidth = 0;


    /***
     * 展示弹框，如果layout不传，会在屏幕中间，弹出默认弹框
     */
    public void show(FragmentManager fm){
        show(fm,t);
    }

    @Override
    public void dismiss() {
        try {
            if (getFragmentManager()==null){
                Log.w("QuickDialog", "dismiss: "+this+" not associated with a fragment manager." );
            }else {
                super.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected int getLayout() {
        return layout;
    }

    @Override
    protected void initView() {
        if(cancelId!=0){
            baseView.findViewById(cancelId).setOnClickListener(v -> {
                getDialog().cancel();
            });
        }
        for(Integer id : clickViewMap.keySet()){
            baseView.findViewById(id).setOnClickListener(clickViewMap.get(id));
        }
        for(Integer id : getViewMap.keySet()){
            Objects.requireNonNull(getViewMap.get(id)).onGet(baseView.findViewById(id));
        }
        for(Integer id : setTextMap.keySet()){
            View v = baseView.findViewById(id);
            if(v instanceof TextView){
                ((TextView) v).setText(setTextMap.get(id));
            }
        }
    }

    @Override
    protected float getBackgroundAlpha() {
        return backgroundAlpha;
    }

    @Override
    protected float getWidthPercent() {
        return perSent;
    }

    @Override
    protected int getWidth() {
        if(mWidth == 0)return 0;
        return (int) getResources().getDimension(mWidth);
    }

    @Override
    protected int getHeight() {
        if(mHeight == 0)return 0;
        return (int) getResources().getDimension(mHeight);
    }

    @Override
    protected int getGravity() {
        return gravity;
    }

    @Override
    protected int getAnim() {
        return anim;
    }

    @Override
    protected boolean cancelable() {
        return cancelable;
    }

    @Override
    protected boolean outsideCancelable() {
        return cancelOutside;
    }


    public static class Builder{
        @LayoutRes
        private int layout;
        @StyleRes
        private int anim;
        private float perSent = 0.85f;
        private boolean cancelOutside  = true;
        private int height = 0;
        private int width = 0;
        private Map<Integer, View.OnClickListener> clickViewMap = new HashMap<>();
        private Map<Integer, GetViewListener<? extends View>> getViewMap = new HashMap<>();
        private HashMap<Integer, String> setTextMap = new HashMap<>();
        private boolean cancelable = true;
        private float backgroundAlpha  = 0.4f;
        @IdRes
        private int  cancelId = 0;
        @DialogGravity
        private int gravity = Gravity.CENTER;
        private String tag = "dialog";

        /***
         * 设置自定义layout
         */
        public Builder layout(@LayoutRes int layout){
            this.layout = layout;
            return this;
        }

        /***
         * 设置height
         */
        public Builder height(@DimenRes int height){
            this.height = height;
            return this;
        }

        /***
         * 设置width 设置了之后screenWidthPercent无效
         */
        public Builder width(@DimenRes int width){
            this.width = width;
            return this;
        }

        /***
         * 设置动画效果
         */
        public Builder anim(@StyleRes int anim){
            this.anim = anim;
            return this;
        }

        /***
         * 设置宽度占屏幕宽度百分比
         */
        public Builder screenWidthPercent(@FloatRange(from = 0.0, to = 1.0)float p){
            this.perSent = p;
            return this;
        }

        /***
         * 设置背景透明度
         */
        public Builder backgroundAlpha(@FloatRange(from = 0.0, to = 1.0) float a){
            this.backgroundAlpha = a;
            return this;
        }

        /***
         * 设置显示的位置，主要的几个位置
         *
         */
        public Builder gravity(@DialogGravity int g){
            this.gravity = g;
            return this;
        }

        /***
         * 设置点击弹框外部是否取消弹框
         */
        public Builder outsideCancelable(boolean cancelable){
            this.cancelOutside = cancelable;
            return this;
        }

        /***
         * 设置tag
         */
        public Builder tag(String tag){
            this.tag = tag;
            return this;
        }

        /***
         * 设置物理返回键和点击外部能否取消弹框，
         * 该属性为false，也会导致outsideCancelable不起作用
         */
        public Builder cancelable(boolean cancelable){
            this.cancelable = cancelable;
            return this;
        }

        /***
         * 设置“取消”按钮的id，如果不需要在点击“取消”按钮时，有其他操作，
         * 可直接设置这个属性，如果有额外操作，可用addViewClick属性自定义添加
         */
        public Builder cancelId(@IdRes int cancelId){
            this.cancelId = cancelId;
            return this;
        }

        /***
         * 设置文字
         */
        public Builder setText(@IdRes int id,String s){
            this.setTextMap.put(id,s);
            return this;
        }

        /***
         * 设置点击事件
         */
        public Builder addViewClick(@IdRes int id, View.OnClickListener onClickListener){
            if(onClickListener!=null){
                this.clickViewMap.put(id,onClickListener);
            }
            return this;
        }

        /***
         * 获取view，实现自定义操作，例如初始化属性
         */
        public Builder getView(@IdRes int id,@NonNull GetViewListener<? extends View> getViewListener){
            this.getViewMap.put(id,getViewListener);
            return this;
        }


        public QuickDialog build(){
            QuickDialog baseDialog = new QuickDialog();
            if(layout == 0){
                baseDialog.layout = R.layout.dialog_base;
                baseDialog.gravity = Gravity.CENTER;
            }else {
                baseDialog.layout = layout;
                baseDialog.gravity = gravity;
            }
            baseDialog.mHeight = height;
            baseDialog.mWidth = width;
            baseDialog.t = tag;
            baseDialog.cancelId = cancelId;
            baseDialog.perSent = perSent;
            baseDialog.anim = anim;
            baseDialog.cancelOutside = cancelOutside;
            baseDialog.clickViewMap = clickViewMap;
            baseDialog.getViewMap = getViewMap;
            baseDialog.setTextMap = setTextMap;
            baseDialog.cancelable = cancelable;
            baseDialog.backgroundAlpha = backgroundAlpha;
            return baseDialog;
        }


    }

    public interface GetViewListener<V extends View>{
        void onGet(V v);
    }
}
