package edu.ttn.templates.demo.core.models;

import com.day.cq.wcm.api.TemplatedResource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

@Model(adaptables = SlingHttpServletRequest.class)
public class ListTitleModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListTitleModel.class);

    private static final String RESPONSIVE_GRID_NODE_NAME_PREFIX = "responsivegrid-";
    private static final String TITLE_PROPERTY_NAME = "title";
    private static final Comparator<Resource> RESOURCE_BY_NAME_COMPARATOR = Comparator.comparing(Resource::getName);
    @SlingObject
    private Resource currentResource;
    @SlingObject
    private SlingHttpServletRequest slingHttpServletRequest;
    /**
     * Map containing properties of resource representing list items. Key is resource's path and value is resource's {@link ValueMap}.
     * <br><br>
     */
    private Map<String, ValueMap> resourcePropertiesMapForList;


}
