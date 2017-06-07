package com.app.justclap.activities;

import android.content.Context;

/**
 * Created by android on 11/10/15.
 */
public class DataManager
{
      private static  DataManager dataManager = new DataManager();
    /*  public DashBoardData dashBoardData;
      public ArrayList<Service> serviceArrayList;
      public VendorSearchInput vendorSearchInput;
      public List<VendorProfile> vendorProfileList;*/
      public int NAVIGATION=1;
      public double latitude;
      public double longitude;
      public String address="";
      public String vendorId="";
      public String selectedServiceId="";
      public String selectedServices="";
      public String bookingType="";
   /*   public VendorDetails vendorDetails;
      public String from="Vendor";
      public ConfirmationDetail confirmationDetail;
      public  List<PromotionalImage>promotionalImageList;*/
      public static DataManager getInstance(Context context)
      {
          return dataManager;
      }

      public String g_address="";
      public String m_address="";




}
