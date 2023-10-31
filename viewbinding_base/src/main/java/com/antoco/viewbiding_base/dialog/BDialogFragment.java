package com.antoco.viewbiding_base.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Field;

/**********************************
 * @Name: BDialogFragment
 * @Copyright： CreYond
 * @CreateDate： 2022/1/13 17:39
 * @author: HuangFeng
 * @Version： 1.0
 * @Describe:
 *
 **********************************/
public class BDialogFragment extends DialogFragment {

    View rootView;
    Context mContext;

    public View getRootView(ViewGroup container, int resId) {
        mContext = getActivity();
        rootView = LayoutInflater.from(mContext).inflate(resId, container, false);
        return rootView;
    }

    public <T extends  View> T obtainView(int resId){
        if(null == rootView){
            return null;
        }
        return (T) rootView.findViewById(resId);
    }

    /**
     * 主要时这个方法。上面两个方法只是获取布局用的，可以不要
     * @param manager
     * @param tag
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        try{
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this,false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field showByMe = DialogFragment.class.getDeclaredField("mShownByMe");
            showByMe.setAccessible(true);
            showByMe.set(this,true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if(!this.isAdded()
                &&!this.isVisible()
                &&!this.isRemoving())
        {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }

    }
}