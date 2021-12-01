package com.example.eventstorecommand;

import com.example.eventstore.command.grpc.BoardCommandServiceGrpc;
import com.example.eventstore.command.grpc.CreateBoardResponse;
import com.example.eventstore.command.grpc.RenameBoardRequest;
import com.example.eventstore.command.grpc.RenameBoardResponse;
import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"grpc.enableReflection=true", "grpc.port=0", "grpc.shutdownGrace=5", "eureka.client.enabled=false"})
@AutoConfigureWireMock(port = 0)
@EnableAutoConfiguration
@Slf4j
class EventStoreCommandApplicationTests extends GrpcServerTestBase {

  @Rule
  public OutputCaptureRule outputCapture = new OutputCaptureRule();

  @Test
  public void interceptorsTest() throws ExecutionException, InterruptedException {
    stubFor(post(urlEqualTo("/esd-event-store"))
                .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(HttpStatus.ACCEPTED.value())
                )
    );

    CreateBoardResponse createBoardResponse = BoardCommandServiceGrpc.newFutureStub(channel)
                                                                     .createBoard(Empty.newBuilder().build()).get();

    Assertions.assertNotNull(createBoardResponse);
    log.info("BoardUuid {}", createBoardResponse.getBoardUuid());
    Assertions.assertNotNull(createBoardResponse.getBoardUuid());
  }

  @Test
  public void renameBoardTest() throws ExecutionException, InterruptedException {
    final String boardUuid = UUID.randomUUID().toString();
    stubFor(get(urlEqualTo("/esd-event-store/" + boardUuid))
                .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(HttpStatus.OK.value())
                                .withBody("{\"boardUuid\":\"" + boardUuid + "\"," +
                                              "\"domainEvents\":[{\"eventType\":\"BoardInitialized\"," +
                                              "\"boardUuid\":\"" + boardUuid + "\",\"occurredOn\":1583119878.537120000}]}\n")
                )
    );
    stubFor(post(urlEqualTo("/esd-event-store"))
                .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(HttpStatus.ACCEPTED.value())
                )
    );

    RenameBoardResponse renameBoardResponse = BoardCommandServiceGrpc.newFutureStub(channel)
                                                                     .renameBoard(
                                                                         RenameBoardRequest
                                                                             .newBuilder().setBoardUuid(boardUuid)
                                                                             .setBoardName("Name Changed")
                                                                             .build()).get();
    Assertions.assertNotNull(renameBoardResponse);
    log.info(renameBoardResponse.getBoard().toString());
    Assertions.assertEquals(boardUuid, renameBoardResponse.getBoard().getBoardUuid());
  }
}
