package com.example.disaster_message_notificator;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MsgInfoAdapter extends ArrayAdapter<MsgInfo> {
    private Context context;
    private List mList;
    private ListView mListView;

    private class UserViewHolder {
        public TextView tvCreateDate;
        public TextView tvMsg;
    }

    public MsgInfoAdapter(Context context, List<MsgInfo> list, ListView listView) {
        super(context, 0, list);

        this.context = context;
        this.mList = list;
        this.mListView = listView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        UserViewHolder viewHolder;
        String status;

        if(rowView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            rowView = layoutInflater.inflate(R.layout.list_item, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.tvCreateDate = (TextView) rowView.findViewById(
                    R.id.tvListCreateDate // 한 줄에 대한 레이아웃 파일(R.layout.list_item)의 구성요소,
            );
            viewHolder.tvMsg = (TextView) rowView.findViewById(R.id.tvListMsg);

            rowView.setTag(viewHolder);

            status = "created";
        } else {
            viewHolder = (UserViewHolder) rowView.getTag();

            status = "reused";
        }

        //MsgInfo 객체 리스트의 position 위치에 있는 MsgInfo 객체를 가져옵니다.
        MsgInfo msgInfo = (MsgInfo) mList.get(position);

        //현재 선택된 MsgInfo 객체를 화면에 보여주기 위해서 앞에서 미리 찾아 놓은 뷰에 데이터를 집어넣습니다.
        viewHolder.tvCreateDate.setText(msgInfo.getCreate_date());
        viewHolder.tvMsg.setText(msgInfo.getMsg());

        return rowView;
    }
}
