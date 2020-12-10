package com.example.letsplay.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.*
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsplay.MainActivity
import com.example.letsplay.R
import com.example.letsplay.common.Validation
import com.example.letsplay.datasources.CreateEventObject
import com.example.letsplay.datasources.EventDao
import com.example.letsplay.models.SessionDtls
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.fragment_create.view.*
import kotlinx.android.synthetic.main.fragment_home_screen.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Create : Fragment() {

    private var date : String? = null
    private var startTime : String? = null
    private var sport : String? = null

    private lateinit var placeBtn : Button
    private var lat = ""
    private var lon = ""
    private lateinit var userMsg : TextView
    private lateinit var client : FusedLocationProviderClient

    private val CHANNEL_ID = "channel_id"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_screen_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager : NotificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){

        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)

        val bitmap : Bitmap = BitmapFactory.decodeResource(activity?.applicationContext?.resources, R.drawable.image1)
        val bitmapLargeIcon : Bitmap = BitmapFactory.decodeResource(activity?.applicationContext?.resources, R.drawable.image2)

        val builder : NotificationCompat.Builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.mipmap.logo)
            .setContentTitle("Lets Play")
            .setContentText("Event Created")
            .setLargeIcon(bitmapLargeIcon)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())){
            notify(notificationId, builder.build())
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        placeBtn = view.findViewById(R.id.placeButton)
        userMsg = view.findViewById(R.id.userMsg)

        client = activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!

        placeBtn.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view : View?) {
                if (ContextCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ){

                    getCurrentLocation()

                } else{
                    var parameter = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    requestPermissions(parameter, 100)
                }
            }
        })

        val sports = resources.getStringArray(R.array.sports1)
        val sportsSpinner = view.findViewById<Spinner>(R.id.sport)
        if(sportsSpinner != null){
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports)
            sportsSpinner.adapter = adapter
        }

        return view
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){

        var locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            client.lastLocation.addOnCompleteListener(object : OnCompleteListener<Location>{

                @SuppressLint("MissingPermission")
                override fun onComplete(task: Task<Location>) {
                    var location = task.getResult()
                    if(location != null){
                        lat = location.latitude.toString()
                        lon = location.longitude.toString()
                        userMsg.setText("Current location is set.")
                    } else{
                        var locationRequest = LocationRequest()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10000)
                            .setFastestInterval(1000)
                            .setNumUpdates(1)

                        var locationCallBack = object : LocationCallback(){
                            override fun onLocationResult(locationResult: LocationResult) {
                                var location1 = locationResult.lastLocation
                                lat = location1.latitude.toString()
                                lon = location1.longitude.toString()
                                userMsg.setText("Current location is set.")
                            }
                        }
                        client.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper())
                    }
                }
            })
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100 && (grantResults.size > 0) &&
            (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){

            getCurrentLocation()
        } else{
            val validation = Validation()
            validation.createErrorToast("Permission denied to get current location.", requireContext())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_create_to_homeScreen)
        }

        create_title.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.home_menu -> {
                    findNavController().navigate(R.id.action_create_to_homeScreen)
                    true
                }
                R.id.create_menu -> {
                    findNavController().navigate(R.id.action_create_self)
                    true
                }
                R.id.view_join_menu -> {
                    findNavController().navigate(R.id.action_create_to_showAllEvents)
                    true
                }
                R.id.update_profile_menu -> {
                    findNavController().navigate(R.id.action_create_to_profile)
                    true
                }
                R.id.log_out_menu -> {
                    SessionDtls.loggedInUser = null
                    findNavController().navigate(R.id.action_create_to_signIn)
                    true
                }
                else -> false
            }
        }

        view.findViewById<Button>(R.id.createBtn).setOnClickListener{

            val list = ArrayList<String?>()
            list.add(date)
            list.add(startTime)

            val spinnerList = ArrayList<Int>()
            spinnerList.add(R.id.sport)

            val validation = Validation()
            if(validation.inputValidationSpinner(view, requireContext(), spinnerList)
                && validation.inputValidationStr(view, requireContext(), list)) {

                val place = view.findViewById<TextInputLayout>(R.id.place).editText?.text.toString()
                if((place == null || place.trim() == "") && (lat == null || lat.trim() == "")){
                    validation.createErrorToast("Provide place in text box or pick current location using the button.", requireContext())
                } else {

                    createNotificationChannel()
                    sendNotification()

                    var timeStamp = Timestamp(System.currentTimeMillis())
                    val createEvent = CreateEventObject()
                    val eventInfo =
                        createEvent.createEventFromView(view, date, startTime, timeStamp, lat, lon)
                    val eventDao = EventDao()
                    eventDao.saveEvent(
                        eventInfo, requireContext(), findNavController(),
                        "Event created successfully.", R.id.action_create_to_homeScreen
                    )
                }
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
                    userSelectedTime.setText("")
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
                    if(date != null || date == ""){
                        val format = SimpleDateFormat("dd MMM yyyy")
                        var date = format.parse(date)
                        var todaysDate = Date()
                        if(date > Date()){
                            var timeformat= SimpleDateFormat("hh:mm a", Locale.CANADA)
                            startTime = timeformat.format(selectedTime.time)
                            userSelectedTime.setText(timeformat.format(selectedTime.time))
                            Toast.makeText(
                                requireContext(),
                                "time" + timeformat.format(selectedTime.time),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if(date.date == todaysDate.date && date.month == todaysDate.month
                            && date.year == todaysDate.year) {

                            if(selectedTime > Calendar.getInstance()){
                                var timeformat= SimpleDateFormat("hh:mm a", Locale.CANADA)
                                startTime = timeformat.format(selectedTime.time)
                                userSelectedTime.setText(timeformat.format(selectedTime.time))
                                Toast.makeText(
                                    requireContext(),
                                    "time" + timeformat.format(selectedTime.time),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else{
                                Toast.makeText(
                                    requireContext(),
                                    "Past time is selected. Please select a time from future.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else{
                            Toast.makeText(
                                requireContext(),
                                "Past time is selected. Please select a time from future.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else{
                        Toast.makeText(
                            requireContext(),
                            "Please select a date before time.",
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