package com.ap.zapper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ap.zapper.R;
import com.ap.zapper.models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanunsworth on 2017/01/22.
 */

public class ItemAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private List<Person> people = new ArrayList<>();

    public ItemAdapter(Context context, List<Person> people) {
        this.people = people;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Person getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if(view == null) {
            view = layoutInflater.inflate(R.layout.row_person_layout, viewGroup, false);

            holder = new ItemAdapter.ViewHolder();

            holder.firstName = (TextView)view.findViewById(R.id.firstName);
            holder.lastName = (TextView)view.findViewById(R.id.lastName);
            view.setTag(holder);


        } else {
            holder = (ItemAdapter.ViewHolder) view.getTag();
        }

        Person person = getItem(i);

        holder.firstName.setText(person.getFirstName());
        holder.lastName.setText(person.getLastName());

        return view;
    }

    static class ViewHolder {
        private TextView firstName;
        private TextView lastName;
    }
}
