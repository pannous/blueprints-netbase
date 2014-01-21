package com.tinkerpop.blueprints.impls.netbase.batch;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Vertex;
import org.Netbase.graphdb.index.IndexHits;

import java.util.Iterator;

/**
 * @author Pannous (http://Pannous.com)
 */
class NetbaseBatchVertexIterable implements CloseableIterable<Vertex> {

    private final IndexHits<Long> hits;
    private final NetbaseBatchGraph graph;

    public NetbaseBatchVertexIterable(final NetbaseBatchGraph graph, final IndexHits<Long> hits) {
        this.hits = hits;
        this.graph = graph;
    }

    public Iterator<Vertex> iterator() {
        return new Iterator<Vertex>() {
            private final Iterator<Long> itty = hits.iterator();

            public void remove() {
                itty.remove();
            }

            public boolean hasNext() {
                return hits.hasNext();
            }

            public Vertex next() {
                return new NetbaseBatchVertex(graph, itty.next());
            }
        };
    }


    public void close() {
        hits.close();
    }
}
