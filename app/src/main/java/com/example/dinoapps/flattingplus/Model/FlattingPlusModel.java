
package com.example.dinoapps.flattingplus.Model;

//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

//@Generated("org.jsonschema2pojo")
public class FlattingPlusModel {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("flatgroup")
    @Expose
    private String flatgroup;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pic")
    @Expose
    private String pic;

    /**
     * No args constructor for use in serialization
     *
     */
    public FlattingPlusModel() {
    }

    /**
     *
     * @param id
     * @param flatgroup
     * @param email
     * @param name
     * @param pic
     */
    public FlattingPlusModel(String email, String name, String flatgroup, Integer id, String pic) {
        this.email = email;
        this.name = name;
        this.flatgroup = flatgroup;
        this.id = id;
        this.pic = pic;
    }

    /**
     *
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public FlattingPlusModel withEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public FlattingPlusModel withName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     *     The flatgroup
     */
    public String getFlatgroup() {
        return flatgroup;
    }

    /**
     *
     * @param flatgroup
     *     The flatgroup
     */
    public void setFlatgroup(String flatgroup) {
        this.flatgroup = flatgroup;
    }

    public FlattingPlusModel withFlatgroup(String flatgroup) {
        this.flatgroup = flatgroup;
        return this;
    }

    /**
     *
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public FlattingPlusModel withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     *
     * @return
     *     The pic
     */
    public String getPic() {
        return pic;
    }

    /**
     *
     * @param pic
     *     The pic
     */
    public void setPic(String pic) {
        this.pic = pic;
    }

    public FlattingPlusModel withPic(String pic) {
        this.pic = pic;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
