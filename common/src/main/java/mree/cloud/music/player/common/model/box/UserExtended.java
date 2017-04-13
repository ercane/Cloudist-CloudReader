package mree.cloud.music.player.common.model.box;

/**
 * Created by eercan on 21.02.2017.
 */

public class UserExtended {
    private String type;
    private String id;
    private String name;
    private String login;
    private String created_at;
    private String modified_at;
    private String language;
    private long space_amount;
    private int space_used;
    private int max_upload_size;
    private String status;
    private String job_title;
    private String phone;
    private String address;
    private String avatar_url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getSpace_amount() {
        return space_amount;
    }

    public void setSpace_amount(long space_amount) {
        this.space_amount = space_amount;
    }

    public int getSpace_used() {
        return space_used;
    }

    public void setSpace_used(int space_used) {
        this.space_used = space_used;
    }

    public int getMax_upload_size() {
        return max_upload_size;
    }

    public void setMax_upload_size(int max_upload_size) {
        this.max_upload_size = max_upload_size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
