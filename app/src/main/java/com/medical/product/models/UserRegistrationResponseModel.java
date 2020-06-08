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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "user_data",
        "message"
})
public class UserRegistrationResponseModel implements Parcelable {

    @JsonProperty("status")
    private boolean status;
    @JsonProperty("user_data")
    private List<UserModel> userData = new ArrayList<UserModel>();
    @JsonProperty("message")
    private String message;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<UserRegistrationResponseModel> CREATOR = new Creator<UserRegistrationResponseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserRegistrationResponseModel createFromParcel(Parcel in) {
            return new UserRegistrationResponseModel(in);
        }

        public UserRegistrationResponseModel[] newArray(int size) {
            return (new UserRegistrationResponseModel[size]);
        }

    };

    protected UserRegistrationResponseModel(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        in.readList(this.userData, (UserModel.class.getClassLoader()));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public UserRegistrationResponseModel() {
    }

    @JsonProperty("status")
    public boolean isStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(boolean status) {
        this.status = status;
    }

    @JsonProperty("user_data")
    public List<UserModel> getUserData() {
        return userData;
    }

    @JsonProperty("user_data")
    public void setUserData(List<UserModel> userData) {
        this.userData = userData;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
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
        return new ToStringBuilder(this).append("status", status).append("message", message).append("userData", userData).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(userData).append(message).append(additionalProperties).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserRegistrationResponseModel)) {
            return false;
        }
        UserRegistrationResponseModel rhs = ((UserRegistrationResponseModel) other);
        return new EqualsBuilder().append(userData, rhs.userData).append(message, rhs.message).append(additionalProperties, rhs.additionalProperties).append(status, rhs.status).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(userData);
        dest.writeValue(message);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}