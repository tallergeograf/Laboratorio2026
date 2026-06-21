package uy.edu.taller.sige.geo_api.utils;

import uy.edu.taller.sige.geo_api.model.enums.AddressType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StreetMutator {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    public record MutationResult(String mutated, AddressType type) {}

    public static String mutate(String streetName) {
        return mutateWithType(streetName).mutated();
    }

    public static MutationResult mutateWithType(String streetName) {
        if (streetName == null || streetName.isBlank()) {
            return new MutationResult(streetName, AddressType.ERR1_S);
        }

        int distance = streetName.length() > 10 ? 2 : 1;
        Random random = new Random(streetName.hashCode());
        String result = streetName;
        StringBuilder ops = new StringBuilder();

        for (int i = 0; i < distance; i++) {
            List<Integer> letterPositions = getLetterPositions(result);

            if (letterPositions.isEmpty()) break;

            boolean replace = random.nextBoolean();

            if (!replace && letterPositions.size() == 1) {
                replace = true;
            }

            if (replace) {
                result = replaceLetter(result, random, letterPositions);
                ops.append("S");
            } else {
                result = removeLetter(result, random, letterPositions);
                ops.append("B");
            }
        }

        String typeKey = "ERR" + distance + "_" + ops;
        AddressType type;
        try {
            type = AddressType.valueOf(typeKey);
        } catch (IllegalArgumentException e) {
            type = AddressType.ERR1_S;
        }

        return new MutationResult(result, type);
    }

    private static String replaceLetter(String text, Random random, List<Integer> letterPositions) {
        int pos = letterPositions.get(random.nextInt(letterPositions.size()));
        char current = text.charAt(pos);
        char replacement;
        do {
            replacement = LETTERS.charAt(random.nextInt(LETTERS.length()));
        } while (replacement == current);
        StringBuilder sb = new StringBuilder(text);
        sb.setCharAt(pos, replacement);
        return sb.toString();
    }

    private static String removeLetter(String text, Random random, List<Integer> letterPositions) {
        int pos = letterPositions.get(random.nextInt(letterPositions.size()));
        return text.substring(0, pos) + text.substring(pos + 1);
    }

    private static List<Integer> getLetterPositions(String text) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                positions.add(i);
            }
        }
        return positions;
    }
}