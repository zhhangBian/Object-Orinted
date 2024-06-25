import com.oocourse.spec3.main.Message;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyNetworkTest {
    @Test
    public void deleteColdEmoji() throws Exception {
        MyNetwork network = new MyNetwork();
        for (int i = 0; i < 10; i++) {
            network.addPerson(new MyPerson(i + 1, "1", 1));
        }
        Message message = new MyEmojiMessage(1, 1,
            network.getPerson(1), network.getPerson(2));

        network.addRelation(1, 2, 1);
        network.addRelation(2, 4, 1);
        network.addRelation(2, 3, 1);
        network.addRelation(1, 3, 1);
        network.addRelation(1, 5, 1);

        network.storeEmojiId(1); // 4
        network.storeEmojiId(2); // 6
        network.storeEmojiId(3); // 5
        network.storeEmojiId(4); // 0, but 1 in messages, have not sent
        network.storeEmojiId(5); // 0

        network.addMessage(message);
        network.sendMessage(1);
        network.addMessage(message);
        network.sendMessage(1);
        network.addMessage(message);
        network.sendMessage(1);
        network.addMessage(message);
        network.sendMessage(1);

        message = new MyEmojiMessage(2, 2,
            network.getPerson(2), network.getPerson(4));
        network.addMessage(message);
        network.sendMessage(2);
        network.addMessage(message);
        network.sendMessage(2);
        network.addMessage(message);
        network.sendMessage(2);

        message = new MyEmojiMessage(3, 2,
            network.getPerson(2), network.getPerson(3));
        network.addMessage(message);
        network.sendMessage(3);
        network.addMessage(message);
        network.sendMessage(3);
        network.addMessage(message);
        network.sendMessage(3);

        message = new MyEmojiMessage(
            9, 3, network.getPerson(1), network.getPerson(3));
        network.addMessage(message);
        network.sendMessage(9);
        network.addMessage(message);
        network.sendMessage(9);
        network.addMessage(message);

        MyMessage message1 = new MyMessage(11, 2,
            network.getPerson(1), network.getPerson(5));
        network.addMessage(message1);
        network.sendMessage(9);
        network.addMessage(message);
        network.sendMessage(9);
        network.addMessage(message);
        network.sendMessage(11);
        network.sendMessage(9);

        network.addMessage(message);
        network.addMessage(message1);
        message = new MyEmojiMessage(22, 4,
            network.getPerson(1), network.getPerson(5));
        network.addMessage(message);

        Message redMessage = new MyRedEnvelopeMessage(92, 114,
            network.getPerson(1), network.getPerson(2));

        Message noticeMessage = new MyNoticeMessage(99, "1",
            network.getPerson(3), network.getPerson(2));

        MyTag tag = new MyTag(100);

        Message noticeToGroup = new MyNoticeMessage(98, "2",
            network.getPerson(1), tag);

        network.addTag(1, tag);
        network.addPersonToTag(3, 1, 100);
        network.addPersonToTag(5, 1, 100);

        network.addMessage(redMessage);
        network.addMessage(noticeMessage);
        network.addMessage(noticeToGroup);

        network.sendMessage(92);
        network.sendMessage(98);
        network.sendMessage(99);

        network.addMessage(redMessage);
        network.addMessage(noticeMessage);
        network.addMessage(noticeToGroup);

        int limit = 5;

        Message[] oldMessages = network.getMessages();
        int[] oldEmojiIds = network.getEmojiIdList();
        int[] oldEmojiHeats = network.getEmojiHeatList();
        int returnValue = network.deleteColdEmoji(limit);
        Message[] newMessages = network.getMessages();
        int[] newEmojiIds = network.getEmojiIdList();
        int[] newEmojiHeats = network.getEmojiHeatList();

        assertEquals(newEmojiHeats.length, newEmojiIds.length);
        assertEquals(oldEmojiHeats.length, oldEmojiIds.length);

        // ensures 1
        for (int i = 0; i < oldEmojiIds.length; i++) {
            if (oldEmojiHeats[i] >= limit) {
                assertTrue(contains(newEmojiIds, oldEmojiIds[i]));
            }
        }

        // ensures 2
        boolean flag = true;
        for (int i = 0; i < newEmojiIds.length; i++) {
            boolean found = false;
            for (int j = 0; j < oldEmojiIds.length; j++) {
                if (newEmojiIds[i] == oldEmojiIds[j]
                    && newEmojiHeats[i] == oldEmojiHeats[j]) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                flag = false;
                break;
            }
        }
        assertTrue(flag);

        // ensures 3
        int cnt = (int) IntStream.range(0, oldEmojiIds.length).
            filter(i -> oldEmojiHeats[i] >= limit).count();
        assertEquals(cnt, newEmojiIds.length);

        // ensures 4
        assertEquals(newEmojiHeats.length, newEmojiIds.length);
        assertEquals(oldEmojiHeats.length, oldEmojiIds.length);

        // ensures 5
        for (Message oldMessage : oldMessages) {
            if (oldMessage instanceof MyEmojiMessage
                && network.containsEmojiId(((MyEmojiMessage) oldMessage).getEmojiId())) {
                assertTrue(contains(newMessages, oldMessage));
            }
        }

        // ensures 6
        for (Message oldMessage : oldMessages) {
            if (!(oldMessage instanceof MyEmojiMessage)) {
                assertTrue(contains(newMessages, oldMessage));
            }
        }

        // ensures 7
        cnt = 0;
        for (Message oldMessage : oldMessages) {
            if (oldMessage instanceof MyEmojiMessage) {
                MyEmojiMessage emojiMessage = (MyEmojiMessage) oldMessage;
                cnt += network.containsEmojiId(emojiMessage.getEmojiId()) ? 1 : 0;
            } else {
                cnt++;
            }
        }
        assertEquals(cnt, newMessages.length);

        // ensures 8
        assertEquals(returnValue, newEmojiIds.length);
    }

    private boolean contains(Object[] list, Object o) {
        for (Object object : list) {
            if (o.equals(object)) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(int[] list, Object o) {
        for (Object object : list) {
            if (o.equals(object)) {
                return true;
            }
        }
        return false;
    }
}
