package com.example.singletonpractice

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val enteredValue = findViewById<EditText>(R.id.etEntry)
        val dateView = findViewById<TextView>(R.id.tvDate)
        val order = findViewById<TextView>(R.id.tvOrder)
        val convert = findViewById<Button>(R.id.buttonConvert)
        val convertList = findViewById<Spinner>(R.id.convertSpinner)
        val result = findViewById<TextView>(R.id.tvResult)
        val change = findViewById<ImageButton>(R.id.change)
        var date =""
        val listOfCoverts = arrayListOf<String>()
        val listOfCovertsNO = arrayListOf<Double>()
        var selected: Int = 0
        var mode =0
        
        APIClient.getAPIInterface()?.doGetListResources()?.enqueue(object : Callback<ConvertDetails?> {
            override fun onResponse(call: Call<ConvertDetails?>?, response: Response<ConvertDetails?>) {
                val resource: ConvertDetails? = response.body()
                date = resource?.date!!
                val datumList = resource.eur
                if (datumList != null) {
                    val datum = datumList.keySet().toTypedArray()
                    if (convertList != null) {
                        val adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item, datum
                        )
                        convertList.adapter = adapter
                        convertList.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                            ) {
                                selected = position
                                if (mode == 1){
                                    order.text = "Enter ${listOfCoverts[selected]} Value"
                                }
                            }
                            override fun onNothingSelected(parent: AdapterView<*>) {}
                        }
                    }
                    for (value in datum) {
                        listOfCovertsNO.add(datumList.get(value).toString().toDouble())
                        listOfCoverts.add(value)
                    }
                }
            }
            override fun onFailure(call: Call<ConvertDetails?>, t: Throwable?){
                call.cancel()
            }
        })

        change.setOnClickListener{
            if (mode == 0) {
                mode = 1
                order.text = "Enter ${listOfCoverts[selected]} Value"
            }
            else{
                mode = 0
                order.text = "Enter Euro Value"
            }
        }


        convert.setOnClickListener{
            dateView.text = "Date: $date"
            var number: Double = 0.0
            if (enteredValue.text.isNotBlank())
                number = enteredValue.text.toString().toDouble()
            var res = 0.0
            if (mode == 0){
                res = listOfCovertsNO[selected] * number
                result.text = "result $res ${listOfCoverts[selected]}"
            }
            else{
                res = number / listOfCovertsNO[selected]
                result.text = "result $res Euro"
            }
        }

    }
}