package app.vreport.com.Model;


import java.util.ArrayList;

/**
 * Simple POJO model for example
 */
public class Item {

    private String price;
    private String pledgePrice;
    private String fromAddress;
    private String toAddress;
    private String category;
    private String subCategory;
    private String description;
    private String reporter;
    private int upVotes;
    private String location;
    private String city;
    private String country;
    private int requestsCount;
    private String date;
    private String time;

    public Item() {
    }

    public Item(String price, String pledgePrice, String fromAddress, String toAddress, int requestsCount, String date, String time) {
        this.price = price;
        this.pledgePrice = pledgePrice;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.requestsCount = requestsCount;
        this.date = date;
        this.time = time;
    }

    public Item(int requestsCount, String date, String time, String category, String subCategory, String reporter, String description, int upVotes, String location, String city, String country) {
        this.requestsCount = requestsCount;
        this.date = date;
        this.time = time;
        this.reporter = reporter;
        this.category = category;
        this.subCategory = subCategory;
        this.description = description;
        this.upVotes = upVotes;
        this.location = location;
        this.city = city;
        this.country = country;
    }

    public static ArrayList<Item> getNewList() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(1, "TODAY", "05:10 PM", "Accident", "Road Accident", "Farjad", "A guy was driving so fast he crashed into the wall injuring 3 more.", 3, "Shahrah-e-Faisal", "Karachi", "Pakistan"));
        items.add(new Item(1, "TODAY", "05:10 PM", "Accident", "Road Accident", "Farjad", "A guy was driving so fast he crashed into the wall injuring 3 more.", 3, "Shahrah-e-Faisal", "Karachi", "Pakistan"));
        items.add(new Item(1, "TODAY", "05:10 PM", "Accident", "Road Accident", "Farjad", "A guy was driving so fast he crashed into the wall injuring 3 more.", 3, "Shahrah-e-Faisal", "Karachi", "Pakistan"));
        items.add(new Item(1, "TODAY", "05:10 PM", "Accident", "Road Accident", "Farjad", "A guy was driving so fast he crashed into the wall injuring 3 more.", 3, "Shahrah-e-Faisal", "Karachi", "Pakistan"));
        items.add(new Item(1, "TODAY", "05:10 PM", "Accident", "Road Accident", "Farjad", "A guy was driving so fast he crashed into the wall injuring 3 more.", 3, "Shahrah-e-Faisal", "Karachi", "Pakistan"));
        items.add(new Item(1, "TODAY", "05:10 PM", "Accident", "Road Accident", "Farjad", "A guy was driving so fast he crashed into the wall injuring 3 more.", 3, "Shahrah-e-Faisal", "Karachi", "Pakistan"));
        return items;

    }

    public int getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(int requestsCount) {
        this.requestsCount = requestsCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory() {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescription(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (requestsCount != item.requestsCount) return false;
        if (price != null ? !price.equals(item.price) : item.price != null) return false;
        if (pledgePrice != null ? !pledgePrice.equals(item.pledgePrice) : item.pledgePrice != null)
            return false;
        if (fromAddress != null ? !fromAddress.equals(item.fromAddress) : item.fromAddress != null)
            return false;
        if (toAddress != null ? !toAddress.equals(item.toAddress) : item.toAddress != null)
            return false;
        if (date != null ? !date.equals(item.date) : item.date != null) return false;
        return !(time != null ? !time.equals(item.time) : item.time != null);

    }

    /**
     * @return List of elements prepared for tests
     */
    public static ArrayList<Item> getTestingList() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("$14", "$270", "W 79th St, NY, 10024", "W 139th St, NY, 10030", 3, "TODAY", "05:10 PM"));
        items.add(new Item("$23", "$116", "W 36th St, NY, 10015", "W 114th St, NY, 10037", 10, "TODAY", "11:10 AM"));
        items.add(new Item("$63", "$350", "W 36th St, NY, 10029", "56th Ave, NY, 10041", 0, "TODAY", "07:11 PM"));
        items.add(new Item("$19", "$150", "12th Ave, NY, 10012", "W 57th St, NY, 10048", 8, "TODAY", "4:15 AM"));
        items.add(new Item("$5", "$300", "56th Ave, NY, 10041", "W 36th St, NY, 10029", 0, "TODAY", "06:15 PM"));
        return items;

    }


    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}