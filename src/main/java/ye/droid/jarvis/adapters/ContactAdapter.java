package ye.droid.jarvis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ye.droid.jarvis.R;
import ye.droid.jarvis.beans.ContactBean;

/**
 * Created by ye on 2017/5/18.
 */

public class ContactAdapter extends BaseAdapter {
    private List<ContactBean> contacts;
    private Context context;
    private TextView tv_contact_name;
    private TextView tv_contact_phone;

    public ContactAdapter(Context context, List<ContactBean> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_contact, null);
        } else {
            view = convertView;
        }

        tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
        tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);

        ContactBean contactBean = contacts.get(position);
        String contact_id = contactBean.getContactID();
        String name = contactBean.getContactName();
        String phone = contactBean.getContactPhone();
        String phone_v2 = contactBean.getContactPhone_v2();
        tv_contact_name.setText(name);
        tv_contact_phone.setText((phone_v2 != null) ? phone_v2 : phone);

        return view;
    }
}
