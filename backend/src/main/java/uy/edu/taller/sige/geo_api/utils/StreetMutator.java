package uy.edu.taller.sige.geo_api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StreetMutator {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String mutate(String streetName) {
        if (streetName == null || streetName.isBlank()) {
            return streetName;
        }

        int distance = streetName.length() > 10 ? 2 : 1;

        Random random = new Random(streetName.hashCode());

        String result = streetName.toUpperCase();

        for (int i = 0; i < distance; i++) {
            result = applyEdit(result, random);
        }

        return result;
    }

    private static String applyEdit(String text, Random random) {
        List<Integer> letterPositions = getLetterPositions(text);

        if (letterPositions.isEmpty()) {
            return text;
        }

        boolean replace = random.nextBoolean();

        if (replace) {
            return replaceLetter(text, random, letterPositions);
        }

        // Evita dejar la cadena vacía
        if (letterPositions.size() == 1) {
            return replaceLetter(text, random, letterPositions);
        }

        return removeLetter(text, random, letterPositions);
    }

    private static String replaceLetter(
            String text,
            Random random,
            List<Integer> letterPositions) {

        int pos = letterPositions.get(
                random.nextInt(letterPositions.size()));

        char current = text.charAt(pos);

        char replacement;
        do {
            replacement = LETTERS.charAt(
                    random.nextInt(LETTERS.length()));
        } while (replacement == current);

        StringBuilder sb = new StringBuilder(text);
        sb.setCharAt(pos, replacement);

        return sb.toString();
    }

    private static String removeLetter(
            String text,
            Random random,
            List<Integer> letterPositions) {

        int pos = letterPositions.get(
                random.nextInt(letterPositions.size()));

        return text.substring(0, pos)
                + text.substring(pos + 1);
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
