package com.cepgoz.kou.cepgoz;

import android.os.StrictMode;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by faruk on 10.5.2016.
 */
public class WebServiceBarcode {

    public String getItemPriceByItemName(String name) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CelsiusToFahrenheit();
        String METHOD_NAME = "getItemPriceByItemName";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/getItemPriceByItemName";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("name", name);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            String resultData = result.toString();
            return resultData;
        } catch (XmlPullParserException e) {
            return e.getMessage();
        } catch (SoapFault soapFault) {
            return soapFault.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    public String getItemNameByBarcodeNumber(String barcode){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CelsiusToFahrenheit();
        String METHOD_NAME = "getItemNameByBarcode";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/getItemNameByBarcode";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("barcode", barcode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            String resultData = result.toString();
            return resultData;
        } catch (XmlPullParserException e) {
            return e.getMessage();
        } catch (SoapFault soapFault) {
            return soapFault.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    public String getItemMarketNameByPriceAndByItemName(String itemName,String price){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CelsiusToFahrenheit();
        String METHOD_NAME = "getItemMarketNameByPriceAndItemName";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/getItemMarketNameByPriceAndItemName";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("name", itemName);
            request.addProperty("price",price);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            String resultData = result.toString();
            return resultData;
        } catch (XmlPullParserException e) {
            return e.getMessage();
        } catch (SoapFault soapFault) {
            return soapFault.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}
