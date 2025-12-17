import React from 'react';
import './assets/styles.css';

export function ProductList({ products, onAddToCart }) {
  if (products.length === 0) {
    return <p className="no-products">No products available.</p>;
  }

  return (
    <div className="product-list">
      <div className="product-grid">
        {products.map((product) => {
          // Handle different image formats from backend
          const imageUrl = product.images && product.images.length > 0 
            ? product.images[0] 
            : product.imageUrl 
            ? product.imageUrl 
            : 'https://via.placeholder.com/300?text=No+Image';
          
          return (
            <div key={product.productId || product.product_id} className="product-card">
              <div className="product-image-wrapper">
                <img
                  src={imageUrl}
                  alt={product.name}
                  className="product-image"
                  loading="lazy"
                  onError={(e) => {
                    e.target.src = 'https://via.placeholder.com/300?text=No+Image';
                  }}
                />
              </div>
              <div className="product-info">
                <h3 className="product-name">{product.name}</h3>
                <p className="product-description">
                  {product.description || 'Premium quality product'}
                </p>
                <div className="product-price-section">
                  <span className="product-price">₹{product.price?.toFixed(2) || '0.00'}</span>
                  {product.stock !== undefined && (
                    <span className="product-stock">
                      {product.stock > 0 ? `In Stock (${product.stock})` : 'Out of Stock'}
                    </span>
                  )}
                </div>
                <button
                  className="add-to-cart-btn"
                  onClick={() => onAddToCart(product.productId || product.product_id)}
                  disabled={product.stock === 0}
                >
                  {product.stock === 0 ? 'Out of Stock' : 'Add to Cart'}
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}