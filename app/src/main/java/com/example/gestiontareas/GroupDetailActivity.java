package com.example.gestiontareas;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GroupDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        TextView groupNameTextView = findViewById(R.id.textViewGroupNameDetail);
        TextView groupDescriptionTextView = findViewById(R.id.textViewGroupDescriptionDetail);
        TextView groupCodeTextView = findViewById(R.id.textViewGroupCodeDetail);

        String groupName = getIntent().getStringExtra("group_name");
        String groupDescription = getIntent().getStringExtra("group_description");
        String groupCode = getIntent().getStringExtra("group_code");

        groupNameTextView.setText(groupName);
        groupDescriptionTextView.setText(groupDescription);
        groupCodeTextView.setText(groupCode);
    }
}
