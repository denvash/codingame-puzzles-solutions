<!-- https://www.codingame.com/training/medium/the-gift -->

<?php

fscanf(STDIN, "%d", $N);
fscanf(STDIN, "%d", $C);
$budget = array();
$sum = 0;

for ($i = 0; $i < $N; $i++) {
    fscanf(STDIN, "%d", $B);
    $budget[] = $B;
    $sum+= $B;
}

if ($sum < $C) {
    echo("IMPOSSIBLE\n");
} else {

    sort($budget);

    for ($i = 0; $i < $N; $i++) {

        $p = floor($C / ($N - $i));
        $m = min($budget[$i], $p);

        echo $m, "\n";
        $C-= $m;
    }
}