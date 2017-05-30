package com.cepgoz.kou.cepgoz;

/**
 * Created by faruk on 3.5.2016.
 */
public class BookProccess{

    //Veritabanından sistemdeki kayıtlı kitaplar çekilecek.
    //engellinin isteği ile bu kayıtlar arasında karşılaştırma yapılacak.
    //Yoksa gönüllülere bildirim gidecek ve


    public String[] duzenleme(String marketler){
        String[] markets = new String[]{};
        markets = marketler.split("anyType=");
        for (int i=0;i<markets.length-1;i++ ){
            markets[i]=markets[i+1];
        }
        String[] donus = new String[markets.length-1];
//        for (int i=0;i<markets.length-1;i++){
//            markets[i] = markets[i].substring(0,markets[i].indexOf(" "));
//            donus[i]=markets[i];
//        }
        for (int i=0;i<markets.length-1;i++){
            if(markets[i].contains(";")) {
                markets[i] = markets[i].substring(0, markets[i].indexOf(";"));
                donus[i] = markets[i].trim();
            }
        }
        return  donus;
    }


}
