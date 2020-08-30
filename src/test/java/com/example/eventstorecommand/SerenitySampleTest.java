package com.example.eventstorecommand;

import com.example.eventstorecommand.grpc.BoardCommandServiceGrpc;
import com.example.eventstorecommand.grpc.CreateBoardResponse;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;

import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@RunWith(SerenityRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = { "grpc.enableReflection=true", "grpc.port=0", "grpc.shutdownGrace=5", "eureka.client.enabled=false" })
@Slf4j
@AutoConfigureWireMock(port = 0)
@EnableAutoConfiguration
public class SerenitySampleTest extends GrpcServerJUnit4TestBase {

  @Rule
  public SpringIntegrationMethodRule springIntegrationMethodRule = new SpringIntegrationMethodRule();

  @Steps
  private SampleTestSteps sampleTestSteps;

  @Test
  @Title("send gRPC request to create board")
  public void given_mock_do_grpc_call() throws ExecutionException, InterruptedException {
    sampleTestSteps.stub();
    sampleTestSteps.sendGRpcRequest(channel);
    sampleTestSteps.verifyGRpcResponse();
  }

  public class SampleTestSteps {

    private CreateBoardResponse createBoardResponse;

    @Step("stub response if request url is matched")
    public void stub() {
      stubFor(post(urlEqualTo("/esd-event-store"))
                  .willReturn(aResponse()
                                  .withHeader("Content-Type", "application/json")
                                  .withStatus(HttpStatus.ACCEPTED.value())
                  )
      );
    }

    @Step("send gRPC request to mock server")
    public void sendGRpcRequest(ManagedChannel channel) throws ExecutionException, InterruptedException {
      this.createBoardResponse = BoardCommandServiceGrpc.newFutureStub(channel)
                                                        .createBoard(Empty.newBuilder().build()).get();
    }

    @Step ("verify gRPC response")
    public void verifyGRpcResponse() {
      Assert.assertNotNull(this.createBoardResponse);
      log.info("BoardUuid {}", this.createBoardResponse.getBoardUuid());
      Assert.assertNotNull(this.createBoardResponse.getBoardUuid());
    }
  }
}
