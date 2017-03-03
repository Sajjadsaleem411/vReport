package app.vreport.com.Model;

/**
 * Created by Sajjad Saleem on 1/3/2017.
 */
public class Report {
    public Double Latitude, Longitude;
    public String describtion;
    public String ReportImage;
    public String date;
    public String SubCategory;

    public String place;
    public String City;
    public int vote;
    public String category;

    public String userName;
    public String userImage;

    public String getSubCategory1() {
        return SubCategory1;
    }

    public void setSubCategory1(String subCategory1) {
        SubCategory1 = subCategory1;
    }

    public String SubCategory1;

    public Report() {

    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public String getReportImage() {
        return ReportImage;
    }

    public void setReportImage(String reportImage) {
        ReportImage = reportImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
