/*
 * Model class for populating the authorable header component fields.
 */
package edu.ttn.templates.demo.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
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

    @ScriptVariable
    private ResourceResolver resolver;

    private Map<String, String> languages = new HashMap<>();

    private String currentLanguage;

    private String pagePath;

    private String pageLocale;

    private Resource headerResource;

    @PostConstruct
    protected void init() {

        //populate header properties
        setHeaderResource();
        if (Objects.nonNull(headerResource)) {
            ValueMap headerValueMap = headerResource.getValueMap();
            logoURL = headerValueMap.get(LOGO_URL_PROPERTY_NAME, String.class);
            siteName = headerValueMap.get(SITE_NAME_PROPERTY_NAME, String.class);
        }
    }

    /**
     * Look beneath current resource for Header resource
     * if not defined, iterate up content tree to find header resource
     */
    private void setHeaderResource() {
        String relativeHeaderPath = currentStyle.get(REL_PATH_PROPERTY_NAME, DEFAULT_REL_PATH);
        //target page where navigation is defined
        Page targetPage = currentPage;
        headerResource = targetPage.getContentResource(relativeHeaderPath);

        //iterate until we find a header resource that is not null or finish traversing the hierarchy
        while (isEmptyHeader(headerResource) && targetPage != null) {
            targetPage = targetPage.getParent();
            if (targetPage != null) {
                headerResource = targetPage.getContentResource(relativeHeaderPath);
            }
        }

    }

    public String getLogoURL() {
        return logoURL;
    }

    public String getSiteName() {
        return siteName;
    }

    /**
     * Return true if the header resource is null or not populated
     *
     * @param headerResource
     * @return
     */
    private boolean isEmptyHeader(Resource headerResource) {
        if (headerResource != null) {

            final String rootPathValue = headerResource.getValueMap().get(SITE_NAME_PROPERTY_NAME, String.class);

            if (rootPathValue != null && rootPathValue != "") {
                return false;
            }
        }
        return true;
    }

}