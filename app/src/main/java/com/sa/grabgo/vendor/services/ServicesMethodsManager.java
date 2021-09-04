package com.sa.grabgo.vendor.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
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
import com.sa.grabgo.vendor.services.model.NotificationListModel;
import com.sa.grabgo.vendor.services.model.ProfileMainModel;
import com.sa.grabgo.vendor.services.model.ProfileModel;
import com.sa.grabgo.vendor.services.model.PushNotificationModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.sa.grabgo.vendor.services.model.TokenPostModel;
import com.sa.grabgo.vendor.services.model.UpdateLanguageModel;

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
        }  else if (obj instanceof ProfileModel) {
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

    public void getMaterialList(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        //getData(context, new KeyValueMainModel(), ServerConstants.URL_GetMaterialList, query, TAG);
    }

    public void getSubCatList(Context context, String catId, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&id_parent=" + catId : "id_parent=" + catId;
        // getData(context, new SubCatMainModel(), ServerConstants.URL_GetSubCategoryList, query, TAG);
    }

    public void getGuestUserCreation(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        HomeIndexModel model = new HomeIndexModel();
        model.setSystem_info(globalFunctions.getDevice());
        model.setUid(globalFunctions.getUniqueID(context));
        postData(context, model, ServerConstants.URL_GuestUserCreation, null, TAG, true);
    }

    public void loginUser(Context context, LoginModel loginModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, loginModel, ServerConstants.URL_LoginUser, TAG);
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

    public void updateUser(Context context, ProfileModel profileModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String url = ServerConstants.URL_UpdateProfile;
        postData(context, profileModel, url, TAG);
    }




    public void getProfile(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        String url = ServerConstants.URL_GetProfile;
        getData(context, new ProfileMainModel(), url, query, TAG);
    }


    public void sendPushNotificationID(Context context, PushNotificationModel pushNotificationModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        //pushNotificationModel.setuii(GlobalFunctions.getUniqueID(context));
        //pushNotificationModel.setSystemInfo(GlobalFunctions.getDevice());
        postData(context, pushNotificationModel, ServerConstants.URL_PushNotification, query, TAG);
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

    public void getHomeDetails(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        // query = query != null ? query + "&id_material_type=" + id : "id_material_type=" + id;
        getData(context, new HomePageMainModel(), ServerConstants.URL_Homepage, query, TAG);
    }


    public void getMenu(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        // getData(context, new UtilityMainModel(), ServerConstants.URL_GetMenu, query, TAG);
    }





    public void getProductDetails(Context context, String productId, String variationId, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&id_product=" + productId : "id_product=" + productId;
        query = query != null ? query + "&id_variation=" + variationId : "id_variation=" + variationId;
        //getData(context, new ProductDetailMainModel(), ServerConstants.URL_ProductDetail, query, TAG);
    }





    public void getVendorRating(Context context, String vendorId, String size, String index, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&id_vendor=" + vendorId : "id_vendor=" + vendorId;
        query = query != null ? query + "&index=" + index : "index=" + index;
        query = query != null ? query + "&size=" + size : "size=" + size;
        // getData(context, new CommentListMainModel(), ServerConstants.URL_VendorRating, query, TAG);
    }




  public  void getRelatedProducts(Context context, String productId, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&product_id=" + productId : "product_id=" + productId;
        // getData(context, new RelatedProductsListModel(), ServerConstants.URL_RelatedProduct, query, TAG);
    }




    public void getUserAddress(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
//        query = query != null ? query + "&type=" + "1" : "type=" + "1";
        //   String url = ServerConstants.URL_GetUserAddress;
        //  getData(context, new UserAddressListModel(), url, query, TAG);
    }

    public void getDefaultUserAddress(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&type=" + "1" : "type=" + "1";
        //  String url = ServerConstants.URL_GetUserAddress;
        // getData(context, new UserAddressListModel(), url, query, TAG);
    }





    public void getReOrder(Context context, String orderId, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String query = null;
        query = query != null ? query + "&order_id=" + orderId : "order_id=" + orderId;
       // getData(context, new StatusMainModel(), ServerConstants.URL_ReOrder, query, TAG);
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
