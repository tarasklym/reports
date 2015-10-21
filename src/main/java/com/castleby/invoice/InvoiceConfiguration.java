package com.castleby.invoice;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vaadin.spring.annotation.EnableVaadin;

/**
 * 
 * @author taras.klym
 *
 */
@Configuration
@EnableVaadin
@EnableJpaRepositories
public class InvoiceConfiguration {

}
