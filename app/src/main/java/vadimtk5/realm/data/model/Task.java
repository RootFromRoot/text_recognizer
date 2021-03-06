package vadimtk5.realm.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {
    @PrimaryKey
    public int id;

    private String name;
    private String description;
    private Date date;

    public Task() {
    }

    public Task(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate() {
      /*  SimpleDateFormat df = new SimpleDateFormat("dd MMM", Locale.getDefault());
        return df.format(getDate());*/

        return getDate().toString();
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
