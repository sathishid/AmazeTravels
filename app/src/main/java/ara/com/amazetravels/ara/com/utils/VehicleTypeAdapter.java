package ara.com.amazetravels.ara.com.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ara.com.amazetravels.ara.com.amazetravels.models.VehicleType;

/**
 * Created by sathi on 30-03-2018.
 */

public class VehicleTypeAdapter extends ArrayAdapter<VehicleType> {

    private Context context;
    private VehicleType[] vehicleTypes;

    public VehicleTypeAdapter(@NonNull Context context, int resource, @NonNull VehicleType[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.vehicleTypes = vehicleTypes;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public VehicleType getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }
}
