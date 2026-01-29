/** @type {import('tailwindcss').Config} */
const {
  lightColors,
  darkColors,
} = require('./src/main/resources/tailwind-tokens/colors');

const {
  lightModal,
  darkModal,
} = require('./src/main/resources/tailwind-tokens/modal');

const lightVars = {
  ...lightColors,
  ...lightModal,
};

const darkVars = {
  ...darkColors,
  ...darkModal,
};

module.exports = {
  content: ['./src/main/resources/templates/**/*.html'],
  darkMode: 'media',
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: 'var(--color-primary)',
          light: 'var(--color-primary-light)',
          dark: 'var(--color-primary-dark)',
        },
        secondary: {
          DEFAULT: 'var(--color-secondary)',
          light: 'var(--color-secondary-light)',
          dark: 'var(--color-secondary-dark)',
        },
        tertiary: {
          DEFAULT: 'var(--color-tertiary)',
          light: 'var(--color-tertiary-light)',
          dark: 'var(--color-tertiary-dark)',
        },
        accent: {
          DEFAULT: 'var(--color-accent)',
          light: 'var(--color-accent-light)',
          dark: 'var(--color-accent-dark)',
        },
        neutral: {
          100: 'var(--color-neutral-100)',
          200: 'var(--color-neutral-200)',
          300: 'var(--color-neutral-300)',
          400: 'var(--color-neutral-400)',
          500: 'var(--color-neutral-500)',
          600: 'var(--color-neutral-600)',
          700: 'var(--color-neutral-700)',
          800: 'var(--color-neutral-800)',
          900: 'var(--color-neutral-900)',
        },
        'green-bg': 'var(--color-green-bg)',
        'green-text': 'var(--color-green-text)',
        'green-border': 'var(--color-green-border)',
        'green-bg-hover': 'var(--color-green-bg-hover)',
        'green-text-hover': 'var(--color-green-text-hover)',
        'green-border-hover': 'var(--color-green-border-hover)',
        'blue-bg': 'var(--color-blue-bg)',
        'blue-text': 'var(--color-blue-text)',
        'blue-border': 'var(--color-blue-border)',
        'blue-bg-hover': 'var(--color-blue-bg-hover)',
        'blue-text-hover': 'var(--color-blue-text-hover)',
        'blue-border-hover': 'var(--color-blue-border-hover)',
        'yellow-bg': 'var(--color-yellow-bg)',
        'yellow-text': 'var(--color-yellow-text)',
        'yellow-border': 'var(--color-yellow-border)',
        'yellow-bg-hover': 'var(--color-yellow-bg-hover)',
        'yellow-text-hover': 'var(--color-yellow-text-hover)',
        'yellow-border-hover': 'var(--color-yellow-border-hover)',
        'red-bg': 'var(--color-red-bg)',
        'red-text': 'var(--color-red-text)',
        'red-border': 'var(--color-red-border)',
        'red-bg-hover': 'var(--color-red-bg-hover)',
        'red-text-hover': 'var(--color-red-text-hover)',
        'red-border-hover': 'var(--color-red-border-hover)',
        'purple-bg': 'var(--color-purple-bg)',
        'purple-text': 'var(--color-purple-text)',
        'purple-border': 'var(--color-purple-border)',
        'purple-bg-hover': 'var(--color-purple-bg-hover)',
        'purple-text-hover': 'var(--color-purple-text-hover)',
        'purple-border-hover': 'var(--color-purple-border-hover)',
      },
    },
    screens: {
      sm: '480px', // Mobile
      md: '768px', // Tablet
      lg: '1024px', // Desktop
    },
  },
  plugins: [
    function ({ addBase }) {
      addBase({
        ':root': lightVars,
        '@media (prefers-color-scheme: dark)': {
          ':root': darkVars,
        },
      });
    },
  ],
};
