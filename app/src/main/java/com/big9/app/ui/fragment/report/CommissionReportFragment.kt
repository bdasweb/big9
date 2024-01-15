package com.big9.app.ui.fragment.report


import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.R
import com.big9.app.adapter.reportAdapter.CommissionReportAdapter

import com.big9.app.data.model.allReport.CommissionReportData

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.data.viewMovel.TableViewModel
import com.big9.app.databinding.CommissionFragmentReportBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants.commissionReportAdapter
import com.big9.app.utils.helpers.Constants.commissionReportList
import com.big9.app.utils.helpers.Constants.commissionReportList2
import com.big9.app.utils.helpers.Constants.reportList
import com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommissionReportFragment : BaseFragment() {
    lateinit var binding: CommissionFragmentReportBinding
    private val viewModel: MyViewModel by activityViewModels()

    private val myViewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var startDate = ""
    var endDate = ""
    var isAsintask=true
    private var lastClickTime1: Long = 0
    var startIndex = 0
    var endIndex = 10
    var extraArraySize=0
    private lateinit var recyclerView: RecyclerView

    var isDataLoadingFromLocal=false

    private var currentPage = 0
    private var isLoading = false

    private lateinit var tableViewModel: TableViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.commission_fragment_report, container, false)
        tableViewModel = ViewModelProvider(this)[TableViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observer()
        onViewClick()
    }

    override fun onResume() {
        super.onResume()
        //reportList?.clear()
        /* reportAdapter?.let {
             it.items=ArrayList()
             it.notifyDataSetChanged()
         }*/
        //getAllData()
    }

    private fun onViewClick() {

        binding.apply {
            imgBack.setOnClickListener {
                reportList?.clear()
                findNavController().popBackStack()
            }
            //imgBack.back()

            tvStartDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.startDate?.value = s
                    }

                })
            }

            tvEndDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.enddate?.value = s
                    }

                })
            }

            tvConfirm.setOnClickListener {
                tvConfirm.setBottonLoader(false,llLoader)
                commissionReportAdapter?.let {
                    commissionReportList.clear()
                    commissionReportList2.clear()
                    it.notifyDataSetChanged()
                }

                getAllData()
            }
            Handler(Looper.getMainLooper()).postDelayed({
                commissionReportAdapter?.let {
                    commissionReportList.clear()
                    commissionReportList2.clear()
                    it.notifyDataSetChanged()
                }

                getAllData()
            },100)


        }
    }

    fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

        }


        viewModel?.apply {
            startDate.value = "".currentdate()
            enddate.value = "".currentdate()
        }
        binding.recycleViewReport.apply {
            recyclerView = this
            //val itemAnimator = FadeInAnimator()
            recyclerView.itemAnimator = itemAnimator
            commissionReportAdapter = CommissionReportAdapter(ArrayList(), object : CallBack {
                override fun getValue(s: String) {
                    val bundle = Bundle()
                    bundle.putString("jsonData", s)
                    findNavController().navigate(
                        R.id.action_reportFragment_to_reportDetailsFragment,
                        bundle
                    )
                }

            })
            adapter = commissionReportAdapter
        }
        /*commissionReportAdapter?.let {
            commissionReportList.clear()
            commissionReportList2.clear()
            it.notifyDataSetChanged()
        }*/
       // getAllData()
    }



    private fun getAllData() {
        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        if (isLogin) {
            loginResponse?.let { loginData ->
                val data = mapOf(
                    "userid" to loginData.userid,
                    "startdate" to startDate,
                    "enddate" to endDate,
                )
                val gson = Gson()
                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    myViewModel?.commissionReport(it, jsonString.encrypt())
                    //     loader?.show()
                }
            }
        }
    }


    private fun observer() {
      /*      binding.nsvTop.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // Check if the scroll position has changed
            *//*if (scrollY != oldScrollY) {
                Log.d("TAG_position", "observer: ")
            }*//*

                if (scrollY != oldScrollY) {
                    // Get the last child view in the NestedScrollView
                    val lastChildIndex = binding.nsvTop.childCount - 1
                    val lastChildView = binding.nsvTop.getChildAt(lastChildIndex)

                    // Now you can perform actions on the last child view
                    // For example, if it's a TextView, you can get its text
                    if (lastChildView is Button) {
                        val lastItemText = lastChildView.text.toString()
                        Log.d("TAG_position", "Last item text: $lastItemText")
                    }
                }
            }
*/
        CoroutineScope(Dispatchers.Main).launch{
            launch(Dispatchers.Main) {
                // Perform UI-related operation here, e.g., update UI elements
                binding.nsvTop.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                    // Check if the scroll position has changed
                    if (scrollY != oldScrollY) {
                        // Check if the NestedScrollView has reached the bottom
                        val maxScrollRange = binding.nsvTop.getChildAt(0).height - binding.nsvTop.height
                        val isAtBottom = scrollY >= maxScrollRange

                        if (isAtBottom) {
                            // NestedScrollView is at the bottom, perform your actions here
                            /*if (!isDataLoadingFromLocal) {
                               // if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 10000) ){

                                    CoroutineScope(Dispatchers.Main).launch{
                                        binding.loaderBottom.visibility=View.VISIBLE
                                    }
                                CoroutineScope(Dispatchers.IO).launch {
                                    getAllData3()
                                }
                                    //isDataLoadingFromLocal=true
                                //}
                            }*/
                              CoroutineScope(Dispatchers.IO).launch {
                                  //getAllData3()
                                  if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                      if (!(endIndex >= (commissionReportList.size - 1))) {
                                          if (isAsintask) {
                                              //commissionReportAdapter?.setLoading(true)
                                              CoroutineScope(Dispatchers.Main).launch {
                                                  binding.loaderBottom.visibility = View.VISIBLE
                                              }
                                              val loadMoreDataTask = MyAsyncTask()
                                              loadMoreDataTask.execute()
                                              isAsintask = false
                                          }
                                      }
                                  }
                              }

                        } else {
                            // NestedScrollView is not at the bottom


                        }
                    }
                }
            }

            // Other code in the main coroutine

        }


        myViewModel?.commissionReportResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                    if (!it.data?.data.isNullOrEmpty()) {
                        it.data?.data?.let { responseData ->

                            lifecycleScope.launch {
                                /*if (responseData.isNotEmpty()) {
                                    commissionReportList =responseData
                                }*/
                                for (index in responseData.indices) {
                                    if (responseData.isNotEmpty() /*&& index<=15*/) {
                                        responseData[index].apply {
                                            commissionReportList.add(
                                                CommissionReportData(
                                                    responseData[index].opname,
                                                    " Commission ${responseData[index].comm}",
                                                    responseData[index].type
                                                )
                                            )
                                            // tableViewModel.insertData(DataEntity(desc="${responseData[index].opname} Commission ${responseData[index].comm}${responseData[index].type}"))
                                        }
                                    }
                                }
                                extraArraySize=(commissionReportList.size%10)
                                /* for (index in responseData.indices) {

                                            // paging++

                                              if (responseData.isNotEmpty()) {
                                                  commissionReportList=
                                                  responseData[index].apply {
                                                      val desc = "$opname   "
                                                      if (index<=20) {
                                                          commissionReportList.add(
                                                          ReportModel(
                                                              "",
                                                              desc = desc,
                                                              price = comm,
                                                              imageInt = R.drawable.rounded_i
                                                          )
                                                      )
                                                  }


                                                  }
                                                 // showPagingRecycleView()

                                              }

                                         }*/
                                showrecycleView()
                            }
                        }
                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


    }

    fun showrecycleView() {

      /*  binding.nsv?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding.nsv?.scrollY
            val scrollViewHeight = binding.nsv?.height
            val contentViewHeight = binding.nsv?.getChildAt(0)?.height
            if (scrollViewHeight != null && contentViewHeight != null) {
                // Check if the button is visible
                val isButtonVisible = scrollY?.plus(scrollViewHeight) ?: 0 < contentViewHeight

                if (isButtonVisible) {
                    loadMoreData()
                } else {
                    // Button is not visible
                    // Add your code here
                    // For example, disable the button
                    // btnHasData?.isEnabled = false
                }
            }
        }*/

        //val handler = Handler(Looper.getMainLooper())
        /*binding.recycleViewReport.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        loadMoreData()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // If you want to cancel the delayed runnable when the touch is released or canceled
                        handler.removeCallbacksAndMessages(null)
                    }
                }
                // Return true to consume the touch event
                return true
            }
        })*/
        /* binding.nsv.setOnScrollChangeListener { _, _, scrollY, _, _ ->
             val lastVisibleItemPosition =
                 (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
             val totalItemCount = commissionReportAdapter?.itemCount ?: 0

             if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                 // Load more data when the last item is visible
                 loadMoreData()
             }
         }*/

        // Initial data loading
        loadMoreData()
    }

    private fun loadMoreData() {
        /*if (SystemClock.elapsedRealtime() - lastClickTime1 < 5000) {
            return
        }*/
        //loader?.show()
        /*  if (isLoading) {
              return
          }
          isLoading = true*/

        // Increment the current page
        currentPage++

        // Calculate the start and end indices for the next page
        //val startIndex = currentPage * itemsPerPage
        //val endIndex = startIndex + itemsPerPage

        // Call your function to load more data using the new indices
        //getAllData(startIndex, endIndex)
        activity?.let {

            lifecycleScope.launch {

                getAllData2()
            }


        }
    }

    private fun getAllData2() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (commissionReportList.size>10) {
                if (!(endIndex >= (commissionReportList.size - 1))) {
                    //for (index in commissionReportList.indices) {
                    for (index in startIndex until minOf(endIndex, commissionReportList.size)) {
                        if (index >= startIndex && index <= endIndex) {
                            var items = commissionReportList[index]
                            items.apply {
                                commissionReportList2.add(CommissionReportData(opname, comm, type))
                            }

                        }
                    }

                    commissionReportAdapter?.items = commissionReportList2


                } else {
                    isDataLoadingFromLocal = false
                }
            }
            else{
                CoroutineScope(Dispatchers.Main).launch {
                    commissionReportAdapter?.items = commissionReportList
                    commissionReportAdapter?.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {

            // Perform UI-related operation here, e.g., update UI elements
            commissionReportAdapter?.notifyDataSetChanged()
            delay(3000)
            loader?.dismiss()
            binding.loaderBottom.visibility = View.GONE
            // Other code in the main coroutine
            println("Main coroutine is not blocked")
        }
    }



        if(commissionReportList.size>20) {
           // isDataLoadingFromLocal=true
        }
        else{
          //  isDataLoadingFromLocal=false
        }
        //delay(2000)
        loader?.dismiss()


        startIndex += 10
        endIndex += 10
        //loader?.dismiss()
        if (!(endIndex >= (commissionReportList.size - 1))) {
            isDataLoadingFromLocal = false
        }
        else{
            isDataLoadingFromLocal = true
        }
        //showrecycleView()
    }

    /*private fun getAllData3() {
        CoroutineScope(Dispatchers.IO).launch{

                if (!(endIndex >= (commissionReportList.size - 1))) {
                    //for (index in commissionReportList.indices) {
                    for (index in startIndex until minOf(endIndex, commissionReportList.size)) {
                        if (index >= startIndex && index <= endIndex) {
                            var items = commissionReportList[index]
                            items.apply {
                                commissionReportList2.add(CommissionReportData(opname, comm, type))
                            }

                        }
                    }

                    //commissionReportAdapter?.items = commissionReportList2


                } else {
                    isDataLoadingFromLocal=false
                }

        }


        CoroutineScope(Dispatchers.Main).launch{

                    // Perform UI-related operation here, e.g., update UI elements
                    commissionReportAdapter?.notifyDataSetChanged()
                    delay(3000)
                    loader?.dismiss()
                    binding.loaderBottom.visibility=View.GONE
                // Other code in the main coroutine
                println("Main coroutine is not blocked")
            }




        if(commissionReportList.size>20) {
           // isDataLoadingFromLocal=true
        }
        else{
          //  isDataLoadingFromLocal=false
        }
        //delay(2000)
        loader?.dismiss()
        startIndex = endIndex + 1
        endIndex += 10
        //loader?.dismiss()
        if (!(endIndex >= (commissionReportList.size - 1))) {
            isDataLoadingFromLocal = false
        }
        else{
            isDataLoadingFromLocal = true
        }
        //showrecycleView()
    }*/
    /*lifecycleScope.launch {
        var data = tableViewModel.getDataInRange(0, 10)
        data?.forEach{
            Log.d("TAGp", "showrecycleView: "+it?.desc)
        }
    }*/
    /*val handler = Handler(Looper.getMainLooper())
    handler.post {
        binding.recycleViewReport.apply {
            //reportList.clear()

            commissionReportAdapter = CommissionReportAdapter(commissionReportList,  object : CallBack {
                override fun getValue(s: String) {
                    val bundle = Bundle()
                    bundle.putString("jsonData", s)
                    findNavController().navigate(
                        R.id.action_reportFragment_to_reportDetailsFragment,
                        bundle
                    )
                }

            })
            adapter=commissionReportAdapter






        }


    }*/

    inner class MyAsyncTask : AsyncTask<Void, Void, Unit>() {

        override fun doInBackground(vararg params: Void?) {
            // Background work (in a background thread)

            if (!(endIndex >= (commissionReportList.size - 1))) {
                for (index in startIndex until minOf(endIndex, commissionReportList.size)) {
                    if (index >= startIndex && index <= endIndex) {
                        val items = commissionReportList[index]
                        items.apply {
                            commissionReportList2.add(CommissionReportData(opname, comm, type))
                        }
                    }
                }
            }

        }

        override fun onPostExecute(result: Unit?) {
            // UI-related operations (in the main thread)
            activity?.let {
               // it.runOnUiThread(){
                    CoroutineScope(Dispatchers.Main).launch {
                        if (!(endIndex >= (commissionReportList.size - 1))) {
                            commissionReportAdapter?.notifyDataSetChanged()
                        }
                        loader?.dismiss()


                        if (commissionReportList.size > 20) {
                            // isDataLoadingFromLocal = true
                        } else {
                            // isDataLoadingFromLocal = false
                        }

                        startIndex = endIndex + 1
                        endIndex += 10

                        if (!(endIndex >= (commissionReportList.size - 1))) {
                            isDataLoadingFromLocal = false
                        } else {
                            isDataLoadingFromLocal = true
                        }

                        delay(500)
                        isAsintask = true
                        binding.loaderBottom.visibility = View.GONE
                    }
               // }
            }


        }
    }


}



