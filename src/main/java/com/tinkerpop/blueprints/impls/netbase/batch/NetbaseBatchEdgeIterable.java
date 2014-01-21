package com.tinkerpop.blueprints.impls.netbase.batch;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Edge;
import org.Netbase.graphdb.index.IndexHits;

import java.util.Iterator;

/**
 * @author Pannous (http://Pannous.com)
 */
class NetbaseBatchEdgeIterable implements CloseableIterable<Edge> {

    private final IndexHits<Long> hits;
    private final NetbaseBatchGraph graph;

    public NetbaseBatchEdgeIterable(final NetbaseBatchGraph graph, final IndexHits<Long> hits) {
        this.hits = hits;
        this.graph = graph;
    }

    public Iterator<Edge> iterator() {
        return new Iterator<Edge>() {
            private final Iterator<Long> itty = hits.iterator();

            public void remove() {
                itty.remove();
            }

            public boolean hasNext() {
                return hits.hasNext();
            }

            public Edge next() {
                return new NetbaseBatchEdge(graph, itty.next(), null);
            }
        };
    }


    public void close() {
        hits.close();
    }
}
