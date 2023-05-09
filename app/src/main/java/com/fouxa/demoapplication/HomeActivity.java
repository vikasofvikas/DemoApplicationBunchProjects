package com.fouxa.demoapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.fouxa.demoapplication.adapter.EmployeeAdapter;
import com.fouxa.demoapplication.data.DatabaseHelper;
import com.fouxa.demoapplication.data.Employee;
import com.fouxa.demoapplication.data.EmployeeDbTask;

import com.fouxa.demoapplication.utils.CSVWritter;
import com.fouxa.demoapplication.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private EmployeeAdapter adapter;
    private EmployeeDbTask employeeDb;
    List<Employee> employeeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        employeeDb = new EmployeeDbTask(this);

        initRcv();
        getDataAndShow();

        binding.btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
            }
        });

        binding.btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {
                if (Environment.isExternalStorageManager()) {
                    exportDB();
                } else {
                    //request for the permission
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });
    }

    private void getDataAndShow() {
        employeeList.clear();
        Cursor crs = employeeDb.viewData();
        while(crs.moveToNext()){
            String empId = crs.getString(crs.getColumnIndex(DatabaseHelper.COL_EMP_ID));
            String eName = crs.getString(crs.getColumnIndex(DatabaseHelper.COL_EMP_NAME));

            Employee employee = new Employee();
            employee.setEmployeeID(empId);
            employee.setEmployeeName(eName);
            employeeList.add(employee);
        }

        adapter.setData(employeeList);
    }

    private void initRcv() {
        adapter = new EmployeeAdapter();
        binding.rcvEmployee.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvEmployee.setAdapter(adapter);
    }

    private void exportDB() {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "hello_csv");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csv_data.csv");
        try
        {
            file.createNewFile();
            CSVWritter csvWrite = new CSVWritter(new FileWriter(file));
            Cursor crs = employeeDb.viewData();
            csvWrite.writeNext(crs.getColumnNames());
            while(crs.moveToNext()){
                String empId = crs.getString(crs.getColumnIndex(DatabaseHelper.COL_EMP_ID));
                String eName = crs.getString(crs.getColumnIndex(DatabaseHelper.COL_EMP_NAME));

                String arrStr[] = {empId, eName};
                csvWrite.writeNext(arrStr);
            }

            csvWrite.close();
            crs.close();

            sendFileForEmail(file);
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    private void sendFileForEmail(File file) {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("message/rfc822");
        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.fouxa.demoapplication.fileprovider", file);

        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"vikasdeepsinghsaini@gmail.com"});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is the SQLite to CSV");
        mailIntent.putExtra(Intent.EXTRA_TEXT, "Please find the attached file");
        mailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

        try {
            startActivity(Intent.createChooser(mailIntent, "Send email.."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No Email Service Found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==101){
            if (grantResults.length!=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportDB();
            }
            else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}