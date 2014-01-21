package com.tinkerpop.blueprints.impls.netbase.batch;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.ExceptionFactory;
import com.tinkerpop.blueprints.util.StringFactory;

import java.util.Map;

/**
 * @author Pannous (http://Pannous.com)
 */
class NetbaseBatchEdge extends NetbaseBatchElement implements Edge {

    private final String label;

    public NetbaseBatchEdge(final NetbaseBatchGraph graph, final Long id, final String label) {
        super(graph, id);
        this.label = label;
    }

    public <T> T removeProperty(final String key) {
        final Map<String, Object> properties = this.getPropertyMapClone();
        final Object value = properties.remove(key);
        this.graph.getRawGraph().setRelationshipProperties(this.id, properties);
        return (T) value;

    }

    public void setProperty(final String key, final Object value) {
        if (key.isEmpty())
            throw ExceptionFactory.propertyKeyCanNotBeEmpty();
        if (key.equals(StringFactory.ID))
            throw ExceptionFactory.propertyKeyIdIsReserved();
        if (key.equals(StringFactory.LABEL))
            throw ExceptionFactory.propertyKeyLabelIsReservedForEdges();

        final Map<String, Object> properties = this.getPropertyMapClone();
        properties.put(key, value);
        this.graph.getRawGraph().setRelationshipProperties(this.id, properties);
    }

    public Map<String, Object> getPropertyMap() {
        return this.graph.getRawGraph().getRelationshipProperties(this.id);
    }

    /**
     * @throws UnsupportedOperationException
     */
    public Vertex getVertex(final Direction direction) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public String getLabel() {
        return this.label;
    }

    public String toString() {
        return "e[" + this.id + "][?-" + this.label + "->?]";
    }

}
