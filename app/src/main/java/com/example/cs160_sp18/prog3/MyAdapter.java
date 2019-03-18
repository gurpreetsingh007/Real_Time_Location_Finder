package com.example.cs160_sp18.prog3;

import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.ImageViewHolder>  {
    private int[] images;
    private String[] titles;
    private String username;
    private double current_longitude;
    private double current_latitiude;
    private double []album_latitude;
    private double []album_longitude;
    private Location current_location;
    private Location album_location;

    public MyAdapter(int[] images,String[]titles, double latitiude,double longitude,double []album_latitude,
                     double []album_longitude,Location current_location, String username){
        this.images=images;
        this.titles=titles;
        this.current_latitiude=latitiude;
        this.current_longitude=longitude;
        this.current_location = current_location;
        this.album_latitude = album_latitude;
        this.album_longitude= album_longitude;
        album_location = new Location("");
        this.username = username;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return an object of viewholder, first create view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout,parent,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder,int position) {
        //Placing the data on view components
        //get the image position first
        int image_id = images[position];
        holder.album.setImageResource(image_id); //pass the image id
        holder.album_Title.setText(titles[position]); // pass the titles

        double distance = getDistancBetweenTwoPoints(current_latitiude,album_latitude[position]
                ,current_longitude,album_longitude[position]);
        Log.d("Location distance", "Value: " + distance);
        holder.album_range.setText((distance)+" meters away");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (distance < 100000000){
//                    Toast.makeText(view.getContext(),
//                            "You must be within 10 meters of a landmark to access it's feed",
//                            Toast.LENGTH_SHORT).show();
//                }
//                else{
                    Intent intent = new Intent(view.getContext(),CommentFeedActivity.class);
                    intent.putExtra("passing_username",username);
                    intent.putExtra("landmark", titles[position]);
                    view.getContext().startActivity(intent);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length; //total images at the recycle view
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView album;
        TextView album_Title;
        TextView album_range;
//        Context context;

        public ImageViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.album);
            album_Title = itemView.findViewById(R.id.album_title);
            album_range = itemView.findViewById(R.id.album_range);
//            context = itemView.getContext();
        }
    }

    private double getDistancBetweenTwoPoints(double lat1,double lon1,double lat2,double lon2) {

        Location current = new Location("");
        Location other = new Location("");
        current.setLatitude(lat1);
        current.setLongitude(lon1);
        other.setLatitude(lat2);
        other.setLongitude(lon2);
        return Math.round(current.distanceTo(other));

//        float[] distance = new float[2];
//
//        Location.distanceBetween( lat1, lon1,
//                lat2, lon2, distance);
//
//        return distance[0];


//        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output
//
//        double dLat = Math.toRadians(lat2-lat1);
//        double dLng = Math.toRadians(lon2-lon1);
//
//        double sindLat = Math.sin(dLat / 2);
//        double sindLng = Math.sin(dLng / 2);
//
//        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
//                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
//
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//
//        double dist = earthRadius * c;
//
//        return dist/1609.34;
    }
}
