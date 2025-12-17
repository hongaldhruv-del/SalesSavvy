import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ThemeToggle } from './components/ThemeToggle';
import './LandingPage.css';

export default function LandingPage() {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [featuredProducts, setFeaturedProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFeaturedProducts();
  }, []);

  const fetchFeaturedProducts = async () => {
    try {
      // Fetch products from all categories using public endpoint
      const categories = ['Shirts', 'Pants', 'Mobiles', 'Accessories'];
      const allProducts = [];
      
      for (const category of categories) {
        try {
          const response = await fetch(`http://localhost:9090/api/products/public?category=${category}`);
          if (response.ok) {
            const data = await response.json();
            if (data.products && data.products.length > 0) {
              allProducts.push(...data.products.slice(0, 2)); // Get 2 from each category
            }
          }
        } catch (error) {
          console.error(`Error fetching ${category}:`, error);
        }
      }
      
      setProducts(allProducts);
      setFeaturedProducts(allProducts.slice(0, 8)); // Show top 8 featured
    } catch (error) {
      console.error('Error fetching products:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="landing-page">
      {/* Header */}
      <header className="landing-header">
        <div className="landing-header-content">
          <div className="landing-logo">
            <h1>SalesSavvy</h1>
            <p>Your one-stop shop for all your needs</p>
          </div>
          <div className="landing-header-actions">
            <ThemeToggle />
            <button className="login-btn" onClick={() => navigate('/login')}>
              Sign In
            </button>
            <button className="register-btn" onClick={() => navigate('/register')}>
              Create Account
            </button>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-content">
          <h1 className="hero-title">Welcome to SalesSavvy</h1>
          <p className="hero-subtitle">Discover amazing products at unbeatable prices</p>
          <div className="hero-buttons">
            <button className="cta-button primary" onClick={() => navigate('/register')}>
              Start Shopping Now
            </button>
            <button className="cta-button secondary" onClick={() => navigate('/login')}>
              Sign In to Your Account
            </button>
          </div>
        </div>
      </section>

      {/* Offers Banner */}
      <section className="offers-section">
        <div className="offers-container">
          <div className="offer-card">
            <span className="offer-icon">🎉</span>
            <h3>New User Offer</h3>
            <p>Get 20% off on your first order</p>
          </div>
          <div className="offer-card">
            <span className="offer-icon">🚚</span>
            <h3>Free Shipping</h3>
            <p>On orders above ₹999</p>
          </div>
          <div className="offer-card">
            <span className="offer-icon">💳</span>
            <h3>Easy Payments</h3>
            <p>Secure payment with Razorpay</p>
          </div>
          <div className="offer-card">
            <span className="offer-icon">↩️</span>
            <h3>Easy Returns</h3>
            <p>30-day return policy</p>
          </div>
        </div>
      </section>

      {/* Featured Products */}
      <section className="featured-products-section">
        <div className="section-container">
          <h2 className="section-title">Featured Products</h2>
          {loading ? (
            <div className="loading-message">Loading products...</div>
          ) : featuredProducts.length > 0 ? (
            <div className="products-grid">
              {featuredProducts.map((product) => (
                <div key={product.productId} className="product-card-landing">
                  <div className="product-image-container">
                    <img
                      src={product.imageUrl || 'https://via.placeholder.com/200?text=Product'}
                      alt={product.name}
                      className="product-image-landing"
                    />
                    <div className="product-badge">Featured</div>
                  </div>
                  <div className="product-info-landing">
                    <h3 className="product-name-landing">{product.name}</h3>
                    <p className="product-description-landing">
                      {product.description || 'Premium quality product'}
                    </p>
                    <div className="product-price-landing">
                      <span className="price-current">₹{product.price?.toFixed(2) || '0.00'}</span>
                      {product.originalPrice && (
                        <span className="price-original">₹{product.originalPrice.toFixed(2)}</span>
                      )}
                    </div>
                    <button
                      className="view-product-btn"
                      onClick={() => {
                        navigate('/register');
                      }}
                    >
                      View Product
                    </button>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="no-products-message">
              <p>No products available yet. Check back soon!</p>
            </div>
          )}
        </div>
      </section>

      {/* Categories Section */}
      <section className="categories-section">
        <div className="section-container">
          <h2 className="section-title">Shop by Category</h2>
          <div className="categories-grid">
            <div className="category-card" onClick={() => navigate('/register')}>
              <div className="category-icon">👔</div>
              <h3>Shirts</h3>
              <p>Premium quality shirts</p>
            </div>
            <div className="category-card" onClick={() => navigate('/register')}>
              <div className="category-icon">👖</div>
              <h3>Pants</h3>
              <p>Comfortable & stylish</p>
            </div>
            <div className="category-card" onClick={() => navigate('/register')}>
              <div className="category-icon">📱</div>
              <h3>Mobiles</h3>
              <p>Latest smartphones</p>
            </div>
            <div className="category-card" onClick={() => navigate('/register')}>
              <div className="category-icon">⌚</div>
              <h3>Accessories</h3>
              <p>Complete your look</p>
            </div>
          </div>
        </div>
      </section>

      {/* Branding Section */}
      <section className="branding-section">
        <div className="section-container">
          <h2 className="section-title">Why Choose SalesSavvy?</h2>
          <div className="branding-grid">
            <div className="branding-card">
              <div className="branding-icon">⭐</div>
              <h3>Quality Products</h3>
              <p>We source only the best quality products for our customers</p>
            </div>
            <div className="branding-card">
              <div className="branding-icon">💰</div>
              <h3>Best Prices</h3>
              <p>Competitive pricing with regular discounts and offers</p>
            </div>
            <div className="branding-card">
              <div className="branding-icon">🔒</div>
              <h3>Secure Shopping</h3>
              <p>Your data and payments are completely secure</p>
            </div>
            <div className="branding-card">
              <div className="branding-icon">👥</div>
              <h3>24/7 Support</h3>
              <p>Our customer support team is always here to help</p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="cta-content">
          <h2>Ready to Start Shopping?</h2>
          <p>Join thousands of happy customers today</p>
          <button className="cta-button large" onClick={() => navigate('/register')}>
            Create Your Account Now
          </button>
        </div>
      </section>

      {/* Footer */}
      <footer className="landing-footer">
        <div className="footer-content">
          <div className="footer-section">
            <h4>SalesSavvy</h4>
            <p>Your one-stop shop for all your needs</p>
          </div>
          <div className="footer-section">
            <h4>Quick Links</h4>
            <a href="#products">Products</a>
            <a href="#categories">Categories</a>
            <a href="#offers">Offers</a>
          </div>
          <div className="footer-section">
            <h4>Support</h4>
            <a href="#about">About Us</a>
            <a href="#contact">Contact</a>
            <a href="#terms">Terms of Service</a>
          </div>
        </div>
        <div className="footer-bottom">
          <p>© 2024 SalesSavvy. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}

