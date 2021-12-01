package com.example.eventstore.command.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.1)",
    comments = "Source: BoardCommandProto.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BoardCommandServiceGrpc {

  private BoardCommandServiceGrpc() {}

  public static final String SERVICE_NAME = "model.BoardCommandService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.example.eventstore.command.grpc.CreateBoardResponse> getCreateBoardMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateBoard",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.example.eventstore.command.grpc.CreateBoardResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.example.eventstore.command.grpc.CreateBoardResponse> getCreateBoardMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.example.eventstore.command.grpc.CreateBoardResponse> getCreateBoardMethod;
    if ((getCreateBoardMethod = BoardCommandServiceGrpc.getCreateBoardMethod) == null) {
      synchronized (BoardCommandServiceGrpc.class) {
        if ((getCreateBoardMethod = BoardCommandServiceGrpc.getCreateBoardMethod) == null) {
          BoardCommandServiceGrpc.getCreateBoardMethod = getCreateBoardMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.example.eventstore.command.grpc.CreateBoardResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateBoard"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.eventstore.command.grpc.CreateBoardResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BoardCommandServiceMethodDescriptorSupplier("CreateBoard"))
              .build();
        }
      }
    }
    return getCreateBoardMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.eventstore.command.grpc.RenameBoardRequest,
      com.example.eventstore.command.grpc.RenameBoardResponse> getRenameBoardMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RenameBoard",
      requestType = com.example.eventstore.command.grpc.RenameBoardRequest.class,
      responseType = com.example.eventstore.command.grpc.RenameBoardResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.eventstore.command.grpc.RenameBoardRequest,
      com.example.eventstore.command.grpc.RenameBoardResponse> getRenameBoardMethod() {
    io.grpc.MethodDescriptor<com.example.eventstore.command.grpc.RenameBoardRequest, com.example.eventstore.command.grpc.RenameBoardResponse> getRenameBoardMethod;
    if ((getRenameBoardMethod = BoardCommandServiceGrpc.getRenameBoardMethod) == null) {
      synchronized (BoardCommandServiceGrpc.class) {
        if ((getRenameBoardMethod = BoardCommandServiceGrpc.getRenameBoardMethod) == null) {
          BoardCommandServiceGrpc.getRenameBoardMethod = getRenameBoardMethod =
              io.grpc.MethodDescriptor.<com.example.eventstore.command.grpc.RenameBoardRequest, com.example.eventstore.command.grpc.RenameBoardResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RenameBoard"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.eventstore.command.grpc.RenameBoardRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.eventstore.command.grpc.RenameBoardResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BoardCommandServiceMethodDescriptorSupplier("RenameBoard"))
              .build();
        }
      }
    }
    return getRenameBoardMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BoardCommandServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BoardCommandServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BoardCommandServiceStub>() {
        @java.lang.Override
        public BoardCommandServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BoardCommandServiceStub(channel, callOptions);
        }
      };
    return BoardCommandServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BoardCommandServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BoardCommandServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BoardCommandServiceBlockingStub>() {
        @java.lang.Override
        public BoardCommandServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BoardCommandServiceBlockingStub(channel, callOptions);
        }
      };
    return BoardCommandServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BoardCommandServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BoardCommandServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BoardCommandServiceFutureStub>() {
        @java.lang.Override
        public BoardCommandServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BoardCommandServiceFutureStub(channel, callOptions);
        }
      };
    return BoardCommandServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class BoardCommandServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *    rpc CreateBoard(CreateBoardRequest) returns (CreateBoardResponse){}
     * </pre>
     */
    public void createBoard(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.example.eventstore.command.grpc.CreateBoardResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateBoardMethod(), responseObserver);
    }

    /**
     */
    public void renameBoard(com.example.eventstore.command.grpc.RenameBoardRequest request,
        io.grpc.stub.StreamObserver<com.example.eventstore.command.grpc.RenameBoardResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRenameBoardMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreateBoardMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.example.eventstore.command.grpc.CreateBoardResponse>(
                  this, METHODID_CREATE_BOARD)))
          .addMethod(
            getRenameBoardMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.example.eventstore.command.grpc.RenameBoardRequest,
                com.example.eventstore.command.grpc.RenameBoardResponse>(
                  this, METHODID_RENAME_BOARD)))
          .build();
    }
  }

  /**
   */
  public static final class BoardCommandServiceStub extends io.grpc.stub.AbstractAsyncStub<BoardCommandServiceStub> {
    private BoardCommandServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BoardCommandServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BoardCommandServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     *    rpc CreateBoard(CreateBoardRequest) returns (CreateBoardResponse){}
     * </pre>
     */
    public void createBoard(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.example.eventstore.command.grpc.CreateBoardResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateBoardMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void renameBoard(com.example.eventstore.command.grpc.RenameBoardRequest request,
        io.grpc.stub.StreamObserver<com.example.eventstore.command.grpc.RenameBoardResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRenameBoardMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BoardCommandServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<BoardCommandServiceBlockingStub> {
    private BoardCommandServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BoardCommandServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BoardCommandServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *    rpc CreateBoard(CreateBoardRequest) returns (CreateBoardResponse){}
     * </pre>
     */
    public com.example.eventstore.command.grpc.CreateBoardResponse createBoard(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateBoardMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.eventstore.command.grpc.RenameBoardResponse renameBoard(com.example.eventstore.command.grpc.RenameBoardRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRenameBoardMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BoardCommandServiceFutureStub extends io.grpc.stub.AbstractFutureStub<BoardCommandServiceFutureStub> {
    private BoardCommandServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BoardCommandServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BoardCommandServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *    rpc CreateBoard(CreateBoardRequest) returns (CreateBoardResponse){}
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.eventstore.command.grpc.CreateBoardResponse> createBoard(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateBoardMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.eventstore.command.grpc.RenameBoardResponse> renameBoard(
        com.example.eventstore.command.grpc.RenameBoardRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRenameBoardMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_BOARD = 0;
  private static final int METHODID_RENAME_BOARD = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BoardCommandServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BoardCommandServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_BOARD:
          serviceImpl.createBoard((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.example.eventstore.command.grpc.CreateBoardResponse>) responseObserver);
          break;
        case METHODID_RENAME_BOARD:
          serviceImpl.renameBoard((com.example.eventstore.command.grpc.RenameBoardRequest) request,
              (io.grpc.stub.StreamObserver<com.example.eventstore.command.grpc.RenameBoardResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class BoardCommandServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BoardCommandServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.eventstore.command.grpc.BoardCommandProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BoardCommandService");
    }
  }

  private static final class BoardCommandServiceFileDescriptorSupplier
      extends BoardCommandServiceBaseDescriptorSupplier {
    BoardCommandServiceFileDescriptorSupplier() {}
  }

  private static final class BoardCommandServiceMethodDescriptorSupplier
      extends BoardCommandServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BoardCommandServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BoardCommandServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BoardCommandServiceFileDescriptorSupplier())
              .addMethod(getCreateBoardMethod())
              .addMethod(getRenameBoardMethod())
              .build();
        }
      }
    }
    return result;
  }
}
