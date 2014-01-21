package com.tinkerpop.blueprints.impls.netbase.batch;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.util.ElementHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Pannous (http://Pannous.com)
 */
abstract class NetbaseBatchElement implements Element {

    protected final NetbaseBatchGraph graph;
    protected final Long id;

    protected NetbaseBatchElement(final NetbaseBatchGraph graph, final Long id) {
        this.graph = graph;
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    public abstract Map<String, Object> getPropertyMap();

    public Set<String> getPropertyKeys() {
        return this.getPropertyMap().keySet();
    }

    public <T> T getProperty(final String key) {
        return (T) this.getPropertyMap().get(key);
    }

    protected Map<String, Object> getPropertyMapClone() {
        final Map<String, Object> clone = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : this.getPropertyMap().entrySet()) {
            clone.put(entry.getKey(), entry.getValue());
        }
        return clone;

    }

    public int hashCode() {
        return this.getId().hashCode();
    }

    public void remove() {
        throw new UnsupportedOperationException(NetbaseBatchTokens.DELETE_OPERATION_MESSAGE);
    }

    public boolean equals(final Object object) {
        return ElementHelper.areEqual(this, object);
    }
}
