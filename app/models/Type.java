package models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An enum which presents types which can be chosen
 * for a particular {@link Field}. Each {@link Type} has it is own {@code String}
 * representation adapted for a user.
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

    /**
     * A convenient method to get a {@code List<String>} of the {@link Type}.
     *
     * @return A {@code List<String>} which represent this {@link Type}
     */
    public static List<String> asList() {
        return Arrays.stream(Type.values())
                .map(Enum::toString).collect(Collectors.toList());
    }
}
