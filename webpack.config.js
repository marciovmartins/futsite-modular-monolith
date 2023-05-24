const path = require('path');

const port = process.env.PORT || 8081;

module.exports = {
    entry: './src/main/js/index.js',
    devtool: 'inline-source-map',
    devServer: {
        historyApiFallback: true,
        static: [
            './src/main/resources/templates',
            './src/main/resources/static',
        ],
        port: port,
        liveReload: true,
        hot: true,
    },
    cache: false,
    mode: 'development',
    output: {
        path: path.resolve(__dirname, './src/main/resources/static/built/'),
        filename: 'bundle.js'
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