module.exports = {
    env: {
        es6: true,
        node: true,
        jest: true,
    },
    extends: [
        'plugin:@typescript-eslint/recommended', // Uses the recommended rules from the @typescript-eslint/eslint-plugin// Uses eslint-config-prettier to disable ESLint rules from @typescript-eslint/eslint-plugin that would conflict with prettier
        'plugin:prettier/recommended',
    ],
    parser: '@typescript-eslint/parser',
    plugins: ['@typescript-eslint'],
    parserOptions: {
        ecmaVersion: 2017,
        sourceType: 'module',
    },
    rules: {
        '@typescript-eslint/explicit-module-boundary-types': 'off',
        //     indent: ['error', 2],
        // 'linebreak-style': ['error', 'unix'],
        //     quotes: ['error', 'single'],
        //     'no-console': 'warn',
        //     'no-unused-vars': 'off',
        //     '@typescript-eslint/no-unused-vars': ['error', { vars: 'all', args: 'after-used', ignoreRestSiblings: false }],
        //     '@typescript-eslint/explicit-function-return-type': 'warn', // Consider using explicit annotations for object literals and function return types even when they can be inferred.
        //     'no-empty': 'warn',
    },
};
