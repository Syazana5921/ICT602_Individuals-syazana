package com.example.individuals_syazana;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BillDetailActivity extends AppCompatActivity {

    TextView dMonth, dUnit, dRebate, dTtlCharge, dFnlCost;
    DatabaseReference db;
    String billId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dMonth = findViewById(R.id.dMonth);
        dUnit = findViewById(R.id.dUnit);
        dRebate = findViewById(R.id.dRebate);
        dTtlCharge = findViewById(R.id.dTtlCharge);
        dFnlCost = findViewById(R.id.dFnlCost);

        billId = getIntent().getStringExtra("billId");
        db = FirebaseDatabase.getInstance().getReference("ElectricityBills").child(billId);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ElectricityBill bill=snapshot.getValue(ElectricityBill.class);
                if (snapshot.exists()) {
                    // Dapatkan data
                    String month = snapshot.child("month").getValue(String.class);
                    int unit = snapshot.child("unit").getValue(Integer.class);
                    double rebate = snapshot.child("rebate").getValue(Double.class);
                    double ttlCharge = snapshot.child("ttlCharge").getValue(Double.class);
                    double fnlCost = snapshot.child("fnlCost").getValue(Double.class);

                    // Papar ke TextView
                    dMonth.setText("Month: " + month);
                    dUnit.setText("Unit: " + unit + " kWh");
                    dRebate.setText("Rebate: " + rebate + " %");
                    dTtlCharge.setText("Total Charges: RM" + String.format("%.2f", ttlCharge));
                    dFnlCost.setText("Final Cost: RM" + String.format("%.2f", fnlCost));
                }
                findViewById(R.id.btnBack).setOnClickListener(view -> finish());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BillDetailActivity.this,"Failed to load details",Toast.LENGTH_SHORT).show();
            }
        });

    }
}