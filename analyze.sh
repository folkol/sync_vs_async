for i in {1..10}; do
    N=$((2 ** i))
    printf "%s\t" "$N"
    awk '/Requests per second/ { print $4 }' results/tiny_few_sync_$N.json
done > rps_few_sync.dat

for i in {1..10}; do
    N=$((2 ** i))
    printf "%s\t" "$N"
    awk '/Requests per second/ { print $4 }' results/tiny_few_async_$N.json
done > rps_few_async.dat

for i in {1..9}; do
    N=$((2 ** i))
    printf "%s\t" "$N"
    awk '/Requests per second/ { print $4 }' results/tiny_many_sync_$N.json
done > rps_many_sync.dat

for i in {1..9}; do
    N=$((2 ** i))
    printf "%s\t" "$N"
    awk '/Requests per second/ { print $4 }' results/tiny_many_async_$N.json
done > rps_many_async.dat

gnuplot -p rps.plt

for i in {1..10}; do
    N=$((2 ** i))
    printf "%s\t" "$N"
    awk '/99%/ { print $2 }' results/tiny_few_sync_$N.json
done > 99_few_sync.dat

for i in {1..10}; do
    N=$((2 ** i))
    printf "%s\t" "$N"
    awk '/99%/ { print $2 }' results/tiny_few_async_$N.json
done > 99_few_async.dat

for i in {1..9}; do
    N=$((2 ** i))
    printf "%s\t" "$N"
    awk '/99%/ { print $2 }' results/tiny_many_sync_$N.json
done > 99_many_sync.dat

for i in {1..9}; do
    N=$((2 ** i))
    printf "%s\t" "$N"
    awk '/99%/ { print $2 }' results/tiny_many_async_$N.json
done > 99_many_async.dat

gnuplot -p 99.plt
