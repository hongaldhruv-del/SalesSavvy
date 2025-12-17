-- ============================================
-- Add Sample Products with Image URLs
-- ============================================
-- This script adds sample products with placeholder image URLs
-- You can update the image URLs later when you have actual images
-- ============================================

USE salessavvy;

-- Clear existing sample data (optional - comment out if you want to keep existing data)
-- DELETE FROM productimages WHERE product_id IN (SELECT product_id FROM products);
-- DELETE FROM products;

-- Ensure categories exist
INSERT IGNORE INTO categories (category_name) VALUES 
    ('Shirts'),
    ('Pants'),
    ('Accessories'),
    ('Mobiles'),
    ('Mobile Accessories');

-- Get category IDs
SET @shirts_id = (SELECT category_id FROM categories WHERE category_name = 'Shirts');
SET @pants_id = (SELECT category_id FROM categories WHERE category_name = 'Pants');
SET @accessories_id = (SELECT category_id FROM categories WHERE category_name = 'Accessories');
SET @mobiles_id = (SELECT category_id FROM categories WHERE category_name = 'Mobiles');
SET @mobile_accessories_id = (SELECT category_id FROM categories WHERE category_name = 'Mobile Accessories');

-- Add SHIRTS with images
INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Classic White Shirt', 'Premium cotton white shirt, perfect for formal occasions. Comfortable fit with quality fabric.', 1299.00, 50, @shirts_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1594938291221-94ad2e5eb930?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Blue Denim Shirt', 'Casual blue denim shirt, comfortable and stylish. Perfect for everyday wear.', 899.00, 30, @shirts_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1603252109303-2751441dd157?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Striped Formal Shirt', 'Professional striped shirt for office wear. Premium quality fabric with perfect fit.', 1499.00, 40, @shirts_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Polo T-Shirt', 'Classic polo t-shirt in various colors. Comfortable cotton blend fabric.', 599.00, 60, @shirts_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Checkered Shirt', 'Trendy checkered pattern shirt. Modern design with comfortable fit.', 799.00, 35, @shirts_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1503341504253-dff4815485f1?w=400');

-- Add PANTS with images
INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Slim Fit Jeans', 'Comfortable slim fit jeans in blue. Premium denim with perfect stretch.', 1999.00, 45, @pants_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Formal Trousers', 'Professional formal trousers for office. Perfect fit with wrinkle-free fabric.', 2499.00, 30, @pants_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Cargo Pants', 'Stylish cargo pants with multiple pockets. Durable and comfortable.', 1499.00, 25, @pants_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1506629905607-4c4c0a0c0a0a?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Chinos', 'Classic chinos in beige and navy. Versatile and comfortable for all occasions.', 1799.00, 40, @pants_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Track Pants', 'Comfortable track pants for casual wear. Perfect for workouts and lounging.', 999.00, 50, @pants_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=400');

-- Add ACCESSORIES with images
INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Leather Belt', 'Genuine leather belt with classic buckle. Durable and stylish.', 699.00, 60, @accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1624222247344-550fb60583fd?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Wrist Watch', 'Elegant wrist watch with leather strap. Classic design with modern features.', 2999.00, 20, @accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Sunglasses', 'UV protection sunglasses with stylish frame. Polarized lenses for eye protection.', 1299.00, 40, @accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Wallet', 'Leather wallet with multiple card slots. RFID blocking technology.', 899.00, 35, @accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1627123424574-724758594ecc?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Backpack', 'Durable backpack for daily use. Multiple compartments with laptop sleeve.', 1999.00, 30, @accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400');

-- Add MOBILES with images
INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Smartphone Pro Max', 'Latest smartphone with 128GB storage, 6.7 inch display, triple camera setup, 5G ready', 49999.00, 15, @mobiles_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Budget Smartphone', 'Affordable smartphone with great features, 64GB storage, dual camera, long battery life', 12999.00, 30, @mobiles_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Gaming Phone', 'High-performance gaming phone with 256GB storage, 120Hz display, advanced cooling system', 39999.00, 10, @mobiles_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Camera Phone', 'Phone with exceptional camera quality, 128GB, 108MP main camera, 8K video recording', 34999.00, 20, @mobiles_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1601972602237-8c79241e468b?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Compact Phone', 'Small and lightweight phone, perfect for one-hand use, 128GB, premium build quality', 19999.00, 25, @mobiles_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=400');

-- Add MOBILE ACCESSORIES with images
INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Phone Case', 'Protective phone case with shock absorption. Clear design to show your phone beauty.', 499.00, 100, @mobile_accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1601972602237-8c79241e468b?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Screen Protector', 'Tempered glass screen protector. 9H hardness, bubble-free installation.', 299.00, 150, @mobile_accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1601972602237-8c79241e468b?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Wireless Earbuds', 'Bluetooth wireless earbuds with noise cancellation. 30-hour battery life, premium sound.', 2999.00, 40, @mobile_accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1572569511254-d8f925fe2cbb?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('Power Bank', '10000mAh power bank with fast charging. Dual USB ports, LED indicator.', 1499.00, 50, @mobile_accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1609091839311-d5365f9ff1c8?w=400');

INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) VALUES
('USB-C Cable', 'Fast charging USB-C cable, 1 meter. Durable braided design, supports fast charging.', 299.00, 80, @mobile_accessories_id, NOW(), NOW());

SET @product_id = LAST_INSERT_ID();
INSERT INTO productimages (product_id, image_url) VALUES
(@product_id, 'https://images.unsplash.com/photo-1583394838336-acd977736f90?w=400');

-- Display summary
SELECT 
    c.category_name AS 'Category',
    COUNT(p.product_id) AS 'Products Added',
    COUNT(pi.image_id) AS 'Images Added',
    MIN(p.price) AS 'Min Price',
    MAX(p.price) AS 'Max Price'
FROM products p
JOIN categories c ON p.category_id = c.category_id
LEFT JOIN productimages pi ON p.product_id = pi.product_id
GROUP BY c.category_name
ORDER BY c.category_name;

SELECT 'Sample products with images added successfully!' AS Status;
SELECT 'Note: Image URLs use Unsplash placeholders. Update them with your actual product images.' AS Note;

