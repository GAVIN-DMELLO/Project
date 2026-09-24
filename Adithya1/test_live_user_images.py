import urllib.request
import json
import base64
import time

bmw_file = r"C:\Users\Akash\.gemini\antigravity-ide\brain\a1aa85a6-342c-45ba-8597-12f70af5f645\.user_uploaded\media_1790269348691.jpg"
swift_file = r"C:\Users\Akash\.gemini\antigravity-ide\brain\a1aa85a6-342c-45ba-8597-12f70af5f645\.user_uploaded\media_1790269356683.jpg"

def test_endpoint(file_path, car_name):
    print(f"\n[Testing Endpoint /api/analyze]: {car_name}...")
    with open(file_path, "rb") as f:
        img_bytes = f.read()
    
    b64_img = "data:image/jpeg;base64," + base64.b64encode(img_bytes).decode('utf-8')
    payload = json.dumps({"image": b64_img}).encode('utf-8')
    
    req = urllib.request.Request(
        "http://localhost:8080/api/analyze",
        data=payload,
        headers={"Content-Type": "application/json"}
    )
    
    t0 = time.time()
    resp = urllib.request.urlopen(req, timeout=12)
    elapsed = (time.time() - t0) * 1000
    res = json.loads(resp.read().decode('utf-8'))
    
    print(f"  --> Car Detected: {res.get('carDetected')}")
    print(f"  --> Make:         {res.get('make')}")
    print(f"  --> Model:        {res.get('model')}")
    print(f"  --> Colour:       {res.get('colour')} ({res.get('hexColor')})")
    print(f"  --> Body Type:    {res.get('bodyType')}")
    print(f"  --> Confidence:   {res.get('confidence')}")
    print(f"  --> Latency:      {elapsed:.1f}ms")
    print(f"  --> Notes:        {res.get('notes')}")

test_endpoint(bmw_file, "BMW Photo (Image 3)")
test_endpoint(swift_file, "Suzuki Swift Photo (Image 4)")
