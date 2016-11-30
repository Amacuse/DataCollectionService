package models;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class presents a user answer.
 * All user answer data are stored in the table 'UserAnswerContent'. We have a collection of
 * the user particular answer here. Use the cascade type persist for a convenient persisting user answer.
 * The fetch type eager is used we always need user data.
 * As we do not store the user answer data in some specific order we need retrieve it in a defined order for
 * subsequent manipulations.
 * <p>
 * The initial value and the allocation size of the sequence generator are assigned explicitly
 * because sometimes the Hibernate provides a negative number
 */
@NamedQueries({
        @NamedQuery(name = "getAllUsers",
                query = "SELECT u FROM UserAnswer u"
        )
})
@Entity
@Table(name = "user_answer")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_answer_id_seq")
    @SequenceGenerator(name = "user_answer_id_seq", sequenceName = "user_answer_id_seq",
            initialValue = 1, allocationSize = 1)
    public Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @org.hibernate.annotations.OrderBy(clause = "field asc")
    public Collection<UserAnswerContent> userAnswerContent = new ArrayList<>();

    public UserAnswer() {
    }
}
