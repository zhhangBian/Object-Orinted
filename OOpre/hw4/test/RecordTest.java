import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTest {
    BasicData basicData = new BasicData();
    Record record = new Record("2023/10", 2, 123, "123"
            , 114, 456, "456", basicData);

    @Test
    public void getDate() {
        assertEquals(record.GetDate(), "2023/10");
    }

    @Test
    public void getType() {
        assertEquals(record.GetType(), 2);
    }

    @Test
    public void getFighterId() {
        assertEquals(record.GetFighterId(), 123);
    }

    @Test
    public void getFighterName() {
        assertEquals(record.GetFighterName(), "123");
    }

    @Test
    public void getUseName() {
        assertEquals(record.GetUseName(), "456");
    }

    @Test
    public void getFightedId() {
        assertEquals(record.GetFightedId(), 114);
    }

    @Test
    public void isFighted() {
        assertTrue(record.IsFighted(114));
    }
}