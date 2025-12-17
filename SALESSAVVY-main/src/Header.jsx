import React from 'react';
import { CartIcon } from './CartIcon';
import { ProfileDropdown } from './ProfileDropdown';
import { ThemeToggle } from './components/ThemeToggle';
import './assets/styles.css';
import Logo from './Logo';

export function Header({ cartCount, username }) {
  return (
    <header className="header">
      <div className="header-content">
        <Logo />
        <div className="header-actions">
          <ThemeToggle />
          <CartIcon count={cartCount} />
          <ProfileDropdown username={username} />
        </div>
      </div>
    </header>
  );
}
