package com.laras.pi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ParseException;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Ortu_menu_3 extends Activity implements OnClickListener{
	ImageButton btn_home, btn_hadiah, btn_report, btn_keluar;
	String server, nama_ortu, user_ortu, nama_anak, user_anak, jenis_pet, status_pet, status_makanan, misi_desc, hadiah;
	int jml_tabungan, misi_gol;
	
	TableLayout tabelBiodata;
	
	InputStream is=null;
	String result=null;
	String line=null;
	
	String hasil = null;
	   StringBuilder sb = null;
	   
	String id;
	String[] nama;
	
	ArrayList<TextView> detil = new ArrayList<TextView>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ortu_menu_3);
		
		tabelBiodata = (TableLayout) findViewById(R.id.tableBiodata);
		btn_home = (ImageButton) findViewById(R.id.imageButton1);
		btn_hadiah = (ImageButton) findViewById(R.id.imageButton2);
		btn_report = (ImageButton) findViewById(R.id.imageButton3);
		btn_keluar = (ImageButton) findViewById(R.id.imageButton4);
		
		//mengambil data yg dikirim dari halaman sebelumnya
		Intent intent = getIntent();
		server = intent.getStringExtra("server");
		nama_ortu = intent.getStringExtra("nama_ortu");
		user_ortu = intent.getStringExtra("user_ortu");
		nama_anak = intent.getStringExtra("nama_anak");
		user_anak = intent.getStringExtra("user_anak");
		jenis_pet = intent.getStringExtra("jenis_pet");
		status_pet = intent.getStringExtra("status_pet");
		status_makanan = intent.getStringExtra("status_makanan");
		misi_desc = intent.getStringExtra("misi_desc");
		hadiah = intent.getStringExtra("hadiah");
		jml_tabungan = intent.getIntExtra("jml_tabungan", 0);
		misi_gol = intent.getIntExtra("misi_gol", 0);
		
		//Membuat layout table
		TableRow barisTabel = new TableRow(this);
        barisTabel.setBackgroundColor(Color.parseColor("#44916c"));

        TextView viewHeaderMisi = new TextView(this);
        TextView viewHeaderHadiah = new TextView(this);
        TextView viewHeaderStatus = new TextView(this);

        viewHeaderMisi.setText("Misi"); viewHeaderMisi.setTextColor(Color.parseColor("#FFFFFF"));
        viewHeaderHadiah.setText("Hadiah"); viewHeaderHadiah.setTextColor(Color.parseColor("#FFFFFF"));
        viewHeaderStatus.setText("Status"); viewHeaderStatus.setTextColor(Color.parseColor("#FFFFFF"));

        viewHeaderMisi.setPadding(5, 10, 15, 20);
        viewHeaderHadiah.setPadding(5, 10, 15, 20);
        viewHeaderStatus.setPadding(5, 10, 15, 20);

        barisTabel.addView(viewHeaderMisi);
        barisTabel.addView(viewHeaderHadiah);
        barisTabel.addView(viewHeaderStatus);

        tabelBiodata.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        //################################################################################################
        //################################################################################################
        //################################################################################################
        //memuat dari php
        //################################################################################################
        //################################################################################################
        //################################################################################################
        //################################################################################################
        
        //mengirimkan variabel ke url
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("user_anak", user_anak));
    	
	    //fungsi untuk memberikan izin pemakaian jaringan
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
	    //mencoba mengakses url melalui httppost
	    try{
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"tampil_semua_misi.php"); // <= definisi url di variabel httppost
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
	        
	    }catch(Exception e){
	    	
	    }
	    
	    //membaca keluaran dari php
        try{        	
         	BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8); // <= membaca file balasan dengan standar iso
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null){
       		    sb.append(line + "\n");
           	}
            is.close();
            result = sb.toString();
	        Log.e("pass 2", "connection success ");
	        
        }
        catch(Exception e){
        	Log.e("Fail 2", e.toString());
        	
        }
       
        //memasukkan hasil keluaran dari php ke JSON
        JSONArray jArray;
        try{
        	//JSONObject json_data = new JSONObject(result);
        	// menyimpan nilai dari tipe user
        	jArray = new JSONArray(result);
            JSONObject jObject = null;
            nama = new String[jArray.length()];
            for(int i = 0; i<jArray.length(); i++){
	            jObject = jArray.getJSONObject(i);
	            //nama[i] = jObject.getString("status");
	            
	            barisTabel = new TableRow(this);
	    		
	            TextView viewId = new TextView(this);
                viewId.setText("Menabung hingga Rp."+jObject.getString("misi"));
                viewId.setPadding(5, 10, 15, 20);
                barisTabel.addView(viewId);

                TextView viewNama = new TextView(this);
                viewNama.setText(jObject.getString("hadiah"));
                viewNama.setPadding(5, 10, 15, 20);
                barisTabel.addView(viewNama);
                
        		detil.add(i, new TextView(this));
                detil.get(i).setId(Integer.parseInt(jObject.getString("id_misi")));
                detil.get(i).setTag("detil");
                detil.get(i).setText(jObject.getString("status"));
                detil.get(i).setTextColor(Color.parseColor("#0000FF"));
                detil.get(i).setPadding(5, 10, 15, 20);
                detil.get(i).setOnClickListener(this);
        		barisTabel.addView(detil.get(i));
                
                tabelBiodata.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        }
        catch(Exception e){
        	//Toast.makeText(getApplicationContext(), "Terjadi kesalahan : \n"+e.toString(), Toast.LENGTH_SHORT).show();
        	Toast.makeText(getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
    	}
		
        //################################################################################################
        //################################################################################################
        //################################################################################################
        //################################################################################################
		
		//aksi untuk btn_home
		btn_home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Ortu_home.class);
				i.putExtra("nama_ortu", nama_ortu);
				i.putExtra("user_ortu", user_ortu);
				i.putExtra("nama_anak", nama_anak);
				i.putExtra("user_anak", user_anak);
				i.putExtra("jml_tabungan", jml_tabungan);
				i.putExtra("jenis_pet", jenis_pet);
				i.putExtra("status_pet", status_pet);
				i.putExtra("status_makanan", status_makanan);
				i.putExtra("misi_desc", misi_desc);
				i.putExtra("misi_gol", misi_gol);
				i.putExtra("hadiah", hadiah);
				i.putExtra("server", server);
				startActivity(i);
    			finish();
			}
		});
		
		//aksi untuk btn_hadiah
		btn_hadiah.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Ortu_menu_2.class);
				i.putExtra("nama_ortu", nama_ortu);
				i.putExtra("user_ortu", user_ortu);
				i.putExtra("nama_anak", nama_anak);
				i.putExtra("user_anak", user_anak);
				i.putExtra("jml_tabungan", jml_tabungan);
				i.putExtra("jenis_pet", jenis_pet);
				i.putExtra("status_pet", status_pet);
				i.putExtra("status_makanan", status_makanan);
				i.putExtra("misi_desc", misi_desc);
				i.putExtra("misi_gol", misi_gol);
				i.putExtra("hadiah", hadiah);
				i.putExtra("server", server);
				startActivity(i);
    			finish();
			}
		});
		
		//aksi untuk btn_keluar
		btn_keluar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				keluar_alert();
			}
		});	
	}
	
	@Override
	public void onBackPressed(){
		//nothing
		keluar_alert();
	}
	
	public void keluar_alert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			    this);
			 
			   // set title
			   alertDialogBuilder.setTitle("Konfirmasi");
			 
			   // set dialog message
			   alertDialogBuilder
			    .setMessage("Yakin ingin keluar?")
			    .setCancelable(false)
			    .setPositiveButton("Iya",new DialogInterface.OnClickListener() {
				     public void onClick(DialogInterface dialog,int id) {
				    	 //if this button is clicked, close
				    	 // current activity
				    	 finish();
				     }
			     })
			    .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
				     public void onClick(DialogInterface dialog,int id) {
				    	 // if this button is clicked, just close
				    	 // the dialog box and do nothing
				    	 dialog.cancel();
				     }
			    });
			 
			    // create alert dialog
			    AlertDialog alertDialog = alertDialogBuilder.create();
			 
			    // show it
			    alertDialog.show();
	}
	
	public void suara_button(String soundfile) {
		MediaPlayer m;
		try {
			m = new MediaPlayer();
			// AssetManager mngr = getAssets();
			if (m.isPlaying()) {
				m.stop();
				m.release();
				m = new MediaPlayer();
			}

			// AssetFileDescriptor afd = getAssets().openFd(fileName);
			AssetFileDescriptor descriptor = getAssets().openFd(soundfile);
			m.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			m.prepare();
			m.setVolume(1f, 1f);
			// m.setLooping(true);
			m.start();
		} catch (Exception e) {
		}
	}
	
	public void tampil_semua_misi(){
		//mengirimkan variabel ke url
	    //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    
	    //fungsi untuk memberikan izin pemakaian jaringan
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
	    //mencoba mengakses url melalui httppost
	    try{    		
	    	HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://192.168.100.11:90/pilaras/tampil_semua_misi.php"); // <= definisi url di variabel httppost
	        //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
	        
	    }catch(Exception e){
	    	
	    }
	    
	    //membaca keluaran dari php
        try{        	
         	BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8); // <= membaca file balasan dengan standar iso
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null){
       		    sb.append(line + "\n");
           	}
            is.close();
            result = sb.toString();
	        Log.e("pass 2", "connection success ");
	        
        }
        catch(Exception e){
        	Log.e("Fail 2", e.toString());
        	
        }
       
        //memasukkan hasil keluaran dari php ke JSON
        JSONArray jArray;
        try{
        	//JSONObject json_data = new JSONObject(result);
        	// menyimpan nilai dari tipe user
        	jArray = new JSONArray(result);
            JSONObject jObject = null;
            nama = new String[jArray.length()];
            for(int i = 0; i<jArray.length(); i++){
	            jObject = jArray.getJSONObject(i);
	            //nama[i] = jObject.getString("status");
	            
	    		TableRow barisTabel = new TableRow(this);
	    		
	            TextView viewId = new TextView(this);
                viewId.setText("Menabung hingga Rp."+jObject.getString("misi"));
                viewId.setPadding(5, 10, 15, 20);
                barisTabel.addView(viewId);

                TextView viewNama = new TextView(this);
                viewNama.setText(jObject.getString("hadiah"));
                viewNama.setPadding(5, 10, 15, 20);
                barisTabel.addView(viewNama);

                /*TextView viewAlamat = new TextView(this);
                viewAlamat.setText(jObject.getString("status"));
                viewAlamat.setPadding(5, 10, 15, 20);
                barisTabel.addView(viewAlamat);*/
                
                TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        		params.topMargin = 55;
        		params.leftMargin = 20;
        		params.rightMargin = 20;
                
        		detil.add(i, new Button(this));
                detil.get(i).setId(Integer.parseInt(id));
                detil.get(i).setTag("detil");
                detil.get(i).setText(jObject.getString("status"));
                detil.get(i).setTextSize(15);
                detil.get(i).setBackgroundColor(Color.parseColor("#0146fe"));
                detil.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                detil.get(i).setLayoutParams(params);
                detil.get(i).setOnClickListener(this);
        		barisTabel.addView(detil.get(i));
            }
            Toast.makeText(getApplicationContext(), "sukses", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
        	Toast.makeText(getApplicationContext(), "gagal"+e.toString(), Toast.LENGTH_SHORT).show();
    	}
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		for (int i = 0; i < detil.size(); i++) {
			/* jika yang diklik adalah button edit */
			if (view.getId() == detil.get(i).getId() && view.getTag().toString().trim().equals("detil")) {
                final int id_data = detil.get(i).getId();
                suara_button("click.mp3");
				Intent j = new Intent(getApplicationContext(), Ortu_menu_3_3.class);
				j.putExtra("nama_ortu", nama_ortu);
				j.putExtra("user_ortu", user_ortu);
				j.putExtra("nama_anak", nama_anak);
				j.putExtra("user_anak", user_anak);
				j.putExtra("jml_tabungan", jml_tabungan);
				j.putExtra("jenis_pet", jenis_pet);
				j.putExtra("status_pet", status_pet);
				j.putExtra("status_makanan", status_makanan);
				j.putExtra("misi_desc", misi_desc);
				j.putExtra("misi_gol", misi_gol);
				j.putExtra("hadiah", hadiah);
				j.putExtra("server", server);
				j.putExtra("id_misi", id_data);
				j.putExtra("dibuka_dari", "ortu_menu_3");
				startActivity(j);
    			finish();
        	}
		}
	}
}
