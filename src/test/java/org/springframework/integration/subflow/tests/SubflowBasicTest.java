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

    @Resource(name = "outputChannel1")
    PollableChannel outputChannel1;

    @Resource(name = "outputChannel2")
    PollableChannel outputChannel2;

    @Test
    public void testBasicSubflow() {
        inputChannel.send(MessageBuilder.withPayload("REQUEST1").build());
        Message msgReceived = outputChannel1.receive(2000L);
        Assert.assertEquals("REQUEST1_RESPONSE1", msgReceived.getPayload());
    }

    @Test
    public void testAlternativeOutputChannelSubflow() {
        inputChannel.send(MessageBuilder.withPayload("REQUEST2").build());
        Message msgReceived = outputChannel2.receive(2000L);
        Assert.assertEquals("REQUEST2_RESPONSE2", msgReceived.getPayload());
    }
}
