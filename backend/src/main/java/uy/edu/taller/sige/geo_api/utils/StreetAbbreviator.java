package uy.edu.taller.sige.geo_api.utils;

import java.util.Map;

public class StreetAbbreviator {

    private static final Map<String, String> ABBREVIATIONS = Map.ofEntries(
            Map.entry("AVENIDA", "AV."),
            Map.entry("BULEVAR", "Blvr."),
            Map.entry("BOULEVARD", "Blvr."),
            Map.entry("RAMBLA", "Rbla."),
            Map.entry("CAMINO", "Cno."),
            Map.entry("GENERAL", "Gral."),
            Map.entry("DOCTOR", "DR."),
            Map.entry("PRESIDENTE", "PTE."),
            Map.entry("INGENIERO", "ING.")
    );

    public static String abbreviate(String streetName) {

        if (streetName == null || streetName.isBlank()) {
            return streetName;
        }

        String[] parts = streetName.trim().split("\\s+", 2);

        String firstWord = parts[0].toUpperCase();

        String abbreviation = ABBREVIATIONS.get(firstWord);

        if (abbreviation == null) {
            return null;
        }

        return parts.length > 1
                ? abbreviation + " " + parts[1]
                : abbreviation;
    }
}