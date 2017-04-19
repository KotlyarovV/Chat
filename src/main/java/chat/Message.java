package chat;

import accounts.UserProfile;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by vitaly on 04.04.17.
 */
@Entity
@Table (name = "messages")
public class Message {

    @Id
    @Column(name = "message_id")
    public int id;

    @Column(name = "value")
    public String value;


    @Column(name = "from_id", insertable = false, updatable = false)
    public int fromId;


    @Column(name = "to_id", insertable = false, updatable = false)
    public Integer toId;


    @ManyToOne
    @ForeignKey(name = "from_id")
    public UserProfile from;


    @ManyToOne
    @ForeignKey(name = "to_id")
    public UserProfile to;


    public Message(UserProfile from, String value) {
        this.from = from;
        this.fromId = from.id;
        this.value = value;
    }

    public Message() {};

    public Message(UserProfile from, UserProfile to, String value) {
        this(from, value);
        this.to = to;
        this.toId = to.id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return message.id == this.id;
    }
}
