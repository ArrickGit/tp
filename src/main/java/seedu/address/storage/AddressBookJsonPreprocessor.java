package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Remark;

/**
 * Preprocesses the address book JSON file by validating, fixing and separating entries.
 * <p>
 * This class reads a JSOn file containing a list of persons, validates each entry,
 * attempts to auto-fix minor issues, and separates invalid or unrecoverable
 * entries into a different file.
 * <p>
 * Valid entries are rewritten to the original file, while invalid
 * entries are stored in a separate file named
 * {@code address_invalid.json} in the same directory.
 */
public class AddressBookJsonPreprocessor {

    private static final Logger logger = LogsCenter.getLogger(AddressBookJsonPreprocessor.class);
    private static final String[] REQUIRED_FIELDS = {"name", "phone", "email", "address", "interviewed"};

    private static final String[] TRUE_VALUES = {"true", "yes", "y", "1", "t"};
    private static final String[] FALSE_VALUES = {"false", "no", "n", "0", "f"};

    private final Path jsonFilePath;
    private final Path invalidFilePath;
    private final ObjectMapper mapper;
    private final ArrayNode validPersons;
    private final ArrayNode invalidPersons;

    /**
     * Constructs a preprocessor for the
     * specified JSON file path.
     *
     * @param jsonFilePath Path to the address book
     *                     JSON file.
     */
    public AddressBookJsonPreprocessor(Path jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
        this.invalidFilePath = jsonFilePath.getParent().resolve("addressbook_invalid.json");
        this.mapper = new ObjectMapper();
        this.validPersons = mapper.createArrayNode();
        this.invalidPersons = mapper.createArrayNode();
    }

    /**
     * Executes the preprocessing pipeline:
     * <ul>
     *     <li>Reads the JSON file</li>
     *     <li>Parses its contents</li>
     *     <li>Processes and classifies entries</li>
     *     <li>Writes valid entries back to
     *     the original file</li>
     *     <li>Writes invalid entries to a
     *     separate file (if any)</li>
     * </ul>
     */
    public void preProcess() {
        readJsonContent()
                .flatMap(this::parseJson)
                .ifPresentOrElse(this::processEntries, () ->
                        logger.log(Level.WARNING, "JSON unreadable."
                                + "Starting empty."));

        writeJson(jsonFilePath, validPersons);
        if (invalidPersons.size() > 0) {
            writeJson(invalidFilePath, invalidPersons);
            logger.log(Level.INFO, "Invalid entries saved to: "
                    + invalidFilePath);
        }

        logger.log(Level.INFO, "Preprocessing complete: " + jsonFilePath);
    }

    private Optional<String> readJsonContent() {
        try {
            return Optional.of(Files.readString(jsonFilePath));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read JSON file: " + jsonFilePath);
            return Optional.empty();
        }
    }

    /**
     * Parses raw JSON content into a {@link JsonNode}.
     * <p>
     * If the JSON is malformed, attempts recovery.
     *
     * @param content Raw JSON string
     * @return An {@code Optional} containing the
     *          parsed {@code JsonNode}, or empty if parsing fails.
     */
    private Optional<JsonNode> parseJson(String content) {
        try {
            return Optional.of(mapper.readTree(content));
        } catch (JsonProcessingException e) {
            logger.log(Level.WARNING, "Malformed JSON. Attempting recovery...");
            return attemptedRecovery(content);
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "Error parsing JSON" + ioException);
            return Optional.empty();
        }
    }

    /**
     * Processes the root JSON node by iterating
     * through all person entries.
     *
     * @param rootNode Root JSON node containing the
     *                 "person" array
     */
    private void processEntries(JsonNode rootNode) {
        StreamSupport.stream(rootNode.path("persons").spliterator(), false)
                .forEach(this::handleNode);
    }

    private void handleNode(JsonNode node) {
        if (!(node instanceof ObjectNode objectNode)) {
            logger.log(Level.SEVERE, "Non-object entry: " + node.toString());
            invalidPersons.add(mapper.createObjectNode().put("raw", node.toString()));
            return;
        }
        classifyPerson(objectNode);
    }

    private void classifyPerson(ObjectNode node) {
        ObjectNode fixedNode = autoFixPerson(node.deepCopy());
        if (!normalizeInterviewedIfRecognized(fixedNode)) {
            logger.log(Level.WARNING, "Invalid interviewed value detected: "
                    + node.toString());
            invalidPersons.add(node.deepCopy());
        } else if (isValidPerson(fixedNode) && isModelCompatible(fixedNode)) {
            validPersons.add(fixedNode);
        } else if (isUnfixable(node)) {
            logger.log(Level.SEVERE, "Missing required field(s): " + node.toString());
            invalidPersons.add(node);
        } else {
            logger.log(Level.WARNING, "Malformed field(s) detected: " + node.toString());
            invalidPersons.add(node.deepCopy());
        }
    }

    private boolean isUnfixable(ObjectNode node) {
        return Arrays.stream(REQUIRED_FIELDS)
                .anyMatch(field -> !node.has(field));
    }

    private boolean isValidPerson(ObjectNode node) {
        return isTextual(node, "name")
                && isTextual(node, "phone")
                && isTextual(node, "email")
                && isTextual(node, "address")
                && isBoolean(node, "interviewed");
    }

    /**
     * Automatically fixes minor issues in a
     * valid person entry.
     * <p>
     * Fixes includes:
     * <ul>
     *     <li>Adding a default remark if missing
     *     or invalid</li>
     *     <li>Ensuring tags field is an array</li>
     * </ul>
     *
     * @param node Person entry
     * @return The modified {@link ObjectNode}
     */
    private ObjectNode autoFixPerson(ObjectNode node) {
        if (!node.has("remark") || !node.get("remark").isTextual()) {
            node.put("remark", Remark.DEFAULT_REMARK);
        }

        if (!node.has("tags") || !node.get("tags").isArray()) {
            node.putArray("tags");
        }

        return node;
    }

    private boolean isModelCompatible(ObjectNode node) {
        try {
            mapper.treeToValue(node, JsonAdaptedPerson.class).toModelType();
            return true;
        } catch (IllegalValueException | JsonProcessingException e) {
            logger.log(Level.WARNING, "Person entry failed model validation: "
                    + node, e);
            return false;
        }
    }

    /**
     * Attempts to recover malformed JSON content by
     * extracting and reconstructing individual person
     * objects.
     * <p>
     * Missing required fields are filled with
     * default values:
     * <ul>
     *     <li>Empty string for textual fields</li>
     *     <li>{@code false} for boolean fields</li>
     * </ul>
     *
     * Unrecoverable entries are added to the
     * invalid person list.
     *
     * @param content Malformed JSON content
     * @return an {@code Optional} containing
     *          a reconstructed JSON root node.
     */
    private Optional<JsonNode> attemptedRecovery(String content) {
        ArrayNode recovered = mapper.createArrayNode();

        String personArray = content.replaceAll("(?s).*\"persons\"\\s*:\\s*\\[(.*)\\].*", "$1");

        String[] entries = personArray.split("(?<=\\}),(?=\\s*\\{)");
        for (String obj : entries) {
            obj = obj.trim();
            if (!obj.startsWith("{")) {
                obj = "{" + obj;
            }
            if (!obj.endsWith("}")) {
                obj = obj + "}";
            }

            try {
                ObjectNode node = (ObjectNode) mapper.readTree(obj);

                for (String field : REQUIRED_FIELDS) {
                    if (!node.has(field) && field.equals("interviewed")) {
                        node.put(field, false);
                    } else if (!node.has(field)) {
                        node.put(field, "");
                    }
                }

                if (!node.has("remark") || !node.get("remark").isTextual()) {
                    node.put("remark", Remark.DEFAULT_REMARK);
                }

                if (!node.has("tags") || !node.get("tags").isArray()) {
                    node.putArray("tags");
                }

                recovered.add(node);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not recover object: " + obj);
                invalidPersons.add(mapper.createObjectNode().put("raw", obj));
            }
        }
        ObjectNode root = mapper.createObjectNode();
        root.set("persons", recovered);
        return Optional.of(root);
    }

    private boolean isTextual(ObjectNode node, String field) {
        return node.has(field) && node.get(field).isTextual();
    }

    private boolean isBoolean(ObjectNode node, String field) {
        return node.has(field) && node.get(field).isBoolean();
    }

    private boolean normalizeInterviewedIfRecognized(ObjectNode node) {
        if (!node.has("interviewed") || node.get("interviewed").isNull()) {
            node.put("interviewed", false);
            return true;
        }

        JsonNode value = node.get("interviewed");
        return normalizeBooleanValue(node, value);
    }

    private boolean normalizeBooleanValue(ObjectNode node, JsonNode value) {
        if (value == null || value.isNull()) {
            node.put("interviewed", false);
            return true;
        }

        if (value.isBoolean()) {
            return true;
        }

        if (value.isTextual()) {
            String text = value.asText().trim().toLowerCase();
            if (Arrays.asList(TRUE_VALUES).contains(text)) {
                node.put("interviewed", true);
                return true;
            }

            if (Arrays.asList(FALSE_VALUES).contains(text)) {
                node.put("interviewed", false);
                return true;
            }

            return false;
        }

        if (value.isNumber()) {
            int normalized = value.asInt();
            if (normalized == 1) {
                node.put("interviewed", true);
                return true;
            }

            if (normalized == 0) {
                node.put("interviewed", false);
                return true;
            }
            return false;
        }

        return false;
    }

    private void writeJson(Path path, ArrayNode persons) {

        ObjectNode root = mapper.createObjectNode();
        root.set("persons", persons);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), root);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to write JSON file: " + path, e);
        }
    }
}
