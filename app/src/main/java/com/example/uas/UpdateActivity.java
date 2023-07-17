package com.example.uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNim, edtNama;
    private Button btnUpdate;
    private NavigationView navigationView;

    public static final String EXTRA_MAHASISWA = "extra_mahasiswa";
    public final int ALERT_DIALOG_CLOSE = 10;
    public final int ALERT_DIALOG_DELETE = 20;

    private Mahasiswa mahasiswa;
    private String mahasiswaId;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        edtNama = findViewById(R.id.edt_edit_nama);
        edtNim = findViewById(R.id.edt_edit_nim);
        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);

        navigationView = findViewById(R.id.menu_delete);

        mahasiswa = getIntent().getParcelableExtra(EXTRA_MAHASISWA);

        if (mahasiswa != null) {
            mahasiswaId = mahasiswa.getId();
        } else {
            mahasiswa = new Mahasiswa();
        }

        if (mahasiswa != null) {
            edtNim.setText(mahasiswa.getNim());
            edtNama.setText(mahasiswa.getNama());

        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Data");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete) {
                    deleteMahasiswa();
                    return true;
                }
                return false;
            }
        });

    }


    private void deleteMahasiswa() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Hapus Data");
        alertDialogBuilder.setMessage("Apakah anda yakin ingin menghapus item ini?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference mahasiswaRef = mDatabase.child("mahasiswa").child(mahasiswaId);

                mahasiswaRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateActivity.this, "Data mahasiswa dihapus", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateActivity.this, "Gagal menghapus data mahasiswa", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_update) {
            updateMahasiswa();
        }
    }

    public void updateMahasiswa() {
        String nama = edtNama.getText().toString().trim();
        String nim = edtNim.getText().toString().trim();

        boolean isEmptyFields = false;

        if (TextUtils.isEmpty(nama)) {
            isEmptyFields = true;
            edtNama.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(nim)) {
            isEmptyFields = true;
            edtNim.setError("Field ini tidak boleh kosong");
        }

        if (! isEmptyFields) {

            Toast.makeText(UpdateActivity.this, "Updating Data...", Toast.LENGTH_SHORT).show();

            mahasiswa.setNim(nim);
            mahasiswa.setNama(nama);
            mahasiswa.setPhoto("");

            DatabaseReference dbMahasiswa = mDatabase.child("mahasiswa");

            //update data
            dbMahasiswa.child(mahasiswaId).setValue(mahasiswa);

            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //pilih menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_delete) {
            showAlertDialog(ALERT_DIALOG_DELETE);
        } else if (itemId == android.R.id.home) {
            showAlertDialog(ALERT_DIALOG_CLOSE);
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form";
        } else {
            dialogTitle = "Hapus Data";
            dialogMessage = "Apakah anda yakin ingin menghapus item ini";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (isDialogClose) {
                            finish();
                        } else {
                            //hapus data
                            DatabaseReference dbMahasiswa = mDatabase.child("mahasiswa").child(mahasiswaId);

                            dbMahasiswa.removeValue();

                            Toast.makeText(UpdateActivity.this, "Deleting data...",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}