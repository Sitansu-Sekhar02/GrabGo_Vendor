package com.sa.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

public class ProfileModel implements Serializable {
    private final String TAG = "ProfileModel";
    private final String
            ID                  = "id",
            USER_ID             ="user_id",
            FIRST_NAME          = "first_name",
            FULL_NAME           = "full_name",
            LAST_NAME           = "last_name",
            IS_OTP_VERIFIED     = "is_otp_verified",
            EMAIL               = "email_id",
            PHONE               = "mobile_number",
            ADDRESS             = "address",
            IMAGE               = "image",
            GENDER               = "gender",
            ORDER_COUNT         = "order_count",
            COUNTRY_CODE        = "country_code",
            IS_AVAILABLE        = "is_available",
            WALLET              ="wallet",
            VEHICLE_NUMBER      ="vehicle_number";


    String
            id                      = null,
            user_id                 =null,
            fullname                = null,
            tinNumber               = null,
            orderCount              = null,
            address                 = null,
            panNumber               = null,
            agencyName              = null,
            firstName               = null,
            lastName                = null,
            email                   = null,
            phone                   = null,
            image                   = null,
            gender                  = null,
            country_code            =null,
            is_available            =null,
            wallet                  =null,
            vehicle_number          =null;






    boolean otp_verified = true;


    public ProfileModel() {
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }



    public boolean isOtp_verified() {
        return otp_verified;
    }

    public void setOtp_verified(boolean otp_verified) {
        this.otp_verified = otp_verified;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }




    public boolean toObject(String jsonObject) {
        try {
            JSONObject json = new JSONObject(jsonObject);
            if (json.has(ID)) {
                id = json.getString(ID);
            }
            if (json.has(USER_ID)) {
                user_id = json.getString(USER_ID);
            }

            if (json.has(FIRST_NAME)) {
                firstName = json.getString(FIRST_NAME);
            }
            if (json.has(LAST_NAME)) {
                lastName = json.getString(LAST_NAME);
            }
            if (json.has(FULL_NAME)) {
                fullname = json.getString(FULL_NAME);
            }
            if (json.has(EMAIL)) {
                email = json.getString(EMAIL);
            }
            if (json.has(PHONE)) {
                phone = json.getString(PHONE);
            }
            if (json.has(ADDRESS)) {
                address = json.getString(ADDRESS);
            }
            if (json.has(IMAGE)) {
                image = json.getString(IMAGE);
            }
            if (json.has(GENDER)) {
                gender = json.getString(GENDER);
            }
            if (json.has(IS_OTP_VERIFIED)) {
                otp_verified = json.getBoolean(IS_OTP_VERIFIED);
            }
            if (json.has(ORDER_COUNT)) {
                orderCount = json.getString(ORDER_COUNT);
            }

            if (json.has(COUNTRY_CODE)) {
                country_code = json.getString(COUNTRY_CODE);
            }  if (json.has(WALLET)) {
                wallet = json.getString(WALLET);
            }
            if (json.has(VEHICLE_NUMBER)) {
                vehicle_number = json.getString(VEHICLE_NUMBER);
            }
            if (json.has(IS_AVAILABLE)) {
                is_available = json.getString(IS_AVAILABLE);
            }

            return true;
        } catch (Exception ex) {
            Log.d(TAG, "Json Exception : " + ex);
        }
        return false;
    }

    @Override
    public String toString() {
        String returnString = null;
        try {
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(ID, id);
            jsonMain.put(USER_ID,user_id);
            jsonMain.put(FIRST_NAME, firstName);
            jsonMain.put(LAST_NAME, lastName);
            jsonMain.put(FULL_NAME, fullname);
            jsonMain.put(EMAIL, email);
            jsonMain.put(PHONE, phone);
            jsonMain.put(ADDRESS, address);
            jsonMain.put(IS_OTP_VERIFIED, isOtp_verified());
            jsonMain.put(IMAGE, image);
            jsonMain.put(GENDER, gender);
            jsonMain.put(ORDER_COUNT, orderCount);
            jsonMain.put(COUNTRY_CODE, country_code);
            jsonMain.put(WALLET, wallet);
            jsonMain.put(VEHICLE_NUMBER, vehicle_number);
            jsonMain.put(IS_AVAILABLE, is_available);
            returnString = jsonMain.toString();
        } catch (Exception ex) {
            Log.d(TAG, " To String Exception : " + ex);
        }
        return returnString;
    }
}
