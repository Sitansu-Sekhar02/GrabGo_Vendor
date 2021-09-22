package com.sa.grabgo.vendor.menu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.image_picker.ImagePickerActivity;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.CategoryListModel;
import com.sa.grabgo.vendor.services.model.CategoryMainModel;
import com.sa.grabgo.vendor.services.model.CategoryModel;
import com.sa.grabgo.vendor.services.model.MenuModel;
import com.sa.grabgo.vendor.services.model.MenuSubModel;
import com.sa.grabgo.vendor.services.model.MenuTypeListModel;
import com.sa.grabgo.vendor.services.model.MenuTypeMainModel;
import com.sa.grabgo.vendor.services.model.MenuTypeModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.sa.grabgo.vendor.upload.UploadImage;
import com.sa.grabgo.vendor.upload.UploadListener;
import com.sa.grabgo.vendor.view.AlertDialog;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements UploadListener {

    private static final String TAG = "AddItemActivity";
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static final int REQUEST_IMAGE = 100;
    public static final String
            BUNDLE_EDIT_MENU_MODEL = "Bundle_Edit_Menu_Model";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    Context context;
    private static Activity activity;
    static Window mainWindow = null;

    View mainView;

    static Toolbar toolbar;
    static ActionBar actionBar;
    static String mTitle;
    static int mResourceID;
    static int titleResourseID;
    static TextView toolbar_title;
    static ImageView toolbar_logo, tool_bar_back_icon;

    private EditText etv_item_name, etv_item_ar_name, etv_description, etv_description_ar, etv_item_price;
    private ImageView iv_item;
    private String iv_item_image;
    private Button btn_saveItem;
    private TextView tv_select_status, tv_menuItems, tv_addImage, tv_selectMenuType;
    private RadioButton rd_type_veg, rd_type_nonveg;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    int selectedId;

    String selectedRadioType = "1";
    String selectedCategoryPosition = "0";

    String menuType_id = null;
    String category_id = null;


    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    private LayoutInflater layoutInflater;
    MenuModel menuModel = null;

    CategoryListModel categoryListModel = null;
    MenuTypeListModel menuTypeListModel = null;
    MenuSubModel menuSubModel = null;


    String
            selectStatus = "1";

    String menu_id = null;

    List<String> profileImageList;
    List<Uri> uriProfileImageList;
    List<String> downloadProfileImageList;
    String imagePath = "";

    public static Intent newInstance(Activity activity, MenuSubModel model) {
        Intent intent = new Intent(activity, AddItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_EDIT_MENU_MODEL, model);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        context = this;
        activity = this;
        mainWindow = getWindow();


        this.layoutInflater = activity.getLayoutInflater();

        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tool_bar_back_icon = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        tool_bar_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_select_status = findViewById(R.id.tv_select_status);
        tv_menuItems = findViewById(R.id.tv_menuItems);
        etv_item_name = findViewById(R.id.etv_item_name);
        etv_item_ar_name = findViewById(R.id.etv_item_ar_name);
        etv_description = findViewById(R.id.etv_description);
        etv_description_ar = findViewById(R.id.etv_description_ar);
        etv_item_price = findViewById(R.id.etv_item_price);
        tv_addImage = findViewById(R.id.tv_addImage);
        iv_item = findViewById(R.id.iv_item);
        btn_saveItem = findViewById(R.id.btn_saveItem);
        rd_type_veg = findViewById(R.id.rd_type_veg);
        rd_type_nonveg = findViewById(R.id.rd_type_nonveg);
        radioGroup = findViewById(R.id.radioGroup);
        tv_selectMenuType = findViewById(R.id.tv_selectMenuType);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorStatusBar));
        }

        mainView = toolbar;

        if (getIntent().hasExtra(BUNDLE_EDIT_MENU_MODEL)) {

            menuSubModel = (MenuSubModel) getIntent().getSerializableExtra(BUNDLE_EDIT_MENU_MODEL);
        } else {
            menuSubModel = null;
        }

        setThisPage(menuSubModel);

        downloadProfileImageList = new ArrayList<>();
        uriProfileImageList = new ArrayList<>();
        profileImageList = new ArrayList<>();

        profileImageList.clear();
        uriProfileImageList.clear();
        downloadProfileImageList.clear();


        // get selected radio button from radioGroup
        selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioId) {
                switch (radioId) {
                    case R.id.rd_type_veg:
                        if (rd_type_veg.isChecked()) {
                            selectedRadioType = "1";
                        }
                        break;

                    case R.id.rd_type_nonveg:
                        if (rd_type_nonveg.isChecked()) {
                            selectedRadioType = "2";
                        }
                        break;
                }
            }
        });

        tv_select_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStatusDialog(activity);
            }
        });

        tv_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCropFuctionalImage();

            }
        });


        btn_saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
            }
        });


        getMenuTypeList();
        getCategoryTypeList();

        tv_selectMenuType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MenuItemListActivity.newInstance(activity, menuTypeListModel);
                startActivityForResult(intent, GlobalVariables.REQUEST_RESULT_CODE_MENU);
            }
        });

        tv_menuItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CategoryItemListActivity.newInstance(activity, categoryListModel);
                startActivityForResult(intent, GlobalVariables.REQUEST_RESULT_CODE_CATEGORY);
            }
        });

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        setTitle(getString(R.string.add_new_item), 0, 0);
    }

    private void setThisPage(MenuSubModel menuSubModel) {
        if (menuSubModel != null && context != null) {
            if (GlobalFunctions.isNotNullValue(menuSubModel.getName())) {
                etv_item_name.setText(menuSubModel.getName());

            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getAr_name())) {
                etv_item_ar_name.setText(menuSubModel.getAr_name());
            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getId())) {
                menu_id = menuSubModel.getId();

            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getMenu_category_id())) {
                category_id = menuSubModel.getMenu_category_id();

            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getMenu_type_id())) {
                menuType_id = menuSubModel.getMenu_type_id();

            }

            if (GlobalFunctions.isNotNullValue(menuSubModel.getDescription())) {
                etv_description.setText(globalFunctions.getHTMLString(menuSubModel.getDescription()));

            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getAr_description())) {
                etv_description_ar.setText(globalFunctions.getHTMLString(menuSubModel.getAr_description()));

            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getImage())) {
                Picasso.with(activity).load(menuSubModel.getImage()).placeholder(R.drawable.ic_lazyload).into(iv_item);
                iv_item_image=menuSubModel.getImage();



            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getPrice())) {
                etv_item_price.setText(menuSubModel.getPrice());

            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getMenu_type_name())) {
                tv_selectMenuType.setText(menuSubModel.getMenu_type_name());
            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getMenu_category_name())) {
                tv_menuItems.setText(menuSubModel.getMenu_category_name());
            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getStatus())) {
                if (menuSubModel.getStatus().equalsIgnoreCase("1")) {
                    tv_select_status.setText(getString(R.string.active));

                } else {
                    tv_select_status.setText(getString(R.string.inactive));

                }
            }
            if (GlobalFunctions.isNotNullValue(menuSubModel.getType())) {
                if (menuSubModel.getType().equalsIgnoreCase("1")) {
                    rd_type_veg.setChecked(true);

                } else {
                    rd_type_nonveg.setChecked(true);
                }

            }
        }
    }

    private void getMenuTypeList() {
        // globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getItemList(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                //  globalFunctions.hideProgress();
                Log.d(TAG, "Response: " + arg0.toString());
                MenuTypeMainModel menuTypeMainModel = (MenuTypeMainModel) arg0;

                if (menuTypeMainModel != null) {

                    menuTypeListModel = menuTypeMainModel.getMenuTypeListModel();
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                //globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);
                Log.d(TAG, "Failure : " + msg);

            }

            @Override
            public void OnError(String msg) {
                //globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);

                Log.d(TAG, "Error : " + msg);
            }

        }, "MenuType List");
    }


    private void getCategoryTypeList() {
        // globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getCategoryList(context, new ServerResponseInterface() {
            @SuppressLint("LongLogTag")
            @Override
            public void OnSuccessFromServer(Object arg0) {
                // globalFunctions.hideProgress();
                Log.d(TAG, "Response: " + arg0.toString());
                CategoryMainModel categoryMainModel = (CategoryMainModel) arg0;
                if (categoryMainModel != null) {
                    categoryListModel = categoryMainModel.getCategoryListModel();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void OnFailureFromServer(String msg) {
                //globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);
                Log.d(TAG, "Failure : " + msg);

            }

            @SuppressLint("LongLogTag")
            @Override
            public void OnError(String msg) {
                //globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);

                Log.d(TAG, "Error : " + msg);
            }

        }, "CategoryType List");
    }


    private void openCropFuctionalImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        Uri selectUri = null;
        GlobalFunctions.hideProgress();
        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                try {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri selectedImageUri = result.getUri();//data.getData();
                    String selectedImagePath = globalFunctions.getRealPathFromURI(context, selectedImageUri);
                    Log.d(TAG, "Path = " + selectedImagePath);
                    File imagePath = new File(selectedImagePath);
                    if (imagePath.exists()) {
                        Bitmap bmp = result.getBitmap();
                        if (bmp == null) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(selectedImagePath, options);
                            final int REQUIRED_SIZE = 200;
                            int scale = 1;
                            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                                scale *= 2;
                            options.inSampleSize = scale;
                            options.inJustDecodeBounds = false;
                            bmp = BitmapFactory.decodeFile(selectedImagePath, options);
                            bitmap = bmp;
                        }
                    }
                    selectUri = selectedImageUri;
                    this.imagePath = selectedImagePath;
                    setProfileImageToModel(bitmap, selectUri);

                } catch (Exception exccc) {
                    globalFunctions.displayMessaage(context, mainView, getString(R.string.something_went_wrong_message));
                }
            } else if (requestCode == REQUEST_IMAGE) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    selectUri = uri;
                    setProfileImageToModel(bitmap, selectUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == GlobalVariables.REQUEST_RESULT_CODE_CATEGORY) {
                CategoryModel categoryModel = (CategoryModel) data.getExtras().getSerializable(CategoryItemListActivity.BUNDLE_CATEGORY_RESPONSE_MODEL);
                if (categoryModel != null) {

                    tv_menuItems.setText(categoryModel.getName());
                    category_id = categoryModel.getMenu_id();
                    selectedCategoryPosition = categoryModel.getPosition();

                }
            } else if (requestCode == GlobalVariables.REQUEST_RESULT_CODE_MENU) {
                MenuTypeModel menuTypeModel = (MenuTypeModel) data.getExtras().getSerializable(MenuItemListActivity.BUNDLE_MENU_RESPONSE_MODEL);
                if (menuTypeModel != null) {

                    tv_selectMenuType.setText(menuTypeModel.getCount());
                    menuType_id = menuTypeModel.getId();

                }
            }
        }
    }

    private void setProfileImageToModel(Bitmap bitmap, Uri selectUri) {
        profileImageList.clear();
        uriProfileImageList.clear();
        iv_item.setImageBitmap(bitmap);
        profileImageList.add("image_one_iv");
        uriProfileImageList.add(selectUri);
    }

    private void uploadImage(String type) {
        if (type.equalsIgnoreCase(GlobalVariables.UPLOAD_PROFILE_PHOTO_PATH_CODE)) {
            if (uriProfileImageList != null) {
                if (uriProfileImageList.size() != 0) {
                    globalFunctions.showProgress(activity, context.getString(R.string.uploading_image));
                    for (int j = 0; j < uriProfileImageList.size(); j++) {
                        Uri uri = (uriProfileImageList.get(j));
                        UploadImage uploadImage = new UploadImage(context);
                        uploadImage.startUploading(uri, type, "image", this);
                    }
                } else {
                    GlobalFunctions.hideProgress();
                    createMenu(activity, menuModel);
                }
            } else {
                GlobalFunctions.hideProgress();
                createMenu(activity, menuModel);
            }
        }
    }

    @Override
    public void OnSuccess(String fileName, String type, String uploadingFile) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                globalFunctions.displayMessaage(activity, mainView, "Uploaded");
                // GlobalFunctions.hideProgress();
                if (uploadingFile.equalsIgnoreCase("image")) {
                    setUploadedImageToModel(type, fileName);
                }
            }
        });
    }

    private void setUploadedImageToModel(String type, String imagePath) {
        if (type != null) {
            if (type.equalsIgnoreCase(GlobalVariables.UPLOAD_PROFILE_PHOTO_PATH_CODE)) {
                if (menuModel == null) {
                    menuModel = new MenuModel();
                }
                menuModel.setImage(imagePath);
                GlobalFunctions.hideProgress();
                createMenu(activity, menuModel);
            }
        }
    }

    @Override
    public void OnFailure() {
        GlobalFunctions.hideProgress();
        globalFunctions.displayErrorDialog(activity, context.getString(R.string.failed_upload_image));
    }

    private void validateInput() {
        if (tv_menuItems != null && etv_item_name != null && etv_item_ar_name != null && etv_description != null && etv_description_ar != null && etv_item_price != null && tv_selectMenuType != null) {
            String
                    tv_menu_item = tv_menuItems.getText().toString().trim(),
                    etv_itemName = etv_item_name.getText().toString().trim(),
                    etv_item_ArabicName = etv_item_ar_name.getText().toString().trim(),
                    etv_item_Price = etv_item_price.getText().toString().trim(),
                    tv_description = etv_description.getText().toString().trim(),
                    etv_ArabicDesc = etv_description_ar.getText().toString().trim(),
                    etv_itemPrice = etv_item_price.getText().toString().trim(),
                    tv_selected_item = tv_selectMenuType.getText().toString().trim();


            if (!GlobalFunctions.isNotNullValue(category_id)) {
                GlobalFunctions.displayMessaage(activity, mainView, activity.getString(R.string.pleaseFillMandatoryDetails));
            } else if (etv_itemName.isEmpty()) {
                etv_item_name.setError(getString(R.string.pleaseFillMandatoryDetails));
                etv_item_name.setFocusableInTouchMode(true);
                etv_item_name.requestFocus();
            } else if (etv_item_ArabicName.isEmpty()) {
                etv_item_ar_name.setError(getString(R.string.pleaseFillMandatoryDetails));
                etv_item_ar_name.setFocusableInTouchMode(true);
                etv_item_ar_name.requestFocus();
            } else if (tv_description.isEmpty()) {
                etv_description.setError(getString(R.string.pleaseFillMandatoryDetails));
                etv_description.setFocusableInTouchMode(true);
                etv_description.requestFocus();
            } else if (etv_item_Price.isEmpty()) {
                etv_item_price.setError(getString(R.string.pleaseFillMandatoryDetails));
                etv_item_price.setFocusableInTouchMode(true);
                etv_item_price.requestFocus();
            } else if (etv_ArabicDesc.isEmpty()) {
                etv_description_ar.setError(getString(R.string.pleaseFillMandatoryDetails));
                etv_description_ar.setFocusableInTouchMode(true);
                etv_description_ar.requestFocus();
            } else if (!GlobalFunctions.isNotNullValue(menuType_id)) {
                GlobalFunctions.displayMessaage(activity, mainView, activity.getString(R.string.pleaseFillMandatoryDetails));
            } else if (etv_itemPrice.isEmpty()) {
                etv_item_price.setError(getString(R.string.pleaseFillMandatoryDetails));
                etv_item_price.setFocusableInTouchMode(true);
                etv_item_price.requestFocus();
            } else if (tv_selected_item.isEmpty()) {
                tv_selectMenuType.setError(getString(R.string.pleaseFillMandatoryDetails));
                tv_selectMenuType.setFocusableInTouchMode(true);
                tv_selectMenuType.requestFocus();
            } else {
                if (menuModel == null) {
                    menuModel = new MenuModel();
                }

                menuModel.setMenu_id(menu_id);
                menuModel.setCategory_id(category_id);
                menuModel.setName(etv_itemName);
                menuModel.setAr_name(etv_item_ArabicName);
                menuModel.setPrice(etv_item_Price);
                menuModel.setDescription(tv_description);
                menuModel.setAr_description(etv_ArabicDesc);
                menuModel.setMenu_type_id(menuType_id);
                menuModel.setType(selectedRadioType);
                menuModel.setPosition(selectedCategoryPosition);
                menuModel.setStatus(selectStatus);
                menuModel.setImage(iv_item_image);

                if (profileImageList.size() > 0) {
                    uploadImage(GlobalVariables.UPLOAD_PROFILE_PHOTO_PATH_CODE);

                } else {
                    createMenu(context, menuModel);
                }
            }
        }
    }

    private void createMenu(final Context context, final MenuModel model) {
        globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.createMenu(context, model, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                globalFunctions.hideProgress();
                Log.d(TAG, "Response : " + arg0.toString());
                validateOutputAfterCreateMenu(arg0);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                globalFunctions.hideProgress();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failure : " + msg);
            }

            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error : " + msg);
            }
        }, "Create_Menu");
    }

    private void validateOutputAfterCreateMenu(Object arg0) {
        if (arg0 instanceof StatusMainModel) {
            StatusMainModel statusMainModel = (StatusMainModel) arg0;
            StatusModel statusModel = statusMainModel.getStatusModel();
            if (statusMainModel.isStatus()) {
                globalFunctions.displayMessaage(activity, mainView, statusModel.getMessage());
                showAlertDialog(statusModel);

            } else {
                globalFunctions.displayMessaage(activity, mainView, statusModel.getMessage());

            }
        }
    }

    private void showAlertDialog(StatusModel statusModel) {
        final AlertDialog alertDialog = new AlertDialog(context);
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.app_icon);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(statusModel.getMessage());
        alertDialog.setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                closeThisActivity();

            }
        });

        alertDialog.show();

    }


    private void openStatusDialog(final Context context) {

        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(activity);
        final View alertView = layoutInflater.inflate(R.layout.select_status_custom_dialog, null, false);
        alertDialog.setView(alertView);
        alertDialog.setCancelable(true);
        // alertDialog.setIcon(R.drawable.app_icon);
        final android.app.AlertDialog dialog = alertDialog.create();

        TextView back_tv, active_tv, inactive_tv;

        back_tv = (TextView) alertView.findViewById(R.id.back_tv);
        active_tv = (TextView) alertView.findViewById(R.id.tv_active);
        inactive_tv = (TextView) alertView.findViewById(R.id.tv_inActive);


        back_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        active_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatus = globalVariables.STATUS_ACTIVE;
                tv_select_status.setText(getString(R.string.active));
                dialog.dismiss();
            }
        });

        inactive_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatus = globalVariables.STATUS_INACTIVE;
                tv_select_status.setText(getString(R.string.inactive));
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public static void setTitle(String title, int titleImageID, int backgroundResourceID) {
        mTitle = title;
        if (backgroundResourceID != 0) {
            mResourceID = backgroundResourceID;
        } else {
            mResourceID = 0;
        }
        if (titleImageID != 0) {
            titleResourseID = titleImageID;
        } else {
            titleResourseID = 0;
        }
        restoreToolbar();
    }

    @SuppressLint("LongLogTag")
    private static void restoreToolbar() {
        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        Log.d(TAG, "Restore Tool Bar");
        if (actionBar != null) {
            Log.d(TAG, "Restore Action Bar not Null");
            Log.d(TAG, "Title : " + mTitle);
            toolbar_title.setText(mTitle);
            if (mResourceID != 0) toolbar.setBackgroundResource(mResourceID);
            //actionBar.setTitle("");
            // actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onBackPressed() {
        closeThisActivity();
        super.onBackPressed();
    }

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
            //activity.overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getFragmentManager().findFragmentByTag(TAG) != null)
            getFragmentManager().findFragmentByTag(TAG).setRetainInstance(true);
    }

    @Override
    public void onStart() {

       /* if(hint != null) {
            hint.launchAutomaticHintForCall(activity.findViewById(R.id.action_call));
        }*/
//       globalFunctions.launchAutomaticHintForSearch(mainView, getString(R.string.search_title),  getString(R.string.search_description));
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (activity != null) activity = null;
        super.onDestroy();
    }

}
