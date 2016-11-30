package models;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class presents an admin hwo has a login and password which
 * are stored in DB. Creation of this entity is only available from DB.
 * The admin has an access to such areas of the application:
 * '/fields', '/fields/ID_OF_THE_FIELD_ENTITY', '/responses'.
 * <p>
 * An admin authentication is proceed by login form which is available by the rout '/login'
 * or if a user tries to get an access to the pages which were mentioned earlier
 * he would be redirected to the login page. A cookie should be enabled because
 * they would be used for the user authentication after login.
 * <p>
 * The initial value and the allocation size of the sequence generator are assigned explicitly
 * because sometimes the Hibernate provides a negative number
 */
@NamedQueries({
        @NamedQuery(name = "checkLogin",
                query = "SELECT u FROM Admin u WHERE u.login LIKE :login and u.password LIKE :password"
        )
})
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_id_seq")
    @SequenceGenerator(name = "admin_id_seq", sequenceName = "admin_id_seq",
            initialValue = 1, allocationSize = 1)
    public Long id;

    @Constraints.Required
    public String login;

    @Constraints.Required
    public String password;

    public Admin() {
    }

    /**
     * An admin authentication. Just verify the login and password
     *
     * @return An appropriate message if the verification dose not pass.
     */
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        List resultList = JPA.em().createNamedQuery("checkLogin")
                .setParameter("login", this.login)
                .setParameter("password", this.password)
                .getResultList();
        if (resultList.isEmpty()) {
            errors.add(new ValidationError("loginError", "The email or the password is incorrect"));
        }
        return errors.isEmpty() ? null : errors;
    }
}
