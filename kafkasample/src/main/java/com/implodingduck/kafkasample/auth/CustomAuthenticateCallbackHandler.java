package com.implodingduck.kafkasample;


import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.security.auth.AuthenticateCallbackHandler;
import org.apache.kafka.common.security.oauthbearer.OAuthBearerToken;
import org.apache.kafka.common.security.oauthbearer.OAuthBearerTokenCallback;

import com.microsoft.azure.credentials.MSICredentials;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import com.implodingduck.kafkasample.auth.OAuthBearerTokenImp;

// https://github.com/Azure/azure-event-hubs-for-kafka/blob/master/tutorials/oauth/java/managedidentity/producer/src/main/java/CustomAuthenticateCallbackHandler.java
public class CustomAuthenticateCallbackHandler implements AuthenticateCallbackHandler {

    final static ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);
    final static MSICredentials CREDENTIALS = new MSICredentials();
    
    private String sbUri;

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Override
    public void configure(Map<String, ?> configs, String mechanism, List<AppConfigurationEntry> jaasConfigEntries) {
        String bootstrapServer = bootstrapAddress;
        bootstrapServer = bootstrapServer.replaceAll("\\[|\\]", "");
        URI uri = URI.create("https://" + bootstrapServer);
        this.sbUri = uri.getScheme() + "://" + uri.getHost();
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback: callbacks) {
            if (callback instanceof OAuthBearerTokenCallback) {
                try {
                    OAuthBearerToken token = getOAuthBearerToken();
                    OAuthBearerTokenCallback oauthCallback = (OAuthBearerTokenCallback) callback;
                    oauthCallback.token(token);
                } catch (InterruptedException | ExecutionException | TimeoutException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }

    OAuthBearerToken getOAuthBearerToken() throws InterruptedException, ExecutionException, TimeoutException, IOException, ParseException
    {
        String accesToken = CREDENTIALS.getToken(sbUri);
        JWT jwt = JWTParser.parse(accesToken);
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        
        return new OAuthBearerTokenImp(accesToken, claims.getExpirationTime());
    }
    
    public void close() throws KafkaException {
        // NOOP
    }
}