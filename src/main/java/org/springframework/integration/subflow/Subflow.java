package org.springframework.integration.subflow;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.handler.BridgeHandler;
import org.springframework.integration.support.channel.BeanFactoryChannelResolver;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.core.DestinationResolver;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Subflow
        implements InitializingBean, BeanNameAware, DestinationResolver<MessageChannel>, ApplicationContextAware {

    private static final String DEFAULT_INPUT_CHANNEL_NAME = "inputChannel";

    private String[] configLocations;
    private Properties flowProperties;
    private String beanName;
    private String flowInputChannelName = DEFAULT_INPUT_CHANNEL_NAME;
    private SubscribableChannel flowInputChannel = null;
    private Map<String, MessageChannel> flowOutputChannelsMap = new HashMap<String, MessageChannel>();
    private String flowId = null;
    private Map<String, Object> overridedBeansMap = new HashMap<String, Object>();
    private BeanFactoryChannelResolver flowChannelResolver;

    private AbstractRefreshableConfigApplicationContext flowContext;
    private ApplicationContext applicationContext;
    private ApplicationContext parentContext = null;

    public Subflow() {
        this(null);
    }

    public Subflow(String[] configLocations) {
        this(configLocations, new Properties());
    }

    public Subflow(String[] configLocations, Properties flowProperties) {
        this.flowProperties = flowProperties;
        this.configLocations = configLocations;
    }

    @Override
    public void afterPropertiesSet() {

        if (this.flowId == null) {
            this.flowId = this.beanName;
        }

        if (configLocations == null) {
            configLocations = new String[]{String.format("classpath*:META-INF/spring/integration/flows/%s/*.xml",
                    this.flowId)};
        }

        Assert.notEmpty(configLocations, "configLocations cannot be empty");

        if (parentContext==null) {
            parentContext = applicationContext;
        }

        flowContext = new ClassPathXmlApplicationContext(parentContext);

        overrideBeans();

        addReferencedProperties();

        this.flowContext.setConfigLocations(configLocations);

        this.flowContext.refresh();

        this.flowChannelResolver = new BeanFactoryChannelResolver(flowContext);

        bridgeMessagingPorts();

        if (flowInputChannel != null) {
            bridgeChannels(flowInputChannel, resolveDestination(flowInputChannelName));
        }
    }

    private void overrideBeans() {
        flowContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
                for (Map.Entry<String, Object> entry : overridedBeansMap.entrySet()) {
                    bf.registerSingleton(entry.getKey(), entry.getValue());
                }
            }
        });
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;

    }

    public String getBeanName() {
        return this.beanName;
    }

    /**
     * @param flowProperties properties referenced in the flow definition
     *                       property placeholders
     */
    public void setProperties(Properties flowProperties) {
        this.flowProperties = flowProperties;
    }

    public Properties getProperties() {
        return this.flowProperties;
    }

    @Override
    public MessageChannel resolveDestination(String channelName) {
        return flowChannelResolver.resolveDestination(channelName);
    }

    private void addReferencedProperties() {
        if (flowProperties != null) {
            PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
            ppc.setProperties(flowProperties);
            flowContext.addBeanFactoryPostProcessor(ppc);
        }
    }

    private void bridgeMessagingPorts() {
        for (Map.Entry<String, MessageChannel> entry : flowOutputChannelsMap.entrySet()) {

            String subflowOutputChannelName = entry.getKey();
            SubscribableChannel subflowOutputChannel = (SubscribableChannel) resolveDestination(subflowOutputChannelName);

            bridgeChannels(subflowOutputChannel, entry.getValue());
        }
    }

    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (parentContext==null) {
            parentContext = applicationContext;
        }
    }

    public ApplicationContext getFlowContext() {
        return this.flowContext;
    }

    public static void bridgeChannels(SubscribableChannel inputChannel, MessageChannel outputChannel) {
        BridgeHandler bridgeHandler = new BridgeHandler();
        bridgeHandler.setOutputChannel(outputChannel);
        inputChannel.subscribe(bridgeHandler);
    }

    public String[] getConfigLocations() {
        return configLocations;
    }

    public void setConfigLocations(String[] configLocations) {
        this.configLocations = configLocations;
    }

    public Properties getFlowProperties() {
        return flowProperties;
    }

    public void setFlowProperties(Properties flowProperties) {
        this.flowProperties = flowProperties;
    }

    public SubscribableChannel getFlowInputChannel() {
        return flowInputChannel;
    }

    public void setFlowInputChannel(SubscribableChannel flowInputChannel) {
        this.flowInputChannel = flowInputChannel;
    }

    public Map<String, MessageChannel> getFlowOutputChannelsMap() {
        return flowOutputChannelsMap;
    }

    public void setFlowOutputChannelsMap(Map<String, MessageChannel> flowOutputChannelsMap) {
        this.flowOutputChannelsMap = flowOutputChannelsMap;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowInputChannelName() {
        return flowInputChannelName;
    }

    public void setFlowInputChannelName(String flowInputChannelName) {
        this.flowInputChannelName = flowInputChannelName;
    }

    public synchronized ApplicationContext getParentApplicationContext() {
        return parentContext;
    }

    public synchronized void setParentApplicationContext(ApplicationContext parentContext) {
        this.parentContext = parentContext;
    }
}