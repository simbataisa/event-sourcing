package com.example.eventstore.command.config;

import brave.grpc.GrpcTracing;
import io.grpc.ServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.lognet.springboot.grpc.autoconfigure.GRpcServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GRpcServerProperties.class)
public class GrpcConfig {

  @Bean
  @GRpcGlobalInterceptor
  public ServerInterceptor grpcServerSleuthInterceptor(GrpcTracing grpcTracing) {
    return grpcTracing.newServerInterceptor();
  }
}
