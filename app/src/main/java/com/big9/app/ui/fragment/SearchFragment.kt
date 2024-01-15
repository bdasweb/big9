package com.big9.app.ui.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.SearchAdapter
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentSearchBinding

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.helpers.Constants.isFromSearchPage
import com.big9.app.utils.helpers.Constants.searchList
import com.big9.app.utils.helpers.Constants.searchValue
import com.big9.app.utils.helpers.Constants.searchValueTag
import com.big9.app.utils.`interface`.CallBack2

class SearchFragment : BaseFragment() {
    lateinit var binding: FragmentSearchBinding
    var searchAdapter: SearchAdapter?=null
    private val viewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        isFromSearchPage=true
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
    }

    fun initView() {
        binding?.apply {
            recycleViewSearchService?.apply {
                searchAdapter= SearchAdapter(searchList,R.drawable.circle_shape2,object :
                    CallBack2 {
                override fun getValue2(s: String,tag: String) {
                    searchValue =s
                    searchValueTag=tag
                    viewModel.from_page_message.value="search"
                    findNavController().popBackStack()
                }

            })
           adapter=searchAdapter
        }
        }

    }

    fun setObserver() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length>1){
                    binding.recycleViewSearchService.visibility=View.VISIBLE
                    searchAdapter?.filteredList?.size?.let {size->
                        if (size >0){
                            binding.tvNoDataFound.isVisible=false
                        }
                        else{
                            binding.tvNoDataFound.text="No data found"
                            binding.tvNoDataFound.isVisible=true
                        }
                    }

                }
                else{

                    binding.recycleViewSearchService.visibility=View.GONE
                    binding.tvNoDataFound.isVisible=true
                    binding.tvNoDataFound.text="Search..."
                }

                //binding.tvNoDataFound.isVisible=!binding.recycleViewSearchService.isVisible

                searchAdapter?.filter?.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}