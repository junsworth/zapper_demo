package com.ap.zapper;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ap.zapper.controllers.ConnectivityController;
import com.ap.zapper.controllers.HttpClientController;
import com.ap.zapper.controllers.PeopleController;
import com.ap.zapper.fragments.DetailFragment;
import com.ap.zapper.fragments.MasterFragment;
import com.ap.zapper.listeners.PeopleTaskListener;
import com.ap.zapper.listeners.PersonTaskListener;
import com.ap.zapper.listeners.ProgressListener;
import com.ap.zapper.models.Person;
import com.ap.zapper.services.requests.PeopleRequest;
import com.ap.zapper.services.requests.PersonRequest;
import com.ap.zapper.services.tasks.PeopleTask;
import com.ap.zapper.services.tasks.PersonTask;
import com.ap.zapper.utililites.Constants;

public class MainActivity extends AppCompatActivity implements
        MasterFragment.OnFragmentInteractionListener,
        DetailFragment.OnFragmentInteractionListener,
        PersonTaskListener, PeopleTaskListener, ProgressListener {

    /**
     * A fragment object to hold the master view
     */
    private MasterFragment masterFragment = null;

    /**
     * A fragment object to hold the detailed view
     */
    private DetailFragment detailFragment = null;

    /**
     * A configuration object to hold application configuration
     */
    private Configuration configuration = null;

    /**
     * A relative layout container object
     */
    private RelativeLayout container = null;

    /**
     * A progress dialog object, for showing progress.
     */
    private ProgressDialog progressDialog;

    /**
     * A handler object to manage the refreshing of data
     */
    private Handler handler = null;

    /**
     * A runnable object
     */
    private Runnable runnable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (RelativeLayout)findViewById(R.id.container);

        configuration = getResources().getConfiguration();

        addMasterFragment();

        // refresh content
        refreshContent();

        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            addDetailFragment();

        } else if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0f);
            container.setLayoutParams(params);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();

            if(detailFragment != null && detailFragment.isVisible()) {
                fm.popBackStack();
            }

            addDetailFragment();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0f);
            container.setLayoutParams(params);

        }
    }

    /**
     * Add master fragment
     */
    public void addMasterFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // init master fragment
        masterFragment = MasterFragment.newInstance("Master", "Fragment");

        // add master fragment to container
        ft.add(R.id.container, masterFragment, MasterFragment.TAG);

        // Add this transaction to the back stack
        ft.addToBackStack(null);

        // Commit the transaction.
        ft.commit();

    }

    /**
     * Add detailed fragment
     */
    public void addDetailFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f);
        container.setLayoutParams(params);

        detailFragment = DetailFragment.newInstance("Detail", "Fragment");

        ft.replace(R.id.container_detail, detailFragment, DetailFragment.TAG);

        // Add this transaction to the back stack
        ft.addToBackStack(null);

        // Commit the transaction.
        ft.commit();

    }

    /**
     * Refresh content
     */
    public void refreshContent() {

        if(ConnectivityController.getInstance().isConnected(this)) {

            if(handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {

                    PeopleRequest request = new PeopleRequest(Constants.ZAPPER_URL);

                    PeopleTask task = new PeopleTask(HttpClientController.getInstance(
                            MainActivity.this).client(),
                            MainActivity.this,
                            masterFragment,
                            null);

                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);

                    handler.postDelayed(runnable, Constants.REFRESH_INTERVAL_CONTINUOUS_DOWNLOAD);

                }
            };

            handler.postAtTime(runnable, Constants.REFRESH_INTERVAL_START_DOWNLOAD);

        } else {
            Toast.makeText(this, "No connectivity", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Update detailed fragment
     * @param position
     */
    public void updateDetailFragment(int position) {

        if(ConnectivityController.getInstance().isConnected(this)) {

            Person person = PeopleController.getInstance().getPeople().get(position);

            PersonRequest request = new PersonRequest(Constants.ZAPPER_URL, person);

            PersonTask task = new PersonTask(HttpClientController.getInstance(MainActivity.this).client(), this, this);

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);

        } else {
            Toast.makeText(this, "No connectivity", Toast.LENGTH_LONG).show();
        }

        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            detailFragment = DetailFragment.newInstance("Detail", "Fragment");

            ft.add(R.id.container, detailFragment, DetailFragment.TAG);

            // Add this transaction to the back stack
            ft.addToBackStack(null);

            // Commit the transaction.
            ft.commit();

        }

    }

    @Override
    public void personTaskFinished() {
        detailFragment.updateUI();
    }

    @Override
    public void peopleTaskFinished() {

    }

    @Override
    public void showProgressDialog(CharSequence message) {

        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setIndeterminate(true);
        }

        this.progressDialog.setMessage(message);
        this.progressDialog.show();

    }

    @Override
    public void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
