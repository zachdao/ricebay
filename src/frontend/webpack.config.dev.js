const { merge } = require('webpack-merge');
const base = require('./webpack.config');
const path = require('path');

module.exports = merge(base, {
    output: {
        filename: 'main.[contenthash].js',
        path: path.resolve(
            __dirname,
            '..',
            '..',
            'target',
            'classes',
            'public',
        ),
        clean: {
            keep: /\.gitkeep|images/,
        },
    },
});
