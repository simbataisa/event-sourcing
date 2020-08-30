package com.example.eventstorecommand.domain.service;

import com.example.eventstorecommand.domain.client.BoardClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = {"eureka.client.enabled=false"})
@AutoConfigureWireMock(port = 0)
@Slf4j
class BoardServiceTest {

  @Autowired
  private BoardService boardService;

  @MockBean
  private BoardClient boardClient;

  @Autowired
  private  ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void createBoardWithWireMockServer() throws Exception {
    //doNothing().when(boardClient).save(any());
    //Wiremock Server Stubbing
    stubFor(post(urlEqualTo("/esd-event-store"))
                .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(HttpStatus.ACCEPTED.value())
                )
    );

    UUID uuid = boardService.createBoard();
    log.info("UUID {}", uuid);
  }

  @Test
  void createBoardWithMockBean() throws Exception {
    doNothing().when(boardClient).save(any());
    UUID uuid = boardService.createBoard();
    log.info("UUID {}", uuid);
  }

  /*@Configuration
  @EnableFeignClients(clients = {RestConfig.EventStoreClient.class})
  @ImportAutoConfiguration( {
      HttpMessageConvertersAutoConfiguration.class,
      RibbonAutoConfiguration.class,
      FeignRibbonClientAutoConfiguration.class,
      FeignAutoConfiguration.class,
      CommandConfig.class
  })
  static class ContextConfiguration {

    @Autowired
    Environment env;

    @Bean
    @Primary
    public BoardClient boardClient(final RestConfig.EventStoreClient eventStoreClient) {
      return new EventStoreBoardClient(eventStoreClient);
    }

    @Bean
    ServletWebServerFactory servletWebServerFactory() {
      return new TomcatServletWebServerFactory();
    }

    @Bean
    public ServerList<Server> ribbonServerList() {
      return new StaticServerList<>(new Server("localhost", Integer.valueOf(this.env.getProperty("wiremock.server.port"))));
    }
  }*/
}