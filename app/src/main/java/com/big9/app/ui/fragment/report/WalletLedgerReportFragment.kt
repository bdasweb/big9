package com.big9.app.ui.fragment.report


import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
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
import com.big9.app.adapter.reportAdapter.ReportAdapter

import com.big9.app.data.model.ReportPropertyModel

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.data.viewMovel.TableViewModel
import com.big9.app.databinding.WalletLedgerFragmentReportBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants

import com.big9.app.utils.helpers.Constants.reportAdapter
import com.big9.app.utils.helpers.Constants.reportList
import com.big9.app.utils.helpers.Constants.reportList2
import com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WalletLedgerReportFragment : BaseFragment() {
    lateinit var binding: WalletLedgerFragmentReportBinding
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
            DataBindingUtil.inflate(inflater, R.layout.wallet_ledger_fragment_report, container, false)
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
                reportAdapter?.let {
                    reportList.clear()
                    reportList2.clear()
                    it.notifyDataSetChanged()
                }

                getAllData()
            }
            Handler(Looper.getMainLooper()).postDelayed({
                reportAdapter?.let {
                    reportList.clear()
                    reportList2.clear()
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
            recyclerView = this
            reportAdapter = ReportAdapter(ReportPropertyModel(""),ArrayList(),  object : CallBack {
                override fun getValue(s: String) {
                    val bundle = Bundle()
                    bundle.putString("jsonData", s)
                    /*findNavController().navigate(
                        R.id.action_reportFragment_to_reportDetailsFragment,
                        bundle
                    )*/
                }

            })
            adapter=reportAdapter
        }
        /*commissionReportAdapter?.let {
            commissionReportList.clear()
            commissionReportList2.clear()
            it.notifyDataSetChanged()
        }*/
       // getAllData()
    }



    private fun getAllData() {
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        if (isLogin){
            loginResponse?.let {loginData->
                val data = mapOf(
                    "userid" to loginData.userid,
                    "startdate" to "01-12-2023",
                    "enddate" to "15-12-2023",
                )
                val gson= Gson()
                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    myViewModel?.walletLedgerReport(it,jsonString.encrypt())
                    //    loader?.show()
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
                                      if (!(endIndex >= (reportList.size - 1))) {
                                          if (isAsintask) {
                                              //commissionReportAdapter?.setLoading(true)
                                              CoroutineScope(Dispatchers.Main).launch {
                                                  binding.loaderBottom.visibility = View.VISIBLE
                                              }
                                              val loadMoreDataTask = MyAsyncTask3()
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


        myViewModel?.walletLedgerReportResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                    /*reportList.add(
                        ReportModel(
                            "001",
                            "-778.00",
                            "10-10-2023\n" +
                                    "05:49:11",
                            "ePotlyNB Money\nForward",
                            3,
                            desc = "",
                            image1 = 2,
                            imageInt=R.drawable.rupee_rounded,
                            price2 = "Closing ₹1021.00",
                            proce1TextColor = 2,
                            isMiniStatement = false
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "001",
                            "-778.00",
                            "10-10-2023\n" +
                                    "05:49:11",
                            "ePotlyNB Money\nForward",
                            3,
                            desc = "",
                            image1 = 2,
                            imageInt=R.drawable.rupee_rounded,
                            price2 = "Closing ₹1021.00",
                            proce1TextColor = 2,
                            isMiniStatement = false
                        )
                    )*/


                    if(!it.data?.data.isNullOrEmpty()){
                        /*val size=if (it.data?.data?.size?:0 >=60){
                            60
                        }
                        else{
                            it.data?.data?.size?.minus(1)?:0
                        }
*/
                        Log.d("TAG_observer", "observer: "+it.data?.data?.size)
                        it.data?.data?.let {responseData->
                              for (index in responseData.indices){
                           // for (index in 0 until minOf(responseData.size, size)) {
                                val items=responseData[index]
                                items.apply {

                                      //  tableViewModel.insertData(items)

                                    //reportList.add(ReportModel(refillid,amount,insdate,type,3,desc = "",image1 = 2,imageInt=R.drawable.rupee_rounded,price2 = "Closing ₹$curramt",proce1TextColor = 2,isMiniStatement = false))
                                }

                            }
                            //showrecycleView()
                        }


                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
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
            if (reportList.size>10) {
                if (!(endIndex >= (reportList.size - 1))) {
                    //for (index in commissionReportList.indices) {
                    for (index in startIndex until minOf(endIndex, reportList.size)) {
                        if (index >= startIndex && index <= endIndex) {
                            var items = reportList[index]
                            items.apply {
                                reportList2.add(items)
                            }

                        }
                    }

                  //  commissionReportAdapter?.items = commissionReportList2


                } else {
                    isDataLoadingFromLocal = false
                }
            }
            else{
                CoroutineScope(Dispatchers.Main).launch {
                   // commissionReportAdapter?.items = commissionReportList
                    reportAdapter?.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {

            // Perform UI-related operation here, e.g., update UI elements
            reportAdapter?.notifyDataSetChanged()
            delay(3000)
            loader?.dismiss()
            binding.loaderBottom.visibility = View.GONE
            // Other code in the main coroutine
            println("Main coroutine is not blocked")
        }
    }




        //delay(2000)
        loader?.dismiss()


        startIndex += 10
        endIndex += 10
        //loader?.dismiss()

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

    inner class MyAsyncTask3 : AsyncTask<Void, Void, Unit>() {

        override fun doInBackground(vararg params: Void?) {
            // Background work (in a background thread)

            /*for (index in reportList.indices) {
                if (index >= startIndex && index <= endIndex) {
                    var items = reportList[index]
                    items.apply {
                        newReportList.add(this)
                    }

                }
            }

            reportAdapter?.items = newReportList*/


            if (!(endIndex >= (reportList.size - 1))) {
                // Log.d("TAG_s2", "observer:444 ")
                for (index in startIndex until minOf(endIndex, reportList.size)) {
                    // Log.d("TAG_s2", "observer:555 ")
                    if (index >= startIndex && index <= endIndex) {
                        //  Log.d("TAG_s2", "observer:666 ")
                        val items = Constants.reportList[index]
                        items.apply {
                            //reportList2.add(items)
                            Constants.newReportList?.add(items)
                        }
                    }
                }
            }


        }

        override fun onPostExecute(result: Unit?) {
            // UI-related operations (in the main thread)
            /*activity?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!(endIndex >= (Constants.commissionReportList.size - 1))) {
                        reportAdapter?.notifyDataSetChanged()
                    }

                startIndex += 10
                endIndex += 10
                loader?.dismiss()
            }


        }*/

            activity?.let {
                it.runOnUiThread(){
                    CoroutineScope(Dispatchers.IO).launch {
                        CoroutineScope(Dispatchers.Main).launch {
                            if (!(endIndex >= (reportList.size - 1))) {
                                Log.d("TAG_s2", "observer:777 ")
                                /* withContext(Dispatchers.Default) {
                                     reportAdapter?.items=reportList
                                 }*/
                                withContext(Dispatchers.Main) {
                                    // Update the adapter on the main thread
                                    reportAdapter?.notifyDataSetChanged()
                                }

                                // reportAdapter?.notifyDataSetChanged()
                            }
                            loader?.dismiss()



                            CoroutineScope(Dispatchers.IO).launch {
                                startIndex = endIndex + 1
                                endIndex += 10



                                delay(500)
                                isAsintask = true
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.GONE
                            }
                        }
                    }
                }
            }

        }

    }


}



