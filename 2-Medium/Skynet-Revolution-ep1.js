// https://www.codingame.com/training/medium/skynet-revolution-episode-1

var [N, L, E] = readline().split(' ').map(Number);

var graph = new Array(N);
var gates = new Array(E);

for (var i = 0; i < L; i++) {
  var [N1, N2] = readline().split(' ').map(Number);

  if (graph[N1] === undefined) graph[N1] = [];
  if (graph[N2] === undefined) graph[N2] = [];

  graph[N1].push(N2);
  graph[N2].push(N1);
}
for (var i = 0; i < E; i++) {
  gates[i] = +readline();
}

while (true) {
  var SI = +readline();
  var r1, r2 = SI;

  // Check if agent neighbours are close to a gate
  var neighbours = graph[SI];
  var gate = neighbours.find(n => gates.indexOf(n) !== -1);

  if (gate !== undefined) {
    r1 = gate;
  } else if (neighbours.length === 2) {

    // If the agent has two options, cut the one with more neighbours
    if (graph[neighbours[0]].length < graph[neighbours[1]].length) {
      r1 = neighbours[1];
    } else {
      r1 = neighbours[0];
    }
  } else {

    // Create a list of gate neighbours having 3 neighbours themselves
    var gateNeighbours = [];
    for (var g = 0; g < gates.length; g++) {
      var gn = graph[gates[g]];

      for (var n = 0; n < gn.length; n++) {
        if (graph[gn[n]].length === 3) {
          gateNeighbours.push(gn[n]);
        }
      }
    }

    // Take the first gate neighbour for the moment, could be improved
    r1 = gateNeighbours[0];

    // Cut on the ring to another neighbour
    r2 = graph[r1].find(n => gateNeighbours.indexOf(n) !== -1);

    if (r2 === undefined) {
      r1 = neighbours[0];
      r2 = SI;
    }
  }

  graph[r1] = graph[r1].filter(x => x !== r2);
  graph[r2] = graph[r2].filter(x => x !== r1);

  print(r1 + ' ' + r2);
}