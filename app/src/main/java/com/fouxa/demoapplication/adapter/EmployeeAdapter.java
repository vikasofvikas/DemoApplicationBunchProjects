package com.fouxa.demoapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fouxa.demoapplication.data.Employee;
import com.fouxa.demoapplication.databinding.ItemEmployeeBinding;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    List<Employee> employeeList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemEmployeeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvId.setText(employeeList.get(position).getEmployeeID());
        holder.binding.tvName.setText(employeeList.get(position).getEmployeeName());
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemEmployeeBinding binding;
        public ViewHolder(@NonNull ItemEmployeeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public void setData(List<Employee> employeeList){
        this.employeeList.clear();
        this.employeeList.addAll(employeeList);
        notifyDataSetChanged();
    }
}
