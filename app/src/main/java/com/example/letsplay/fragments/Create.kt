package com.example.letsplay.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.common.Constants
import com.example.letsplay.common.Validation
import com.example.letsplay.datasources.CreateEventObject
import com.example.letsplay.datasources.EventDao
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.fragment_create.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Create : Fragment() {

    private var date : String? = null
    private var startTime : String? = null
    private var sport : String? = null
    private var place : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_screen_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        val sports = resources.getStringArray(R.array.sports1)
        val sportsSpinner = view.findViewById<Spinner>(R.id.sport)
        if(sportsSpinner != null){
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports)
            sportsSpinner.adapter = adapter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_create_to_homeScreen)
        }
        view.findViewById<Button>(R.id.create).setOnClickListener{

            place = view.findViewById<TextInputLayout>(R.id.place).editText?.text.toString()

            val list = ArrayList<String?>()
            list.add(date)
            list.add(startTime)
            list.add(place)

            val spinnerList = ArrayList<Int>()
            spinnerList.add(R.id.sport)

            val validation = Validation()
            if(validation.inputValidationSpinner(view, requireContext(), spinnerList)
                && validation.inputValidationStr(view, requireContext(), list)) {

                var timeStamp = System.currentTimeMillis().toString()
                val createEvent = CreateEventObject()
                val eventInfo = createEvent.createEventFromView(view, date, startTime, timeStamp)
                val eventDao = EventDao()
                eventDao.saveEvent(eventInfo, requireContext(), findNavController(),
                    "Event created successfully.", R.id.action_create_to_homeScreen)

            }
        }

        view.dateButton.setOnClickListener{
            //getting date
            val now = Calendar.getInstance()
            val cyear: Int = now.get(Calendar.YEAR)
            val cmonth: Int = now.get(Calendar.MONTH)
            val cday: Int = now.get(Calendar.DAY_OF_MONTH)
            val datepicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selecteddate = Calendar.getInstance()
                    selecteddate.set(Calendar.YEAR, year)
                    selecteddate.set(Calendar.MONTH, month)
                    selecteddate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    var format = SimpleDateFormat("dd MMM YYYY" , Locale.CANADA)
                    date = format.format(selecteddate.time)
                    userSelectedDate.setText(date)
                },
                cyear,
                cmonth,
                cday
            )
            datepicker.getDatePicker().setMinDate(now.getTimeInMillis());
            now.add(Calendar.DAY_OF_MONTH, 7);
            datepicker.getDatePicker().setMaxDate(now.getTimeInMillis());
            datepicker.show()
        }

        view.timeButton.setOnClickListener {
            val now = Calendar.getInstance()
            val timepicker = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)
                    if(selectedTime > Calendar.getInstance()) {
                        var timeformat= SimpleDateFormat("hh:mm a", Locale.CANADA)
                        startTime = timeformat.format(selectedTime.time)
                        userSelectedTime.setText(timeformat.format(selectedTime.time))
                        Toast.makeText(
                            requireContext(),
                            "time" + timeformat.format(selectedTime.time),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Past time is selected. Please select a time from future.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
            )
            timepicker.show()
        }

        view.sport.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                sport = null
            }

            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                val sports =resources.getStringArray(R.array.sports)
                sport = sports[position]
                if(sport.equals(sports[0])) {
                    sport = null
                }
            }
        }
    }


}