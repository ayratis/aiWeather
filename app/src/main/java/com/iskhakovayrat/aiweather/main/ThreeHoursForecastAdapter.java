package com.iskhakovayrat.aiweather.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iskhakovayrat.aiweather.ConstantInterface;
import com.iskhakovayrat.aiweather.R;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastListItem;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastResponse;
import com.iskhakovayrat.aiweather.utils.DateConverter;
import com.iskhakovayrat.aiweather.utils.TempConverter;

import java.util.List;

public class ThreeHoursForecastAdapter
        extends RecyclerView.Adapter<ThreeHoursForecastAdapter.ViewHolder> {

    private List<ThreeHoursForecastListItem> items;

    public ThreeHoursForecastAdapter(ThreeHoursForecastResponse threeHoursForecastResponse) {
        items = threeHoursForecastResponse.getList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.three_hours_forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.threeHoursDate
                .setText(DateConverter.getDayOfWeekAndHours(items.get(position).getDt()));

        String threeHoursIconUrl = ConstantInterface.iconUrlPath
                + items.get(position).getWeather().get(0).getIcon() + ".png";
        Glide.with(holder.threeHoursIcon.getContext())
                .load(threeHoursIconUrl)
                .into(holder.threeHoursIcon);

        holder.threeHoursTemp
                .setText(TempConverter.convert(items.get(position).getMain().getTemp()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView threeHoursDate;
        private ImageView threeHoursIcon;
        private TextView threeHoursTemp;

        public ViewHolder(View itemView) {
            super(itemView);
            threeHoursDate = itemView.findViewById(R.id.threeHoursDate);
            threeHoursIcon = itemView.findViewById(R.id.threeHoursIcon);
            threeHoursTemp = itemView.findViewById(R.id.threeHoursTemp);
        }
    }
}
