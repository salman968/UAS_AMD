package com.example.uas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Book> bookList = new ArrayList<>();

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    public BookAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int i) {
        return bookList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;

        if (itemView == null) {
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_book, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        Book book = (Book) getItem(i);
        viewHolder.bind(book);
        return itemView;
    }

    private class ViewHolder {
        private TextView tv_title, tv_author;

        ViewHolder(View view) {
            tv_title = view.findViewById(R.id.tv_title);
            tv_author = view.findViewById(R.id.tv_author);
        }

        void bind(Book book) {
            tv_title.setText(book.getTitle());
            tv_author.setText(book.getAuthor());
        }
    }
}
