import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.Tag;

public class MyRedEnvelopeMessage implements RedEnvelopeMessage {
    private final int id;
    private final int type;
    private final int socialValue;
    private final Person person1;
    private final Person person2;
    private final Tag tag;
    private final int money;

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,
                                Person messagePerson1, Person messagePerson2) {
        this.id = messageId;
        this.type = 0;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.tag = null;

        this.money = luckyMoney;
        this.socialValue = 5 * luckyMoney;
    }

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,
                                Person messagePerson1, Tag messageTag) {
        this.id = messageId;
        this.type = 1;
        this.person1 = messagePerson1;
        this.person2 = null;
        this.tag = messageTag;

        this.money = luckyMoney;
        this.socialValue = 5 * luckyMoney;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public Person getPerson1() {
        return this.person1;
    }

    @Override
    public Person getPerson2() {
        return this.person2;
    }

    @Override
    public Tag getTag() {
        return this.tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Message)) {
            return false;
        }

        return (((Message) obj).getId() == id);
    }

    @Override
    public int getMoney() {
        return this.money;
    }
}
