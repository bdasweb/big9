package com.big9.app.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.adapter.BottomStateListAdapter
import com.big9.app.data.model.BottomSheetStateListModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.StateBottomsheetLayoutBinding

import com.big9.app.ui.base.BaseBottomSheetFragment
import com.big9.app.utils.`interface`.CallBack

class EStateListDialog(val callBack: CallBack, val stateListAlldata: ArrayList<String>?) : BaseBottomSheetFragment() {
    lateinit var binding: StateBottomsheetLayoutBinding
    private val viewModel: MyViewModel by activityViewModels()

    var stateList = ArrayList<BottomSheetStateListModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.state_bottomsheet_layout, container, false)
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
            stateList.clear()
            stateListAlldata?.forEach{
                stateList.add(BottomSheetStateListModel(it, false))
            }

            /*stateList.add(BottomSheetStateListModel("Andhra Pradesh", false));
            stateList.add(BottomSheetStateListModel("Arunachal Pradesh", false));
            stateList.add(BottomSheetStateListModel("Assam", false));
            stateList.add(BottomSheetStateListModel("Bihar", false));
            stateList.add(BottomSheetStateListModel("Chhattisgarh", false));
            stateList.add(BottomSheetStateListModel("Goa", false));
            stateList.add(BottomSheetStateListModel("Gujarat", false));
            stateList.add(BottomSheetStateListModel("Haryana", false));
            stateList.add(BottomSheetStateListModel("Himachal Pradesh", false));
            stateList.add(BottomSheetStateListModel("Jharkhand", false));
            stateList.add(BottomSheetStateListModel("Karnataka", false));
            stateList.add(BottomSheetStateListModel("Kerala", false));
            stateList.add(BottomSheetStateListModel("Madhya Pradesh", false));
            stateList.add(BottomSheetStateListModel("Maharashtra", false));
            stateList.add(BottomSheetStateListModel("Manipur", false));
            stateList.add(BottomSheetStateListModel("Meghalaya", false));
            stateList.add(BottomSheetStateListModel("Mizoram", false));
            stateList.add(BottomSheetStateListModel("Nagaland", false));
            stateList.add(BottomSheetStateListModel("Odisha", false));
            stateList.add(BottomSheetStateListModel("Punjab", false));
            stateList.add(BottomSheetStateListModel("Rajasthan", false));
            stateList.add(BottomSheetStateListModel("Sikkim", false));
            stateList.add(BottomSheetStateListModel("Tamil Nadu", false));
            stateList.add(BottomSheetStateListModel("Telangana", false));
            stateList.add(BottomSheetStateListModel("Tripura", false));
            stateList.add(BottomSheetStateListModel("Uttar Pradesh", false));
            stateList.add(BottomSheetStateListModel("Uttarakhand", false));
            stateList.add(BottomSheetStateListModel("West Bengal", false));*/

            adapter= BottomStateListAdapter(stateList, object : CallBack {
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