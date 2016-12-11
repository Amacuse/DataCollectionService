package models;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An enum which presents types which can be chosen
 * for a particular {@link Field}. Each {@link Type} has it is own {@code String}
 * representation adapted for a user.
 * There is a TYPE_MAP field which will be used to make 'option - value' pairs
 * on the {@link views.html.fieldEdit} page
 */
public enum Type {
    SINGLE_LINE_TEXT {
        @Override
        public String toString() {
            return "Single line text";
        }
    },
    MULTI_LINE_TEXT {
        @Override
        public String toString() {
            return "Multi line text";
        }
    },
    RADIO_BUTTON {
        @Override
        public String toString() {
            return "Radio button";
        }
    },
    CHECK_BOX {
        @Override
        public String toString() {
            return "Check box";
        }
    },
    COMBO_BOX {
        @Override
        public String toString() {
            return "Combo box";
        }
    },
    DATE {
        @Override
        public String toString() {
            return "Date";
        }
    },
    SLIDER {
        @Override
        public String toString() {
            return "Slider";
        }
    };

    public static final Map<String, String> TYPE_MAP = new LinkedHashMap<>();

    static {
        Arrays.asList(Type.values()).
                forEach(type ->
                        TYPE_MAP.put(type.toString(), type.toString()));
    }

    /**
     * Converts a {@code String} representation of this {@link Type}.
     *
     * @param type A {@code String} that presents a {@link Type}
     * @return {@link Type}
     */
    public static Type valueOfCustom(String type) {
        String[] split = type.toUpperCase().split("[ ]");
        String tmp = "";
        for (int i = 0; i < split.length; i++) {
            tmp += split[i];
            if (i != split.length - 1) {
                tmp += "_";
            }
        }
        return Type.valueOf(tmp);
    }
}
