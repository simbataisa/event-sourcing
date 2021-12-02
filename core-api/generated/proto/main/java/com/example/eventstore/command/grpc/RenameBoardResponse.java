// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: BoardCommandProto.proto

package com.example.eventstore.command.grpc;

/**
 * Protobuf type {@code model.RenameBoardResponse}
 */
public final class RenameBoardResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:model.RenameBoardResponse)
    RenameBoardResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RenameBoardResponse.newBuilder() to construct.
  private RenameBoardResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RenameBoardResponse() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new RenameBoardResponse();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private RenameBoardResponse(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            com.example.eventstore.command.grpc.Board.Builder subBuilder = null;
            if (board_ != null) {
              subBuilder = board_.toBuilder();
            }
            board_ = input.readMessage(com.example.eventstore.command.grpc.Board.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(board_);
              board_ = subBuilder.buildPartial();
            }

            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.example.eventstore.command.grpc.BoardCommandProto.internal_static_model_RenameBoardResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.example.eventstore.command.grpc.BoardCommandProto.internal_static_model_RenameBoardResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.example.eventstore.command.grpc.RenameBoardResponse.class, com.example.eventstore.command.grpc.RenameBoardResponse.Builder.class);
  }

  public static final int BOARD_FIELD_NUMBER = 1;
  private com.example.eventstore.command.grpc.Board board_;
  /**
   * <code>.model.Board board = 1;</code>
   * @return Whether the board field is set.
   */
  @java.lang.Override
  public boolean hasBoard() {
    return board_ != null;
  }
  /**
   * <code>.model.Board board = 1;</code>
   * @return The board.
   */
  @java.lang.Override
  public com.example.eventstore.command.grpc.Board getBoard() {
    return board_ == null ? com.example.eventstore.command.grpc.Board.getDefaultInstance() : board_;
  }
  /**
   * <code>.model.Board board = 1;</code>
   */
  @java.lang.Override
  public com.example.eventstore.command.grpc.BoardOrBuilder getBoardOrBuilder() {
    return getBoard();
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (board_ != null) {
      output.writeMessage(1, getBoard());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (board_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getBoard());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.example.eventstore.command.grpc.RenameBoardResponse)) {
      return super.equals(obj);
    }
    com.example.eventstore.command.grpc.RenameBoardResponse other = (com.example.eventstore.command.grpc.RenameBoardResponse) obj;

    if (hasBoard() != other.hasBoard()) return false;
    if (hasBoard()) {
      if (!getBoard()
          .equals(other.getBoard())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasBoard()) {
      hash = (37 * hash) + BOARD_FIELD_NUMBER;
      hash = (53 * hash) + getBoard().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.eventstore.command.grpc.RenameBoardResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.example.eventstore.command.grpc.RenameBoardResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code model.RenameBoardResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:model.RenameBoardResponse)
      com.example.eventstore.command.grpc.RenameBoardResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.example.eventstore.command.grpc.BoardCommandProto.internal_static_model_RenameBoardResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.example.eventstore.command.grpc.BoardCommandProto.internal_static_model_RenameBoardResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.example.eventstore.command.grpc.RenameBoardResponse.class, com.example.eventstore.command.grpc.RenameBoardResponse.Builder.class);
    }

    // Construct using com.example.eventstore.command.grpc.RenameBoardResponse.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (boardBuilder_ == null) {
        board_ = null;
      } else {
        board_ = null;
        boardBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.example.eventstore.command.grpc.BoardCommandProto.internal_static_model_RenameBoardResponse_descriptor;
    }

    @java.lang.Override
    public com.example.eventstore.command.grpc.RenameBoardResponse getDefaultInstanceForType() {
      return com.example.eventstore.command.grpc.RenameBoardResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.example.eventstore.command.grpc.RenameBoardResponse build() {
      com.example.eventstore.command.grpc.RenameBoardResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.example.eventstore.command.grpc.RenameBoardResponse buildPartial() {
      com.example.eventstore.command.grpc.RenameBoardResponse result = new com.example.eventstore.command.grpc.RenameBoardResponse(this);
      if (boardBuilder_ == null) {
        result.board_ = board_;
      } else {
        result.board_ = boardBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.example.eventstore.command.grpc.RenameBoardResponse) {
        return mergeFrom((com.example.eventstore.command.grpc.RenameBoardResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.example.eventstore.command.grpc.RenameBoardResponse other) {
      if (other == com.example.eventstore.command.grpc.RenameBoardResponse.getDefaultInstance()) return this;
      if (other.hasBoard()) {
        mergeBoard(other.getBoard());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.example.eventstore.command.grpc.RenameBoardResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.example.eventstore.command.grpc.RenameBoardResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.example.eventstore.command.grpc.Board board_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.example.eventstore.command.grpc.Board, com.example.eventstore.command.grpc.Board.Builder, com.example.eventstore.command.grpc.BoardOrBuilder> boardBuilder_;
    /**
     * <code>.model.Board board = 1;</code>
     * @return Whether the board field is set.
     */
    public boolean hasBoard() {
      return boardBuilder_ != null || board_ != null;
    }
    /**
     * <code>.model.Board board = 1;</code>
     * @return The board.
     */
    public com.example.eventstore.command.grpc.Board getBoard() {
      if (boardBuilder_ == null) {
        return board_ == null ? com.example.eventstore.command.grpc.Board.getDefaultInstance() : board_;
      } else {
        return boardBuilder_.getMessage();
      }
    }
    /**
     * <code>.model.Board board = 1;</code>
     */
    public Builder setBoard(com.example.eventstore.command.grpc.Board value) {
      if (boardBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        board_ = value;
        onChanged();
      } else {
        boardBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.model.Board board = 1;</code>
     */
    public Builder setBoard(
        com.example.eventstore.command.grpc.Board.Builder builderForValue) {
      if (boardBuilder_ == null) {
        board_ = builderForValue.build();
        onChanged();
      } else {
        boardBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.model.Board board = 1;</code>
     */
    public Builder mergeBoard(com.example.eventstore.command.grpc.Board value) {
      if (boardBuilder_ == null) {
        if (board_ != null) {
          board_ =
            com.example.eventstore.command.grpc.Board.newBuilder(board_).mergeFrom(value).buildPartial();
        } else {
          board_ = value;
        }
        onChanged();
      } else {
        boardBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.model.Board board = 1;</code>
     */
    public Builder clearBoard() {
      if (boardBuilder_ == null) {
        board_ = null;
        onChanged();
      } else {
        board_ = null;
        boardBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.model.Board board = 1;</code>
     */
    public com.example.eventstore.command.grpc.Board.Builder getBoardBuilder() {
      
      onChanged();
      return getBoardFieldBuilder().getBuilder();
    }
    /**
     * <code>.model.Board board = 1;</code>
     */
    public com.example.eventstore.command.grpc.BoardOrBuilder getBoardOrBuilder() {
      if (boardBuilder_ != null) {
        return boardBuilder_.getMessageOrBuilder();
      } else {
        return board_ == null ?
            com.example.eventstore.command.grpc.Board.getDefaultInstance() : board_;
      }
    }
    /**
     * <code>.model.Board board = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.example.eventstore.command.grpc.Board, com.example.eventstore.command.grpc.Board.Builder, com.example.eventstore.command.grpc.BoardOrBuilder> 
        getBoardFieldBuilder() {
      if (boardBuilder_ == null) {
        boardBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.example.eventstore.command.grpc.Board, com.example.eventstore.command.grpc.Board.Builder, com.example.eventstore.command.grpc.BoardOrBuilder>(
                getBoard(),
                getParentForChildren(),
                isClean());
        board_ = null;
      }
      return boardBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:model.RenameBoardResponse)
  }

  // @@protoc_insertion_point(class_scope:model.RenameBoardResponse)
  private static final com.example.eventstore.command.grpc.RenameBoardResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.example.eventstore.command.grpc.RenameBoardResponse();
  }

  public static com.example.eventstore.command.grpc.RenameBoardResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RenameBoardResponse>
      PARSER = new com.google.protobuf.AbstractParser<RenameBoardResponse>() {
    @java.lang.Override
    public RenameBoardResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new RenameBoardResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RenameBoardResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RenameBoardResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.example.eventstore.command.grpc.RenameBoardResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
