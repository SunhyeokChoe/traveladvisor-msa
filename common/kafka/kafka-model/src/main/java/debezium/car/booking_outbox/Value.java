/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package debezium.car.booking_outbox;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class Value extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -4650931779394850009L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Value\",\"namespace\":\"debezium.car.booking_outbox\",\"fields\":[{\"name\":\"id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\",\"connect.version\":1,\"connect.name\":\"io.debezium.data.Uuid\"}},{\"name\":\"saga_action_id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\",\"connect.version\":1,\"connect.name\":\"io.debezium.data.Uuid\"}},{\"name\":\"event_type\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"event_payload\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\",\"connect.version\":1,\"connect.name\":\"io.debezium.data.Json\"}},{\"name\":\"booking_status\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\",\"connect.version\":1,\"connect.parameters\":{\"allowed\":\"CAR_BOOKED,CAR_CANCELLED\"},\"connect.name\":\"io.debezium.data.Enum\"}},{\"name\":\"outbox_status\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\",\"connect.version\":1,\"connect.parameters\":{\"allowed\":\"STARTED,COMPLETED,FAILED\"},\"connect.name\":\"io.debezium.data.Enum\"}},{\"name\":\"created_at\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\",\"connect.version\":1,\"connect.default\":\"1970-01-01T00:00:00.000000Z\",\"connect.name\":\"io.debezium.time.ZonedTimestamp\"},\"default\":\"1970-01-01T00:00:00.000000Z\"},{\"name\":\"updated_at\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\",\"connect.version\":1,\"connect.name\":\"io.debezium.time.ZonedTimestamp\"}],\"default\":null},{\"name\":\"completed_at\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\",\"connect.version\":1,\"connect.name\":\"io.debezium.time.ZonedTimestamp\"}],\"default\":null},{\"name\":\"version\",\"type\":\"int\"}],\"connect.name\":\"debezium.car.booking_outbox.Value\"}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Value> ENCODER =
      new BinaryMessageEncoder<Value>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Value> DECODER =
      new BinaryMessageDecoder<Value>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<Value> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<Value> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<Value> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Value>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this Value to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a Value from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a Value instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static Value fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.String id;
  private java.lang.String saga_action_id;
  private java.lang.String event_type;
  private java.lang.String event_payload;
  private java.lang.String booking_status;
  private java.lang.String outbox_status;
  private java.lang.String created_at;
  private java.lang.String updated_at;
  private java.lang.String completed_at;
  private int version;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Value() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param saga_action_id The new value for saga_action_id
   * @param event_type The new value for event_type
   * @param event_payload The new value for event_payload
   * @param booking_status The new value for booking_status
   * @param outbox_status The new value for outbox_status
   * @param created_at The new value for created_at
   * @param updated_at The new value for updated_at
   * @param completed_at The new value for completed_at
   * @param version The new value for version
   */
  public Value(java.lang.String id, java.lang.String saga_action_id, java.lang.String event_type, java.lang.String event_payload, java.lang.String booking_status, java.lang.String outbox_status, java.lang.String created_at, java.lang.String updated_at, java.lang.String completed_at, java.lang.Integer version) {
    this.id = id;
    this.saga_action_id = saga_action_id;
    this.event_type = event_type;
    this.event_payload = event_payload;
    this.booking_status = booking_status;
    this.outbox_status = outbox_status;
    this.created_at = created_at;
    this.updated_at = updated_at;
    this.completed_at = completed_at;
    this.version = version;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return saga_action_id;
    case 2: return event_type;
    case 3: return event_payload;
    case 4: return booking_status;
    case 5: return outbox_status;
    case 6: return created_at;
    case 7: return updated_at;
    case 8: return completed_at;
    case 9: return version;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = value$ != null ? value$.toString() : null; break;
    case 1: saga_action_id = value$ != null ? value$.toString() : null; break;
    case 2: event_type = value$ != null ? value$.toString() : null; break;
    case 3: event_payload = value$ != null ? value$.toString() : null; break;
    case 4: booking_status = value$ != null ? value$.toString() : null; break;
    case 5: outbox_status = value$ != null ? value$.toString() : null; break;
    case 6: created_at = value$ != null ? value$.toString() : null; break;
    case 7: updated_at = value$ != null ? value$.toString() : null; break;
    case 8: completed_at = value$ != null ? value$.toString() : null; break;
    case 9: version = (java.lang.Integer)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public java.lang.String getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.String value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'saga_action_id' field.
   * @return The value of the 'saga_action_id' field.
   */
  public java.lang.String getSagaActionId() {
    return saga_action_id;
  }


  /**
   * Sets the value of the 'saga_action_id' field.
   * @param value the value to set.
   */
  public void setSagaActionId(java.lang.String value) {
    this.saga_action_id = value;
  }

  /**
   * Gets the value of the 'event_type' field.
   * @return The value of the 'event_type' field.
   */
  public java.lang.String getEventType() {
    return event_type;
  }


  /**
   * Sets the value of the 'event_type' field.
   * @param value the value to set.
   */
  public void setEventType(java.lang.String value) {
    this.event_type = value;
  }

  /**
   * Gets the value of the 'event_payload' field.
   * @return The value of the 'event_payload' field.
   */
  public java.lang.String getEventPayload() {
    return event_payload;
  }


  /**
   * Sets the value of the 'event_payload' field.
   * @param value the value to set.
   */
  public void setEventPayload(java.lang.String value) {
    this.event_payload = value;
  }

  /**
   * Gets the value of the 'booking_status' field.
   * @return The value of the 'booking_status' field.
   */
  public java.lang.String getBookingStatus() {
    return booking_status;
  }


  /**
   * Sets the value of the 'booking_status' field.
   * @param value the value to set.
   */
  public void setBookingStatus(java.lang.String value) {
    this.booking_status = value;
  }

  /**
   * Gets the value of the 'outbox_status' field.
   * @return The value of the 'outbox_status' field.
   */
  public java.lang.String getOutboxStatus() {
    return outbox_status;
  }


  /**
   * Sets the value of the 'outbox_status' field.
   * @param value the value to set.
   */
  public void setOutboxStatus(java.lang.String value) {
    this.outbox_status = value;
  }

  /**
   * Gets the value of the 'created_at' field.
   * @return The value of the 'created_at' field.
   */
  public java.lang.String getCreatedAt() {
    return created_at;
  }


  /**
   * Sets the value of the 'created_at' field.
   * @param value the value to set.
   */
  public void setCreatedAt(java.lang.String value) {
    this.created_at = value;
  }

  /**
   * Gets the value of the 'updated_at' field.
   * @return The value of the 'updated_at' field.
   */
  public java.lang.String getUpdatedAt() {
    return updated_at;
  }


  /**
   * Sets the value of the 'updated_at' field.
   * @param value the value to set.
   */
  public void setUpdatedAt(java.lang.String value) {
    this.updated_at = value;
  }

  /**
   * Gets the value of the 'completed_at' field.
   * @return The value of the 'completed_at' field.
   */
  public java.lang.String getCompletedAt() {
    return completed_at;
  }


  /**
   * Sets the value of the 'completed_at' field.
   * @param value the value to set.
   */
  public void setCompletedAt(java.lang.String value) {
    this.completed_at = value;
  }

  /**
   * Gets the value of the 'version' field.
   * @return The value of the 'version' field.
   */
  public int getVersion() {
    return version;
  }


  /**
   * Sets the value of the 'version' field.
   * @param value the value to set.
   */
  public void setVersion(int value) {
    this.version = value;
  }

  /**
   * Creates a new Value RecordBuilder.
   * @return A new Value RecordBuilder
   */
  public static debezium.car.booking_outbox.Value.Builder newBuilder() {
    return new debezium.car.booking_outbox.Value.Builder();
  }

  /**
   * Creates a new Value RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Value RecordBuilder
   */
  public static debezium.car.booking_outbox.Value.Builder newBuilder(debezium.car.booking_outbox.Value.Builder other) {
    if (other == null) {
      return new debezium.car.booking_outbox.Value.Builder();
    } else {
      return new debezium.car.booking_outbox.Value.Builder(other);
    }
  }

  /**
   * Creates a new Value RecordBuilder by copying an existing Value instance.
   * @param other The existing instance to copy.
   * @return A new Value RecordBuilder
   */
  public static debezium.car.booking_outbox.Value.Builder newBuilder(debezium.car.booking_outbox.Value other) {
    if (other == null) {
      return new debezium.car.booking_outbox.Value.Builder();
    } else {
      return new debezium.car.booking_outbox.Value.Builder(other);
    }
  }

  /**
   * RecordBuilder for Value instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Value>
    implements org.apache.avro.data.RecordBuilder<Value> {

    private java.lang.String id;
    private java.lang.String saga_action_id;
    private java.lang.String event_type;
    private java.lang.String event_payload;
    private java.lang.String booking_status;
    private java.lang.String outbox_status;
    private java.lang.String created_at;
    private java.lang.String updated_at;
    private java.lang.String completed_at;
    private int version;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(debezium.car.booking_outbox.Value.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.saga_action_id)) {
        this.saga_action_id = data().deepCopy(fields()[1].schema(), other.saga_action_id);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.event_type)) {
        this.event_type = data().deepCopy(fields()[2].schema(), other.event_type);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.event_payload)) {
        this.event_payload = data().deepCopy(fields()[3].schema(), other.event_payload);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.booking_status)) {
        this.booking_status = data().deepCopy(fields()[4].schema(), other.booking_status);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
      if (isValidValue(fields()[5], other.outbox_status)) {
        this.outbox_status = data().deepCopy(fields()[5].schema(), other.outbox_status);
        fieldSetFlags()[5] = other.fieldSetFlags()[5];
      }
      if (isValidValue(fields()[6], other.created_at)) {
        this.created_at = data().deepCopy(fields()[6].schema(), other.created_at);
        fieldSetFlags()[6] = other.fieldSetFlags()[6];
      }
      if (isValidValue(fields()[7], other.updated_at)) {
        this.updated_at = data().deepCopy(fields()[7].schema(), other.updated_at);
        fieldSetFlags()[7] = other.fieldSetFlags()[7];
      }
      if (isValidValue(fields()[8], other.completed_at)) {
        this.completed_at = data().deepCopy(fields()[8].schema(), other.completed_at);
        fieldSetFlags()[8] = other.fieldSetFlags()[8];
      }
      if (isValidValue(fields()[9], other.version)) {
        this.version = data().deepCopy(fields()[9].schema(), other.version);
        fieldSetFlags()[9] = other.fieldSetFlags()[9];
      }
    }

    /**
     * Creates a Builder by copying an existing Value instance
     * @param other The existing instance to copy.
     */
    private Builder(debezium.car.booking_outbox.Value other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.saga_action_id)) {
        this.saga_action_id = data().deepCopy(fields()[1].schema(), other.saga_action_id);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.event_type)) {
        this.event_type = data().deepCopy(fields()[2].schema(), other.event_type);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.event_payload)) {
        this.event_payload = data().deepCopy(fields()[3].schema(), other.event_payload);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.booking_status)) {
        this.booking_status = data().deepCopy(fields()[4].schema(), other.booking_status);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.outbox_status)) {
        this.outbox_status = data().deepCopy(fields()[5].schema(), other.outbox_status);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.created_at)) {
        this.created_at = data().deepCopy(fields()[6].schema(), other.created_at);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.updated_at)) {
        this.updated_at = data().deepCopy(fields()[7].schema(), other.updated_at);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.completed_at)) {
        this.completed_at = data().deepCopy(fields()[8].schema(), other.completed_at);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.version)) {
        this.version = data().deepCopy(fields()[9].schema(), other.version);
        fieldSetFlags()[9] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public java.lang.String getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setId(java.lang.String value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'saga_action_id' field.
      * @return The value.
      */
    public java.lang.String getSagaActionId() {
      return saga_action_id;
    }


    /**
      * Sets the value of the 'saga_action_id' field.
      * @param value The value of 'saga_action_id'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setSagaActionId(java.lang.String value) {
      validate(fields()[1], value);
      this.saga_action_id = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'saga_action_id' field has been set.
      * @return True if the 'saga_action_id' field has been set, false otherwise.
      */
    public boolean hasSagaActionId() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'saga_action_id' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearSagaActionId() {
      saga_action_id = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'event_type' field.
      * @return The value.
      */
    public java.lang.String getEventType() {
      return event_type;
    }


    /**
      * Sets the value of the 'event_type' field.
      * @param value The value of 'event_type'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setEventType(java.lang.String value) {
      validate(fields()[2], value);
      this.event_type = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'event_type' field has been set.
      * @return True if the 'event_type' field has been set, false otherwise.
      */
    public boolean hasEventType() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'event_type' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearEventType() {
      event_type = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'event_payload' field.
      * @return The value.
      */
    public java.lang.String getEventPayload() {
      return event_payload;
    }


    /**
      * Sets the value of the 'event_payload' field.
      * @param value The value of 'event_payload'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setEventPayload(java.lang.String value) {
      validate(fields()[3], value);
      this.event_payload = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'event_payload' field has been set.
      * @return True if the 'event_payload' field has been set, false otherwise.
      */
    public boolean hasEventPayload() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'event_payload' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearEventPayload() {
      event_payload = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'booking_status' field.
      * @return The value.
      */
    public java.lang.String getBookingStatus() {
      return booking_status;
    }


    /**
      * Sets the value of the 'booking_status' field.
      * @param value The value of 'booking_status'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setBookingStatus(java.lang.String value) {
      validate(fields()[4], value);
      this.booking_status = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'booking_status' field has been set.
      * @return True if the 'booking_status' field has been set, false otherwise.
      */
    public boolean hasBookingStatus() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'booking_status' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearBookingStatus() {
      booking_status = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'outbox_status' field.
      * @return The value.
      */
    public java.lang.String getOutboxStatus() {
      return outbox_status;
    }


    /**
      * Sets the value of the 'outbox_status' field.
      * @param value The value of 'outbox_status'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setOutboxStatus(java.lang.String value) {
      validate(fields()[5], value);
      this.outbox_status = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'outbox_status' field has been set.
      * @return True if the 'outbox_status' field has been set, false otherwise.
      */
    public boolean hasOutboxStatus() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'outbox_status' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearOutboxStatus() {
      outbox_status = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'created_at' field.
      * @return The value.
      */
    public java.lang.String getCreatedAt() {
      return created_at;
    }


    /**
      * Sets the value of the 'created_at' field.
      * @param value The value of 'created_at'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setCreatedAt(java.lang.String value) {
      validate(fields()[6], value);
      this.created_at = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'created_at' field has been set.
      * @return True if the 'created_at' field has been set, false otherwise.
      */
    public boolean hasCreatedAt() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'created_at' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearCreatedAt() {
      created_at = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
      * Gets the value of the 'updated_at' field.
      * @return The value.
      */
    public java.lang.String getUpdatedAt() {
      return updated_at;
    }


    /**
      * Sets the value of the 'updated_at' field.
      * @param value The value of 'updated_at'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setUpdatedAt(java.lang.String value) {
      validate(fields()[7], value);
      this.updated_at = value;
      fieldSetFlags()[7] = true;
      return this;
    }

    /**
      * Checks whether the 'updated_at' field has been set.
      * @return True if the 'updated_at' field has been set, false otherwise.
      */
    public boolean hasUpdatedAt() {
      return fieldSetFlags()[7];
    }


    /**
      * Clears the value of the 'updated_at' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearUpdatedAt() {
      updated_at = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /**
      * Gets the value of the 'completed_at' field.
      * @return The value.
      */
    public java.lang.String getCompletedAt() {
      return completed_at;
    }


    /**
      * Sets the value of the 'completed_at' field.
      * @param value The value of 'completed_at'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setCompletedAt(java.lang.String value) {
      validate(fields()[8], value);
      this.completed_at = value;
      fieldSetFlags()[8] = true;
      return this;
    }

    /**
      * Checks whether the 'completed_at' field has been set.
      * @return True if the 'completed_at' field has been set, false otherwise.
      */
    public boolean hasCompletedAt() {
      return fieldSetFlags()[8];
    }


    /**
      * Clears the value of the 'completed_at' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearCompletedAt() {
      completed_at = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /**
      * Gets the value of the 'version' field.
      * @return The value.
      */
    public int getVersion() {
      return version;
    }


    /**
      * Sets the value of the 'version' field.
      * @param value The value of 'version'.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder setVersion(int value) {
      validate(fields()[9], value);
      this.version = value;
      fieldSetFlags()[9] = true;
      return this;
    }

    /**
      * Checks whether the 'version' field has been set.
      * @return True if the 'version' field has been set, false otherwise.
      */
    public boolean hasVersion() {
      return fieldSetFlags()[9];
    }


    /**
      * Clears the value of the 'version' field.
      * @return This builder.
      */
    public debezium.car.booking_outbox.Value.Builder clearVersion() {
      fieldSetFlags()[9] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Value build() {
      try {
        Value record = new Value();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.String) defaultValue(fields()[0]);
        record.saga_action_id = fieldSetFlags()[1] ? this.saga_action_id : (java.lang.String) defaultValue(fields()[1]);
        record.event_type = fieldSetFlags()[2] ? this.event_type : (java.lang.String) defaultValue(fields()[2]);
        record.event_payload = fieldSetFlags()[3] ? this.event_payload : (java.lang.String) defaultValue(fields()[3]);
        record.booking_status = fieldSetFlags()[4] ? this.booking_status : (java.lang.String) defaultValue(fields()[4]);
        record.outbox_status = fieldSetFlags()[5] ? this.outbox_status : (java.lang.String) defaultValue(fields()[5]);
        record.created_at = fieldSetFlags()[6] ? this.created_at : (java.lang.String) defaultValue(fields()[6]);
        record.updated_at = fieldSetFlags()[7] ? this.updated_at : (java.lang.String) defaultValue(fields()[7]);
        record.completed_at = fieldSetFlags()[8] ? this.completed_at : (java.lang.String) defaultValue(fields()[8]);
        record.version = fieldSetFlags()[9] ? this.version : (java.lang.Integer) defaultValue(fields()[9]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Value>
    WRITER$ = (org.apache.avro.io.DatumWriter<Value>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Value>
    READER$ = (org.apache.avro.io.DatumReader<Value>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.id);

    out.writeString(this.saga_action_id);

    out.writeString(this.event_type);

    out.writeString(this.event_payload);

    out.writeString(this.booking_status);

    out.writeString(this.outbox_status);

    out.writeString(this.created_at);

    if (this.updated_at == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeString(this.updated_at);
    }

    if (this.completed_at == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeString(this.completed_at);
    }

    out.writeInt(this.version);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readString();

      this.saga_action_id = in.readString();

      this.event_type = in.readString();

      this.event_payload = in.readString();

      this.booking_status = in.readString();

      this.outbox_status = in.readString();

      this.created_at = in.readString();

      if (in.readIndex() != 1) {
        in.readNull();
        this.updated_at = null;
      } else {
        this.updated_at = in.readString();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.completed_at = null;
      } else {
        this.completed_at = in.readString();
      }

      this.version = in.readInt();

    } else {
      for (int i = 0; i < 10; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readString();
          break;

        case 1:
          this.saga_action_id = in.readString();
          break;

        case 2:
          this.event_type = in.readString();
          break;

        case 3:
          this.event_payload = in.readString();
          break;

        case 4:
          this.booking_status = in.readString();
          break;

        case 5:
          this.outbox_status = in.readString();
          break;

        case 6:
          this.created_at = in.readString();
          break;

        case 7:
          if (in.readIndex() != 1) {
            in.readNull();
            this.updated_at = null;
          } else {
            this.updated_at = in.readString();
          }
          break;

        case 8:
          if (in.readIndex() != 1) {
            in.readNull();
            this.completed_at = null;
          } else {
            this.completed_at = in.readString();
          }
          break;

        case 9:
          this.version = in.readInt();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










