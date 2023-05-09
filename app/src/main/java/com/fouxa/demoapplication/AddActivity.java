package com.fouxa.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.fouxa.demoapplication.data.EmployeeDbTask;
import com.fouxa.demoapplication.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {
    private ActivityAddBinding binding;
    private EmployeeDbTask employeeDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        employeeDb = new EmployeeDbTask(this);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewEmployee();
            }
        });
    }

    private void saveNewEmployee() {
        String employeeId = binding.employeeId.getText().toString();
        String employeeName = binding.employeeName.getText().toString();

        if (employeeId.isEmpty()){
            Toast.makeText(this, "Please Enter Employee ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (employeeName.isEmpty()){
            Toast.makeText(this, "Please Enter Employee Name", Toast.LENGTH_SHORT).show();
            return;
        }

        employeeDb.insertData(employeeId, employeeName);
        Toast.makeText(this, "Employee added success", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}