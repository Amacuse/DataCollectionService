package models;

import javax.persistence.*;

/**
 * This class presents data which admin chooses when created a field.
 * An attribute 'content' in fact presents data.
 * <p>
 * There is a foreign key to the table 'Field'
 * <p>
 * The initial value and the allocation size of the sequence generator are assigned explicitly
 * because sometimes the Hibernate provides a negative number
 */
@Entity
@Table(name = "admin_data")
public class AdminData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_data_id_seq")
    @SequenceGenerator(name = "admin_data_id_seq", sequenceName = "admin_data_id_seq",
            initialValue = 1, allocationSize = 1)
    public Long id;

    public String content;

    @ManyToOne
    public Field field;

    public AdminData() {
    }

    public AdminData(String content, Field field) {
        this.content = content;
        this.field = field;
    }
}
