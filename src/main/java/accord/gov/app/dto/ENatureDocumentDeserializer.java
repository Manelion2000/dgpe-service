package accord.gov.app.dto;

import accord.gov.app.enums.ENatureDocument;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * @author : <A HREF="mailto:abdraman.bakouan@gmail.com">Abdramane BAKOUAN (ManeLion2000)</A>
 * @version : 1.0
 * Copyright (c) 2025 All rights reserved.
 * @Project : traiteAccordService
 * @since : 02/10/2025 à 23:54
 */
public class ENatureDocumentDeserializer extends StdDeserializer<ENatureDocument> {
    public ENatureDocumentDeserializer() {
        super(ENatureDocument.class);
    }

    @Override
    public ENatureDocument deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return ENatureDocument.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Valeur invalide => null ou lever une exception personnalisée
            return null;
        }
    }
}
