package org.acme;

import javax.servlet.annotation.WebFilter;

@WebFilter("/*")
public class CORSFilterRegistration extends CORSFilter {
    // This class is used to register the CORSFilter
}
