package pankaj.cdac.womenSecurity;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import pankaj.cdac.womenSecurity.ContactAdder;
import pankaj.cdac.womenSecurity.R;

public final class ContactManager extends Activity
{

    public static final String TAG = "ContactManager";

    private Button mAddAccountButton;
    private ListView mContactList;
    private boolean mShowInvisible;
    private CheckBox mShowInvisibleControl;

    /**
     * Called when the activity is first created. Responsible for initializing the UI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_manager);

        // Obtain handles to UI objects
        mAddAccountButton = (Button) findViewById(R.id.addContactButton);
        mContactList = (ListView) findViewById(R.id.contactList);
        mShowInvisibleControl = (CheckBox) findViewById(R.id.showInvisible);
        
      

        // Initialize class properties
        mShowInvisible = false;
        mShowInvisibleControl.setChecked(mShowInvisible);

        // Register handler for UI elements
        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "mAddAccountButton clicked");
                launchContactAdder();
            }
        });
        mShowInvisibleControl.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "mShowInvisibleControl changed: " + isChecked);
                mShowInvisible = isChecked;
                populateContactList();
            }
        });

        // Populate the contact list
        populateContactList();
    }

    /**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void populateContactList() {
        // Build adapter with contact entries
        Cursor cursor = getContacts();
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME
        };
       final String[] ids = new String [] {
        		ContactsContract.Data._ID
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, cursor,
                fields, new int[] {R.id.contactEntryText});
        mContactList.setAdapter(adapter);
        
        
       
        mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				
						FileOutputStream fOut = null;
						try {
							fOut = openFileOutput("emergency.txt", Context.MODE_WORLD_READABLE);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						OutputStreamWriter osw = new OutputStreamWriter(fOut);
						try {
							osw.write(ids[pos]);
						} catch (IOException e) {
							e.printStackTrace();
						}
						Toast.makeText(getApplicationContext(), "Added Contact to File.", Toast.LENGTH_LONG).show();
				

			}
		});
        
    }

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    private Cursor getContacts()
    {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME                
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
                (mShowInvisible ? "0" : "1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
       
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

    /**
     * Launches the ContactAdder activity to add a new contact to the selected accont.
     */
    protected void launchContactAdder() {
        Intent i = new Intent(this, ContactAdder.class);
        startActivity(i);
    }
}
