package com.example.module_base.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.module_base.R
import com.example.module_base.utils.LogUtils
import com.example.module_base.databinding.LayoutToolbarNewBinding
//import kotlinx.android.synthetic.main.layout_toolbar_new.view.*

/**
 * @name WeatherOne
 * @class name：com.nanjing.tqlhl.ui.custom.mj15day
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2020/10/20 11:43
 * @class describe
 */
class MyToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mTitle: String = ""
    private var mRightTitle: String? = null
    private var mTitleColor: Int = Color.BLACK
    private var mBarBgColor: Int = Color.WHITE
    private var mLeftIcon: Int? = null
    private var mRightIcon: Int? = null
    private var mRightTwoIcon: Int? = null
    private var isHaveAdd: Boolean? = null
    private var isHaveBack: Boolean? = null
    private var isHaveRightTwo: Boolean? = null
    private var isHaveRight: Boolean? = null
    private var mRightTitleColor: Int? = null

    private val binding: LayoutToolbarNewBinding by lazy {
        DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_toolbar_new,this,true)
    }
    init {
        binding.root
        context.obtainStyledAttributes(attrs, R.styleable.MyToolbar).apply {
            mTitle = getString(R.styleable.MyToolbar_toolbarTitle) ?: "顶部栏"
            mRightTitle = getString(R.styleable.MyToolbar_rightTitle)
            mTitleColor = getColor(R.styleable.MyToolbar_titleColor, Color.WHITE)
            mBarBgColor = getColor(R.styleable.MyToolbar_barBgColor, Color.WHITE)
            mRightTitleColor = getColor(R.styleable.MyToolbar_rightTitleColor, Color.WHITE)
            mLeftIcon = getResourceId(R.styleable.MyToolbar_backIconStyle, R.drawable.icon_bar_white_back)
            mRightIcon = getResourceId(R.styleable.MyToolbar_rightIconStyle, -1)
            mRightTwoIcon = getResourceId(R.styleable.MyToolbar_rightIconTwoStyle, -1)
            isHaveAdd = getBoolean(R.styleable.MyToolbar_has_right_icon, false)
            isHaveRight = getBoolean(R.styleable.MyToolbar_hasRightTitle, false)
            isHaveRightTwo = getBoolean(R.styleable.MyToolbar_has_right_two_icon, false)
            isHaveBack = getBoolean(R.styleable.MyToolbar_has_right_icon, true)
            recycle()
        }
        initView()
        initEvent()
    }





    private fun initView() {
        mTitle?.let {
            binding.tvToolbarTitle?.text = it
        }

        binding.tvToolbarTitle?.setTextColor(mTitleColor)

        binding.rlBar?.setBackgroundColor(mBarBgColor)

        mLeftIcon?.let {
            if (it != -1) {
                binding.ivBarBack.setImageResource(it)
            }
        }

        mRightIcon?.let {
            if (it != -1) {
                binding.ivBarAdd.setImageResource(it)
            }
        }

        mRightTwoIcon?.let {
            if (it != -1) {
                binding.ivBarRight.setImageResource(it)
            }
        }



        if (isHaveAdd!!) {
            binding.ivBarAdd.visibility = View.VISIBLE
        } else {
            binding.ivBarAdd.visibility = View.GONE
        }

        if (isHaveRightTwo!!) {
            binding.ivBarRight.visibility = View.VISIBLE
        } else {
            binding.ivBarRight.visibility = View.GONE
        }


        if (isHaveBack!!) {
            binding.ivBarBack.visibility = View.VISIBLE
        } else {
            binding.ivBarBack.visibility = View.GONE
        }

        mRightTitle?.let {
            binding.tvBarRight?.text = it
        }
        mRightTitleColor?.let {
            binding.tvBarRight.setTextColor(it)
        }

        if (isHaveRight!!) {
            binding.tvBarRight.visibility = View.VISIBLE
        } else {
            binding.tvBarRight.visibility = View.GONE
        }


    }

    private fun initEvent() {
        binding.ivBarBack.setOnClickListener {
            mOnBackClickListener?.onBack()
        }

        binding.ivBarAdd.setOnClickListener {
            mOnBackClickListener?.onRightTo()
        }

        binding.tvBarRight.setOnClickListener {
            mOnBackClickListener?.onRightTo()
        }

        binding.ivBarRight.setOnClickListener {
            mOnBackClickListener?.onRightTwoTo()
        }

    }

    private var mOnBackClickListener: OnBackClickListener? = null
    fun setOnBackClickListener(listener: OnBackClickListener?) {
        this.mOnBackClickListener = listener
    }

    fun setTitle(title: String) {
        mTitle = title
        initView()
    }


    interface OnBackClickListener {
        fun onBack()

        fun onRightTo()

        fun onRightTwoTo()
    }


    inline fun addBarListener(
        crossinline comeBack: () -> Unit = {},
        crossinline rightTo: () -> Unit = {},
        crossinline rightTwoTo: () -> Unit = {}
    ): OnBackClickListener {
        val listener = object : OnBackClickListener {
            override fun onBack() {
                comeBack()
            }

            override fun onRightTo() {
                rightTo()
            }

            override fun onRightTwoTo() {
                rightTwoTo()
            }

        }
        setOnBackClickListener(listener)
        return listener
    }

    inline fun doComeBack(crossinline action: () -> Unit) = addBarListener(comeBack = action)

    inline fun doOnRightTo(crossinline action: () -> Unit) = addBarListener(rightTo = action)

    inline fun doRightTwoTo(crossinline action: () -> Unit) = addBarListener(rightTwoTo = action)


}


