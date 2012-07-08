package pankaj.cdac.womenSecurity;
   
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeScreenActivity extends Activity {
	
	protected static final int PICK_CONTACT = 0;
	Button btnPanic,btnAddContacts,btnExit;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindItems();
        myListener();
    }
    
    void bindItems(){
    	btnPanic=(Button)findViewById(R.id.btnPanic);
    	btnAddContacts=(Button)findViewById(R.id.btnAddContact);
    	btnExit=(Button)findViewById(R.id.btnExit);
    }
    
    void myListener(){
    	
    	btnPanic.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				createPanic();
			}
		});
    	
    	btnExit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				HomeScreenActivity.this.finish();
				
			}
		});
    	
    	btnAddContacts.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

					addToPanicContact();
				
			}
		});    	
    }
    
    void addToPanicContact(){
    	
    		AlertDialog.Builder addingContactDialog = new AlertDialog.Builder(this);
    		addingContactDialog.setMessage(R.string.addCotactDialog);
    		addingContactDialog.setCancelable(true);

    		addingContactDialog.setPositiveButton(R.string.addCotactDialogOptionFromPresent, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					Intent showContacts = new Intent();
					showContacts.setClassName("pankaj.cdac.womenSecurity", "pankaj.cdac.womenSecurity.ContactManager");
					showContacts.setType(ContactsContract.Contacts.CONTENT_TYPE);
					startActivity(showContacts);
				}
			});
    		
    		addingContactDialog.setNegativeButton(R.string.addCotactDialogOptionAddNew, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
				
				try{		
						Intent addContact = new Intent();
						addContact.setClassName("pankaj.cdac.womenSecurity", "pankaj.cdac.womenSecurity.ContactAdder");
						startActivity(addContact);
					}
					catch(Exception e){
						Toast.makeText(getApplicationContext(), "Failed to Open Contacts", Toast.LENGTH_LONG).show();
					}

				}
			});
    	
    		AlertDialog addContacts = addingContactDialog.create();
    		
    		addContacts.show();
    }
    
    public void createPanic(){
    	Intent showMap = new Intent();
    	showMap.setClassName("pankaj.cdac.womenSecurity", "pankaj.cdac.womenSecurity.ShowLocation");
    	startActivity(showMap);
    }
    
}