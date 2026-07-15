//package com.ecom.pranaya.config;
//
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.ws.config.annotation.EnableWs;
//import org.springframework.ws.config.annotation.WsConfigurer;
//import org.springframework.ws.transport.http.MessageDispatcherServlet;
//import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
//import org.springframework.xml.xsd.SimpleXsdSchema;
//import org.springframework.xml.xsd.XsdSchema;
//
//
////SOAP
//@EnableWs
//@Configuration
//public class WebServiceConfig implements WsConfigurer {
//
//    @Bean
//    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
//        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
//        servlet.setApplicationContext(applicationContext);
//        servlet.setTransformWsdlLocations(true);
//        return new ServletRegistrationBean<>(servlet, "/ws/*");
//    }
//
//    @Bean(name = "sarees")
//    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema sareesSchema) {
//        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
//        wsdl11Definition.setPortTypeName("SareesPort");
//        wsdl11Definition.setLocationUri("/ws");
//        wsdl11Definition.setTargetNamespace("http://com.ecom.pranaya/soap");
//        wsdl11Definition.setSchema(sareesSchema);
//        return wsdl11Definition;
//    }
//
//    @Bean
//    public XsdSchema sareesSchema() {
//        return new SimpleXsdSchema(new ClassPathResource("sarees.xsd"));
//    }
//}