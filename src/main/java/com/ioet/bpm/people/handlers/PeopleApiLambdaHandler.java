package com.ioet.bpm.people.handlers;


import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.ioet.bpm.people.BpmPeopleApiApplication;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class PeopleApiLambdaHandler implements RequestStreamHandler {


    public final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    public PeopleApiLambdaHandler() throws ContainerInitializationException {
        handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(BpmPeopleApiApplication.class);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
        outputStream.close();
    }
}
