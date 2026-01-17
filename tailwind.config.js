/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/main/resources/templates/**/*.html'],
  theme: {
    extend: {
      colors: {
        'primary-dark': '#000000',
        'primary-base': '#333333',
        'primary-mid': '#aaaaaa',
        'primary-light': '#f5f5f5',
        'green-bg': '#d1fae5',
        'green-text': '#065f46',
        'green-border': '#34d399',
        'green-bg-hover': '#bbf7d0',
        'green-text-hover': '#047857',
        'green-border-hover': '#059669',
        'blue-bg': '#dbeafe',
        'blue-text': '#1e40af',
        'blue-border': '#3b82f6',
        'blue-bg-hover': '#bfdbfe',
        'blue-text-hover': '#2563eb',
        'blue-border-hover': '#2563eb',
        'yellow-bg': '#fef3c7',
        'yellow-text': '#78350f',
        'yellow-border': '#fbbf24',
        'yellow-bg-hover': '#fde68a',
        'yellow-text-hover': '#b45309',
        'yellow-border-hover': '#d97706',
        'red-bg': '#fee2e2',
        'red-text': '#991b1b',
        'red-border': '#f87171',
        'red-bg-hover': '#fecaca',
        'red-text-hover': '#b91c1c',
        'red-border-hover': '#ef4444',
        'purple-bg': '#ede9fe',
        'purple-text': '#6d28d9',
        'purple-border': '#a78bfa',
        'purple-bg-hover': '#ddd6fe',
        'purple-text-hover': '#7c3aed',
        'purple-border-hover': '#8b5cf6',
      },
    },
    screens: {
      sm: '480px', // Mobile
      md: '768px', // Tablet
      lg: '1024px', // Desktop
    },
  },
  plugins: [
    function ({ addBase, theme }) {
      addBase({
        ':root': {
          '--btn-primary-bg': theme('colors.red-bg'),
          '--btn-primary-text': theme('colors.red-text'),
          '--btn-primary-border': theme('colors.red-border'),

          '--btn-secondary-bg': theme('colors.primary-light'),
          '--btn-secondary-text': theme('colors.primary-dark'),
          '--btn-secondary-border': theme('colors.primary-mid'),
        },
        '@media (prefers-color-scheme: dark)': {
          ':root': {
            '--btn-primary-bg': '#7f1d1d', // pull dark colors dynamically when it has been set in theme
            '--btn-primary-text': '#fee2e2',
            '--btn-primary-border': '#991b1b',

            '--btn-secondary-bg': '#1e293b',
            '--btn-secondary-text': '#f9fafb',
            '--btn-secondary-border': '#334155',
          },
        },
      });
    },
  ],
};
