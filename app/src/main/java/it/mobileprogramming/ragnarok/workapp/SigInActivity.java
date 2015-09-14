package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.Date;

import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserSerializer;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;
import it.mobileprogramming.ragnarok.workapp.util.NetworkTest;


public class SigInActivity extends BaseActivityWithToolbar implements GoogleApiClient.ConnectionCallbacks,
                                                GoogleApiClient.OnConnectionFailedListener,
                                                View.OnClickListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    @Override
    public void onClick(View v) {

        // check for network -> if there is not connectivity then the activity will be finished
        if(!NetworkTest.Connectivity(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.signin_error), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (v.getId() == R.id.sign_in_button) {
            onSignInClicked();
        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // design of the GooglePlus button
        SignInButton signin = (SignInButton) findViewById(R.id.sign_in_button);
        signin.setColorScheme(SignInButton.COLOR_LIGHT);
        signin.setSize(SignInButton.SIZE_WIDE);

        // StrictMode settings -> it is not necessary to use another thread to perform this few operations
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();

        // adding the onclick listener to the sign in button
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        findViewById(R.id.developer_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String personName   = "Emanuele Abate";
                final int personGender    = 0;
                final String personImage  = "http://img.datasport.it/images/2012/6/8/18423.jpg";
                final String personEmail  = "emanuele.abate@gmail.com";

                // getting shared preferences
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefeditor = pref.edit();
                // getting DB serializer
                UserSerializer userSerializer = ((App) getApplication()).getDBSerializer();
                User user = (userSerializer.loadUser(personEmail) != null ? userSerializer.loadUser(personEmail) :
                        new User(personName             ,
                                personEmail             ,
                                personGender            ,
                                new Date()              ,
                                personImage             ,
                                userSerializer          )
                );
                ((App) getApplication()).setCurrentUser(user);

                prefeditor.putString("userEmail", personEmail);
                prefeditor.putBoolean("signed_in", true);
                prefeditor.apply();
                finish();
            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e("LOGIN", "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                Log.e("LOGERROR", connectionResult.toString());
            }
        } else {
            // Show the signed-out UI
            Log.d("LOGOUT", "signing out...");
        }

        Log.e("LOGIN", "onConnectionFailed:" + connectionResult);
        if (connectionResult.getErrorCode() == ConnectionResult.SERVICE_MISSING  ||
            connectionResult.getErrorCode() == ConnectionResult.SERVICE_DISABLED ||
            connectionResult.getErrorCode() == ConnectionResult.SERVICE_UPDATING ||
            connectionResult.getErrorCode() == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sign_in_no_service) ,Toast.LENGTH_LONG);
            finish();
        }
        else if (connectionResult.getErrorCode() == ConnectionResult.NETWORK_ERROR  ||
                 connectionResult.getErrorCode() == ConnectionResult.INTERNAL_ERROR ||
                 connectionResult.getErrorCode() == ConnectionResult.SIGN_IN_FAILED ||
                 connectionResult.getErrorCode() == ConnectionResult.INVALID_ACCOUNT) {
                 Toast.makeText(getApplicationContext(), getResources().getString(R.string.signin_error), Toast.LENGTH_LONG).show();
                 finish();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LOGIN", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        mShouldResolve = false;

        // retrieve info and store them
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            String personName     = currentPerson.getDisplayName();
            int personGender      = (currentPerson.getGender() == Person.Gender.MALE    ?  Person.Gender.MALE    :
                                    (currentPerson.getGender() == Person.Gender.FEMALE  ?  Person.Gender.FEMALE  :
                                                                                           Person.Gender.OTHER  ));
            String personImage    = currentPerson.getImage().getUrl();

            String personGooglePlusProfile = Plus.AccountApi.getAccountName(mGoogleApiClient);

            // getting shared preferences
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor prefeditor = pref.edit();
            // getting DB serializer
            UserSerializer userSerializer = ((App) getApplication()).getDBSerializer();
            User user = (userSerializer.loadUser(personGooglePlusProfile) != null ? userSerializer.loadUser(personGooglePlusProfile) :
                         new User(personName              ,
                                  personGooglePlusProfile ,
                                  personGender            ,
                                  new Date()              ,
                                  personImage             ,
                                  userSerializer          )
                        );
            ((App) getApplication()).setCurrentUser(user);

            prefeditor.putString("userEmail", personGooglePlusProfile);
            prefeditor.putBoolean("signed_in", true);
            prefeditor.apply();
            finish();
        }

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // it will try to reconnect automatically
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}



