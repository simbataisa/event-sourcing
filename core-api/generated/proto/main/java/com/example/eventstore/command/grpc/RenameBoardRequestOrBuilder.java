// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: BoardCommandProto.proto

package com.example.eventstore.command.grpc;

public interface RenameBoardRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:model.RenameBoardRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string boardUuid = 1;</code>
   * @return The boardUuid.
   */
  java.lang.String getBoardUuid();
  /**
   * <code>string boardUuid = 1;</code>
   * @return The bytes for boardUuid.
   */
  com.google.protobuf.ByteString
      getBoardUuidBytes();

  /**
   * <code>string boardName = 2;</code>
   * @return The boardName.
   */
  java.lang.String getBoardName();
  /**
   * <code>string boardName = 2;</code>
   * @return The bytes for boardName.
   */
  com.google.protobuf.ByteString
      getBoardNameBytes();
}
