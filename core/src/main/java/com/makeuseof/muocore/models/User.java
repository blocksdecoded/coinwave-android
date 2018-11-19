/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        // распаковываем объект из Parcel
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(){}

    public User(JsonNode objectNode){
        token = objectNode.get("token").asText();

        JsonNode info = objectNode.get("user");

        id = info.get("id").asText();
        login = info.get("login").asText();
        displayName = info.get("display_name").asText();
        avatar = info.get("avatar").asText();
    }

    private User(Parcel parcel) {
        id = parcel.readString();
        login = parcel.readString();
        displayName = parcel.readString();
        avatar = parcel.readString();
        token = parcel.readString();
    }

    @JsonProperty("id")
    public String id;

    @JsonProperty("login")
    public String login;

    @JsonProperty("display_name")
    public String displayName;

    @JsonProperty("avatar")
    public String avatar;

    public String token;

    public String provider;

    public boolean isValid(){
        return token != null && login != null && id != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(login);
        parcel.writeString(displayName);
        parcel.writeString(avatar);
        parcel.writeString(token);
    }
}
