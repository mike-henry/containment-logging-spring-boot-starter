package com.spx.containment.logging;

import java.util.UUID;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class MDCLoggingFilters {

  private static final String TRANSACTION_ID = "tx-id";

  @Provider
  @PreMatching
  public static class Before implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
      String transactionId = getTransactionId(containerRequestContext);
      MDC.put(TRANSACTION_ID, transactionId);
      log.debug("MDC {} setTo {}", TRANSACTION_ID, transactionId);
    }

    private String getTransactionId(ContainerRequestContext containerRequestContext) {
      String transactionId = containerRequestContext.getHeaderString(TRANSACTION_ID);
      if (transactionId == null) {
        transactionId = UUID.randomUUID()
            .toString();
        log.warn(" ID {} missing creating as {}", TRANSACTION_ID, transactionId);
      }
      return transactionId;
    }
  }


  @Provider
  public static class After implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext,
        ContainerResponseContext containerResponseContext) {
      log.debug("MDC {} removing {}", TRANSACTION_ID, MDC.get(TRANSACTION_ID));
      MDC.remove(TRANSACTION_ID);
    }
  }


}
