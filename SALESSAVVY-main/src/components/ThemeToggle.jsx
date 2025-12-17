import React, { useState } from 'react';
import { useTheme } from '../contexts/ThemeContext';
import './ThemeToggle.css';

export const ThemeToggle = () => {
  const { theme, toggleTheme } = useTheme();
  const [isOpen, setIsOpen] = useState(false);

  const handleThemeChange = (newTheme) => {
    toggleTheme(newTheme);
    setIsOpen(false);
  };

  const getCurrentThemeLabel = () => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'device') return 'Device';
    return theme === 'dark' ? 'Dark' : 'Light';
  };

  return (
    <div className="theme-toggle-container">
      <button
        className="theme-toggle-button"
        onClick={() => setIsOpen(!isOpen)}
        aria-label="Toggle theme"
      >
        {theme === 'dark' ? '🌙' : '☀️'}
        <span className="theme-label">{getCurrentThemeLabel()}</span>
      </button>
      {isOpen && (
        <div className="theme-dropdown">
          <button
            className={`theme-option ${theme === 'light' && localStorage.getItem('theme') !== 'device' ? 'active' : ''}`}
            onClick={() => handleThemeChange('light')}
          >
            ☀️ Light
          </button>
          <button
            className={`theme-option ${theme === 'dark' && localStorage.getItem('theme') !== 'device' ? 'active' : ''}`}
            onClick={() => handleThemeChange('dark')}
          >
            🌙 Dark
          </button>
          <button
            className={`theme-option ${localStorage.getItem('theme') === 'device' ? 'active' : ''}`}
            onClick={() => handleThemeChange('device')}
          >
            📱 Device
          </button>
        </div>
      )}
    </div>
  );
};

