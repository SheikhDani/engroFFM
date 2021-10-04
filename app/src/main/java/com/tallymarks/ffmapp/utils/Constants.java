package com.tallymarks.ffmapp.utils;

public class Constants {

    public static String LOGIN_USERNAME = "";
    public static String LOGIN_PASSWORD = "";
    public static String LOGIN_GRANT_TYPE = "password";

    public static final String ENVIRONMENT = "Development";
    public static final String DATE_FORMAT= "dd-MM-yyyy";
    public static final String GMT_TIME_FORMAT= "EE MMM dd HH:mm:ss zz yyy";
    public static final String AUTHORIZATION= "Authorization";

    public static final String ACCESS_TOKEN= "AccessToken";
    public static final String TOKEN_TYPE= "TokenType";
    public static final String REFERSH_TOKEN= "RefershToken";
    public static final String ACTIVITY_STATUS = "activityStatus";
    public static final String ACTIVITY_CUSTOMER_CODE= "activityCustomerCode";

    public static final String ACTIVITY_OBJECTIVE = "activityObjective";
    public static final String ACTIVITY_OBJECTIVE_NAME = "activityObjectiveName";
    public static final String CUSTOMER_ID= "customerID";
    public static final String CUSTOMER_CODE = "customerCode";
    public static final String CUSTOMER_NAME = "customerName";
    public static final String CUSTOMER_LAT= "customerLat";
    public static final String CUSTOMER_LNG = "customerLng";
    public static final String CUSTOMER_DAY_ID = "customerDayID";
    public static final String CUSTOMER_JOURNEYPLAN_ID= "customerjourneyplanID";
    public static final String CUSTOMER_SALES_POINT_NAME= "customersalesPointName";








    public static final String GET = "GET";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    public static final String POST = "POST";
    public static final String CONTENT_TYPE_JSON ="application/json";




    // public static String DRIVER_RATING="Driver_status";




    public static final String FFM_BASE_URL = "https://attendance.tallymarkscloud.com:8080/mitracs";
    public static final String FFM_LOGIN = FFM_BASE_URL + "/oauth/token";
    public static final String FFM_GET_ALL_CUSTOMERS= FFM_BASE_URL + "/global/lookup/getAssignedCustomersForCheckIn";
    public static final String FFM_GET_OUTLET_STATUS= FFM_BASE_URL + "/global/lookup/getOutletStatuses";
    public static final String FFM_GET_TODAY_CUSTOMERS= FFM_BASE_URL + "/app/trans/getCustomersTodaysJourneyPlan";
    public static final String FFM_GET_LIST_ALL_CROPS= FFM_BASE_URL + "/global/lookup/listAllCrops";
    public static final String FFM_GET_ASSIGNED_SAELS_POINT=  FFM_BASE_URL + "/global/lookup/getAssignedSalesPoints";
    public static final String FFM_GET_LIST_OF_ALL_FERTTYPES=  FFM_BASE_URL + "/global/lookup/listAllFertAppTypes";
    public static final String FFM_GET_LIST_OF_ALL_DEPTHS=  FFM_BASE_URL + "/global/lookup/listAllDepths";
    public static final String FFM_GET_LIST_OF_ALL_GENDERS=  FFM_BASE_URL + "/global/lookup/listAllGenders";
    public static final String FFM_GET_LIST_OF_ALL_DICTRICT=  FFM_BASE_URL + "/global/lookup/listAllDistricts";
    public static final String FFM_GET_LIST_OF_ALL_MARKET_PLAYERS=  FFM_BASE_URL + "/global/lookup/listAllMarketPlayers";
    public static final String FFM_GET_LIST_OF_ALL_PRODUCT_CATEGORIES=  FFM_BASE_URL + "/global/lookup/listAllProductCategories";
    public static final String FFM_GET_LIST_OF_ALL_PRODUCTBRAND_GROUOPY_CATEOGRY=  FFM_BASE_URL + "/global/lookup/getAllProductBrandsGroupedByCategory";
    public static final String FFM_GET_LIST_OF_ALL_BRANDS=  FFM_BASE_URL + "/admin/mdm/listAllBrands";
    public static final String FFM_GET_LIST_OF_CUSTOMERS_FARMER_HIERARCHY=  FFM_BASE_URL + "/global/lookup/getCustomersInFarmerHierarchy";
    public static final String FFM_GET_LIST_OF_GETCOMPANYHELD_BRAND_BASICLIST=  FFM_BASE_URL + "/global/lookup/getCompanyHeldBrandsBasicList";
    public static final String FFM_GET_FARMER_TODAY_JOURNEY_PLAN=  FFM_BASE_URL + "/app/trans/getFarmersTodaysJourneyPlan";














}
