<!-- https://www.codingame.com/training/hard/skynet-revolution-episode-2 -->

<?php
define('DEBUG', true);
function debug() {if (!DEBUG) return; foreach (func_get_args() as $sArgDebug) error_log(var_export($sArgDebug, true));}
class Network
{
    public $iNbNodes = 0;
    public $iSkynetNode = null;
    public $aMap = [];
    public $aGateways = [];
    public $aCritNodes = [];

    public $aDistances = [];
    public $aPaths = [];

    public $oDijk;

    public function __construct()
    {
        //$N the total number of nodes in the level, including the gateways
        //$L the number of links
        //$E the number of exit gateways
        fscanf(STDIN, "%d %d %d", $this->iNbNodes, $L, $E);
        $this->_defineMap($L);
        $this->_defineGateways($E);
        //debug('aCritNodes', $this->aCritNodes);
        //debug('map', $this->aMap);
    }

    public function runRound()
    {
        // The index of the node on which the Skynet agent is positioned this turn
        fscanf(STDIN, "%d", $this->iSkynetNode);
        if ($this->isUnderPressure()) {
            debug('isUnderPressure');
            return;
        }

        //Analyzing the most links to gateway
        $this->findDistancesAndPaths();

        if ($this->findCriticalPressureForGateways()) {
            debug('findCriticalPressureForGateways');
            return;
        }

        //Before striking the shortest path, we need to find if a node is not in a critical position.
        //Take the farther node and check its shorest path. If, for each node, there's a pression,
        //we need to cut, if exists, the link from gateway to the node which owns the most links number to gateways
        if ($this->findPressureForAllGateways()) {
            debug('findPressureForAllGateways');
            return;
        }

        //If there's no more node link to more than 1 gateway, find the nearest node linked to the nearest gateway to cut this link
        debug('cutClosestLink');
        $this->cutClosestLink();
    }

    private function _defineMap($nbLinks)
    {
        for ($i=0; $i<$nbLinks; ++$i) {
            // N1 and N2 defines a link between these nodes
            fscanf(STDIN, "%d %d", $N1, $N2);
            $this->aMap[$N1][$N2] = 1;
            $this->aMap[$N2][$N1] = 1;
            $this->aMap[$N1][$N1] = 0;
            $this->aMap[$N2][$N2] = 0;
        }
    }

    private function _defineGateways($nbExit)
    {
        for ($i=0; $i<$nbExit; ++$i) {
            // the index of a gateway node
            fscanf(STDIN, "%d", $EI);
            $this->aGateways[$EI] = $this->aMap[$EI];
            unset($this->aGateways[$EI][$EI]);
            unset($this->aMap[$EI]);
            $this->aMap[$EI][$EI] = 0;
            foreach (array_keys($this->aGateways[$EI]) as $iGWChildren) {
                isset($this->aCritNodes[$iGWChildren]) ?: $this->aCritNodes[$iGWChildren] = 0;
                ++$this->aCritNodes[$iGWChildren];
            }
        }
    }

    public function cutLink($n1, $n2)
    {
        (isset($this->aCritNodes[$n1])) ? --$this->aCritNodes[$n1] : null;
        (isset($this->aCritNodes[$n2])) ? --$this->aCritNodes[$n2] : null;

        if (isset($this->aMap[$n1][$n1])) { unset($this->aMap[$n1][$n1]); };
        if (isset($this->aMap[$n1][$n2])) { unset($this->aMap[$n1][$n2]); };
        if (isset($this->aMap[$n2][$n1])) { unset($this->aMap[$n2][$n1]); };
        if (isset($this->aMap[$n2][$n2])) { unset($this->aMap[$n2][$n2]); };

        if (isset($this->aGateways[$n1][$n2])) { unset($this->aGateways[$n1][$n2]); };
        if (isset($this->aGateways[$n2][$n1])) { unset($this->aGateways[$n2][$n1]); };

        echo $n1 . ' ' . $n2 . "\n";
        return true;
    }

    public function isUnderPressure()
    {
        foreach ($this->aGateways as $iGWNode => $aChildrenPath) {
            $aChildren = array_keys($aChildrenPath);
            if (in_array($this->iSkynetNode, $aChildren)) {
                $this->cutLink($iGWNode, $this->iSkynetNode);
                return true;
            }
        }
        return false;
    }

    public function findDistancesAndPaths($si = null)
    {
        if (is_null($si)) {
            $si = $this->iSkynetNode;
        }
        $this->oDijk = new Dijkstra($this->aMap, $this->iNbNodes);
        //debug('aGateways', $this->aGateways);
        foreach (array_keys($this->aGateways) as $iGw) {
            $this->oDijk->findShortestPath($si, $iGw);
            $this->aDistances[$iGw] = $this->oDijk->getDistance($iGw);
            $this->aPathes[$iGw] = $this->oDijk->getShortestPath($iGw);
        }
        debug('aDistances', $this->aDistances);
    }

    public function findCriticalPressureForGateways()
    {
        //debug($this->aCritNodes);
        foreach ($this->aCritNodes as $iCriticalNode => $criticalLevel) {
            if ($criticalLevel <= 1) {
                //This case was previously done by Network::isUnderPressure() so ignore it
                continue;
            }
            $this->oDijk->findShortestPath($this->iSkynetNode, $iCriticalNode);
            $aPath = array_reverse($this->oDijk->getShortestPath($iCriticalNode));
            array_shift($aPath); //Remove first element because it's the node itself.
            while (!empty($aPath)) {
                $iAnalyzingNode = array_shift($aPath);
                //debug($iAnalyzingNode);
                if (!empty($this->aCritNodes[$iAnalyzingNode])) {
                    continue;
                }
                //If there's a node which is not critical on the path, the critical level is reduced
                $criticalLevel--;
            }

            //If this is not critical anymore, let's try another node
            if ($criticalLevel <= 0) {
                continue;
            }

            //Otherwise, there'is a critical link to cut.
            foreach ($this->aGateways as $iGW => $aChildren) {
                if (in_array($iCriticalNode, array_keys($aChildren))) {
                    return $this->cutLink($iCriticalNode, $iGW);
                }
            }
        }

        return false;
    }

    public function findPressureForAllGateways()
    {
        asort($this->aDistances);
        foreach (array_keys($this->aDistances) as $iGW) {
            //Don't get the first node (SI) and the latest (GW)
            $aPathToFarest = array_slice($this->aPathes[$iGW], 1, -1);
            foreach ($aPathToFarest as $iNodeInWay) {
                if (empty($this->aCritNodes[$iNodeInWay])) {
                    //If the gateway has a weak node (a node linked to another gateway), this node can be cut to prevent
                    //impossible double cut at the same time action.
                    if (false !== ($iWeakNode = $this->hasWeakNode($iGW))) {
                        $this->cutLink($iGW, $iWeakNode);
                        return true;
                    }
                    break;
                }
                if (isset($this->aCritNodes[$iNodeInWay]) && $this->aCritNodes[$iNodeInWay] > 1) {
                    $this->cutLink($iGW, $iNodeInWay);
                    return true;
                }
            }
        }
        return false;

    }

    public function hasWeakNode($iGW)
    {
        $aChildrenGWAttacked = $this->aGateways[$iGW];
        foreach ($this->aGateways as $iGatewayNode => $aChildren) {
            if ($iGW === $iGatewayNode) {
                continue;
            }
            $aWeakNodes = array_intersect_key($aChildrenGWAttacked, $aChildren);
            if (empty($aWeakNodes)) {
                continue;
            }
            //If there's 1 weak node, return it to cut the link into this with its current gateway
            if (count($aWeakNodes) === 1) {
                return key($aWeakNodes);
            }
            //If there're severals weak nodes, Dijkstra them all and return the nearest
            $iMinDist = $this->iNbNodes;
            $iWeakestNode = null;
            foreach (array_keys($aWeakNodes) as $iWeakNode) {
                $this->oDijk->findShortestPath($this>iSkynetNode, $iWeakNode);
                $iCurrDist = $this->oDijk->getDistance($iWeakNode);
                if ($iMinDist > $iCurrDist) {
                    $iMinDist = $iCurrDist;
                    $iWeakestNode = $iWeakNode;
                }
            }
            return $iWeakestNode;
        }
        //If none was ok, return false;
        return false;
    }

    public function cutClosestLink()
    {
        asort($this->aDistances);
        reset($this->aDistances);
        $iNearestNode = key($this->aDistances);
        debug('$iNearestNode', $iNearestNode);
        $aShortestPath = $this->aPathes[$iNearestNode];
        debug('aPathes', $this->aPathes);
        $aLinkRemoved = array_slice($aShortestPath, -2);
        debug('$aLinkRemoved', $aLinkRemoved);
        list($iNode1, $iNode2) = $aLinkRemoved;
        $this->cutLink($iNode1, $iNode2);
    }
}
class Dijkstra {
    public $visited = array();
    public $distance = array();
    public $previousNode = array();
    public $startnode = null;
    public $map = array();
    public $infiniteDistance = 0;
    public $numberOfNodes = 0;
    public $bestPath = 0;
    public $matrixWidth = 0;
    public function __construct(&$ourMap, $infiniteDistance) {
        $this->infiniteDistance = $infiniteDistance;
        $this->map = &$ourMap;
        $this->numberOfNodes = count($ourMap);
        $this->bestPath = 0;
    }
    public function findShortestPath($start,$to = null) {
        $this->startnode = $start;
        foreach ($this->map as $node => $aLinks) {
            if ($node === $this->startnode) {
                $this->visited[$node] = true;
                $this->distance[$node] = 0;
            } else {
                $this->visited[$node] = false;
                $this->distance[$node] = isset($this->map[$this->startnode][$node])
                    ? $this->map[$this->startnode][$node]
                    : $this->infiniteDistance;
            }
            $this->previousNode[$node] = $this->startnode;
        }

        $tries = 0;
        while (in_array(false,$this->visited,true) && $tries <= $this->numberOfNodes) {
            $this->bestPath = $this->findBestPath($this->distance, array_keys($this->visited, false, true));
            if ($to !== null && $this->bestPath == $to) {
                break;
            }
            $this->updateDistanceAndPrevious($this->bestPath);
            $this->visited[$this->bestPath] = true;
            $tries++;
        }
    }
    public function findBestPath($ourDistance, $ourNodesLeft) {
        $bestPath = $this->infiniteDistance;
        $bestNode = null;
        foreach ($ourNodesLeft as $nodeLeft) {
            if ($ourDistance[$nodeLeft] < $bestPath) {
                $bestPath = $ourDistance[$nodeLeft];
                $bestNode = $nodeLeft;
            }
        }
        return $bestNode;
    }
    public function updateDistanceAndPrevious($obp) {
        foreach ($this->map as $node => $aLinks) {
            if (
                (isset($this->map[$obp][$node])) &&
                (!($this->map[$obp][$node] == $this->infiniteDistance) || ($this->map[$obp][$node] == 0 )) &&
                (($this->distance[$obp] + $this->map[$obp][$node]) < $this->distance[$node])
            ) {
                $this->distance[$node] = $this->distance[$obp] + $this->map[$obp][$node];
                $this->previousNode[$node] = $obp;
            }
        }
    }
    public function getDistance($to) {
        return $this->distance[$to];
    }

    public function getShortestPath($to = null) {
        $ourShortestPath = array();
        foreach ($this->map as $node => $aLinks) {
            if ($to !== null && $to !== $node) {
                continue;
            }
            $ourShortestPath[$node] = array();
            $endNode = null;
            $currNode = $node;
            $ourShortestPath[$node][] = $node;
            while ($endNode === null || $endNode != $this->startnode) {
                $ourShortestPath[$node][] = $this->previousNode[$currNode];
                $endNode = $this->previousNode[$currNode];
                $currNode = $this->previousNode[$currNode];
            }
            $ourShortestPath[$node] = array_reverse($ourShortestPath[$node]);
            if ($to === null || $to === $node) {
                if ($this->distance[$node] >= $this->infiniteDistance) {
                    $ourShortestPath[$node] = [];
                    continue;
                }
                if ($to === $node) {
                    break;
                }
            }
        }

        if ($to === null) {
            return $ourShortestPath;
        }
        if (isset($ourShortestPath[$to])) {
            return $ourShortestPath[$to];
        }
        return [];
    }

} // end class
$oNetwork = new Network();
// game loop
while (true) {
    $oNetwork->runRound();
}
?>