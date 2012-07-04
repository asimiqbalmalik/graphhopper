/*
 *  Copyright 2012 Peter Karich info@jetsli.de
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.jetsli.graph.storage;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import static de.jetsli.graph.util.MyIteratorable.*;

/**
 *
 * @author Peter Karich
 */
public class MemoryGraphSafeTest extends AbstractGraphTester {

    @Override
    Graph createGraph(int size) {
        return new MemoryGraphSafe(size);
    }

    @Test
    public void testSave() throws IOException {
        String tmpDir = "/tmp/memory-graph-safe";
        SaveableGraph mmgraph = new MemoryGraphSafe(tmpDir, 3, 3);
        mmgraph.addLocation(10, 10);
        mmgraph.addLocation(11, 20);
        mmgraph.addLocation(12, 12);

        mmgraph.edge(0, 1, 100, true);
        mmgraph.edge(0, 2, 200, true);
        mmgraph.edge(1, 2, 120, false);

        checkGraph(mmgraph);
        mmgraph.close();

        mmgraph = new MemoryGraphSafe(tmpDir, 1003, 103);
        // no need here assertTrue(mmgraph.loadExisting());
        assertEquals(3, mmgraph.getLocations());
        checkGraph(mmgraph);
    }

    protected void checkGraph(Graph g) {
        assertEquals(3, g.getLocations());
        assertEquals(10, g.getLatitude(0), 1e-2);
        assertEquals(10, g.getLongitude(0), 1e-2);
        assertEquals(2, count(g.getOutgoing(0)));
        assertTrue(contains(g.getOutgoing(0), 1, 2));

        assertEquals(11, g.getLatitude(1), 1e-2);
        assertEquals(20, g.getLongitude(1), 1e-2);
        assertEquals(2, count(g.getOutgoing(1)));
        assertTrue(contains(g.getOutgoing(1), 0, 2));

        assertEquals(12, g.getLatitude(2), 1e-2);
        assertEquals(12, g.getLongitude(2), 1e-2);
        assertEquals(1, count(g.getOutgoing(2)));
        assertTrue(contains(g.getOutgoing(2), 0));
    }
}