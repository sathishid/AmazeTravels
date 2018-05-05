package ara.com.amazetravels.ara.com.amazetravels.models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ara.com.amazetravels.R;
import ara.com.amazetravels.ara.com.utils.AppConstants;


public class BookingHistoryAdapter extends ArrayAdapter<Booking> {
    private ArrayList<Booking> bookings;
    private Context context;


    // View lookup cache
    private static class ViewHolder {
        TextView BookedStatus;
        TextView ApprovedBy;
        TextView BookedDate;
        ImageView ImageItemStatus;
    }

    @Override
    public int getCount() {
        return bookings.size();
    }

    public BookingHistoryAdapter(Context context, ArrayList<Booking> bookingArrayList) {
        super(context, R.layout.booked_item, bookingArrayList);
        this.bookings = bookingArrayList;
        this.context = context;
    }

    private int lastPosition = -1;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Booking dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.booked_item, parent, false);
        }
        ImageView imageItemStatus = (ImageView) convertView.findViewById(R.id.image_item_status);
        TextView bookedStatus = (TextView) convertView.findViewById(R.id.tv_item_booked_status);
        TextView approvedBy = (TextView) convertView.findViewById(R.id.tv_item_approved_by);
        TextView  bookedDate = (TextView) convertView.findViewById(R.id.tv_item_confirmed_date);
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;

        bookedStatus.setText(dataModel.getBookingId() + "");
        String strApprovedBy = dataModel.getApprovedBy();
        if (strApprovedBy.compareToIgnoreCase("Admin")!=0) {
            approvedBy.setText("Not yet approved.");
            imageItemStatus.setImageResource(R.drawable.ic_progress);
        } else {
            approvedBy.setText("Approved By Booking Team" );
            imageItemStatus.setImageResource(R.drawable.ic_done);
        }
        bookedDate.setText(AppConstants.getStringDate(dataModel.getAppointmentDate(), true));


        // Return the completed view to render on screen
        return convertView;
    }
}

