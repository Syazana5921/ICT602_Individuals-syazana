package com.example.individuals_syazana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CalculateBillActivity extends AppCompatActivity {

    Spinner snMonth;
    EditText Unit;
    EditText Rebate;
    Button btnCalculate;
    TextView cTtlCharge;
    TextView cFnlCost;
    ListView listView;
    DatabaseReference db;
    List<ElectricityBill> billList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculate_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        snMonth = findViewById(R.id.snMonth);
        Unit = findViewById(R.id.Unit);
        Rebate = findViewById(R.id.Rebate);
        btnCalculate = findViewById(R.id.btnCalculate);
        listView = findViewById(R.id.listView);
        cTtlCharge = findViewById(R.id.cTtlCharge);  // <-- INI PENTING
        cFnlCost = findViewById(R.id.cFnlCost);

        FirebaseApp.initializeApp(this);
        db = FirebaseDatabase.getInstance().getReference("ElectricityBills");
        billList = new ArrayList<>();

        snMonth.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Month: ", "Jan", "Feb", "Mar", "Apr", "May", "June", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",}));

        btnCalculate.setOnClickListener(v -> addBill());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ElectricityBill bill = billList.get(position);
            Intent intent = new Intent(CalculateBillActivity.this, BillDetailActivity.class);
            intent.putExtra("billId", bill.id);
            startActivity(intent);
        });
    }

    private void addBill() {
        String month = snMonth.getSelectedItem().toString();
        String unt = Unit.getText().toString().trim();
        String PerRebate = Rebate.getText().toString().trim();

        if (unt.isEmpty() || PerRebate.isEmpty()) {
            Toast.makeText(this, "Fill the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int unit = Integer.parseInt(unt);
        double rebate = Double.parseDouble(PerRebate);

        try {
            unit = Integer.parseInt(unt);
            rebate = Double.parseDouble(PerRebate);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rebate < 0 || rebate > 5) {
            Toast.makeText(this, "must be 0 - 5% ", Toast.LENGTH_SHORT).show();
            return;
        }

        double TtlCharge = calculateCharges(unit);
        double FnlCost = TtlCharge - (TtlCharge * (rebate / 100));

        cTtlCharge.setText("Total Charges: RM" + String.format("%.2f", TtlCharge));
        cFnlCost.setText("Final Cost: RM" + String.format("%.2f", FnlCost));

        String id = db.push().getKey();
        ElectricityBill bill = new ElectricityBill(id, month, unit, rebate, TtlCharge, FnlCost);

        assert id != null;
        db.child(id).setValue(bill);
        Toast.makeText(this, "Has been saved records.", Toast.LENGTH_SHORT).show();
    }


    private double calculateCharges(int unit) {
        double total = 0;
        if (unit <= 200){
            total = unit * 21.8;
        } else if (unit <= 300){
            total = (200 * 21.8) + ((unit - 200) * 33.4);
        } else if (unit <= 600){
            total = (200 * 21.8) + (100 * 33.4) + ((unit - 300) * 51.6);
        }else{
            total = (200 * 21.8) + (100 * 33.4) + (300 * 51.6) + ((unit - 600) * 54.6);
        }
        return total / 100.0;

    }

    protected void onStart(){
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    ElectricityBill bill = data.getValue(ElectricityBill.class);
                    billList.add(bill);
                }

                BillList adapter=new BillList(CalculateBillActivity.this,billList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}