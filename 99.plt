set title "99:th percentile"
set logscale x
set ylabel "ms"
set xlabel "Concurrency"
plot '99_few_sync.dat' with lines, '99_few_async.dat' with lines, '99_many_sync.dat' with lines, '99_many_async.dat' with lines;
