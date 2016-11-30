package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class presents a field. It has a such attributes:
 * label - the field name,
 * required - whether an answer for this filed is needed or not,
 * isActive - whether the field is now available for a users,
 * fieldType - what kind of filed this field is.
 * <p>
 * There is a collection of 'adminData' is present here. It has a bunch of convenient properties
 * which are used for a cascading persistence, deletion and update.
 * <p>
 * Also a 'order by' is used for retrieving data in right order.
 * <p>
 * The initial value and the allocation size of the sequence generator are assigned explicitly
 * because sometimes the Hibernate provides a negative number
 */
@NamedQueries({
        @NamedQuery(name = "getAll",
                query = "SELECT f FROM Field f ORDER BY id"
        )
})
@Entity
@Table(name = "field")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "field_id_seq")
    @SequenceGenerator(name = "field_id_seq", sequenceName = "field_id_seq",
            initialValue = 1, allocationSize = 1)
    public Long id;

    @Constraints.Required
    public String label;

    public Boolean required;

    @Column(name = "is_active")
    public Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Constraints.Required
    public Type fieldType;


    @OneToMany(mappedBy = "field", orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @org.hibernate.annotations.OrderBy(clause = "id asc")
    public Collection<AdminData> content = new ArrayList<>();

    public Field() {
    }

    public Field(Long id, String label, Type type, boolean required, boolean isActive) {
        this.id = id;
        this.label = label;
        this.fieldType = type;
        this.required = required;
        this.isActive = isActive;
    }
}
