package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddressBookJsonPreprocessorTest {

    @TempDir
    Path tempDir;

    private Path jsonFile;
    private Path invalidFile;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        jsonFile = tempDir.resolve("addressbook.json");
        invalidFile = tempDir.resolve("addressbook_invalid.json");
        mapper = new ObjectMapper();
    }

    @Test
    public void preProcess_validJson_allValidEntriesKept()
            throws Exception {
        String json = """
               {
                  "persons": [
                   {
                     "name" : "John Doe",
                     "phone" : "12345678",
                     "email" : "john@test.com",
                     "address" : "Tampines Ave 1",
                     "interviewed" : true,
                     "remark" : "Strong",
                     "tags" : [ "colleagues" ]
                   }
                  ]
                }
               """;

        Files.writeString(jsonFile, json);

        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);
        preprocessor.preProcess();

        JsonNode result = mapper.readTree(jsonFile.toFile());
        assertEquals(1, result.get("persons").size());

        JsonNode person = result.get("persons").get(0);
        assertTrue(person.get("interviewed").asBoolean());

        assertTrue(person.has("remark"));
        assertTrue(person.get("tags").isArray());
    }

    @Test
    public void preProcess_missingRequiredFields_entryMovedToInvalid()
            throws Exception {
        String json = """
                {
                  "persons" : [
                   {
                     "name" : "John Doe"
                   }
                  ]
                }
                """;

        Files.writeString(jsonFile, json);

        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);
        preprocessor.preProcess();

        JsonNode invalid = mapper.readTree(invalidFile.toFile());
        assertEquals(1, invalid.get("persons").size());
    }

    @Test
    public void preProcess_stringBoolean_normalizedCorrectly()
            throws Exception {
        String json = """
                {
                  "persons" : [
                   {
                     "name" : "John Doe",
                     "phone" : "12345678",
                     "email" : "john@test.com",
                     "address" : "Tampines Ave 1",
                     "interviewed" : "yes"
                   }
                  ]
                }
                """;

        Files.writeString(jsonFile, json);

        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);
        preprocessor.preProcess();

        JsonNode result = mapper.readTree(jsonFile.toFile());
        JsonNode person = result.get("persons").get(0);

        assertTrue(person.get("interviewed").asBoolean());
    }

    @Test
    public void preProcess_invalidInterviewedValue_entryMovedToInvalid() throws Exception {
        String json = """
                {
                  "persons" : [
                   {
                     "name" : "John Doe",
                     "phone" : "12345678",
                     "email" : "john@test.com",
                     "address" : "Tampines Ave 1",
                     "interviewed" : "maybe"
                   }
                  ]
                }
                """;


        Files.writeString(jsonFile, json);

        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);
        preprocessor.preProcess();

        JsonNode valid = mapper.readTree(jsonFile.toFile());
        JsonNode invalid = mapper.readTree(invalidFile.toFile());

        assertEquals(0, valid.get("persons").size());
        assertEquals(1, invalid.get("persons").size());
        assertEquals("maybe", invalid.get("persons").get(0).get("interviewed").asText());
    }

    @Test
    public void preProcess_missingRemarkAndTags_autoFixed()
            throws Exception {
        String json = """
                {
                  "persons": [
                   {
                     "name" : "Alice",
                     "phone" : "87654321",
                     "email" : "alice@test.com",
                     "address" : "Pasir Ris St 21",
                     "interviewed" : false
                   }
                  ]
                }
                """;

        Files.writeString(jsonFile, json);

        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);
        preprocessor.preProcess();

        JsonNode result = mapper.readTree(jsonFile.toFile());

        JsonNode person = result.get("persons").get(0);
        assertTrue(person.has("remark"));
        assertTrue(person.get("tags").isArray());
    }

    @Test
    public void preProcess_invalidTagValue_entryMovedToInvalid() throws Exception {
        String json = """
                {
                  "persons": [
                   {
                     "name" : "Alice",
                     "phone" : "87654321",
                     "email" : "alice@test.com",
                     "address" : "Pasir Ris St 21",
                     "interviewed" : false,
                     "remark" : "",
                     "tags" : [ "A A" ]
                   }, {
                     "name" : "Bob",
                     "phone" : "91234567",
                     "email" : "bob@test.com",
                     "address" : "Jurong West Ave 6",
                     "interviewed" : true,
                     "remark" : "",
                     "tags" : [ "friend" ]
                   }
                  ]
                }
                """;

        Files.writeString(jsonFile, json);

        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);
        preprocessor.preProcess();

        JsonNode valid = mapper.readTree(jsonFile.toFile());
        JsonNode invalid = mapper.readTree(invalidFile.toFile());

        assertEquals(1, valid.get("persons").size());
        assertEquals("Bob", valid.get("persons").get(0).get("name").asText());
        assertEquals(1, invalid.get("persons").size());
        assertEquals("Alice", invalid.get("persons").get(0).get("name").asText());
    }

    @Test
    public void preProcess_malformedJson_recoveredSuccessfully()
            throws Exception {
        String malformed = """
                {
                  "persons": [
                   {
                     "name" : "John Doe",
                     "phone" : "12345678",
                     "email" : "john@test.com",
                     "address" : "Tampines Ave 1",
                     "interviewed" : true,
                     "remark" : "Strong",
                     "tags" : [ "colleagues" ]
                   {
                     "name" : "Jane Soe",
                     "phone" : "87654321",
                     "email" : "jane@test.com",
                     "address" : "Tampines Ave 1",
                     "interviewed" : true,
                     "remark" : "Strong",
                     "tags" : [ "friends" ]
                   }
                  ]
                }
                """;

        Files.writeString(jsonFile, malformed);

        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);
        preprocessor.preProcess();

        JsonNode invalid = mapper.readTree(invalidFile.toFile());

        assertTrue(invalid.get("persons").size() >= 1);
    }

    @Test
    public void preProcess_emptyFile_noCrash() throws Exception {
        Files.writeString(jsonFile, "");
        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);

        assertDoesNotThrow(preprocessor::preProcess);
    }

    @Test
    public void preProcess_mixedEntries_splitCorrectly()
            throws Exception {
        String json = """
                {
                  "persons" : [
                   {
                     "name" : "Alice",
                     "phone" : "87654321",
                     "email" : "alice@test.com",
                     "address" : "Pasir Ris St 21",
                     "interviewed" : false,
                     "remark" : "Strong",
                     "tags" : [ "friends" ]
                   }, {
                     "name" : "Invalid"
                   }
                  ]
                }
                """;

        Files.writeString(jsonFile, json);

        AddressBookJsonPreprocessor preprocessor =
                new AddressBookJsonPreprocessor(jsonFile);
        preprocessor.preProcess();

        JsonNode valid = mapper.readTree(jsonFile.toFile());
        JsonNode invalid = mapper.readTree(invalidFile.toFile());

        assertEquals(1, valid.get("persons").size());
        assertEquals(1, invalid.get("persons").size());
    }

    @Test
    public void preProcess_partialClosingBrace_singleObjectRecovered()
            throws Exception {
        String malformed = """
            {
              "persons": [
                { "name": "John", "phone": "12345678", "email": "j@test.com",
                  "address": "Addr", "interviewed": true
              ]
            }
            """;

        Files.writeString(jsonFile, malformed);

        AddressBookJsonPreprocessor processor =
                new AddressBookJsonPreprocessor(jsonFile);
        processor.preProcess();

        JsonNode result = mapper.readTree(jsonFile.toFile());

        assertEquals(1, result.get("persons").size());
    }

    @Test
    public void preProcess_partialClosingBrace_partialRecovery()
            throws Exception {
        String malformed = """
            {
              "persons": [
                { "name": "John", "phone": "12345678", "email": "j@test.com",
                  "address": "Addr", "interviewed": true
              ]
            }
            """;

        Files.writeString(jsonFile, malformed);

        AddressBookJsonPreprocessor processor =
                new AddressBookJsonPreprocessor(jsonFile);
        processor.preProcess();

        JsonNode result = mapper.readTree(jsonFile.toFile());

        assertTrue(result.get("persons").size() >= 1);
    }

    @Test
    public void preProcess_unrecoverableGarbage_movedToInvalid()
            throws Exception {
        String malformed = """
            {
              "persons": [
                asdasdasdasd
              ]
            }
            """;

        Files.writeString(jsonFile, malformed);

        AddressBookJsonPreprocessor processor =
                new AddressBookJsonPreprocessor(jsonFile);
        processor.preProcess();

        assertTrue(Files.exists(invalidFile));

        JsonNode invalid = mapper.readTree(invalidFile.toFile());
        assertTrue(invalid.get("persons").size() >= 1);
    }
}
