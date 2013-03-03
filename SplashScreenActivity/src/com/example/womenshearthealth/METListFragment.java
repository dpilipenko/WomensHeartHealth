package com.example.womenshearthealth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class METListFragment extends Fragment {

    METSDBAdapter db;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// load in the fragment_metlist.xml file and display it on screen
		
		
		return inflater.inflate(R.layout.fragment_metlist, container, false);
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = new METSDBAdapter(activity);
		addMET("Running", 23, "June 6, 1972");
		addMET("Kicking", 21, "June 77, 1972");
		addMET("punching", 42, "June 34, 1979");

        
	    loadMETSDB();

    }

	private void loadMETSDB() {

		try {

            String destPath = "/data/data/" + this.getActivity().getPackageName() +
                "/databases";
            File f = new File(destPath);
            if (!f.exists()) {            	
            	f.mkdirs();
                f.createNewFile();
            	
            	//Copy db from assets folder to the databases folder
                CopyDB(this.getActivity().getBaseContext().getAssets().open("METS"),
                    new FileOutputStream(destPath + "/METS"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       

		db.open();
		
        Cursor c = db.getAllMETS();
        if (c.moveToFirst())
        {
            do {
                DisplayMET(c);
            } while (c.moveToNext());
        }
        db.close();
    }
    
    public void CopyDB(InputStream inputStream, 
    OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public void DisplayMET(Cursor c)
    {
        Toast.makeText(this.getActivity(),
                "id: " + c.getString(0) + "\n" +
                "Name: " + c.getString(1) + "\n" +
                "METS Amount:  " + c.getInt(2) + "\n" +
                "Date:  " + c.getString(3),
                Toast.LENGTH_LONG).show();
    }
	
    public void addMET(String name, int METS, String date) {
    db.open();
    long id = db.insertMETSActivity(name, METS, date);
    db.close();
    }
	
}
