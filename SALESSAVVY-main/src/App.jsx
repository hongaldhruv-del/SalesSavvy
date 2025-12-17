import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { ThemeProvider } from './contexts/ThemeContext';
import AppRoutes from './Routes';
import './assets/styles.css';

function App() {
  return (
    <ThemeProvider>
      <Router>
        <AppRoutes />
      </Router>
    </ThemeProvider>
  );
}

export default App;
