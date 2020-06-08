package com.medical.product.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "user_id",
        "name",
        "email",
        "phone",
        "image",
        "password",
        "created",
        "address",
        "dob",
        "b_group",
        "mar_status",
        "height",
        "weight",
        "em_co_nu",
        "em_co_na",
        "city",
        "ad_cart",
        "ho_m_med",
        "man_em_con",
        "ren_cou_cod",
        "not_sound",
        "ref_code",
        "search_address",
        "lati",
        "longi",
        "gender",
        "token",
        "auth_key",
        "status",
        "time_update"
})
public class UserModel implements Parcelable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("image")
    private String image;
    @JsonProperty("password")
    private String password;
    @JsonProperty("created")
    private String created;
    @JsonProperty("address")
    private String address;
    @JsonProperty("dob")
    private String dob;
    @JsonProperty("b_group")
    private String bGroup;
    @JsonProperty("mar_status")
    private String marStatus;
    @JsonProperty("height")
    private String height;
    @JsonProperty("weight")
    private String weight;
    @JsonProperty("em_co_nu")
    private String emCoNu;
    @JsonProperty("em_co_na")
    private String emCoNa;
    @JsonProperty("city")
    private String city;
    @JsonProperty("ad_cart")
    private String adCart;
    @JsonProperty("ho_m_med")
    private String hoMMed;
    @JsonProperty("man_em_con")
    private String manEmCon;
    @JsonProperty("ren_cou_cod")
    private String renCouCod;
    @JsonProperty("not_sound")
    private String notSound;
    @JsonProperty("ref_code")
    private String refCode;
    @JsonProperty("search_address")
    private String searchAddress;
    @JsonProperty("lati")
    private String lati;
    @JsonProperty("longi")
    private String longi;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("token")
    private String token;
    @JsonProperty("auth_key")
    private String authKey;
    @JsonProperty("status")
    private String status;
    @JsonProperty("time_update")
    private String timeUpdate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<UserModel> CREATOR = new Creator<UserModel>() {


        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return (new UserModel[size]);
        }

    };

    protected UserModel(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.created = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.dob = ((String) in.readValue((String.class.getClassLoader())));
        this.bGroup = ((String) in.readValue((String.class.getClassLoader())));
        this.marStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((String) in.readValue((String.class.getClassLoader())));
        this.weight = ((String) in.readValue((String.class.getClassLoader())));
        this.emCoNu = ((String) in.readValue((String.class.getClassLoader())));
        this.emCoNa = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.adCart = ((String) in.readValue((String.class.getClassLoader())));
        this.hoMMed = ((String) in.readValue((String.class.getClassLoader())));
        this.manEmCon = ((String) in.readValue((String.class.getClassLoader())));
        this.renCouCod = ((String) in.readValue((String.class.getClassLoader())));
        this.notSound = ((String) in.readValue((String.class.getClassLoader())));
        this.refCode = ((String) in.readValue((String.class.getClassLoader())));
        this.searchAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.lati = ((String) in.readValue((String.class.getClassLoader())));
        this.longi = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.token = ((String) in.readValue((String.class.getClassLoader())));
        this.authKey = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.timeUpdate = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public UserModel() {
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("dob")
    public String getDob() {
        return dob;
    }

    @JsonProperty("dob")
    public void setDob(String dob) {
        this.dob = dob;
    }

    @JsonProperty("b_group")
    public String getBGroup() {
        return bGroup;
    }

    @JsonProperty("b_group")
    public void setBGroup(String bGroup) {
        this.bGroup = bGroup;
    }

    @JsonProperty("mar_status")
    public String getMarStatus() {
        return marStatus;
    }

    @JsonProperty("mar_status")
    public void setMarStatus(String marStatus) {
        this.marStatus = marStatus;
    }

    @JsonProperty("height")
    public String getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(String height) {
        this.height = height;
    }

    @JsonProperty("weight")
    public String getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(String weight) {
        this.weight = weight;
    }

    @JsonProperty("em_co_nu")
    public String getEmCoNu() {
        return emCoNu;
    }

    @JsonProperty("em_co_nu")
    public void setEmCoNu(String emCoNu) {
        this.emCoNu = emCoNu;
    }

    @JsonProperty("em_co_na")
    public String getEmCoNa() {
        return emCoNa;
    }

    @JsonProperty("em_co_na")
    public void setEmCoNa(String emCoNa) {
        this.emCoNa = emCoNa;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("ad_cart")
    public String getAdCart() {
        return adCart;
    }

    @JsonProperty("ad_cart")
    public void setAdCart(String adCart) {
        this.adCart = adCart;
    }

    @JsonProperty("ho_m_med")
    public String getHoMMed() {
        return hoMMed;
    }

    @JsonProperty("ho_m_med")
    public void setHoMMed(String hoMMed) {
        this.hoMMed = hoMMed;
    }

    @JsonProperty("man_em_con")
    public String getManEmCon() {
        return manEmCon;
    }

    @JsonProperty("man_em_con")
    public void setManEmCon(String manEmCon) {
        this.manEmCon = manEmCon;
    }

    @JsonProperty("ren_cou_cod")
    public String getRenCouCod() {
        return renCouCod;
    }

    @JsonProperty("ren_cou_cod")
    public void setRenCouCod(String renCouCod) {
        this.renCouCod = renCouCod;
    }

    @JsonProperty("not_sound")
    public String getNotSound() {
        return notSound;
    }

    @JsonProperty("not_sound")
    public void setNotSound(String notSound) {
        this.notSound = notSound;
    }

    @JsonProperty("ref_code")
    public String getRefCode() {
        return refCode;
    }

    @JsonProperty("ref_code")
    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    @JsonProperty("search_address")
    public String getSearchAddress() {
        return searchAddress;
    }

    @JsonProperty("search_address")
    public void setSearchAddress(String searchAddress) {
        this.searchAddress = searchAddress;
    }

    @JsonProperty("lati")
    public String getLati() {
        return lati;
    }

    @JsonProperty("lati")
    public void setLati(String lati) {
        this.lati = lati;
    }

    @JsonProperty("longi")
    public String getLongi() {
        return longi;
    }

    @JsonProperty("longi")
    public void setLongi(String longi) {
        this.longi = longi;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("auth_key")
    public String getAuthKey() {
        return authKey;
    }

    @JsonProperty("auth_key")
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("time_update")
    public String getTimeUpdate() {
        return timeUpdate;
    }

    @JsonProperty("time_update")
    public void setTimeUpdate(String timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("name", name).append("email", email).append("phone", phone).append("image", image).append("password", password).append("created", created).append("address", address).append("dob", dob).append("bGroup", bGroup).append("marStatus", marStatus).append("height", height).append("weight", weight).append("emCoNu", emCoNu).append("emCoNa", emCoNa).append("city", city).append("adCart", adCart).append("hoMMed", hoMMed).append("manEmCon", manEmCon).append("renCouCod", renCouCod).append("notSound", notSound).append("refCode", refCode).append("searchAddress", searchAddress).append("lati", lati).append("longi", longi).append("gender", gender).append("token", token).append("authKey", authKey).append("status", status).append("timeUpdate", timeUpdate).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(adCart).append(gender).append(city).append(timeUpdate).append(longi).append(renCouCod).append(manEmCon).append(password).append(lati).append(id).append(email).append(marStatus).append(height).append(image).append(authKey).append(emCoNu).append(address).append(searchAddress).append(bGroup).append(created).append(weight).append(userId).append(token).append(notSound).append(phone).append(dob).append(name).append(additionalProperties).append(refCode).append(emCoNa).append(hoMMed).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserModel)) {
            return false;
        }
        UserModel rhs = ((UserModel) other);
        return new EqualsBuilder().append(adCart, rhs.adCart).append(gender, rhs.gender).append(city, rhs.city).append(timeUpdate, rhs.timeUpdate).append(longi, rhs.longi).append(renCouCod, rhs.renCouCod).append(manEmCon, rhs.manEmCon).append(password, rhs.password).append(lati, rhs.lati).append(id, rhs.id).append(email, rhs.email).append(marStatus, rhs.marStatus).append(height, rhs.height).append(image, rhs.image).append(authKey, rhs.authKey).append(emCoNu, rhs.emCoNu).append(address, rhs.address).append(searchAddress, rhs.searchAddress).append(bGroup, rhs.bGroup).append(created, rhs.created).append(weight, rhs.weight).append(userId, rhs.userId).append(token, rhs.token).append(notSound, rhs.notSound).append(phone, rhs.phone).append(dob, rhs.dob).append(name, rhs.name).append(additionalProperties, rhs.additionalProperties).append(refCode, rhs.refCode).append(emCoNa, rhs.emCoNa).append(hoMMed, rhs.hoMMed).append(status, rhs.status).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(name);
        dest.writeValue(email);
        dest.writeValue(phone);
        dest.writeValue(image);
        dest.writeValue(password);
        dest.writeValue(created);
        dest.writeValue(address);
        dest.writeValue(dob);
        dest.writeValue(bGroup);
        dest.writeValue(marStatus);
        dest.writeValue(height);
        dest.writeValue(weight);
        dest.writeValue(emCoNu);
        dest.writeValue(emCoNa);
        dest.writeValue(city);
        dest.writeValue(adCart);
        dest.writeValue(hoMMed);
        dest.writeValue(manEmCon);
        dest.writeValue(renCouCod);
        dest.writeValue(notSound);
        dest.writeValue(refCode);
        dest.writeValue(searchAddress);
        dest.writeValue(lati);
        dest.writeValue(longi);
        dest.writeValue(gender);
        dest.writeValue(token);
        dest.writeValue(authKey);
        dest.writeValue(status);
        dest.writeValue(timeUpdate);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}