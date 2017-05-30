package com.cepgoz.kou.cepgoz;

import android.os.StrictMode;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by faruk on 18.5.2016.
 */
public class WebServiceLocation {

    public void insertNewLocation(int userId,String longitute,String latitute){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String METHOD_NAME = "insertNewLocation";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/insertNewLocation";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("userId", userId);
            request.addProperty("longitute",longitute);
            request.addProperty("latitute",latitute);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
        } catch (XmlPullParserException e) {
        } catch (SoapFault soapFault) {
        } catch (IOException e) {
        }
    }


    public String getNotificationLocationState(int userId){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String METHOD_NAME = "getNotificationLocationState";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/getNotificationLocationState";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("userId", userId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            String resultData = result.toString();
            return  resultData;
        } catch (XmlPullParserException e) {
            return e.getMessage();
        } catch (SoapFault soapFault) {
            return soapFault.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }


    public void setNotificationLocationState(int userId,int state){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String METHOD_NAME = "setNotificationLocationState";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/setNotificationLocationState";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("userId", userId);
            request.addProperty("state",state);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
        } catch (XmlPullParserException e) {
        } catch (SoapFault soapFault) {
        } catch (IOException e) {
        }
    }


    public String findCloseHelper(int userId){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String METHOD_NAME = "findCloseHelper";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/findCloseHelper";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("userId", userId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            String resultData = result.toString();
            return  resultData;
        } catch (XmlPullParserException e) {
            return e.getMessage();
        } catch (SoapFault soapFault) {
            return soapFault.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
