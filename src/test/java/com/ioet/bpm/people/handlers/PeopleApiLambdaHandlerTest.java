package com.ioet.bpm.people.handlers;


import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.ioet.bpm.people.BpmPeopleApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.env.MockEnvironment;
import software.amazon.ion.IonException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PeopleApiLambdaHandlerTest {

    @Mock
    PeopleApiLambdaHandler peopleApiLambdaHandler;

    @Test
    public void lambdaHandlerClassIsUsed() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        Context context = mock(Context.class);

        peopleApiLambdaHandler.handleRequest(inputStream, outputStream, context);

        verify(peopleApiLambdaHandler).handleRequest(inputStream, outputStream, context);
    }
}
