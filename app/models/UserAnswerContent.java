package models;

import org.joda.time.LocalDate;

import javax.persistence.*;

/**
 * This class presents user answer data. It is stored in such fields:
 * stringContent for all text answer,
 * dataContent for a date,
 * sliderContent for a slider amount.
 * <p>
 * For holding the date use the joda time local date and
 * the jadira user type for convert it in a DB date.
 * This entity has two foreign keys.The first one to a table 'UserAnswer' and the second one to the
 * table 'Field'. The first one must be 'not null' but the second one can be 'null'. It is made
 * for a situation when admin deletes a field for which the user gave the answer.
 * <p>
 * The initial value and the allocation size of the sequence generator are assigned explicitly
 * because sometimes the Hibernate provides a negative number
 */
@Entity
@Table(name = "user_answer_content")
public class UserAnswerContent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_answer_content_id_seq")
    @SequenceGenerator(name = "user_answer_content_id_seq", sequenceName = "user_answer_content_id_seq",
            initialValue = 1, allocationSize = 1)
    public Long id;

    @Column(name = "string_content")
    public String stringContent;

    @Column(name = "date_content")
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    public LocalDate dateContent;

    @Column(name = "slider_content")
    public Byte sliderContent;

    @ManyToOne
    public UserAnswer user;

    @ManyToOne
    public Field field;

    public UserAnswerContent() {
    }

}
