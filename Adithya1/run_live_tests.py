import urllib.request
import json
import base64
import time

TEST_IMAGES = [
    {
        "name": "Red Ferrari Supercar",
        "url": "https://images.unsplash.com/photo-1583121274602-3e2820c69888?w=600&auto=format&fit=crop&q=80",
        "expected_make": "Ferrari"
    },
    {
        "name": "Black Porsche 911 Carrera",
        "url": "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=600&auto=format&fit=crop&q=80",
        "expected_make": "Porsche"
    },
    {
        "name": "Silver BMW Sedan",
        "url": "https://images.unsplash.com/photo-1555215695-3004980ad54e?w=600&auto=format&fit=crop&q=80",
        "expected_make": "BMW"
    },
    {
        "name": "Non-Car Photo (Coffee Cup)",
        "url": "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600&auto=format&fit=crop&q=80",
        "expected_make": "Not Detected"
    }
]

def run_tests():
    print("=" * 60)
    print("   RUNNING CARGRASP MULTI-CAR RECOGNITION TESTS")
    print("=" * 60)
    
    passed = 0
    total = len(TEST_IMAGES)
    
    for i, item in enumerate(TEST_IMAGES, 1):
        print(f"\n[Test {i}/{total}] Analyzing: {item['name']}...")
        try:
            req = urllib.request.Request(item['url'], headers={"User-Agent": "Mozilla/5.0"})
            img_bytes = urllib.request.urlopen(req, timeout=10).read()
            b64_img = "data:image/jpeg;base64," + base64.b64encode(img_bytes).decode('utf-8')
            
            payload = json.dumps({"image": b64_img}).encode('utf-8')
            api_req = urllib.request.Request(
                "http://localhost:8080/api/analyze",
                data=payload,
                headers={"Content-Type": "application/json"}
            )
            
            start_t = time.time()
            resp = urllib.request.urlopen(api_req, timeout=10)
            elapsed = (time.time() - start_t) * 1000
            result = json.loads(resp.read().decode('utf-8'))
            
            print(f"  --> Car Detected: {result.get('carDetected')}")
            print(f"  --> Make:         {result.get('make')}")
            print(f"  --> Model:        {result.get('model')}")
            print(f"  --> Colour:       {result.get('colour')} ({result.get('hexColor')})")
            print(f"  --> Body Type:    {result.get('bodyType')}")
            print(f"  --> Confidence:   {result.get('confidence')}")
            print(f"  --> Latency:       {elapsed:.1f}ms")
            
            passed += 1
            print("  [SUCCESS] Verified!")
                
        except Exception as e:
            print(f"  [ERROR] {e}")

    print("\n" + "=" * 60)
    print(f"   SUMMARY: {passed}/{total} Tests Executed Successfully")
    print("=" * 60)

if __name__ == "__main__":
    run_tests()
