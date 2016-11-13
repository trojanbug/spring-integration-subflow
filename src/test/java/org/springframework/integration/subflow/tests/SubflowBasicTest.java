package org.springframework.integration.subflow.tests;


import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@Test
@ContextConfiguration(locations = "classpath:/spring/flow-parent-config-context.xml")
public class SubflowBasicTest extends AbstractTestNGSpringContextTests {

    @Resource(name = "inputChannel")
    MessageChannel inputChannel;

    @Resource(name = "outputChannel")
    PollableChannel outputChannel;

    @Test
    public void testBasicSubflow() {
        inputChannel.send(MessageBuilder.withPayload("TEST").build());
        Message msgReceived = outputChannel.receive(2000L);
        Assert.assertEquals("TEST_RESPONSE", msgReceived.getPayload());
    }
}
