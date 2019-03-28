#!/bin/bash
for n in 16;#16 64 256;
do
  for l in 40 64 128;
  do
    for mode in 0 1 2;
    do
      for d in 0 1 2;
      do
        echo mode: $mode N:$n L:$l D:$d
        java crypto2_stdout/Common $mode $n $l $d | ./stdin -s > "out/mode_${mode}_N:${n}_L:${l}_D:${d}.txt"
        # echo done_mode: $mode N:$n L:$l D:$d
      done
    done
  done
done
