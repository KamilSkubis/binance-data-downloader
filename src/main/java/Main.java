import com.binance.connector.client.impl.SpotClientImpl;
import downloads.BinanceBar;
import downloads.BinanceDownloader;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.HibernateUtil;

import java.util.LinkedHashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SpotClientImpl client = new SpotClientImpl();
        BinanceDownloader binance = new BinanceDownloader(client.createMarket());

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", "BTCUSDT");
        params.put("interval", "1d");
        params.put("limit", "10");
        List<BinanceBar> datas = binance.downloadKlines(params);

        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        for (BinanceBar data : datas) {
            s.persist(data);
        }
        t.commit();
        s.close();
    }

}
