package models;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A DTO for a {@link Field}. Constructing {@link FieldDto}
 * from the given {@link Field}. There is a {@code String} representation
 * of the {@link Type} is used and also a {@code String} with '\n' delimiter
 * is used for present a {@link AdminData}
 * <p>
 * There are two methods here. The first one validates the {@link Field} that comes from a user
 * and the second one that is used for creating the {@link Field}.
 */
public class FieldDto {

    @Constraints.Required
    public String label;

    public Boolean required;

    public Boolean isActive;

    @Constraints.Required
    public String fieldType = "Single line text";

    public String options = "";

    public FieldDto() {
    }

    public FieldDto(Field field) {
        this.label = field.label;
        this.required = field.required;
        this.isActive = field.isActive;
        this.fieldType = field.fieldType.toString();

        if (!field.content.isEmpty()) {
            for (AdminData data : field.content) {
                options += data.content + "\n";
            }
            //remove the '\n' at the end
            options = options.trim();
        }
    }

    /**
     * This method for converting data from form to a {@link Field}
     *
     * @param field A field that would be populated with a data from the form.
     */
    public void populateField(Field field) {
        field.label = label;

        //if user does not choose it is set to null
        //so should handle this
        field.required = required != null;
        field.isActive = isActive != null;

        //convert from String
        Type type = Type.valueOfCustom(fieldType);
        field.fieldType = type;

        //construct a list of options that admin has chose
        if (type.equals(Type.RADIO_BUTTON) || type.equals(Type.CHECK_BOX) ||
                type.equals(Type.COMBO_BOX)) {

            String[] split = this.options.split("[\n]");

            //delete empty strings if user typed with '\n\r'
            List<String> userInput = Arrays.stream(split)
                    .filter(s -> !s.equals("\r"))
                    .collect(Collectors.toList());

            //new field, just adding
            if (field.id == null) {
                field.content.addAll(userInput.stream()
                        .map(s -> new AdminData(s, field))
                        .collect(Collectors.toList()));
            } else {//update existing entity

                //retrieve stored data
                Collection<AdminData> storedContent = field.content;

                Iterator<AdminData> contentItr = storedContent.iterator();
                Iterator<String> userInputItr = userInput.iterator();

                //if both stored data and new one have a content
                while (contentItr.hasNext() && userInputItr.hasNext()) {
                    AdminData next = contentItr.next();
                    next.content = userInputItr.next();
                }

                //admin add some new options
                if (storedContent.size() < userInput.size()) {
                    while (userInputItr.hasNext()) {
                        String next = userInputItr.next();
                        storedContent.add(new AdminData(next, field));
                    }
                }

                //admin delete some existing options
                if (storedContent.size() > userInput.size()) {

                    List<AdminData> listForRemove = new ArrayList<>();

                    while (contentItr.hasNext()) {
                        AdminData next = contentItr.next();
                        listForRemove.add(next);
                    }

                    storedContent.removeAll(listForRemove);
                }
            }
        }
    }

    /**
     * Validate whether an options were chosen if admin chose a
     * {@link Type} 'RADIO_BUTTON', 'CHECK_BOX' or 'COMBO_BOX'.
     *
     * @return An appropriate message.
     */
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        Type type = Type.valueOfCustom(fieldType);
        if (options.isEmpty() && (type.equals(Type.RADIO_BUTTON) ||
                type.equals(Type.CHECK_BOX) || type.equals(Type.COMBO_BOX))) {
            errors.add(new ValidationError("options", "This type of field must have an options"));
        }
        return errors.isEmpty() ? null : errors;
    }
}
