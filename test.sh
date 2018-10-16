### c5.4xlarge, ulimit -n 4096

HOST=${1:-localhost}

echo "Running tests against host: $HOST"

echo "Creating test content"
for flavor in tiny_few tiny_many large_few large_many; do
    curl -XPUT -H'Content-Type: application/json' $HOST:8080/sync/$flavor -d @$flavor.json
done
echo "Waiting..."
sleep 5


echo "Warming up the JVM"
ab -k -n 10000 -c 10 http://$HOST:8080/sync/tiny_many >/dev/null
ab -k -n 10000 -c 10 http://$HOST:8080/async/tiny_many >/dev/null

echo "Waiting..."
sleep 10

echo "Creating test results dir"
mkdir results

echo "Testing sync tiny content without parts"
for threads in {0..10}; do
    n=$((2 ** $threads))
    ab -k -n 100000 -c $n http://$HOST:8080/sync/tiny_few >results/tiny_few_sync_$n.json
done
echo "Waiting..."
sleep 20


echo "Testing sync tiny content with parts"
for threads in {0..9}; do
    n=$((2 ** $threads))
    ab -k -n 100000 -c $n http://$HOST:8080/sync/tiny_many >results/tiny_many_sync_$n.json
done
echo "Waiting..."
sleep 20


#echo "Testing sync large content without parts"
#for threads in {0..9}; do
#    n=$((2 ** $threads))
#    ab -k -n 1000 -c $n http://$HOST:8080/sync/large_few >results/large_few_$n.json
#done
#echo "Waiting..."
#sleep 30


echo "Testing async tiny content without parts"
for threads in {0..10}; do
    n=$((2 ** $threads))
    ab -k -n 100000 -c $n http://$HOST:8080/async/tiny_few >results/tiny_few_async_$n.json
done
echo "Waiting..."
sleep 20


echo "Testing async tiny content with parts"
for threads in {0..9}; do
    n=$((2 ** $threads))
    ab -k -n 100000 -c $n http://$HOST:8080/async/tiny_many >results/tiny_many_async_$n.json
done
#echo "Waiting..."
#sleep 20


#echo "Testing async large content without parts"
#for threads in {0..9}; do
#    n=$((2 ** $threads))
#    ab -k -n 1000 -c $n http://$HOST:8080/async/large_few >results/large_few_$n.json
#done
#echo "Waiting..."
#sleep 30

echo "Archiving test results"
tar cf results.tar results
