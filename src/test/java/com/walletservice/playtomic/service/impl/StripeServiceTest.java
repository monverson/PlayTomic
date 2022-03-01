package com.walletservice.playtomic.service.impl;


import com.walletservice.playtomic.service.stripe.StripeAmountTooSmallException;
import com.walletservice.playtomic.service.stripe.StripeRestTemplateResponseErrorHandler;
import com.walletservice.playtomic.service.stripe.StripeService;
import com.walletservice.playtomic.service.stripe.StripeServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

/**
 * This test is failing with the current implementation.
 * <p>
 * How would you test this?
 */


public class StripeServiceTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private MockRestServiceServer mockServer;

    URI testUri = URI.create("http://how-would-you-test-me.localhost");
    StripeService s = new StripeService(testUri, testUri, new RestTemplateBuilder());

    @BeforeEach
    public void setUp()  {

        restTemplateBuilder = mock(RestTemplateBuilder.class);

        StripeRestTemplateResponseErrorHandler stripeRestTemplateResponseErrorHandler= new StripeRestTemplateResponseErrorHandler();
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder().errorHandler(stripeRestTemplateResponseErrorHandler);
        RestTemplate restTemplate = templateBuilder.build();

        when(restTemplateBuilder.errorHandler(any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        s = new StripeService(testUri, testUri, restTemplateBuilder);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    public void test_ok() throws StripeServiceException {
        mockServer.expect(requestTo(testUri)).andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        s.charge("4242 4242 4242 4242", new BigDecimal(15));
    }

    @Test
    public void test_exception() {
        mockServer.expect(requestTo(testUri)).andRespond(MockRestResponseCreators.withStatus(HttpStatus.UNPROCESSABLE_ENTITY));
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> s.charge("4242 4242 4242 4242", new BigDecimal(5)));
    }

}