package com.sa.grabgo.vendor.services;

public class ServerConstants {

    //public static final String SERVER_URL                              = "http://hardwarewagon.com/hardware_wagon_php/";
    public static final String SERVER_URL                                = "http://35.154.4.154/gograb/rest_app/v0_1/api/";

    public static final String SERVER_API_VERSION                           = "driver_v0_1";
    public static final String BASE_URL                                     = SERVER_URL+"app/"+SERVER_API_VERSION+"/api/";

    public static final String URL_GuestUserCreation                         = SERVER_URL + "guest_user_creation.php";
    public static final String URL_GetIndex                                  = SERVER_URL + "get_index.php";
    public static final String URL_SendOTP                                   = SERVER_URL + "send_otp.php";
    public static final String URL_VerifyOTP                                 = SERVER_URL + "verify_otp.php";
    public static final String URL_Login                                     = SERVER_URL + "login.php";
    public static final String URL_RegisterUser                              = SERVER_URL + "registration.php";
    public static final String URL_CheckMobile                               = SERVER_URL + "check_mobile.php";
    public static final String URL_GetCategoryList                           = SERVER_URL + "category_list.php";
    public static final String URL_GetConfirmedAppointmentsList              = SERVER_URL + "booking_display.php";
    public static final String URL_BookingStatusUpdate                       = SERVER_URL + "booking_status_update.php";
    public static final String URL_LogoutUser                                = SERVER_URL + "logout.php";
    public static final String URL_GetOrderList                              = SERVER_URL + "order_list.php";
    public static final String URL_OrderStatusUpdate                         = SERVER_URL + "order_status_update.php";
    public static final String URL_LatlongUpdate                             = SERVER_URL + "update_lat_long.php";
    public static final String URL_Homepage                                  = SERVER_URL + "home_page.php";
    public static final String URL_Vendorlist                                = SERVER_URL + "vendor_store_detail.php";
    public static final String URL_Vendor_Store_list                         = SERVER_URL + "vendor_store_list.php";
    public static final String URL_Reviewlist                                = SERVER_URL + "review_list.php";
    public static final String URL_InsertReview                              = SERVER_URL + "insert_review.php";
    public static final String URL_Wishlist                                  = SERVER_URL + "wishlist.php";
    public static final String URL_GetAccountList                            = SERVER_URL + "user_account_list.php";


    public static final String URL_UpdateProfile                             = SERVER_URL + "get_profile.php";
    public static final String URL_GetProfile                                = SERVER_URL + "get_profile.php";




    public static final String URL_GetNotifications     = BASE_URL + "get_notifications.php";
    public static final String URL_ForgotPassword       = BASE_URL + "forgot_password.php";
    public static final String URL_PushNotification     = SERVER_URL + "push.php";


    public static final String URL_AboutUs              = "http://hardwarewagon.com/hardware_wagon_ui/about-nomenu.html";
    public static final String URL_Terms                = "http://hardwarewagon.com/hardware_wagon_ui/terms-menu.html";
    public static final String URL_Privacy              = "http://hardwarewagon.com/hardware_wagon_ui/privacy-nomenu.html";


}
