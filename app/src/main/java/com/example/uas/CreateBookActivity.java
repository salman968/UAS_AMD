package com.example.uas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateBookActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editJudul, editNama, editTahun;
    private Button btnSave;

    private Book book;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        editJudul = findViewById(R.id.edt_judul);
        editNama = findViewById(R.id.edt_nama_penulis);
        editTahun = findViewById(R.id.edt_tahun_terbit);

        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(this);

        book = new Book();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_save) {
            saveBook();
        }

    }

    private void saveBook()
    {
        String title = editJudul.getText().toString().trim();
        String author = editNama.getText().toString().trim();
        String year = editTahun.getText().toString().toString();


        boolean isEmptyFields = false;

        if (TextUtils.isEmpty(title)) {
            isEmptyFields = true;
            editTahun.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(author)) {
            isEmptyFields = true;
            editNama.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(year)) {
            isEmptyFields = true;
            editTahun.setError("Field ini tidak boleh kosong");
        }

        if (! isEmptyFields) {

            Toast.makeText(CreateBookActivity.this, "Saving Data...", Toast.LENGTH_SHORT).show();

            DatabaseReference dbBook = mDatabase.child("book");

            String id = dbBook.push().getKey();
            book.setId(id);
            book.setTitle(title);
            book.setAuthor(author);
            book.setYear(Integer.parseInt(year));

            //insert data
            dbBook.child(id).setValue(book);

            finish();

        }
    }
}