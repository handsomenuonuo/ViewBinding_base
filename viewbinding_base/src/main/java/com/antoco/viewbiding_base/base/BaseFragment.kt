package com.antoco.viewbiding_base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.antoco.viewbiding_base.dialog.LoadingDialog
import org.jetbrains.annotations.NotNull

/**********************************
 * @Name:         BaseFragment
 * @Copyright：  CreYond
 * @CreateDate： 2022/12/13 17:04
 * @author:      HuangFeng
 * @Version：    1.0
 * @Describe:
 *
 **********************************/
abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel> : Fragment(){
    var binding: B ? =null
    lateinit var viewModel: VM
    lateinit var mContext: Context
    var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = requireContext()
        binding = initBinding(inflater,container!!)
        viewModel = ViewModelProvider(this).get(createViewModel())

        val root: View = binding!!.root
        viewModel.showLoadingView_event.observe(viewLifecycleOwner){
            if(it){
                showLoadingView()
            }else{
                dismissLoadingView()
            }
        }

        initView()
        initData()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @NotNull
    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup): B
    abstract fun createViewModel(): Class<VM>
    abstract fun initView()
    abstract fun initData()

    fun showLoadingView() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        loadingDialog!!.show(requireActivity().supportFragmentManager)
    }

    fun dismissLoadingView() {
        if (loadingDialog != null) loadingDialog!!.dismiss()
    }
}