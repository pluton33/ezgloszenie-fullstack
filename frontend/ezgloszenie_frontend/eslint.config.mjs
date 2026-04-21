import js from '@eslint/js';
import globals from 'globals';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
import tseslint from 'typescript-eslint';
import prettierConfig from 'eslint-config-prettier';

export default tseslint.config(
  // list of things ignored by eslint:
  { ignores: ['dist', 'node_modules', 'dist-electron', 'out', 'build'] },
  
  {
    // JS and TypeScript rules:
    extends: [js.configs.recommended, ...tseslint.configs.recommended],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
    },
    rules: {
      // React rules:
      ...reactHooks.configs.recommended.rules,
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],

      //! Main Rules:
      // ONLY "console.error" ALLOWED! (warn every console.log)
      'no-console': ['warn', { allow: ['error', 'warn'] }],
      
      // not used variables will not be tolerated
      '@typescript-eslint/no-unused-vars': ['warn', { argsIgnorePattern: '^_' }],
      
      // "any" type of variable will get a warn 
      '@typescript-eslint/no-explicit-any': 'warn',

      // @ts-ignore banned
      '@typescript-eslint/ban-ts-comment': 'error',

      // garbage inside React will get a warn
      'react/jsx-no-useless-fragment': 'warn',

      // boolean "true" shall be saved as *bool* and "false" as *!bool*
      'react/jsx-boolean-value': ['warn', 'never'],
    },
  },
  
  // Leaving Rest to Prettier here:
  prettierConfig
);