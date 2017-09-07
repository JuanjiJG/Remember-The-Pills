package com.example.juanjojg.rememberthepills.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanjojg.rememberthepills.activities.AddUpdatePillActivity;
import com.example.juanjojg.rememberthepills.R;
import com.example.juanjojg.rememberthepills.database.PillOperations;
import com.example.juanjojg.rememberthepills.models.Pill;

import java.util.ArrayList;

/**
 * Created by Juanjo on 08/08/2017.
 */

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.ViewHolder> {
    private ArrayList<Pill> mPillDataSet;
    private Context mContext;
    private static final String EXTRA_ADD_UPDATE = "add_update_mode";
    private static final String EXTRA_PILL_ID = "pillID";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mPillCardView;
        TextView mTextViewName;
        TextView mTextViewDescription;
        TextView mTextViewPillID;
        TextView mTextViewPillActions;
        ImageView mImageViewPillPhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            mPillCardView = (CardView) itemView.findViewById(R.id.pill_card_view);
            mTextViewName = (TextView) itemView.findViewById(R.id.pill_name);
            mTextViewDescription = (TextView) itemView.findViewById(R.id.pill_description);
            mTextViewPillID = (TextView) itemView.findViewById(R.id.pill_id);
            mTextViewPillActions = (TextView) itemView.findViewById(R.id.pill_actions_card_view);
            mImageViewPillPhoto = (ImageView) itemView.findViewById((R.id.card_view_pill_photo));
        }
    }

    public PillAdapter(Context context, ArrayList<Pill> data) {
        this.mPillDataSet = data;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pill, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextViewName.setText(mPillDataSet.get(position).getPillName());
        holder.mTextViewDescription.setText(mPillDataSet.get(position).getPillDescription());
        holder.mTextViewPillID.setText(String.valueOf(mPillDataSet.get(position).getPillID()));
        holder.mImageViewPillPhoto.setImageBitmap(mPillDataSet.get(position).getPillPhoto());
        holder.mTextViewPillActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, position);
            }
        });
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);

        // Inflate menu and set listener
        popup.inflate(R.menu.actions_pill_card_view);
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));

        popup.show();
    }

    // This class implements OnMenuItemClickListener and is used to handle the popup menu actions
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int position;

        MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete_pill_menu_item:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    // Set dialog message
                    builder
                            .setCancelable(true)
                            .setTitle(R.string.delete_dialog_title)
                            .setMessage(R.string.delete_dialog_message)
                            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PillOperations pOps = new PillOperations(mContext);
                                    pOps.open();
                                    pOps.removePill(mPillDataSet.get(position));
                                    pOps.close();
                                    mPillDataSet.remove(position);
                                    notifyDataSetChanged();
                                    Toast t = Toast.makeText(mContext, R.string.remove_pill_success, Toast.LENGTH_SHORT);
                                    t.show();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Remove pill cancelled
                                }
                            })
                            .create()
                            .show();
                    return true;
                case R.id.edit_pill_menu_item:
                    Intent i = new Intent(mContext, AddUpdatePillActivity.class);
                    i.putExtra(EXTRA_PILL_ID, mPillDataSet.get(position).getPillID());
                    i.putExtra(EXTRA_ADD_UPDATE, "Update");
                    mContext.startActivity(i);
                    ((Activity)mContext).finish();
                    return true;
            }
            return true;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPillDataSet.size();
    }
}