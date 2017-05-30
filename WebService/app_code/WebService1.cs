using System;
using System.Collections.Generic;
using System.Web;
using System.Web.Services;
using DataSetTableAdapters;
using System.Data.Sql;
using System.Data;
using System.Collections;


/// <summary>
/// Summary description for WebService1
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
// [System.Web.Script.Services.ScriptService]
public class WebService1 : System.Web.Services.WebService {
    private static int rainRate=0;
    private DataSetTableAdapters.NotificationManagerTableAdapter nmTableAdapter = new NotificationManagerTableAdapter();
    public WebService1 () {
        
        //Uncomment the following line if using designed components 
        //InitializeComponent(); 
    }
    [WebMethod]
    public void setNotificationState(int state,int id) {
        
        DataSetTableAdapters.denemeTableAdapter adp = new denemeTableAdapter();
        adp.UpdateQuery(state, id);
    }
    [WebMethod]
    public int getNotificationState(int id) {

        DataSetTableAdapters.denemeTableAdapter adp = new denemeTableAdapter();
        DataTable dt = adp.GetDataByid(id);
        int state = Convert.ToInt32(dt.Rows[0]["bildirim"].ToString());
        return state;
        
    }
    [WebMethod]
    public void insertnewuser(int id) {

        DataSetTableAdapters.denemeTableAdapter adp = new denemeTableAdapter();
        adp.InsertQuery(id, 0);
        NotificationLocationTableAdapter nadp = new NotificationLocationTableAdapter();
        nadp.InsertQuery(id, 0);

    }

    [WebMethod]
    public void insertNewItem(String barcode,String Name) {

        DataSetTableAdapters.UrunlerTableAdapter dap = new UrunlerTableAdapter();
        dap.InsertUrunler(barcode, Name);

    }

    [WebMethod]
    public string getItemNameByBarcode(String barcode) {

        DataSetTableAdapters.UrunlerTableAdapter adp = new UrunlerTableAdapter();
        DataTable dt =adp.GetDataByBarcode(barcode);
        string name = dt.Rows[0]["Name"].ToString();
        return name;
    
    }
    [WebMethod]
    public String[] getMarketNameByItemName(String name) {
        DataSetTableAdapters.depoTableAdapter adp = new depoTableAdapter();
        DataTable dt = adp.GetDataByUrunName(name);
        String[] marketler = new String[dt.Rows.Count];
        for (int i = 0; i < dt.Rows.Count; i++)
        {
            marketler[i] = dt.Rows[i]["MarketName"].ToString();
        }
       
        return marketler;
    }
    [WebMethod]
    public String[] getItemPriceByItemName(string name) {

        DataSetTableAdapters.depoTableAdapter adp = new depoTableAdapter();
        DataTable dt = adp.GetDataByUrunName(name);
        String[] marketler = new String[dt.Rows.Count];
        for (int i = 0; i < dt.Rows.Count; i++)
        {
            marketler[i] = dt.Rows[i]["Fiyat"].ToString();
        }

        return marketler;

    }
    [WebMethod]
    public string getItemMarketNameByPriceAndItemName(string name, string price) {
        Double para = Convert.ToDouble(price);
        DataSetTableAdapters.depoTableAdapter adp = new depoTableAdapter();
        DataTable dt = adp.GetDataByNameAndPrice(name, para);
        return dt.Rows[0]["MarketName"].ToString();

    }

    [WebMethod]
    public int getRainRate() {
        DataSetTableAdapters.RainTableAdapter dt = new DataSetTableAdapters.RainTableAdapter();
        DataTable dataTable = dt.GetData();
        string deneme = dataTable.Rows[0]["rainRate"].ToString();
        int donus = Int32.Parse(deneme);
        return donus;

    }
    [WebMethod]
    public void setRainRate(int _rainRate) {

        DataSetTableAdapters.RainTableAdapter dt = new RainTableAdapter();
        dt.UpdateQuery(_rainRate);

    }
    [WebMethod]
    public void insertNewLocation(int userId,string longitute,string latitute) {

        LocationResultsTableAdapter adp = new LocationResultsTableAdapter();
        DataTable dt = adp.GetDataByUserId(userId);
        if (dt.Rows.Count>0)
        {
            adp.UpdateQueryByUserId(longitute, latitute, userId);
            
        }
        else
        {
            adp.InsertQuery(userId, longitute, latitute);
        }

    }

    [WebMethod]
    public int findCloseHelper(int userId) {

        LocationResultsTableAdapter adp = new LocationResultsTableAdapter();
        DataTable dt = adp.GetDataByUserId(userId);
        if (dt.Rows.Count > 0)
        {
            double _userLongitute = Convert.ToDouble(dt.Rows[0]["longitute"]);
            double _userLatitute = Convert.ToDouble(dt.Rows[0]["latitute"]);
            ArrayList listOfDistance = new ArrayList();
            ArrayList listOfUsers = new ArrayList();
            DataTable dtAllVariables = adp.GetData();
            // int index=dtAllVariables.Rows.IndexOf(dt.Rows[0]);
            // dtAllVariables.Rows[index].Delete();
            float distance;
            foreach (DataRow dr in dtAllVariables.Rows)
            {

                distance = computeDistanceAndBearing(_userLatitute, _userLongitute, Convert.ToDouble(dr["latitute"]), Convert.ToDouble(dr["longitute"]));
                listOfDistance.Add(distance);
                listOfUsers.Add(Convert.ToInt32(dr["UserId"]));

            }
            ArrayList _newDistances = new ArrayList(listOfDistance);
            _newDistances.Sort();
            int index = listOfDistance.IndexOf(_newDistances[1]);
            //listOfDistance.Sort();
            return Convert.ToInt32(listOfUsers[index]);
        }
        else return 0;
    }
    private float computeDistanceAndBearing(double lat1, double lon1,
       double lat2, double lon2)
    {

        int MAXITERS = 20;
        lat1 *= Math.PI / 180.0;
        lat2 *= Math.PI / 180.0;
        lon1 *= Math.PI / 180.0;
        lon2 *= Math.PI / 180.0;

        double a = 6378137.0; 
        double b = 6356752.3142; 
        double f = (a - b) / a;
        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);
        
        double L = lon2 - lon1;
        double A = 0.0;
        double U1 = Math.Atan((1.0 - f) * Math.Tan(lat1));
        double U2 = Math.Atan((1.0 - f) * Math.Tan(lat2));

        double cosU1 = Math.Cos(U1);
        double cosU2 = Math.Cos(U2);
        double sinU1 = Math.Sin(U1);
        double sinU2 = Math.Sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;

        double sigma = 0.0;
        double deltaSigma = 0.0;
        double cosSqAlpha = 0.0;
        double cos2SM = 0.0;
        double cosSigma = 0.0;
        double sinSigma = 0.0;
        double cosLambda = 0.0;
        double sinLambda = 0.0;
       
        double lambda = L; // initial guess
        for (int iter = 0; iter < MAXITERS; iter++)
        {
            double lambdaOrig = lambda;
            cosLambda = Math.Cos(lambda);
            sinLambda = Math.Sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
            sinSigma = Math.Sqrt(sinSqSigma);
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
            sigma = Math.Atan2(sinSigma, cosSigma); // (16)
            double sinAlpha = (sinSigma == 0) ? 0.0 :
                cosU1cosU2 * sinLambda / sinSigma; // (17)
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SM = (cosSqAlpha == 0) ? 0.0 :
                cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)

            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
            A = 1 + (uSquared / 16384.0) * // (3)
                (4096.0 + uSquared *
                 (-768 + uSquared * (320.0 - 175.0 * uSquared)));
            double B = (uSquared / 1024.0) * // (4)
                (256.0 + uSquared *
                 (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
            double C = (f / 16.0) *
                cosSqAlpha *
                (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B * sinSigma * // (6)
                (cos2SM + (B / 4.0) *
                 (cosSigma * (-1.0 + 2.0 * cos2SMSq) -
                  (B / 6.0) * cos2SM *
                  (-3.0 + 4.0 * sinSigma * sinSigma) *
                  (-3.0 + 4.0 * cos2SMSq)));

            lambda = L +
                (1.0 - C) * f * sinAlpha *
                (sigma + C * sinSigma *
                 (cos2SM + C * cosSigma *
                  (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.Abs(delta) < 1.0e-12)
            {
                break;
            }
        }

        float distance = (float)(b * A * (sigma - deltaSigma));
        return distance;
    }
    [WebMethod]
    public int getNotificationLocationState(int userId) {

        NotificationLocationTableAdapter adp = new NotificationLocationTableAdapter();
        DataTable dt = adp.GetDataByUserId(userId);
        return Convert.ToInt32(dt.Rows[0]["state"]);

    }
    [WebMethod]
    public void setNotificationLocationState(int userId, int state) {

        NotificationLocationTableAdapter adp = new NotificationLocationTableAdapter();
        adp.UpdateQueryByUserId(state, userId);

    }

    [WebMethod]
    public ArrayList getAllBooks() {
        libraryTableAdapter adp = new libraryTableAdapter();
            DataTable dt = adp.GetData();
            ArrayList list = new ArrayList();
           for (int i = 0; i < dt.Rows.Count; i++)
            {
                list.Add(dt.Rows[i]["BookName"].ToString());
                list.Add(dt.Rows[i]["Url"].ToString());
            }
           return list;
    }

    [WebMethod]
    public string getBooksUrl(string bookName) {
        libraryTableAdapter adp = new libraryTableAdapter();
        DataTable dt = adp.GetDataByBookName(bookName);
        return dt.Rows[0]["Url"].ToString();
    }

    [WebMethod]
    public void insertNewBook(string bookName, string url, string writerName) {

        DataSetTableAdapters.libraryTableAdapter adp = new libraryTableAdapter();
        adp.InsertQuery(bookName, url, writerName);

    }

    [WebMethod]
    public ArrayList getGazeteOrMakale() {

        gazetelerTableAdapter adp = new gazetelerTableAdapter();
        DataTable dt = adp.GetData();
        ArrayList list = new ArrayList();
        for (int i = 0; i < dt.Rows.Count; i++)
        {
            list.Add(dt.Rows[i]["GazeteYazarAd"].ToString());
            list.Add(dt.Rows[i]["Url"].ToString());
        }
        return list;
    }
    [WebMethod]
    public string getGazeteOrMakaleByEntryName(string entryName) {
        gazetelerTableAdapter adp = new gazetelerTableAdapter();
        DataTable dt = adp.GetDataByGazeteMakaleAd(entryName);
        return dt.Rows[0]["Url"].ToString();

    }
    public void insertNewGazeteOrMakale(string entryName, string Url) {

        gazetelerTableAdapter adp = new gazetelerTableAdapter();
        adp.InsertQuery(entryName, Url);

    }




}
