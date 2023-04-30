const path = require('path');

module.exports = {
    entry: './src/main/js/index.js',
    devtool: 'eval-cheap-module-source-map',
    devServer: {
        static: [
            './src/main/resources/templates',
            './src/main/resources/static',
        ],
        port: 8081,
        liveReload: true,
        hot: true,
    },
    cache: false,
    mode: 'development',
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"],
                    }
                }]
            }
        ]
    },
};