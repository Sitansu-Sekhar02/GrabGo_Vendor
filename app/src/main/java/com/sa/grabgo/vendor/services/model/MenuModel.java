package com.sa.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

public class MenuModel implements Serializable {
    private final String TAG = "MenuModel";
    private final String

            MENU_ID              = "menu_id",
            CATEGORY_ID          = "category_id",
            NAME                 = "name",
            AR_NAME              = "ar_name",
            IMAGE                = "image",
            PRICE                = "price",
            DESCRIPTION          = "description",
            AR_DESCRIPTION       = "ar_description",
            MENU_TYPE            = "menu_type_id",
            TYPE                 = "type",
            POSITION             = "position",
            STATUS               = "status";

    String
            menu_id                  = null,
            category_id              = null,
            name                     = null,
            ar_name                  = null,
            image                    = null,
            price                    = null,
            description              = null,
            ar_description           = null,
            menu_type_id             = null,
            type                     = null,
            position                 = null,
            status                   = null;

    public MenuModel(){}

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAr_description() {
        return ar_description;
    }

    public void setAr_description(String ar_description) {
        this.ar_description = ar_description;
    }

    public String getMenu_type_id() {
        return menu_type_id;
    }

    public void setMenu_type_id(String menu_type_id) {
        this.menu_type_id = menu_type_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(MENU_ID))this.menu_id = json.getString(MENU_ID);
            if(json.has(CATEGORY_ID))this.category_id = json.getString(CATEGORY_ID);
            if(json.has(NAME))this.name = json.getString(NAME);
            if(json.has(AR_NAME))this.ar_name = json.getString(AR_NAME);
            if(json.has(IMAGE))this.image = json.getString(IMAGE);
            if(json.has(PRICE))this.price = json.getString(PRICE);
            if(json.has(DESCRIPTION))this.description = json.getString(DESCRIPTION);
            if(json.has(AR_DESCRIPTION))this.ar_description = json.getString(AR_DESCRIPTION);
            if(json.has(MENU_TYPE))this.menu_type_id = json.getString(MENU_TYPE);
            if(json.has(TYPE))this.type = json.getString(TYPE);
            if(json.has(POSITION))this.position = json.getString(POSITION);
            if(json.has(STATUS))this.status = json.getString(STATUS);

            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
            return false;}
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(MENU_ID, menu_id);
            jsonMain.put(CATEGORY_ID, category_id);
            jsonMain.put(NAME, name);
            jsonMain.put(AR_NAME, ar_name);
            jsonMain.put(IMAGE, image);
            jsonMain.put(PRICE, price);
            jsonMain.put(DESCRIPTION, description);
            jsonMain.put(AR_DESCRIPTION, ar_description);
            jsonMain.put(MENU_TYPE, menu_type_id);
            jsonMain.put(TYPE, type);
            jsonMain.put(POSITION, position);
            jsonMain.put(STATUS, status);

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }
}
