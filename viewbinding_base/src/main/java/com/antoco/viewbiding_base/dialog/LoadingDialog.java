package com.antoco.viewbiding_base.dialog;

import android.view.Gravity;

import androidx.fragment.app.FragmentManager;

import com.antoco.viewbiding_base.R;


/**********************************
 * @Name: LoadingDialog
 * @Copyright： CreYond
 * @CreateDate： 2021/12/15 20:56
 * @author: HuangFeng
 * @Version： 1.0
 * @Describe:
 *
 **********************************/
public class LoadingDialog {
    QuickDialog baseDialog;
    private boolean isShowing = false;

    public LoadingDialog() {
        baseDialog = new QuickDialog.Builder()
                .layout(R.layout.dialog_loading)
                .height(R.dimen.loadingHeight)
                .width(R.dimen.loadingWidth)
                .gravity(Gravity.CENTER)
                .screenWidthPercent(1f)
                .anim(R.style.DialogAnimAlpha)
                .outsideCancelable(false)
                .cancelable(false)
                .backgroundAlpha(0.4f)
                .tag("LoadingDialog")
                .build();
    }

    public void show(FragmentManager manager){
        if(!isShowing){
            isShowing = true;
            baseDialog.show(manager);
        }
    }

    public void dismiss(){
        isShowing = false;
        baseDialog.dismiss();
    }

    public void destroy(){
        baseDialog.dismiss();
        baseDialog = null;
    }
}
