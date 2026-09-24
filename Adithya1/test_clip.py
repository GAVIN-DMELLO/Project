import sys
import os
import io
import urllib.request
import torch
from PIL import Image

print("[Step 1] Checking PyTorch version:", torch.__version__)
import open_clip

print("[Step 2] Available CLIP pretrained tags for ViT-B-32:", open_clip.list_models())

print("[Step 3] Loading ViT-B-32 with 'openai' weights...")
model, _, preprocess = open_clip.create_model_and_transforms('ViT-B-32', pretrained='openai')
tokenizer = open_clip.get_tokenizer('ViT-B-32')
model.eval()
print("[Step 3 Done] CLIP ViT-B-32 model ready!")

# Candidates
cars = [
    ("BMW", "5 Series / 7 Series", "Silver / Grey", "a silver BMW 5 Series sedan"),
    ("Porsche", "911 Carrera", "Black", "a black Porsche 911 sports car"),
    ("Tesla", "Model 3", "Red", "a red Tesla Model 3 sedan"),
    ("Toyota", "RAV4", "White", "a white Toyota RAV4 SUV"),
    ("Ferrari", "488 GTB", "Red", "a red Ferrari sports car"),
    ("BMW", "M4 Coupe", "Blue", "a blue BMW M4 coupe"),
    ("Mercedes-Benz", "S-Class", "Black", "a black Mercedes-Benz sedan"),
    ("Audi", "A6 Sedan", "Silver", "a silver Audi sedan")
]

prompts = [c[3] for c in cars]
text_tokens = tokenizer(prompts)

with torch.no_grad():
    text_features = model.encode_text(text_tokens)
    text_features /= text_features.norm(dim=-1, keepdim=True)

print("[Step 4 Done] Encoded candidate text embeddings!")

def test_url(url, label):
    print(f"\n--- Testing: {label} ---")
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        data = urllib.request.urlopen(req, timeout=10).read()
        img = Image.open(io.BytesIO(data)).convert("RGB")
        tensor = preprocess(img).unsqueeze(0)
        with torch.no_grad():
            image_features = model.encode_image(tensor)
            image_features /= image_features.norm(dim=-1, keepdim=True)
            similarity = (100.0 * image_features @ text_features.T).softmax(dim=-1)[0]
            top_idx = similarity.argmax().item()
            score = similarity[top_idx].item()
        
        pred = cars[top_idx]
        print(f"Result for [{label}]: {pred[0]} {pred[1]} ({pred[2]}) -> Confidence: {score*100:.1f}%")
    except Exception as e:
        print(f"Error testing {label}: {e}")

test_url("https://images.unsplash.com/photo-1555215695-3004980ad54e?w=600&auto=format&fit=crop&q=80", "Silver BMW")
test_url("https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=600&auto=format&fit=crop&q=80", "Black Porsche 911")
test_url("https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=600&auto=format&fit=crop&q=80", "Red Tesla Model 3")
test_url("https://images.unsplash.com/photo-1583121274602-3e2820c69888?w=600&auto=format&fit=crop&q=80", "Red Ferrari")
