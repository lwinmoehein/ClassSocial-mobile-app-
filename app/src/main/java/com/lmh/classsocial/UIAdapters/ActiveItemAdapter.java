package com.lmh.classsocial.UIAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lmh.classsocial.Model.UserAccount;
import com.lmh.classsocial.R;
import com.lmh.classsocial.Static.CircleTransformation;
import com.lmh.classsocial.Static.FunctionsStatic;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by E on 8/7/2018.
 */

public class ActiveItemAdapter extends RecyclerView.Adapter<ActiveItemAdapter.ActiveItemViewHolder> {
    private List<UserAccount> userAccounts;
    private Context context;

    public ActiveItemAdapter(List<UserAccount> userAccounts, Context context) {
        this.userAccounts = userAccounts;
        this.context = context;
        setHasStableIds(true);
    }


    @Override
    public ActiveItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View activeView= LayoutInflater.from(parent.getContext()).inflate(R.layout.active_item,parent,false);

        return new ActiveItemViewHolder(activeView);
    }

    @Override
    public void onBindViewHolder(ActiveItemViewHolder holder, int position) {UserAccount userAccount=userAccounts.get(position);
        ActiveItemViewHolder activeItemViewHolder=(ActiveItemViewHolder)holder;
        activeItemViewHolder.accName.setText(userAccount.getUs_name());

        Picasso.with(context).load(FunctionsStatic.getProfileImageUrl(userAccount.getUs_id()))
                .placeholder(R.drawable.ic_profile_loading)
                .error(R.drawable.ic_profile)
                .resize(80,80)
                .transform(new CircleTransformation())
                .centerCrop()
                .into(holder.pc);
    }

    @Override
    public int getItemCount() {
        return userAccounts.size();
    }

    static class ActiveItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView pc;

        private TextView accName;

        public ActiveItemViewHolder(View itemView) {
            super(itemView);
            pc = (ImageView) itemView.findViewById(R.id.active_item_pc);
            accName = (TextView) itemView.findViewById(R.id.active_item_name);
        }


    }
}
