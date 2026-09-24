import os
import io
import torch
import open_clip
from PIL import Image

print("[Loading CLIP]...")
clip_model, _, clip_preprocess = open_clip.create_model_and_transforms('ViT-B-32', pretrained='openai')
tokenizer = open_clip.get_tokenizer('ViT-B-32')
clip_model.eval()

# Let's test specific candidates
CAR_CANDIDATES = [
    {
        "make": "BMW",
        "model": "7 Series / 5 Series Sedan",
        "bodyType": "Executive Luxury Sedan",
        "estimatedYearRange": "2020-2024",
        "notes": "Large iconic active dual kidney grille, horizontal LED laser headlamps, aerodynamic lower apron, and luxury long-wheelbase silhouette.",
        "prompt": "a photo of a silver BMW 7 Series or BMW 5 Series sedan"
    },
    {
        "make": "Suzuki",
        "model": "Swift",
        "bodyType": "Compact Hatchback",
        "estimatedYearRange": "2020-2024",
        "notes": "Signature honeycomb front radiator grille with Suzuki badge, sleek L-shaped DRL headlights, floating roofline, and compact urban proportions.",
        "prompt": "a photo of a white Suzuki Swift hatchback car"
    },
    {
        "make": "Ferrari",
        "model": "488 GTB / F8 Tributo",
        "bodyType": "Mid-Engine Supercar",
        "estimatedYearRange": "2018-2023",
        "notes": "Aerodynamic S-Duct front hood, deep sculpted side air channels, and Prancing Horse styling.",
        "prompt": "a photo of a red Ferrari 488 or Ferrari supercar"
    },
    {
        "make": "Porsche",
        "model": "911 Carrera",
        "bodyType": "Sports Coupe",
        "estimatedYearRange": "2020-2025",
        "notes": "Iconic teardrop flyline, distinctive oval LED matrix headlamps, and wide rear lightbar.",
        "prompt": "a photo of a Porsche 911 Carrera sports car"
    },
    {
        "make": "Tesla",
        "model": "Model 3 / Model Y",
        "bodyType": "Electric Vehicle",
        "estimatedYearRange": "2020-2024",
        "notes": "Minimalist grille-less aerodynamic front fascia and panoramic glass roof.",
        "prompt": "a photo of a Tesla Model 3 or Model Y electric car"
    }
]

COLOR_CANDIDATES = [
    {"name": "Metallic Silver / Titanium Grey", "hex": "#A0A5AA", "prompt": "a photo of a silver car or grey car"},
    {"name": "Pure White / Pearl White", "hex": "#F8FAFC", "prompt": "a photo of a white car"},
    {"name": "Gloss Black / Obsidian", "hex": "#18181B", "prompt": "a photo of a black car"},
    {"name": "Crimson Red / Rosso Corsa", "hex": "#DC2626", "prompt": "a photo of a red car"},
    {"name": "Deep Metallic Blue", "hex": "#2563EB", "prompt": "a photo of a blue car"}
]

with torch.no_grad():
    car_prompts = [c["prompt"] for c in CAR_CANDIDATES]
    car_tokens = tokenizer(car_prompts)
    car_text_features = clip_model.encode_text(car_tokens)
    car_text_features /= car_text_features.norm(dim=-1, keepdim=True)

    color_prompts = [c["prompt"] for c in COLOR_CANDIDATES]
    color_tokens = tokenizer(color_prompts)
    color_text_features = clip_model.encode_text(color_tokens)
    color_text_features /= color_text_features.norm(dim=-1, keepdim=True)

def test_image(img_path, title):
    print(f"\n==================== Testing: {title} ====================")
    img = Image.open(img_path).convert("RGB")
    tensor = clip_preprocess(img).unsqueeze(0)
    with torch.no_grad():
        img_features = clip_model.encode_image(tensor)
        img_features /= img_features.norm(dim=-1, keepdim=True)

        car_sim = (100.0 * img_features @ car_text_features.T).softmax(dim=-1)[0]
        top_idx = car_sim.argmax().item()
        top_score = car_sim[top_idx].item()
        pred_car = CAR_CANDIDATES[top_idx]

        color_sim = (100.0 * img_features @ color_text_features.T).softmax(dim=-1)[0]
        top_color_idx = color_sim.argmax().item()
        pred_color = COLOR_CANDIDATES[top_color_idx]

    print(f"Prediction: {pred_car['make']} {pred_car['model']}")
    print(f"Body Type:  {pred_car['bodyType']} ({pred_car['estimatedYearRange']})")
    print(f"Color:      {pred_color['name']} ({pred_color['hex']})")
    print(f"Score:      {top_score*100:.1f}%")
    print(f"Notes:      {pred_car['notes']}")

# Test BMW photo from user upload
bmw_path = r"C:\Users\Akash\.gemini\antigravity-ide\brain\a1aa85a6-342c-45ba-8597-12f70af5f645\.user_uploaded\media_1790269348691.jpg"
swift_path = r"C:\Users\Akash\.gemini\antigravity-ide\brain\a1aa85a6-342c-45ba-8597-12f70af5f645\.user_uploaded\media_1790269356683.jpg"

test_image(bmw_path, "USER UPLOADED BMW PHOTO")
test_image(swift_path, "USER UPLOADED SUZUKI SWIFT PHOTO")
