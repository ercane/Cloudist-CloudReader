package mree.cloud.music.player.common.model.box;


/**
 * Created by eercan on 20.02.2017.
 */

public class File {
    private User owned_by;

    private User created_by;

    private String etag;

    private Version file_version;

    private User parent;

    private String sha1;

    private String type;

    private String item_status;

    private String sequence_id;

    private SharedLink shared_link;

    private String size;

    private String id;

    private User modified_by;

    private String description;

    private String name;

    private String created_at;

    private PathCollection path_collection;

    private String modified_at;

    public User getOwned_by() {
        return owned_by;
    }

    public void setOwned_by(User owned_by) {
        this.owned_by = owned_by;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Version getFile_version() {
        return file_version;
    }

    public void setFile_version(Version file_version) {
        this.file_version = file_version;
    }

    public User getParent() {
        return parent;
    }

    public void setParent(User parent) {
        this.parent = parent;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getSequence_id() {
        return sequence_id;
    }

    public void setSequence_id(String sequence_id) {
        this.sequence_id = sequence_id;
    }

    public SharedLink getShared_link() {
        return shared_link;
    }

    public void setShared_link(SharedLink shared_link) {
        this.shared_link = shared_link;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getModified_by() {
        return modified_by;
    }

    public void setModified_by(User modified_by) {
        this.modified_by = modified_by;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public PathCollection getPath_collection() {
        return path_collection;
    }

    public void setPath_collection(PathCollection path_collection) {
        this.path_collection = path_collection;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }
}
