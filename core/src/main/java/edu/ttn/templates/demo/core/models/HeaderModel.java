/*
 * Model class for populating the authorable header component fields.
 */
package edu.ttn.templates.demo.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Model(
    adaptables = {SlingHttpServletRequest.class},
    adapters = {HeaderModel.class},
    resourceType = {HeaderModel.RESOURCE_TYPE},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

    protected static final String RESOURCE_TYPE = "dynamic-templates-demo/components/structure/header";
    private static final String REL_PATH_PROPERTY_NAME = "relPath";
    private static final String LOGO_URL_PROPERTY_NAME = "logoURL";
    private static final String SITE_NAME_PROPERTY_NAME = "siteName";
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderModel.class);
    private static final String DEFAULT_REL_PATH = "root/header";

    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private Style currentStyle;

    private String logoURL;

    private String siteName;

    @ScriptVariable
    private Page currentPage;

    private Resource headerResource;


}