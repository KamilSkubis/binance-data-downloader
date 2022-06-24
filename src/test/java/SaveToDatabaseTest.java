import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.*;
import persistence.DBWriter;
import persistence.MySQLUtilTesting;

import java.util.List;

import static org.junit.Assert.*;

public class SaveToDatabaseTest {
    Session session;

    @Before
    public void setUp(){
        session = (Session) MySQLUtilTesting.getSessionFactory().openSession();

        session.beginTransaction();
        String binance = "create table binance_1d(\n" +
                "        id bigint AUTO_INCREMENT,\n" +
                "        symbol_id int,\n" +
                "        open_time bigint signed,\n" +
                "        open double,\n" +
                "        high double,\n" +
                "        low double,\n" +
                "        close double,\n" +
                "        volume double,\n" +
                "        key(id)\n" +
                "        );";

        String symbol = "create table symbols(\n" +
                "id bigint AUTO_INCREMENT,\n" +
                "symbol char(15),\n" +
                "key(id)\n" +
                ");";

        session.createSQLQuery(symbol).executeUpdate();
        session.createSQLQuery(binance).executeUpdate();
        session.getTransaction().commit();
    }

    @Test
    public void binance_1d_shouldBeEmpty(){
        Query query = session.createQuery("from Data");
        assertEquals(0,query.getResultList().size());
    }

    @Test
    public void symbol_shouldBeEmpty(){
        Query query = session.createQuery("from Symbol");
        assertEquals(0,query.getResultList().size());

    }

    @Test
    public void canAdd_OneDataToDb(){
        Symbol symbol = new Symbol();
        symbol.setSymbol("test");


        DBWriter writer = new DBWriter(session);
        writer.writeSymbol(symbol);

        List<Symbol> symbolList = session.createQuery("from Symbol").getResultList();
        assertEquals(1,symbolList.size());

    }

    @Test
    public void canAddDataToEmptyDb(){
        Data d = new Data();
        Symbol s = new Symbol();
        s.setSymbol("test");
        d.setSymbol(s);
        d.setOpen(100.00);
        d.setLow(90.00);
        d.setHigh(123.00);
        d.setClose(112.0);
        d.setVolume(2332.0);
        d.setOpenTime(23321l);


        DBWriter writer = new DBWriter(session);
        writer.writeData(d);
        List<Data> results = session.createQuery("from Data").getResultList();
        assertEquals(1, results.size());
    }



    @After
    public void tearDown(){
        session.beginTransaction();
        session.createSQLQuery("DROP TABLE symbols").executeUpdate();
        session.createSQLQuery("DROP TABLE binance_1d").executeUpdate();
        session.getTransaction().commit();
    }

}
