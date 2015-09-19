package com.example.brwong.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brwong.weather.R;
import com.example.brwong.weather.google_location.Constants;
import com.example.brwong.weather.google_location.FetchAddressIntentService;
import com.example.brwong.weather.weather_data.Current;
import com.example.brwong.weather.weather_data.Day;
import com.example.brwong.weather.weather_data.Forecast;
import com.example.brwong.weather.weather_data.Hour;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{




    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            Toast.makeText(MainActivity.this, mAddressOutput, Toast.LENGTH_LONG).show();

        }
    }

    public String mAddressOutput;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public Double mLatitude = 37.8267;
    public Double mLongitude = -122.423;
    public ResultReceiver mResultReceiver;
    private Location mCurrentLocation;



    public static final String TAG = MainActivity.class.getSimpleName();//Tags this activity if IOException is caught and prints error message
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    public static final String DAILY_LOCATION_NAME = "DAILY_LOCATION_NAME";

    //private Current mCurrent;
    private Forecast mForecast; //Using this to set our data now since it contains Current's data as well

    @InjectView(R.id.time_label) TextView mTimeLabel;//How butterknife can set up views in one line of code
    @InjectView(R.id.temperature_label) TextView mTemperatureLabel;
    @InjectView(R.id.humidity_value) TextView mHumidityValue;
    @InjectView(R.id.precip_value) TextView mPrecipValue;
    @InjectView(R.id.summary_label) TextView mSummaryLabel;
    @InjectView(R.id.weather_Icon) ImageView mIconImageView;
    @InjectView(R.id.wind_label) TextView mWindSpeed;
    @InjectView(R.id.refresh_button) ImageView mRefreshButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.location_Label) TextView mLocationName;
    //@InjectView(R.id.daily_location_name) TextView mDailyLocation;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        buildGoogleApiClient();
        createLocationRequest();

        mProgressBar.setVisibility(View.INVISIBLE);//Starts the Progress bar off as invisible which we will set to visible on a refresh button click.

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(mLatitude, mLongitude);
            }
        });

        getForecast(mLatitude, mLongitude);

    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "dcee4386a92b30a378582ccd4091306d";//Setting the input parts of the API URL into parameters
        String forecastURL = "https://api.forecast.io/forecast/"+ apiKey + "/" + latitude+ "," + longitude;
        if(networkIsAvailable()) {
            toggleRefresh();//This will make the ProgressBar VISIBLE
            OkHttpClient client = new OkHttpClient();//Main CLIENT object
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build(); //This builds the request we're going to send to the server through chaining - .url() supplies the url for .build() to actually build it
            //and Request.builder() specifies that we are building a request for the server
            Call call = client.newCall(request);
            call.enqueue(new Callback() { //.enqueue MAKES THIS AN ASYNCHRONOUS TASK! (Instead of .execute())

                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();//Now, with the progress bar VISIBLE, when we get a response, we will now set it to INVISIBLE so we can refresh
                        }
                    });
                    try {
                        String weatherData = response.body().string(); //Declaring our JSON data to use in many places
                        Log.v(TAG, weatherData);//Logs a VERBOSE string that is the body of the response we get
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(weatherData);
                            //Log.v was cut out b/c we want it to log regardless in the if and else statement

                            runOnUiThread(new Runnable() {//Allows updateDisplay() to run and prepare code for main thread while not running on it
                                @Override
                                public void run() {
                                    updateDisplay();//Updates the values EVERY time you call for data
                                }
                            });
                        } else {
                            alertUserAboutError();//This alerts the user that data wasn't retrieved successfully
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);//Catches ALL our errors related to parsing JSON data -
                        // EVERY METHOD THAT PARSES DATA SHOULD THROW A JSONEXCEPTION SO IT CAN BE CAUGHT HERE
                    }

                }
            });
        }
        else {
            alertUserAboutNetwork(); //Error sent if IF there was a network error
        }
    }


    private void toggleRefresh() {//PSEUDO-Animation If the progress bar is invisible, make it visible and the refresh button invisible
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshButton.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshButton.setVisibility(View.VISIBLE);
        }

    }

    private void updateDisplay() {//This actually updates our display with the data we have parsed into the Current class

        Current current = mForecast.getCurrent(); //Gets all of mCurrent's data in Forecast class so we can update them


        mTemperatureLabel.setText(current.getTemperature() + "");  // - NOTE: WE MUSHED THE INT VALUE WITH AN EMPTY STRING TO set the text
        mHumidityValue.setText(current.getHumidity() + "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mWindSpeed.setText(current.getWindSpeed() + "mph");
        mSummaryLabel.setText(current.getSummary());
        Drawable iconPic = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(iconPic);
    }

    private Forecast parseForecastDetails(String weatherData) throws JSONException {//Method to give entire Forecast class' mVars values.
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(weatherData));//Sets mCurrent in the Forecast class
        forecast.setHour(getHourlyDetails(weatherData));
        forecast.setDay(getDailyDetails(weatherData));

        return forecast;

    }

    private Day[] getDailyDetails(String weatherData) throws JSONException {
        JSONObject forecast = new JSONObject(weatherData); //Creates a JSONObject instance to work with the JSON data
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray dailyData = daily.getJSONArray("data");

        Day[] days = new Day[dailyData.length()];

        for (int i=0; i<dailyData.length(); i++){
            JSONObject jsonDay = dailyData.getJSONObject(i);
            Day setDayData = new Day();

            setDayData.setSummary(jsonDay.getString("summary"));
            setDayData.setIcon(jsonDay.getString("icon"));
            setDayData.setMaxTemperature(jsonDay.getDouble("temperatureMax"));
            setDayData.setTime(jsonDay.getLong("time"));
            setDayData.setTimezone(timezone);

            days[i] = setDayData;
        }

        return days;
    }

    private Hour[] getHourlyDetails(String weatherData) throws  JSONException {
        JSONObject forecast = new JSONObject(weatherData); //Creates a JSONObject instance to work with the JSON data
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");//Gets the "hourly" object from forecast.io
        JSONArray hourlyData = hourly.getJSONArray("data");//Gets the "data" array from the "hourly" object

        Hour[] hour = new Hour[hourlyData.length()];//Same length as the JSON Array

        for (int i = 0; i<hourlyData.length(); i++){
            JSONObject jsonHour = hourlyData.getJSONObject(i);//Loops through the entire "data" array to get each object

            Hour setHourData = new Hour();//Instantiate an hour object to set its values and store it in the array each time through the loop

            setHourData.setSummary(jsonHour.getString("summary")); //Gets the respective key's value from every object and sets it
            setHourData.setTemperature(jsonHour.getDouble("temperature"));
            setHourData.setTime(jsonHour.getLong("time"));
            setHourData.setTimezone(timezone);
            setHourData.setIcon(jsonHour.getString("icon"));

            hour[i] = setHourData;//Stores a new object for each loop - MAKE SURE to declare the object being stored INSIDE the loop

        }

        return hour;
    }

    private Current getCurrentDetails(String weatherData) throws JSONException{//Need a try catch block to fix the error when you first do this
                                                                                      // OR WE CAN THROW AN EXCEPTION IN THE METHOD DECLARATION!!
                                                                                      //~ TO MAKE THE throws WORK, WE NEED TO CATCH THE EXCEPTION IN THE TRY CATCH BLOCK ABOVE IN THE MAIN CLASS

            JSONObject forecast = new JSONObject(weatherData); //Creates a JSONObject instance to work with the JSON data
            String timezone = forecast.getString("timezone");
            Log.i(TAG, "The timezone from JSON is: " + timezone);

            JSONObject currently = forecast.getJSONObject("currently");//Gets the whole JSON "currently" object for you to parse through

            Current current = new Current();//Declares a Current object to set values for WITH THE JSON object's (currently) data

            current.setHumidity(currently.getDouble("humidity"));
            current.setPrecipChance(currently.getDouble("precipProbability"));
            current.setIcon(currently.getString("icon"));//Retrieves the icon value for our mIcon in Current
            current.setSummary(currently.getString("summary"));
            current.setTemperature(currently.getDouble("temperature"));
            current.setTime((currently.getLong("time")));//Pulling all these values from JSON keys and setting our Current's mVars
            current.setTimeZone(timezone);
            current.setWind((currently.getInt("windSpeed")));


        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//Sets up connectivity manager to get network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo(); //Gets network info
        boolean isAvailable = false; //Default to check for network info is false
        if(networkInfo!=null && networkInfo.isConnected()){//IF we get network info AND IT IS connected, change from false to true and return!
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutNetwork(){
        AlertNetworkDialog networkDialog = new AlertNetworkDialog();
        networkDialog.show(getFragmentManager(), "network_error_dialog");
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick (R.id.daily_button) //This allows us to initialize a button and intent w/o creating a new variable for it
    public void startDailyForecast(View view) {
        Intent dailyIntent = new Intent(this, DailyForecastActivity.class);
        dailyIntent.putExtra(DAILY_FORECAST, mForecast.getDay());//Passes Day array data to DailyForecastActivity
        startActivity(dailyIntent);
    }

    @OnClick(R.id.hourly_button)
    public void startHourlyForecast(View view){
        Intent hourlyIntent = new Intent(this, HourlyForecastActivity.class);
        hourlyIntent.putExtra(HOURLY_FORECAST, mForecast.getHour());
        startActivity(hourlyIntent);
    }



    ///////////////////////////LOCATION SERVICES////////////////////////////


    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000 * 10);
        mLocationRequest.setFastestInterval(1000 * 5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {//Builds the client for callbacks
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG, "Last location: " + mCurrentLocation);
        if (mCurrentLocation != null) {
            mLatitude = mCurrentLocation.getLatitude();
            mLongitude = mCurrentLocation.getLongitude();
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses!=null && addresses.size() > 0) {
                mLocationName.setText(addresses.get(0).getLocality());
            }
        } else {
            // use hard-coded location as can't update to current
            //Toast.makeText(this, "Using default lat & long", Toast.LENGTH_LONG).show();
        }
        //getForecast(mLatitude, mLongitude);
        try {
            startLocationUpdates();
        } catch (Exception e) {
            Log.d(TAG, "Inside the catch block: " + e);
        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mCurrentLocation);
        startService(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

}


