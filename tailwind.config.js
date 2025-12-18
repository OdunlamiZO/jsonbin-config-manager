/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/templates/**/*.html"],
  theme: {
    extend: {
      colors: {
        "primary-dark": "#000000",
        "primary-base": "#333333",
        "primary-mid": "#aaaaaa",
        "primary-light": "#f5f5f5",
        "green-bg": "#d1fae5",
        "green-text": "#065f46",
        "green-border": "#34d399",
        "green-bg-hover": "#bbf7d0",
        "green-text-hover": "#047857",
        "green-border-hover": "#059669",
        "blue-bg": "#dbeafe",
        "blue-text": "#1e40af",
        "blue-border": "#3b82f6",
        "blue-bg-hover": "#bfdbfe",
        "blue-text-hover": "#2563eb",
        "blue-border-hover": "#2563eb",
        "yellow-bg": "#fef3c7",
        "yellow-text": "#78350f",
        "yellow-border": "#fbbf24",
        "yellow-bg-hover": "#fde68a",
        "yellow-text-hover": "#b45309",
        "yellow-border-hover": "#d97706",
        "red-bg": "#fee2e2",
        "red-text": "#991b1b",
        "red-border": "#f87171",
        "red-bg-hover": "#fecaca",
        "red-text-hover": "#b91c1c",
        "red-border-hover": "#ef4444",
      },
    },
    screens: {
      sm: "480px", // Mobile
      md: "768px", // Tablet
      lg: "1024px", // Desktop
    },
  },
  plugins: [],
};
