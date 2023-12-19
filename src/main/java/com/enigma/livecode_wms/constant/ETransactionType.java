package com.enigma.livecode_wms.constant;

import java.util.Optional;

public enum ETransactionType {

    EAT_IN,
    ONLINE,
    TAKE_AWAY;

    public static Optional<ETransactionType> fromString(String value) {
        // Iterasi melalui semua nilai enumerasi ETransactionType
        for (ETransactionType type : ETransactionType.values()) {
            // Membandingkan nama dari setiap nilai enumerasi dengan nilai string (case-insensitive)
            if (type.name().equalsIgnoreCase(value)) {
                // Jika ada kecocokan, mengembalikan Optional yang berisi nilai enumerasi tersebut
                return Optional.of(type);
            }
        }
        // Jika tidak ada kecocokan, mengembalikan Optional kosong
        return Optional.empty();
    }


}
