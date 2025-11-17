/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/main/resources/templates/**/*.html'],
  theme: {
    extend: {
      colors: {
        "primary-dark": "#000000",
        "primary-base": "#333333",
        "primary-light": "#f5f5f5",
        "primary-lighter": "#ffffff",
        "badge-green-bg": "#d1fae5",
        "badge-green-text": "#065f46",
        "badge-yellow-bg": "#fef3c7",
        "badge-yellow-text": "#78350f",
        "badge-red-bg": "#fee2e2",
        "badge-red-text": "#991b1b",
      },
    },
    screens: {
      sm: '480px', // Mobile
      md: '768px', // Tablet
      lg: '1024px', // Desktop
    },
  },
  plugins: [],
};
