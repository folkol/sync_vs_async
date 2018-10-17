set title "Requests per second"
set logscale x
set ylabel "Requests per second"
set xlabel "Concurrency"
plot 'rps_few_sync.dat' with lines, 'rps_few_async.dat' with lines, 'rps_many_sync.dat' with lines, 'rps_many_async.dat' with lines;
