from PIL import Image, ImageDraw

# Create simple colored penguin placeholders
def create_simple_penguin(color, filename):
    img = Image.new('RGBA', (64, 64), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Simple penguin shape
    draw.ellipse([20, 10, 44, 50], fill=color)  # Body
    draw.ellipse([26, 5, 38, 20], fill='white')  # Head
    draw.ellipse([28, 8, 32, 12], fill='black')  # Eye
    draw.ellipse([32, 8, 36, 12], fill='black')  # Eye
    
    img.save(f'app/src/main/res/drawable/{filename}')

# Create different states
create_simple_penguin('black', 'penguin_simple_idle.png')
create_simple_penguin('blue', 'penguin_simple_happy.png')
create_simple_penguin('red', 'penguin_simple_hurt.png')
create_simple_penguin('green', 'penguin_simple_eating.png')
