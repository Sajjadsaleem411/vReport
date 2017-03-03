package app.vreport.com.Controller;


import android.app.Dialog;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;


import org.hamcrest.core.Is;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import app.vreport.com.Activities.Report.GeneralForm;
import app.vreport.com.Model.Item;
import app.vreport.com.Model.Report;
import app.vreport.com.Model.Resources;
import app.vreport.com.R;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends ArrayAdapter<Report> implements View.OnClickListener {

    public Resources mResourceData;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public FoldingCellListAdapter(Context context, List<Report> objects) {
        super(context, 0, objects);
        mResourceData=new Resources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        final Report item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);

            // binding view parts to view holder

            viewHolder.votes = (TextView) cell.findViewById(R.id.title_votes);
            viewHolder.location = (TextView) cell.findViewById(R.id.title_location);
            viewHolder.city = (TextView) cell.findViewById(R.id.title_city_country);
            viewHolder.category = (TextView) cell.findViewById(R.id.title_category);
            viewHolder.subCategory = (TextView) cell.findViewById(R.id.title_subcategory);
            viewHolder.time = (TextView) cell.findViewById(R.id.title_time_label);
            viewHolder.date = (TextView) cell.findViewById(R.id.title_date_label);
            viewHolder.description = (TextView) cell.findViewById(R.id.title_description);

            viewHolder.content_username = (TextView) cell.findViewById(R.id.content_name_view);
            viewHolder.content_location = (TextView) cell.findViewById(R.id.content_address);
            viewHolder.content_date = (TextView) cell.findViewById(R.id.content_delivery_date);
            viewHolder.content_time = (TextView) cell.findViewById(R.id.content_delivery_time);
            viewHolder.content_votes = (TextView) cell.findViewById(R.id.content_vote);
            viewHolder.content_endores = (TextView) cell.findViewById(R.id.content_endores);

            viewHolder.content_reportImage = (ImageView) cell.findViewById(R.id.head_image);
            viewHolder.content_reportImage.setOnClickListener(this);
            viewHolder.content_userimage = (ImageView) cell.findViewById(R.id.content_avatar);


//            viewHolder.requestsCount = (TextView) cell.findViewById(R.id.title_requests_count);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        String[] date = new String[2];
        date = item.getDate().split(" ");
        date[1] = HoursToAP(date[1]);

/*
        String temp= TimeAgo(item.getDate());
        date=temp.split(",");
*/

        viewHolder.time.setText(date[1]);
        viewHolder.date.setText(date[0]);
        viewHolder.category.setText(item.getCategory());
        viewHolder.subCategory.setText(item.getSubCategory());
        viewHolder.votes.setText("" + item.getVote());
        viewHolder.location.setText(item.getPlace());
        viewHolder.city.setText(item.getCity());

        if (!IsEmpty(item.getDescribtion())) {

            viewHolder.description.setText(item.getDescribtion());

        }

        viewHolder.content_votes.setText("" + item.getVote());
        viewHolder.content_location.setText("" + item.getPlace());
        viewHolder.content_date.setText("" + date[1]);
        viewHolder.content_time.setText("" + date[0]);
        viewHolder.content_endores.setText(item.getVote() + " people have endorsed this");



        if (!IsEmpty(item.getReportImage())) {
            if (item.getReportImage().contains("http://")) {

                Picasso.with(getContext()).load(item.getReportImage()).into(viewHolder.content_reportImage);

            } else {
                File f = new File(item.getReportImage());
                Picasso.with(getContext()).load(f).into(viewHolder.content_reportImage);

            }

            viewHolder.content_reportImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_image);

                    ImageView image=(ImageView)dialog.findViewById(R.id.dialog_image);
                    if (item.getReportImage().contains("http://")) {

                        Picasso.with(getContext()).load(item.getReportImage()).into(image);

                    } else {
                        File f = new File(item.getReportImage());
                        Picasso.with(getContext()).load(f).into(image);

                    }
/*
                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(message);*/

/*

                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });
*/

                    dialog.show();
                }
            });

        }
        else {
            for(int i=0;i<mResourceData.category_dropdown.length;i++){
                if(mResourceData.category_dropdown[i].toString().contains(item.getCategory())){
                    viewHolder.content_reportImage.setImageResource( mResourceData.Images[i][0]);

              //      mResourceData.Images[i][0];
                }
            }

        }
        if (!IsEmpty(item.getUserImage())) {
            if (item.getUserImage().contains("http://")) {

                Picasso.with(getContext()).load(item.getUserImage()).into(viewHolder.content_userimage);

            } else {
                File f = new File(item.getReportImage());
                Picasso.with(getContext()).load(f).into(viewHolder.content_userimage);

            }
        }
        if (!IsEmpty(item.getUserName())) {
            viewHolder.content_username.setText("" + item.getUserName());

        }


        return cell;
    }

    private boolean IsEmpty(String temp) {
        if (temp != null) {
            if (!temp.isEmpty()) {
                if (!temp.contentEquals("null")) {
                    return false;
                }
            }
        }
        return true;
    }

    private String HoursToAP(String time) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("hh:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String TimeAgo(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
            Date past = format.parse(date);
            Date now = new Date();

            return String.valueOf(DateUtils.getRelativeDateTimeString(getContext(), past.getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.YEAR_IN_MILLIS, 0));

        } catch (Exception j) {
            j.printStackTrace();
        }
        return "null";
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    @Override
    public void onClick(View view) {



    }

    // View lookup cache
    private static class ViewHolder {
        TextView category;
        TextView subCategory;
        TextView description;
        TextView reporter;
        TextView votes;
        TextView location;
        TextView requestsCount;
        TextView date;
        TextView time;
        TextView city;
        TextView country;

        /*For All unfold*/
        TextView content_location;
        TextView content_username;
        TextView content_votes;
        TextView content_date;
        TextView content_time;
        ImageView content_userimage;
        ImageView content_reportImage;
        TextView content_endores;

    }

}