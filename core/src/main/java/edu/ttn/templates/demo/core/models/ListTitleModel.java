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

    @PostConstruct
    protected void init() {

        Resource listTitleTemplatedResource = getEffectiveResource();

        Iterator<Resource> listTitleChildrenIterator = listTitleTemplatedResource.listChildren();
        if (listTitleChildrenIterator.hasNext()) {
            Resource columnControlResource = listTitleChildrenIterator.next();
            LOGGER.debug("columnControlResource: {}", columnControlResource);

            TreeSet<Resource> responsiveGridResourceSet = getColumnControlResponsiveGridResourceSet(columnControlResource);

            resourcePropertiesMapForList = new LinkedHashMap<>();

            addListItems(responsiveGridResourceSet);
            LOGGER.debug("List items for column control {}: {}", columnControlResource.getPath(), resourcePropertiesMapForList.keySet());

        }
    }

    public Map<String, ValueMap> getResourcePropertiesMapForList() {
        return resourcePropertiesMapForList;
    }

    /**
     * Returns a set of all child resources of given resource whose name starts with {@value ListTitleModel#RESPONSIVE_GRID_NODE_NAME_PREFIX}.
     *
     * @param columnControlResource {@link Resource}
     * @return set of column control's responsive grid resources.
     */
    private TreeSet<Resource> getColumnControlResponsiveGridResourceSet(final Resource columnControlResource) {
        TreeSet<Resource> responsiveGridResourceSet = new TreeSet<>(RESOURCE_BY_NAME_COMPARATOR);
        for (Resource columnControlChildResource : columnControlResource.getChildren()) {
            if (columnControlChildResource.getName().startsWith(RESPONSIVE_GRID_NODE_NAME_PREFIX)) {
                responsiveGridResourceSet.add(columnControlChildResource);
            }
        }

        LOGGER.debug("Found {} responsive grids under {}", responsiveGridResourceSet.size(), columnControlResource.getPath());
        return responsiveGridResourceSet;
    }

    /**
     * Add list item candidates in {@link ListTitleModel#resourcePropertiesMapForList}.
     * <br><br>
     * Each direct children of column control's responsive grid having
     * {@value ListTitleModel#TITLE_PROPERTY_NAME} property will be qualified as list item.
     *
     * @param responsiveGridResourceSet Ordered {@link java.util.Set} of column control's responsive grid.
     */
    private void addListItems(final TreeSet<Resource> responsiveGridResourceSet) {
        responsiveGridResourceSet.forEach(responsiveGridResource ->
            responsiveGridResource.getChildren().forEach(componentResource -> {
                    ValueMap componentProperties = componentResource.adaptTo(ValueMap.class);
                    if (Objects.nonNull(componentProperties.get(TITLE_PROPERTY_NAME))) {
                        resourcePropertiesMapForList.put(componentResource.getPath(), componentProperties);
                    }
                }
            ));
    }

    /**
     * Returns {@link TemplatedResource} for landingPageModule component.
     * {@link TemplatedResource} handles editable components behaviour of dynamic templates.
     *
     * @return returns the {@link TemplatedResource} for landingPageModule component
     */
    private Resource getEffectiveResource() {
        if (currentResource instanceof TemplatedResource) {
            return currentResource;
        } else {
            Resource templatedResource = slingHttpServletRequest.adaptTo(TemplatedResource.class);
            return templatedResource == null ? currentResource : templatedResource;
        }
    }

}
