package com.sa.grabgo.vendor.services;

public class ServerConstants {

    //public static final String SERVER_URL                              = "http://hardwarewagon.com/hardware_wagon_php/";
    public static final String SERVER_URL                                = "http://35.154.4.154/gograb/rest_app/v0_1/api/";

    public static final String SERVER_API_VERSION                            = "driver_v0_1";
    public static final String BASE_URL                                      = SERVER_URL+"app/"+SERVER_API_VERSION+"/api/";

    public static final String URL_GuestUserCreation                         = SERVER_URL + "guest_user_creation.php";
    public static final String URL_Login                                     = SERVER_URL + "login.php";
    public static final String URL_CheckMobile                               = SERVER_URL + "check_mobile.php";
    public static final String URL_GetCategoryList                           = SERVER_URL + "category_list.php";
    public static final String URL_AddCategory                               = SERVER_URL + "create_category.php";
    public static final String URL_CreateMenu                                = SERVER_URL + "create_menu.php";
    public static final String URL_Menu_ItemList                             = SERVER_URL + "menu_type_list.php";
    public static final String URL_MenuList                                  = SERVER_URL + "menu_list.php";
    public static final String URL_LogoutUser                                = SERVER_URL + "logout.php";
    public static final String URL_GetOrderList                              = SERVER_URL + "order_list.php";
    public static final String URL_OrderStatusUpdate                         = SERVER_URL + "update_status.php";
    public static final String URL_LatlongUpdate                             = SERVER_URL + "update_lat_long.php";
    public static final String URL_Homepage                                  = SERVER_URL + "home_page.php";
    public static final String URL_EditMenu                                  = SERVER_URL + "edit_menu_detail.php";
    public static final String URL_UpdateAvailableStatus                     = SERVER_URL + "update_available.php";


    public static final String URL_UpdateProfile                             = SERVER_URL + "get_profile.php";
    public static final String URL_GetProfile                                = SERVER_URL + "get_profile.php";


    public static final String URL_GetNotifications                         = SERVER_URL + "get_notifications.php";
    public static final String URL_ForgotPassword                           = SERVER_URL + "forgot_password.php";
    public static final String URL_PushNotification                         = SERVER_URL + "push.php";


    public static final String URL_AboutUs              = "http://hardwarewagon.com/hardware_wagon_ui/about-nomenu.html";
    public static final String URL_Terms                = "http://hardwarewagon.com/hardware_wagon_ui/terms-menu.html";
    public static final String URL_Privacy              = "http://hardwarewagon.com/hardware_wagon_ui/privacy-nomenu.html";


}
