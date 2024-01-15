package com.big9.app.ui.fragment.fragmentDialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.adapter.BottomGasBillerListAdapter
import com.big9.app.data.model.BottomSheetGasBillerListModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.GasBillerBottomsheetLayoutBinding

import com.big9.app.ui.base.BaseBottomSheetFragment
import com.big9.app.utils.`interface`.CallBack

class GasBillerListDialog(val callBack: CallBack) : BaseBottomSheetFragment() {
    lateinit var binding: GasBillerBottomsheetLayoutBinding
    private val viewModel: MyViewModel by activityViewModels()

    private var bottomSheetGasBillerListModel = ArrayList<BottomSheetGasBillerListModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.gas_biller_bottomsheet_layout, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun getTheme(): Int {
        return R.style.SheetDialog
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {

        }

    }

    private fun setObserver() {

    }

    private fun initView() {

        binding.recycleOperator.apply {

            bottomSheetGasBillerListModel.add(BottomSheetGasBillerListModel(R.drawable.bharat_gas,"Bharat Gas", false));
            bottomSheetGasBillerListModel.add(BottomSheetGasBillerListModel(R.drawable.bharat_gas_commercial,"Bharat Gas Commercial", false));
            bottomSheetGasBillerListModel.add(BottomSheetGasBillerListModel(R.drawable.hp_gas,"HP Gas", false));
            bottomSheetGasBillerListModel.add(BottomSheetGasBillerListModel(R.drawable.indian_oil_logo,"Indane Gas(Indian Oil)", false));


            adapter= BottomGasBillerListAdapter(bottomSheetGasBillerListModel, object : CallBack {
                override fun getValue(s: String) {
                    viewModel?.apply {
                        callBack.getValue(s)
                        dismiss()
                    }

                    // callBack.getValue(s)
                    //findNavController().popBackStack()
                }

            })
        }
    }

}