package com.ioet.bpm.people.handlers;


import com.amazonaws.services.lambda.runtime.Context;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PeopleApiLambdaHandler.class)
public class PeopleApiLambdaHandlerTest {

    @Mock
    PeopleApiLambdaHandler peopleApiLambdaHandler;

    @Rule
    public final EnvironmentVariables environmentVariables
            = new EnvironmentVariables();

    @Test
    public void lambdaHandlerClassIsInitialized() throws Exception {
        environmentVariables.set("AWS_ACCESS_KEY_ID", "AKIAJV3HQDU44HTJTUYA");
        environmentVariables.set("AWS_SECRET_ACCESS_KEY", "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");
        environmentVariables.set("AWS_REGION", "us-east-1");
        ;
        PeopleApiLambdaHandler expectedHandler = mock(PeopleApiLambdaHandler.class);

        whenNew(PeopleApiLambdaHandler.class).withNoArguments().thenReturn(expectedHandler);

        PeopleApiLambdaHandler actualhandlHandler = new PeopleApiLambdaHandler();

        PowerMockito.verifyNew(PeopleApiLambdaHandler.class).withNoArguments();
    }



    @Test
    public void lambdaHandlerClassIsUsed() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        Context context = mock(Context.class);

        peopleApiLambdaHandler.handleRequest(inputStream, outputStream, context);

        verify(peopleApiLambdaHandler).handleRequest(inputStream, outputStream, context);
    }
}
