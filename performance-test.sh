#!/bin/bash

# Performance test script for URL Shortener
# This script tests the performance improvements

echo "🚀 Starting URL Shortener Performance Test"

# Build and start the application
echo "📦 Building application..."
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d

# Wait for services to be ready
echo "⏳ Waiting for services to start..."
sleep 30

# Test URL shortening performance
echo "🔗 Testing URL shortening performance..."
for i in {1..100}; do
  curl -s -X POST http://localhost:8080/api/shorten \
    -H "Content-Type: application/json" \
    -d "{\"url\":\"https://example.com/test-$i\"}" \
    > /dev/null &
done
wait

# Test URL resolution performance
echo "🔍 Testing URL resolution performance..."
for i in {1..1000}; do
  # Assuming we have some short codes to test
  curl -s http://localhost:8080/abc123 > /dev/null &
  if (( i % 100 == 0 )); then
    wait
  fi
done
wait

echo "✅ Performance test completed!"
echo "📊 Check http://localhost:8080/actuator/metrics for detailed metrics"
