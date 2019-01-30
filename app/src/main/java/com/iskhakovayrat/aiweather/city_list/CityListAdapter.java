package com.iskhakovayrat.aiweather.city_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iskhakovayrat.aiweather.ConstantInterface;
import com.iskhakovayrat.aiweather.R;
import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;
import com.iskhakovayrat.aiweather.utils.TempConverter;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private List<CurrentWeatherResponse> items;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    private OnCityListItemClickListener onCityListItemClickListener;

    public CityListAdapter(List<CurrentWeatherResponse> items,
                           OnDeleteButtonClickListener onDeleteButtonClickListener,
                           OnCityListItemClickListener onCityListItemClickListener) {
        this.items = items;
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
        this.onCityListItemClickListener = onCityListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.city_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(
                view -> onCityListItemClickListener.onClick(items.get(position).getId()));
        holder.cityItemName.setText(items.get(position).getName());
        holder.cityItemTemp.setText(TempConverter.convert(items.get(position).getMain().getTemp()));
        Glide.with(holder.cityItemIcon.getContext())
                .load(ConstantInterface.iconUrlPath
                        + items.get(position).getWeather().get(0).getIcon() + ".png")
                .into(holder.cityItemIcon);
        holder.cityItemDeleteButton.setOnClickListener(v ->
                onDeleteButtonClickListener.onClick(items.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void deleteItem(int cityId) {
        for(CurrentWeatherResponse item: items){
            if(item.getId() == cityId){
                items.remove(item);
                notifyDataSetChanged();
                break;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cityItemName;
        private Button cityItemDeleteButton;
        private TextView cityItemTemp;
        private ImageView cityItemIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            cityItemName = itemView.findViewById(R.id.cityItemName);
            cityItemDeleteButton = itemView.findViewById(R.id.cityItemDeleteButton);
            cityItemTemp = itemView.findViewById(R.id.cityItemTemp);
            cityItemIcon = itemView.findViewById(R.id.cityItemIcon);
        }
    }

    public void addItem(CurrentWeatherResponse currentWeatherResponse){
        items.add(currentWeatherResponse);
        notifyItemInserted(getItemCount()-1);
    }
}
