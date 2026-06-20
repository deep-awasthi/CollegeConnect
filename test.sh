#!/usr/bin/env bash

# Exit immediately if any command fails
set -e

echo "========================================================================"
echo "🚀 Starting CollegeConnect Backend Caching, DB, and Server..."
echo "========================================================================"

# Launch docker compose services in background
docker compose up -d --build

# Set cleanup trap so docker environment is always shut down on exit or error
cleanup() {
  echo "========================================================================"
  echo "🧹 Cleaning up: Stopping Docker containers..."
  echo "========================================================================"
  docker compose down
}
trap cleanup EXIT

echo "⏳ Waiting for Spring Boot application to start..."
attempts=0
max_attempts=30
server_ready=false

while [ $attempts -lt $max_attempts ]; do
  # Check if port 8080 responds with method not allowed (405) or bad request (400)
  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/auth/register || true)
  if [ "$HTTP_STATUS" = "405" ] || [ "$HTTP_STATUS" = "400" ] || [ "$HTTP_STATUS" = "200" ]; then
    server_ready=true
    break
  fi
  sleep 3
  attempts=$((attempts + 1))
  echo -n "."
done

echo ""

if [ "$server_ready" = "false" ]; then
  echo "❌ Error: Spring Boot server failed to start within 90 seconds."
  exit 1
fi

echo "✅ Server is online! Starting integration tests..."
echo ""

# Helper to extract JSON values without external jq dependency
extract_json_val() {
  local key="$1"
  local json="$2"
  echo "$json" | grep -o "\"$key\":[^,}]*" | head -n1 | cut -d':' -f2 | tr -d '"' | tr -d ' '
}

# -----------------------------------------------------------------------------
# Test 1: User Registration
# -----------------------------------------------------------------------------
echo "📝 Test 1: Registering Alice and Bob..."

REG_ALICE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@college.edu","password":"password123","college":"State SEC","department":"CS","graduationYear":2027,"bio":"Studying CS"}')

REG_BOB=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"bob","email":"bob@college.edu","password":"password123","college":"State SEC","department":"CS","graduationYear":2027,"bio":"Interested in Backend"}')

echo "   Alice Registered: $REG_ALICE"
echo "   Bob Registered: $REG_BOB"
echo ""

# -----------------------------------------------------------------------------
# Test 2: User Login
# -----------------------------------------------------------------------------
echo "🔑 Test 2: Authenticating users..."

LOGIN_ALICE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"password123"}')

ALICE_TOKEN=$(extract_json_val "token" "$LOGIN_ALICE")
ALICE_ID=$(extract_json_val "id" "$LOGIN_ALICE")

LOGIN_BOB=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"bob","password":"password123"}')

BOB_TOKEN=$(extract_json_val "token" "$LOGIN_BOB")
BOB_ID=$(extract_json_val "id" "$LOGIN_BOB")

if [ -z "$ALICE_TOKEN" ] || [ -z "$BOB_TOKEN" ]; then
  echo "❌ Login failed! Could not parse tokens."
  exit 1
fi

echo "   Alice Token: ${ALICE_TOKEN:0:20}..."
echo "   Bob Token: ${BOB_TOKEN:0:20}..."
echo ""

# -----------------------------------------------------------------------------
# Test 3: Connections
# -----------------------------------------------------------------------------
echo "🤝 Test 3: Creating and accepting peer connections..."

CONN_RESP=$(curl -s -X POST "http://localhost:8080/api/peers/connect/$BOB_ID" \
  -H "Authorization: Bearer $ALICE_TOKEN")

CONN_ID=$(extract_json_val "id" "$CONN_RESP")

if [ -z "$CONN_ID" ]; then
  echo "❌ Connection failed! Response: $CONN_RESP"
  exit 1
fi

ACCEPT_RESP=$(curl -s -X POST "http://localhost:8080/api/peers/accept/$CONN_ID" \
  -H "Authorization: Bearer $BOB_TOKEN")

echo "   Connection Accepted: $ACCEPT_RESP"
echo ""

# -----------------------------------------------------------------------------
# Test 4: Forum Post & Comments
# -----------------------------------------------------------------------------
echo "💬 Test 4: Forum posting and commenting..."

POST_RESP=$(curl -s -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Need compiler notes","content":"Struggling with LR parsing. Help please.","category":"Academics"}')

POST_ID=$(extract_json_val "id" "$POST_RESP")

COMMENT_RESP=$(curl -s -X POST "http://localhost:8080/api/posts/$POST_ID/comments" \
  -H "Authorization: Bearer $BOB_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"content":"I have class notes, will upload shortly."}')

echo "   Post Created ID: $POST_ID"
echo "   Comment Created: $COMMENT_RESP"
echo ""

# -----------------------------------------------------------------------------
# Test 5: Document Upload & Download
# -----------------------------------------------------------------------------
echo "📁 Test 5: Uploading and downloading documents..."

# Create a temporary local file to upload
echo "Compiler Design Cheat Sheet notes content" > temp_notes.txt

UPLOAD_RESP=$(curl -s -X POST http://localhost:8080/api/documents/upload \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -F "title=Compiler notes" \
  -F "description=LR table study notes" \
  -F "department=Computer Science" \
  -F "file=@temp_notes.txt")

rm temp_notes.txt

DOC_ID=$(extract_json_val "id" "$UPLOAD_RESP")

DOWNLOAD_RESP=$(curl -s -X GET "http://localhost:8080/api/documents/download/$DOC_ID" \
  -H "Authorization: Bearer $BOB_TOKEN")

echo "   Document Upload ID: $DOC_ID"
echo "   Document Downloaded Content: '$DOWNLOAD_RESP'"
echo ""

# -----------------------------------------------------------------------------
# Test 6: Book Marketplace
# -----------------------------------------------------------------------------
echo "📚 Test 6: Creating and claiming books..."

BOOK_RESP=$(curl -s -X POST http://localhost:8080/api/books \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Compiler Design","author":"Aho Ullman","description":"Classic text","price":0.0}')

BOOK_ID=$(extract_json_val "id" "$BOOK_RESP")

CLAIM_RESP=$(curl -s -X POST "http://localhost:8080/api/books/$BOOK_ID/claim" \
  -H "Authorization: Bearer $BOB_TOKEN")

echo "   Book Created ID: $BOOK_ID"
echo "   Book Claimed: $CLAIM_RESP"
echo ""

# -----------------------------------------------------------------------------
# Test 7: Poll Creation and Voting
# -----------------------------------------------------------------------------
echo "📊 Test 7: Creating poll and casting votes..."

POLL_RESP=$(curl -s -X POST http://localhost:8080/api/polls \
  -H "Authorization: Bearer $BOB_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"question":"What language?","options":["Java","Python"],"expiresAt":"2026-12-31T23:59:59"}')

POLL_ID=$(extract_json_val "id" "$POLL_RESP")

# Retrieve the option id for Java (first option)
# Since options is a nested array: "options":[{"id":1,...}]
OPTION1_ID=$(echo "$POLL_RESP" | grep -o '"id":[0-9]*' | head -n2 | tail -n1 | cut -d':' -f2)

VOTE_RESP=$(curl -s -X POST "http://localhost:8080/api/polls/$POLL_ID/vote" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"optionId\": $OPTION1_ID}")

# Double vote test (should fail)
DOUBLE_VOTE_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST "http://localhost:8080/api/polls/$POLL_ID/vote" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"optionId\": $OPTION1_ID}")

echo "   Poll Created ID: $POLL_ID"
echo "   Alice Voted Response: $VOTE_RESP"
echo "   Double Vote Status (Expect 400): $DOUBLE_VOTE_STATUS"
echo ""

# -----------------------------------------------------------------------------
# Test 8: Fests and Circulars Caching
# -----------------------------------------------------------------------------
echo "⏳ Test 8: Testing Redis caching for Fests and Circulars..."

# Create Fest (Evicts cache)
FEST_RESP=$(curl -s -X POST http://localhost:8080/api/fests \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"TechFest 2026","description":"Coding symposium","location":"Auditorium","eventDate":"2026-10-15T09:00:00","coordinatorName":"Turing","coordinatorEmail":"turing@college.edu"}')

# Fetch Fests (Cache miss - triggers db load)
FESTS_MISS=$(curl -s -X GET http://localhost:8080/api/fests \
  -H "Authorization: Bearer $ALICE_TOKEN")

# Fetch Fests (Cache hit - loads from Redis)
FESTS_HIT=$(curl -s -X GET http://localhost:8080/api/fests \
  -H "Authorization: Bearer $BOB_TOKEN")

# Create Circular (Evicts cache)
CIRC_RESP=$(curl -s -X POST http://localhost:8080/api/circulars \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Exams notice","content":"Finals from Dec 1.","postedBy":"Dean"}')

# Fetch Circulars (Cache miss)
CIRC_MISS=$(curl -s -X GET http://localhost:8080/api/circulars \
  -H "Authorization: Bearer $ALICE_TOKEN")

# Fetch Circulars (Cache hit)
CIRC_HIT=$(curl -s -X GET http://localhost:8080/api/circulars \
  -H "Authorization: Bearer $BOB_TOKEN")

echo "   Fest Created: $(extract_json_val "title" "$FEST_RESP")"
echo "   Circular Created: $(extract_json_val "title" "$CIRC_RESP")"
echo ""

echo "========================================================================"
echo "🎉 All tests successfully completed!"
echo "========================================================================"
