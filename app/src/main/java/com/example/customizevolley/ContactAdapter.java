package com.example.customizevolley;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Student> students = new ArrayList<>();

    public ContactAdapter(List<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bindContact(students.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView firstCharTv;
        private TextView fullNameTv;
        private TextView courseTv;
        private TextView scoreTv;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            firstCharTv = itemView.findViewById(R.id.tv_rv_main_contactFirstChar);
            fullNameTv = itemView.findViewById(R.id.tv_rv_main_fullName);
            courseTv = itemView.findViewById(R.id.tv_rv_main_courseContact);
            scoreTv = itemView.findViewById(R.id.tv_rv_main_scoreContact);
        }

        void bindContact(Student student) {
            firstCharTv.setText(student.getFirstName().substring(0, 1));
            fullNameTv.setText(student.getFirstName() + " " + student.getLastName());
            courseTv.setText(student.getCourse());
            scoreTv.setText(String.valueOf(student.getScore()));
        }

    }

    //add student
    public void add(Student student) {
        this.students.add(0, student);
        notifyItemInserted(0);
    }
}
