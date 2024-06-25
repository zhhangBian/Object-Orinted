import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.EqualTagIdException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.TagIdNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.Tag;
import exceptions.MyAcquaintanceNotFoundException;
import exceptions.MyEmojiIdNotFoundException;
import exceptions.MyEqualEmojiIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyEqualTagIdException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPathNotFoundException;
import exceptions.MyPersonIdNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> persons;
    private final HashMap<Integer, Message> messages;
    private final HashMap<Integer, Integer> emojiHeatMap;

    private final DisJointSet disJointSet;
    private int blockCount;
    private int tripleCount;
    private int coupleCount;
    private final HashMap<Integer, ArrayList<Tag>> personInTags;
    private final HashMap<Integer, HashMap<Integer, Message>> emojiMessageMap;

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.messages = new HashMap<>();
        this.emojiHeatMap = new HashMap<>();

        this.disJointSet = new DisJointSet();
        this.blockCount = 0;
        this.tripleCount = 0;
        this.coupleCount = 0;
        this.personInTags = new HashMap<>();
        this.emojiMessageMap = new HashMap<>();
    }

    @Override
    public boolean containsPerson(int id) {
        return this.persons.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return this.persons.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int personId = person.getId();
        NetworkMethods.ThrowEqualPersonId(this.containsPerson(personId), personId);

        this.persons.put(personId, person);
        this.disJointSet.AddPerson(person);
        this.blockCount++;
        this.personInTags.put(personId, new ArrayList<>());
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
        PersonIdNotFoundException, EqualRelationException {
        this.CheckContainPerson(id1);
        this.CheckContainPerson(id2);

        MyPerson person1 = (MyPerson) this.persons.get(id1);
        MyPerson person2 = (MyPerson) this.persons.get(id2);
        if (person1.isLinked(person2)) {
            throw new MyEqualRelationException(id1, id2);
        }

        int bestIdBefore1 = person1.GetBestId();
        int bestIdBefore2 = person2.GetBestId();
        MyPerson.AddRelation(person1, person2, value);

        this.coupleCount = NetworkMethods.ResetCoupleNum(id1, id2,
            bestIdBefore1, bestIdBefore2, this.persons, this.coupleCount);
        this.blockCount += this.disJointSet.AddRelationship(id1, id2);
        this.tripleCount += person1.GetSharedAcquaintanceNum(person2);
        NetworkMethods.ModifyTagValue(person1, person2,
            0, value, this.personInTags.get(id1));
    }

    @Override
    public void modifyRelation(int id1, int id2, int value)
        throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        this.CheckContainPerson(id1);
        this.CheckContainPerson(id2);
        NetworkMethods.ThrowEqualPersonId(id1 == id2, id1);

        MyPerson person1 = (MyPerson) persons.get(id1);
        MyPerson person2 = (MyPerson) persons.get(id2);
        NetworkMethods.CheckHaveRelationship(person1, person2);

        int oldValue = person1.queryValue(person2);
        int newValue = oldValue + value;
        int bestIdBefore1 = person1.GetBestId();
        int bestIdBefore2 = person2.GetBestId();

        if (newValue > 0) {
            MyPerson.SetNewValue(person1, person2, newValue);
            NetworkMethods.ModifyTagValue(person1, person2,
                oldValue, newValue, this.personInTags.get(id1));
        } else {
            this.tripleCount -= person1.GetSharedAcquaintanceNum(person2);
            NetworkMethods.ModifyTagValue(person1, person2, oldValue, 0,
                this.personInTags.get(id1));

            this.blockCount += NetworkMethods.DeleteRelation(person1, person2,
                this.disJointSet, this.persons);
        }
        this.coupleCount = NetworkMethods.ResetCoupleNum(id1, id2,
            bestIdBefore1, bestIdBefore2, this.persons, this.coupleCount);
    }

    @Override
    public int queryValue(int id1, int id2)
        throws PersonIdNotFoundException, RelationNotFoundException {
        this.CheckContainPerson(id1);
        this.CheckContainPerson(id2);

        MyPerson person1 = (MyPerson) persons.get(id1);
        MyPerson person2 = (MyPerson) persons.get(id2);
        NetworkMethods.CheckHaveRelationship(person1, person2);

        return person1.queryValue(person2);
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        this.CheckContainPerson(id1);
        this.CheckContainPerson(id2);
        return this.disJointSet.IsConnected(id1, id2);
    }

    @Override
    public int queryBlockSum() {
        return this.blockCount;
    }

    @Override
    public int queryTripleSum() {
        return this.tripleCount;
    }

    @Override
    public void addTag(int personId, Tag tag)
        throws PersonIdNotFoundException, EqualTagIdException {
        this.CheckContainPerson(personId);

        Person person = this.persons.get(personId);
        if (person.containsTag(tag.getId())) {
            throw new MyEqualTagIdException(tag.getId());
        }

        person.addTag(tag);
        ((MyTag) tag).SetPersonId(personId);
    }

    @Override
    public void addPersonToTag(int personId1, int personId2, int tagId)
        throws PersonIdNotFoundException, RelationNotFoundException,
        TagIdNotFoundException, EqualPersonIdException {
        this.CheckContainPerson(personId1);
        this.CheckContainPerson(personId2);
        NetworkMethods.ThrowEqualPersonId(personId1 == personId2, personId1);

        Person person1 = this.persons.get(personId1);
        Person person2 = this.persons.get(personId2);
        NetworkMethods.CheckHaveRelationship(person1, person2);
        NetworkMethods.ThrowTagIdNotFound(!person2.containsTag(tagId), tagId);

        Tag tag = person2.getTag(tagId);
        NetworkMethods.ThrowEqualPersonId(tag.hasPerson(person1), personId1);

        if (tag.getSize() <= 1111) {
            tag.addPerson(person1);
            this.personInTags.get(personId1).add(tag);
        }
    }

    @Override
    public int queryTagValueSum(int personId, int tagId)
        throws PersonIdNotFoundException, TagIdNotFoundException {
        this.CheckContainPerson(personId);

        Person person = this.persons.get(personId);
        NetworkMethods.ThrowTagIdNotFound(!person.containsTag(tagId), tagId);

        return person.getTag(tagId).getValueSum();
    }

    @Override
    public int queryTagAgeVar(int personId, int tagId)
        throws PersonIdNotFoundException, TagIdNotFoundException {
        this.CheckContainPerson(personId);

        Person person = this.persons.get(personId);
        NetworkMethods.ThrowTagIdNotFound(!person.containsTag(tagId), tagId);

        return person.getTag(tagId).getAgeVar();
    }

    @Override
    public void delPersonFromTag(int personId1, int personId2, int tagId)
        throws PersonIdNotFoundException, TagIdNotFoundException {
        this.CheckContainPerson(personId1);
        this.CheckContainPerson(personId2);

        Person person1 = this.persons.get(personId1);
        Person person2 = this.persons.get(personId2);
        NetworkMethods.ThrowTagIdNotFound(!person2.containsTag(tagId), tagId);

        Tag tag = person2.getTag(tagId);
        if (!tag.hasPerson(person1)) {
            throw new MyPersonIdNotFoundException(personId1);
        }

        tag.delPerson(person1);
        this.personInTags.get(personId1).remove(tag);
    }

    @Override
    public void delTag(int personId, int tagId)
        throws PersonIdNotFoundException, TagIdNotFoundException {
        this.CheckContainPerson(personId);

        Person person = this.persons.get(personId);
        NetworkMethods.ThrowTagIdNotFound(!person.containsTag(tagId), tagId);

        person.delTag(tagId);
    }

    @Override
    public boolean containsMessage(int id) {
        return this.messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message)
        throws EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        int messageId = message.getId();
        if (this.containsMessage(messageId)) {
            throw new MyEqualMessageIdException(messageId);
        }

        if (message instanceof EmojiMessage) {
            int emojiId = ((EmojiMessage) message).getEmojiId();
            if (!this.containsEmojiId(emojiId)) {
                throw new MyEmojiIdNotFoundException(emojiId);
            }
            this.emojiMessageMap.get(emojiId).put(messageId, message);
        }

        if (message.getType() == 0) {
            Person person1 = message.getPerson1();
            Person person2 = message.getPerson2();

            NetworkMethods.ThrowEqualPersonId(person1.equals(person2), person1.getId());
        }

        this.messages.put(messageId, message);
    }

    @Override
    public Message getMessage(int id) {
        return this.messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
        MessageIdNotFoundException, TagIdNotFoundException {
        if (!this.containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }

        Message message = this.messages.get(id);
        MyPerson person1 = (MyPerson) message.getPerson1();
        MyPerson person2 = (MyPerson) message.getPerson2();
        int socialValue = message.getSocialValue();
        MyTag messageTag = (MyTag) message.getTag();

        if (message.getType() == 0) {
            NetworkMethods.CheckHaveRelationship(person1, person2);

            person1.addSocialValue(socialValue);
            person2.addSocialValue(socialValue);

            if (message instanceof RedEnvelopeMessage) {
                int money = ((RedEnvelopeMessage) message).getMoney();
                person1.addMoney(-money);
                person2.addMoney(money);
            } else if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                if (this.emojiHeatMap.containsKey(emojiId)) {
                    this.emojiHeatMap.replace(emojiId, this.emojiHeatMap.get(emojiId) + 1);
                }
                this.emojiMessageMap.get(emojiId).remove(message.getId());
            }

            person2.AddMessage(message);
        } else if (message.getType() == 1) {
            NetworkMethods.ThrowTagIdNotFound(!person1.containsTag(messageTag.getId()),
                messageTag.getId());

            person1.addSocialValue(socialValue);
            messageTag.AddSocialValue(socialValue);

            if (message instanceof RedEnvelopeMessage && messageTag.getSize() > 0) {
                int tagSize = messageTag.getSize();
                if (tagSize > 0) {
                    int moneyMean = ((RedEnvelopeMessage) message).getMoney() / tagSize;
                    person1.addMoney(-moneyMean * tagSize);
                    messageTag.AddMoney(moneyMean);
                }
            } else if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                if (this.emojiHeatMap.containsKey(emojiId)) {
                    this.emojiHeatMap.replace(emojiId, this.emojiHeatMap.get(emojiId) + 1);
                }
                this.emojiMessageMap.get(emojiId).remove(message.getId());
            }
        }

        this.messages.remove(id);
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        this.CheckContainPerson(id);
        return this.persons.get(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        this.CheckContainPerson(id);
        return this.persons.get(id).getReceivedMessages();
    }

    @Override
    public boolean containsEmojiId(int id) {
        return this.emojiHeatMap.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (this.emojiHeatMap.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        }

        this.emojiHeatMap.put(id, 0);
        this.emojiMessageMap.put(id, new HashMap<>());
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        this.CheckContainPerson(id);
        return this.persons.get(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!this.emojiHeatMap.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }

        return this.emojiHeatMap.get(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        ArrayList<Integer> removeEmojiIdList = new ArrayList<>();
        Iterator<Map.Entry<Integer, Integer>> iterator = this.emojiHeatMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (entry.getValue() < limit) {
                removeEmojiIdList.add(entry.getKey());
                iterator.remove();
            }
        }

        for (Integer emojiId : removeEmojiIdList) {
            HashMap<Integer, Message> emojiIdMessageMap = this.emojiMessageMap.get(emojiId);
            for (Integer messageId : emojiIdMessageMap.keySet()) {
                this.messages.remove(messageId);
            }
            this.emojiMessageMap.remove(emojiId);
        }

        return this.emojiHeatMap.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        this.CheckContainPerson(personId);
        ((MyPerson) (this.persons.get(personId))).ClearNoticeMessage();
    }

    @Override
    public int queryBestAcquaintance(int id)
        throws PersonIdNotFoundException, AcquaintanceNotFoundException {
        this.CheckContainPerson(id);

        MyPerson person = (MyPerson) this.persons.get(id);
        if (person.NotHaveAcquaintance()) {
            throw new MyAcquaintanceNotFoundException(id);
        }

        return person.GetBestId();
    }

    @Override
    public int queryCoupleSum() {
        return this.coupleCount;
    }

    @Override
    public int queryShortestPath(int id1, int id2)
        throws PersonIdNotFoundException, PathNotFoundException {
        this.CheckContainPerson(id1);
        this.CheckContainPerson(id2);
        if (!this.isCircle(id1, id2)) {
            throw new MyPathNotFoundException(id1, id2);
        }

        return id1 == id2 ? 0 : NetworkMethods.BfsSearch(id1, id2, this.persons) - 1;
    }

    public Message[] getMessages() {
        return this.messages.values().toArray(new Message[0]);
    }

    public int[] getEmojiIdList() {
        return this.emojiHeatMap.keySet().stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] getEmojiHeatList() {
        return this.emojiHeatMap.values().stream().mapToInt(Integer::intValue).toArray();
    }

    private void CheckContainPerson(int personId) throws MyPersonIdNotFoundException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
    }
}
