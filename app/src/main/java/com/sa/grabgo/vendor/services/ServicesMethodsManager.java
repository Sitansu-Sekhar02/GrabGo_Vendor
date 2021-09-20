package com.sa.grabgo.vendor.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.services.model.AddCategoryModel;
import com.sa.grabgo.vendor.services.model.CategoryMainModel;
import com.sa.grabgo.vendor.services.model.ChangePasswordModel;
import com.sa.grabgo.vendor.services.model.ForgotPasswordModel;
import com.sa.grabgo.vendor.services.model.HomeIndexModel;
import com.sa.grabgo.vendor.services.model.HomePageMainModel;
import com.sa.grabgo.vendor.services.model.HomePageModel;
import com.sa.grabgo.vendor.services.model.IndexModel;
import com.sa.grabgo.vendor.services.model.KeyValueListModel;
import com.sa.grabgo.vendor.services.model.LanguageListModel;
import com.sa.grabgo.vendor.services.model.LatlongMainModel;
import com.sa.grabgo.vendor.services.model.LatlongModel;
import com.sa.grabgo.vendor.services.model.LoginModel;
import com.sa.grabgo.vendor.services.model.MenuModel;
import com.sa.grabgo.vendor.services.model.MenuSubMainModel;
import com.sa.grabgo.vendor.services.model.MenuTypeMainModel;
import com.sa.grabgo.vendor.services.model.NotificationListModel;
import com.sa.grabgo.vendor.services.model.ProfileMainModel;
import com.sa.grabgo.vendor.services.model.ProfileModel;
import com.sa.grabgo.vendor.services.model.PushNotificationModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.sa.grabgo.vendor.services.model.UpdateLanguageModel;
import com.sa.grabgo.vendor.services.model.UpdateOrderStatus;

import org.json.JSONObject;


public class ServicesMethodsManager {

    public static final String TAG = "ServicesMethodsMgr";
    private ServerResponseInterface mUiCallBack;

    GlobalFunctions globalFunctions;
    GlobalVariables globalVariables;

    public ServicesMethodsManager() {
        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();
    }


    private void setCallbacks(ServerResponseInterface mCallBack) {
        mUiCallBack = mCallBack;
    }

    private void postData(final Context context, final Object obj, String URL, String query, String TAG, final boolean isCookieSave) {
        /*if(obj instanceof LoginModel){
            String token =  GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
            if(token!=null){URL += "?"+"token="+ token;}
        }*/

        final VolleyServices request = new VolleyServices(context);
        request.setCallbacks(new VolleyServices.ResposeCallBack() {
            @Override
            public void OnSuccess(JSONObject arg0) {
                if (isCookieSave) {
                    GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_COOKIE, request.getCookie());
                }
                parseResponse(context, obj, arg0);
            }

            @Override
            public void OnFailure(int cause) {
                mUiCallBack.OnFailureFromServer(context.getString(cause));
            }

            @Override
            public void OnFailure(String cause) {
                mUiCallBack.OnFailureFromServer(cause);
            }
        });
        request.setBody(obj.toString());
        request.makeJsonPostRequest(context, URL, query, TAG);
    }

    private void postData(final Context context, final Object obj, String URL, String query, String TAG) {
        /*if(obj instanceof LoginModel){
            String token =  GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
            if(token!=null){URL += "?"+"token="+ token;}
        }*/

        final VolleyServices request = new VolleyServices(context);
        request.setCallbacks(new VolleyServices.ResposeCallBack() {
            @Override
            public void OnSuccess(JSONObject arg0) {
                if (obj instanceof HomeIndexModel) {
                    GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_COOKIE, request.getCookie());
                }
                parseResponse(context, obj, arg0);
            }

            @Override
            public void OnFailure(int cause) {
                mUiCallBack.OnFailureFromServer(context.getString(cause));
            }

            @Override
            public void OnFailure(String cause) {
                mUiCallBack.OnFailureFromServer(cause);
            }
        });
        request.setBody(obj.toString());
        request.makeJsonPostRequest(context, URL, query, TAG);
    }

    private void postData(final Context context, final Object obj, String URL, String TAG) {
        final VolleyServices request = new VolleyServices(context);
        request.setCallbacks(new VolleyServices.ResposeCallBack() {
            @Override
            public void OnSuccess(JSONObject arg0) {
                if (obj instanceof HomeIndexModel) {
                    GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_COOKIE, request.getCookie());
                }
                parseResponse(context, obj, arg0);
            }

            @Override
            public void OnFailure(int cause) {
                mUiCallBack.OnFailureFromServer(context.getString(cause));
            }

            @Override
            public void OnFailure(String cause) {
                mUiCallBack.OnFailureFromServer(cause);
            }
        });
        request.setBody(obj.toString());
        request.makeJsonPostRequest(context, URL, null, TAG);
    }

    private void getData(@NonNull final Context context, @NonNull final Object obj, @NonNull String URL, @Nullable String query, @Nullable String TAG) {
        //if(query!=null){if(!query.equalsIgnoreCase("")){URL += "?"+query;}}
        VolleyServices request = new VolleyServices(context);
        request.setCallbacks(new VolleyServices.ResposeCallBack() {
            @Override
            public void OnSuccess(JSONObject arg0) {
                Log.d("Response", arg0.toString());
                parseResponse(context, obj, arg0);
            }

            @Override
            public void OnFailure(int cause) {
                mUiCallBack.OnFailureFromServer(context.getString(cause));
            }

            @Override
            public void OnFailure(String cause) {
                mUiCallBack.OnFailureFromServer(cause);
            }
        });
        request.setBody(obj.toString());
        request.makeJsonGETRequest(context, URL, query, TAG);
    }

    private void parseResponse(Context context, Object obj, JSONObject resp) {
        Log.d(TAG, resp.toString());
        if (obj instanceof IndexModel) {
            IndexModel model = new IndexModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                ProfileMainModel profileModel = new ProfileMainModel();
                if (profileModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(profileModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }

        } else if (obj instanceof NotificationListModel) {
            NotificationListModel model = new NotificationListModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof KeyValueListModel) {
            KeyValueListModel model = new KeyValueListModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof HomeIndexModel) {
            HomeIndexModel model = new HomeIndexModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof LoginModel) {
            StatusMainModel model = new StatusMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        }

        else if (obj instanceof UpdateLanguageModel) {
            StatusModel model = new StatusModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof LatlongMainModel) {
            LatlongMainModel model = new LatlongMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof LatlongModel) {
            LatlongMainModel latlongModel = new LatlongMainModel();
            if (latlongModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(latlongModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof LanguageListModel) {
            LanguageListModel model = new LanguageListModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof HomePageModel) {
            HomePageModel model = new HomePageModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof HomePageMainModel) {
            HomePageMainModel model = new HomePageMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof MenuSubMainModel) {
            MenuSubMainModel model = new MenuSubMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof CategoryMainModel) {
            CategoryMainModel model = new CategoryMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        }else if (obj instanceof MenuTypeMainModel) {
            MenuTypeMainModel model = new MenuTypeMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        }  else if (obj instanceof AddCategoryModel) {
            StatusMainModel model = new StatusMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof ProfileModel) {
            ProfileMainModel profileModel = new ProfileMainModel();
            if (profileModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(profileModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof ProfileMainModel) {
            ProfileMainModel profileModel = new ProfileMainModel();
            if (profileModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(profileModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof StatusModel || obj instanceof PushNotificationModel) {
            StatusModel model = new StatusModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                ProfileMainModel profileMode = new ProfileMainModel();
                if (profileMode.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(profileMode);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof ChangePasswordModel) {
            StatusMainModel model = new StatusMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));

            }
        } else if (obj instanceof MenuModel) {
            StatusMainModel model = new StatusMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));

            }
        }  else if (obj instanceof ForgotPasswordModel) {
            StatusModel model = new StatusModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        }  else if (obj instanceof StatusMainModel) {
            StatusMainModel model = new StatusMainModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof IndexModel) {
            IndexModel model = new IndexModel();
            if (model.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(model);
            } else {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }
    }

    public void getwallet(Context context, @NonNull int type, @NonNull int index, @NonNull int size, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "type=" + type : "type=" + type;
        query = query != null ? query + "&index=" + index : "&index=" + index;
        query = query != null ? query + "&size=" + size : "size=" + size;
        // getData(context,new WalletListModel(), ServerConstants.URL_WalletHistory, query, TAG);
    }

    public void loginUser(Context context, LoginModel loginModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, loginModel, ServerConstants.URL_Login, TAG);
    }


    public void checkMobileNumber(Context context, LoginModel loginModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        String url = ServerConstants.URL_CheckMobile;
        postData(context, loginModel, url, query, TAG);
    }


    public void logout(Context context, UpdateLanguageModel updateLanguageModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, updateLanguageModel, ServerConstants.URL_LogoutUser, null, TAG);
    }

    public void insertCategory(Context context, AddCategoryModel addCategoryModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String url = ServerConstants.URL_AddCategory;
        postData(context, addCategoryModel, url, TAG);
    }

    public void getProfile(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        String url = ServerConstants.URL_GetProfile;
        getData(context, new ProfileMainModel(), url, query, TAG);
    }

    public void getStatusUpdate(Context context, String status,String order_id, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&status=" + status : "status=" + status;
        query = query != null ? query + "&order_id=" + order_id : "order_id=" + order_id;

        String URL = ServerConstants.URL_OrderStatusUpdate;
        getData(context,new UpdateOrderStatus(), URL, query, TAG);
    }

    public void getHomeDetails(Context context,String status, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&status=" + status : "status=" + status;
        getData(context, new HomePageMainModel(), ServerConstants.URL_Homepage, query, TAG);
    }

    public void updateAvailableStatus(Context context,String available, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&available=" + available : "available=" + available;
        String URL = ServerConstants.URL_UpdateAvailableStatus;
        getData(context,new StatusMainModel(), URL, query, TAG);
    }

    public void sendPushNotificationID(Context context, PushNotificationModel pushNotificationModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        //pushNotificationModel.setuii(GlobalFunctions.getUniqueID(context));
        //pushNotificationModel.setSystemInfo(GlobalFunctions.getDevice());
        postData(context, pushNotificationModel, ServerConstants.URL_PushNotification, query, TAG);
    }

    public void createMenu(Context context, MenuModel menuModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;

        postData(context, menuModel, ServerConstants.URL_CreateMenu,query, TAG);
    }

    public void forgotMyPassword(Context context, ForgotPasswordModel forgotPasswordModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        // postData(context, forgotPasswordModel, ServerConstants.URL_ForgotMyPassword, TAG);
    }


    public void updateLatLong(Context context, String latitude, String longitude, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&latitude=" + latitude : "latitude=" + latitude;
        query = query != null ? query + "&longitude=" + longitude : "longitude=" + longitude;

        String URL = ServerConstants.URL_LatlongUpdate;
        getData(context, new StatusMainModel(), URL, query, TAG);
    }

    public void getItemList(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        getData(context, new MenuTypeMainModel(), ServerConstants.URL_Menu_ItemList, query, TAG);
    }

    public void getMenuSubList(Context context, String category_id, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&category_id=" + category_id : "category_id=" + category_id;
        getData(context, new MenuSubMainModel(), ServerConstants.URL_MenuList, query, TAG);
    }

    public void getCategoryList(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        getData(context, new CategoryMainModel(), ServerConstants.URL_GetCategoryList, query, TAG);
    }


    public void changePassword(Context context, ChangePasswordModel changePasswordModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        //postData(context, changePasswordModel, ServerConstants.URL_ChangePassword, TAG);
    }

    public void forgotPassword(Context context, LoginModel loginModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        //query = query!=null? query+"&user_type="+loginModel.getUserType() : "user_type="+loginModel.getUserType();
        GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.SHARED_PREFERENCE_COOKIE);
        // String URL = ServerConstants.URL_ForgotMyPassword;
        //postData(context, loginModel, URL, query, TAG);
    }


}
