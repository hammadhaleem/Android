package pankaj.cdac.womenSecurity;

import java.io.IOException;
import java.util.*;
import android.widget.*;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.location.*;
import android.content.*;

public class ShowLocation extends Activity {
    
    Button addressButton;
    TextView locationText;
    TextView addressText;
    Location currentLocation;
    double currentLatitude;
    double currentLongitude;
    StringBuilder globalResult=new StringBuilder();
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        addressText = (TextView)findViewById(R.id.addressText);
        locationText = (TextView)findViewById(R.id.locationText);
        addressButton = (Button)findViewById(R.id.addressButton);
        send_sms();
   //     this.addressText.setText("ready");

        LocationManager locationManager = 
            (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            		updateLocation(location);
            												 }
            public void onStatusChanged(
                    String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        this.addressButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                getAddress();
                //updateLocation(Location location);
            }
        });
    }
    
   public void  getAddress(){
        try{
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = 
                gcd.getFromLocation(currentLatitude, currentLongitude,100);
            if (addresses.size() > 0) {
                StringBuilder result = new StringBuilder();
                for(int i = 0; i < addresses.size(); i++){
                    Address address =  addresses.get(i);
                    int maxIndex = address.getMaxAddressLineIndex();
                    for (int x = 0; x <= maxIndex; x++ ){
                        result.append(address.getAddressLine(x));
                        result.append(",");
                    }               
                    result.append(address.getLocality());
                    result.append(",");
                    result.append(address.getPostalCode());
                    result.append("\n\n");
                }
               globalResult =result ;
                addressText.setText(result.toString());
            }
        }
        catch(IOException ex){
            addressText.setText(ex.getMessage().toString());
            
        } 
        Uri uri = Uri.parse("http://maps.google.com/maps/api/staticmap?center=28.588771,77.225612&zoom=15&size=400x760&sensor=false");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
   }
       
   void send_sms()
        {
        SmsManager sendSms = SmsManager.getDefault();
        int i ;
        String number[]= {"5556","5558","5560","5562","5564" } ;
        Toast.makeText(getApplicationContext(), globalResult, Toast.LENGTH_LONG).show();
        for(i=0;i<5;i++)
        {
        	sendSms.sendTextMessage(number[i], null, "I am in trouble!" +
        			"Find me here: http://maps.google.com/maps/api/staticmap?center=28.588771,77.225612&zoom=15&size=400x760&sensor=false", null, null);
        }
        Toast.makeText(getApplicationContext(), "Messages has been sent to all selected contacts ",Toast.LENGTH_LONG).show() ;
        
     
    }
    
    void updateLocation(Location location){
        currentLocation = location;
        currentLatitude = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
        locationText.setText(currentLatitude + ", " + currentLongitude);
        
        
    }
}